package com.yhm.universityhelper.exception;

import com.yhm.universityhelper.entity.vo.ResponseResult;
import com.yhm.universityhelper.entity.vo.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseResult<ResultEnum> handleException(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return ResponseResult.fail(ResultEnum.ERROR, e.getMessage());
    }
}
