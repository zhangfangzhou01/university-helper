import io
import os
import shutil
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


class ImageNotExistException(Exception):
    def __init__(self, name: str):
        self.name = name


class NonImageException(Exception):
    def __init__(self, name: str):
        self.name = name


app = FastAPI()


image_suffix = ['jpg', 'png', 'jpeg', 'bmp', 'gif', 'webp']


@app.exception_handler(ImageTooManyException)
async def unicorn_exception_handler(request: Request, exc: ImageTooManyException):
    return JSONResponse(
        status_code=500,
        content={
            'code': 1019,
            'msg': '上传图片超过4张，请删除后再上传',
            'data': exc.name
        }
    )


@app.exception_handler(DirNotExistException)
async def unicorn_exception_handler(request: Request, exc: DirNotExistException):
    return JSONResponse(
        status_code=500,
        content={
            'code': 1020,
            'msg': '文件夹不存在',
            'data': exc.name
        }
    )


@app.exception_handler(ImageNotExistException)
async def unicorn_exception_handler(request: Request, exc: ImageNotExistException):
    return JSONResponse(
        status_code=500,
        content={
            'code': 1021,
            'msg': '图片不存在',
            'data': exc.name
        }
    )


@app.exception_handler(NonImageException)
async def unicorn_exception_handler(request: Request, exc: NonImageException):
    return JSONResponse(
        status_code=500,
        content={
            'code': 1022,
            'msg': '上传的文件中包含非图片文件',
            'data': exc.name
        }
    )


def check_if_files_too_many(path):
    length = len(os.listdir(path))
    if length >= 4:
        raise ImageTooManyException(name='image more than 4 error')


def check_if_files_are_image(files):
    for file in files:
        if file.filename.split('.')[-1] not in image_suffix:
            raise ImageTooManyException(name='some files are not image')


async def zip_files(filenames):
    s = io.BytesIO()
    zf = zipfile.ZipFile(s, 'w')
    for fpath in filenames:
        if fpath.split('.')[-1] not in image_suffix:
            continue
        _, fname = os.path.split(fpath)
        async with aiofiles.open(fpath, 'rb') as f:
            zf.writestr(fname, await f.read())
    zf.close()
    return Response(
        s.getvalue(),
        media_type='application/x-zip-compressed',
        headers={
            'Content-Disposition': 'attachment;filename=archive.zip'
        }
    )


@ app.post('/upload/{_type}/{_id}')
async def upload_image(_type: str, _id: int, files: List[UploadFile] = File(...)):
    check_if_files_are_image(files)
    if not os.path.exists(f'/root/image/{_type}/{_id}'):
        os.makedirs(f'/root/image/{_type}/{_id}')
    if (len(files) + len(os.listdir(f'/root/image/{_type}/{_id}'))) > 4:
        raise ImageTooManyException(name='image more than 4 error')
    if files[0].filename == '':
        return {
            'code': 500,
            'msg': 'no image uploaded',
            'data': [f'/root/image/{_type}/{_id}/' + i for i in os.listdir(f'/root/image/{_type}/{_id}') if i.split('.')[-1] in image_suffix]
        }
    try:
        for file in files:
            check_if_files_too_many(f'/root/image/{_type}/{_id}')
            async with aiofiles.open(f'/root/image/{_type}/{_id}/{file.filename}', 'wb') as f:
                await f.write(await file.read())
    except Exception as e:
        return {
            'code': 500,
            'msg': 'image upload fail',
            'data': [f'/root/image/{_type}/{_id}/' + i for i in os.listdir(f'/root/image/{_type}/{_id}') if i.split('.')[-1] in image_suffix]
        }
    return {
        'code': 200,
        'msg': 'image upload success',
        'data': [f'/root/image/{_type}/{_id}/' + i for i in os.listdir(f'/root/image/{_type}/{_id}') if i.split('.')[-1] in image_suffix]
    }


@ app.get('/download/{_type}/{_id}')
async def download_image(_type: str, _id: int):
    if not os.path.exists(f'/root/image/{_type}/{_id}'):
        raise DirNotExistException(name='dir not exist')
    filenames = [f'/root/image/{_type}/{_id}/' +
                 i for i in os.listdir(f'/root/image/{_type}/{_id}')]
    return await zip_files(filenames)


@ app.get('/download/{_type}/{_id}/{filename}')
async def download_image(_type: str, _id: int, filename: str):
    if not os.path.exists(f'/root/image/{_type}/{_id}'):
        raise DirNotExistException(name='dir not exist')
    return await zip_files([f'/root/image/{_type}/{_id}/{filename}'])


@ app.get('/delete/{_type}/{_id}/{filename}')
def delete_image(_type: str, _id: int, filename: str):
    if not os.path.exists(f'/root/image/{_type}/{_id}'):
        raise DirNotExistException(name='dir not exist')
    if not os.path.exists(f'/root/image/{_type}/{_id}/{filename}'):
        raise ImageNotExistException(name='image not exist')
    os.remove(f'/root/image/{_type}/{_id}/{filename}')
    # 如果文件夹已经空了，就删除这个文件夹
    if len(os.listdir(f'/root/image/{_type}/{_id}')) == 0:
        shutil.rmtree(f'/root/image/{_type}/{_id}')
    return {
        'code': 200,
        'msg': 'image delete success',
        'data': None
    }


@ app.get('/delete/{_type}/{_id}')
def delete_image(_type: str, _id: int):
    if not os.path.exists(f'/root/image/{_type}/{_id}'):
        raise DirNotExistException(name='dir not exist')
    shutil.rmtree(f'/root/image/{_type}/{_id}')
    return {
        'code': 200,
        'msg': 'image delete success',
        'data': None
    }


if __name__ == '__main__':
    uvicorn.run(app, host='0.0.0.0', port=8082)
