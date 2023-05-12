import io
import os
import zipfile
import aiofiles
from typing import List

import uvicorn
from fastapi import FastAPI, File, UploadFile, Response, Request
from fastapi.responses import JSONResponse


class ImageTooManyException(Exception):
    def __init__(self, name: str):
        self.name = name


class DirNotExistException(Exception):
    def __init__(self, name: str):
        self.name = name


app = FastAPI()


@app.exception_handler(ImageTooManyException)
async def unicorn_exception_handler(request: Request, exc: ImageTooManyException):
    return JSONResponse(
        status_code=418,
        content={
            'code': 418,
            'msg': '上传图片超过4张，请删除后再上传',
            'data': exc.name
        }
    )


@app.exception_handler(DirNotExistException)
async def unicorn_exception_handler(request: Request, exc: DirNotExistException):
    return JSONResponse(
        status_code=419,
        content={
            'code': 419,
            'msg': '文件夹不存在',
            'data': exc.name
        }
    )


def check_if_files_too_many(path):
    length = len(os.listdir(path))
    if length >= 4:
        raise ImageTooManyException(name='image more than 4 error')


async def zip_files(filenames):
    zip_filename = 'archive.zip'
    s = io.BytesIO()
    zf = zipfile.ZipFile(s, 'w')
    for fpath in filenames:
        fdir, fname = os.path.split(fpath)
    zf.write(fpath, fname)
    zf.close()
    return Response(
        s.getvalue(),
        media_type='application/x-zip-compressed',
        headers={
            'Content-Disposition': f'attachment;filename={zip_filename}'
        }
    )


@app.post('/upload/{_type}/{_id}')
async def upload_image(_type: str, _id: int, files: List[UploadFile] = File(...)):
    if (len(files) + len(os.listdir(f'/root/image/{_type}/{_id}'))) > 4:
        raise ImageTooManyException(name='image more than 4 error')
    if not os.path.exists(f'/root/image/{_type}/{_id}'):
        os.makedirs(f'/root/image/{_type}/{_id}')
    for file in files:
        check_if_files_too_many(f'/root/image/{_type}/{_id}')
        async with aiofiles.open(f'/root/image/{_type}/{_id}/{file.filename}', 'wb') as f:
            await f.write(await file.read())
    return {
        'code': 200,
        'msg': 'image upload success',
        'data': [f'/root/image/{_type}/{_id}/' + i for i in os.listdir(f'/root/image/{_type}/{_id}')]
    }


@app.get('/download/{_type}/{_id}')
async def download_image(_type: str, _id: int):
    if not os.path.exists(f'/root/image/{_type}/{_id}'):
        raise DirNotExistException(name='dir not exist')
    filenames = [f'/root/image/{_type}/{_id}/' +
                 i for i in os.listdir(f'/root/image/{_type}/{_id}')]
    return {
        'code': 200,
        'msg': 'image download success',
        'data': await zip_files(filenames)
    }


@app.get('/download/{_type}/{_id}/{filename}')
async def download_image(_type: str, _id: int, filename: str):
    if not os.path.exists(f'/root/image/{_type}/{_id}'):
        raise DirNotExistException(name='dir not exist')
    return {
        'code': 200,
        'msg': 'image download success',
        'data': await zip_files([f'/root/image/{_type}/{_id}/{filename}'])
    }


@app.get('/delete/{_type}/{_id}/{filename}')
async def delete_image(_type: str, _id: int, filename: str):
    os.remove(f'/root/image/{_type}/{_id}/{filename}')
    return {
        'code': 200,
        'msg': 'image delete success',
        'data': None
    }


@app.get('/delete/{_type}/{_id}')
async def delete_image(_type: str, _id: int):
    os.rmdir(f'/root/image/{_type}/{_id}')
    return {
        'code': 200,
        'msg': 'image delete success',
        'data': None
    }

if __name__ == '__main__':
    uvicorn.run(app, host='0.0.0.0', port=8082)
