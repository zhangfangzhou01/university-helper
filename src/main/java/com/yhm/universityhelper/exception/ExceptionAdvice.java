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
    @ExceptionHandler(GlobalException.class)
    public ResponseResult<ResultEnum> handleGlobalException(GlobalException e) {
        log.error(e.getMessage());
        return ResponseResult.fail(e.getResultEnum());
    }

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
}
