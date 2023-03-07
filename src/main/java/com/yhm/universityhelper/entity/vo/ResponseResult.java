package com.yhm.universityhelper.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "ResponseResult", description = "")
public class ResponseResult implements Serializable {

    private static final long serialVersionUID = 1L;

    Integer code;
    String msg;
    Object data;

    public static @NotNull ResponseResult success(Object data) {
        return new ResponseResult(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), data);
    }

    public static @NotNull ResponseResult success(Object data, String msg) {
        ResultEnum resultEnum = ResultEnum.SUCCESS;
        resultEnum.setMsg(msg);
        return new ResponseResult(resultEnum.getCode(), resultEnum.getMsg(), data);
    }

    public static @NotNull ResponseResult success(String msg) {
        ResultEnum resultEnum = ResultEnum.SUCCESS;
        resultEnum.setMsg(msg);
        return new ResponseResult(resultEnum.getCode(), resultEnum.getMsg(), new HashMap<>());
    }

    public static @Nullable ResponseResult failure(@NotNull ResultEnum resultEnum) {
        return new ResponseResult(resultEnum.getCode(), resultEnum.getMsg(), null);
    }

    public static @Nullable ResponseResult failure(@NotNull ResultEnum resultEnum, String msg) {
        resultEnum.setMsg(msg);
        return new ResponseResult(resultEnum.getCode(), resultEnum.getMsg(), null);
    }

    public static @Nullable ResponseResult failure(String msg) {
        ResultEnum resultEnum = ResultEnum.ERROR;
        resultEnum.setMsg(msg);
        return new ResponseResult(resultEnum.getCode(), resultEnum.getMsg(), null);
    }
}
