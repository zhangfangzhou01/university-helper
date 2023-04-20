package com.yhm.universityhelper.exception;

import cn.hutool.core.exceptions.ValidateException;
import com.yhm.universityhelper.entity.vo.ResponseResult;
import com.yhm.universityhelper.entity.vo.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(ValidateException.class)
    public ResponseResult<ResultEnum> handleValidationException(ValidateException e) {
        log.error(e.getMessage());
        return ResponseResult.fail(ResultEnum.PARAM_ERROR, e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseResult<ResultEnum> handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage());
        return ResponseResult.fail(ResultEnum.ERROR, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseResult<ResultEnum> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return ResponseResult.fail(ResultEnum.ERROR, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult<ResultEnum> handleException(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return ResponseResult.fail(ResultEnum.ERROR, e.getMessage());
    }
}
