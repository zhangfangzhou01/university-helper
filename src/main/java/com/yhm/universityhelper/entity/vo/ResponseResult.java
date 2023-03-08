package com.yhm.universityhelper.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "ResponseResult", description = "")
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;
    private String msg;
    private T data;

    public static @NotNull <T> ResponseResult<T> ok() {
        return new ResponseResult<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), null);
    }

    public static @NotNull <T> ResponseResult<T> ok(String msg) {
        ResultEnum resultEnum = ResultEnum.SUCCESS;
        resultEnum.setMsg(msg);
        return new ResponseResult<>(resultEnum.getCode(), resultEnum.getMsg(), null);
    }

    public static @NotNull <T> ResponseResult<T> ok(@NotNull T data) {
        return new ResponseResult<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), data);
    }

    public static @NotNull <T> ResponseResult<T> ok(T data, String msg) {
        ResultEnum resultEnum = ResultEnum.SUCCESS;
        resultEnum.setMsg(msg);
        return new ResponseResult<>(resultEnum.getCode(), resultEnum.getMsg(), data);
    }

    public static @NotNull <T> ResponseResult<T> fail(@NotNull ResultEnum resultEnum) {
        return new ResponseResult<T>(resultEnum.getCode(), resultEnum.getMsg(), null);
    }

    public static @NotNull <T> ResponseResult<T> fail(@NotNull ResultEnum resultEnum, String msg) {
        resultEnum.setMsg(msg);
        return new ResponseResult<>(resultEnum.getCode(), resultEnum.getMsg(), null);
    }

    public static @NotNull <T> ResponseResult<T> fail(T data, String msg) {
        ResultEnum resultEnum = ResultEnum.ERROR;
        resultEnum.setMsg(msg);
        return new ResponseResult<>(resultEnum.getCode(), resultEnum.getMsg(), data);
    }

    public static @NotNull <T> ResponseResult<T> fail(String msg) {
        ResultEnum resultEnum = ResultEnum.ERROR;
        resultEnum.setMsg(msg);
        return new ResponseResult<>(resultEnum.getCode(), resultEnum.getMsg(), null);
    }
}
