package com.yhm.universityhelper.entity.vo;

import com.yhm.universityhelper.util.BeanUtils;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "ResponseResult", description = "")
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private static RedisTemplate<String, Object> redisTemplate = (RedisTemplate<String, Object>)BeanUtils.getBean("redisTemplate");
    private static Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    private Integer code;
    private String msg;
    private T data;
    private String token;

    public static String token() {
        String username = authentication.getName();
        String token = (String)(username == null ? null : redisTemplate.opsForValue().get("token:" + username));
        redisTemplate.unlink("token:" + username);
        return token;
    }

    public static <T> ResponseResult<T> ok() {
        return new ResponseResult<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), null, token());
    }

    public static <T> ResponseResult<T> ok(String msg) {
        ResultEnum resultEnum = ResultEnum.SUCCESS;
        resultEnum.setMsg(msg);
        return new ResponseResult<>(resultEnum.getCode(), resultEnum.getMsg(), null, token());
    }

    public static <T> ResponseResult<T> ok(T data) {
        return new ResponseResult<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), data, token());
    }

    public static <T> ResponseResult<T> ok(T data, String msg) {
        ResultEnum resultEnum = ResultEnum.SUCCESS;
        resultEnum.setMsg(msg);
        return new ResponseResult<>(resultEnum.getCode(), resultEnum.getMsg(), data, token());
    }

    public static <T> ResponseResult<T> fail(ResultEnum resultEnum) {
        return new ResponseResult<>(resultEnum.getCode(), resultEnum.getMsg(), null, token());
    }

    public static <T> ResponseResult<T> fail(ResultEnum resultEnum, String msg) {
        resultEnum.setMsg(msg);
        return new ResponseResult<>(resultEnum.getCode(), resultEnum.getMsg(), null, token());
    }

    public static <T> ResponseResult<T> fail(T data, String msg) {
        ResultEnum resultEnum = ResultEnum.ERROR;
        resultEnum.setMsg(msg);
        return new ResponseResult<>(resultEnum.getCode(), resultEnum.getMsg(), data, token());
    }

    public static <T> ResponseResult<T> fail(String msg) {
        ResultEnum resultEnum = ResultEnum.ERROR;
        resultEnum.setMsg(msg);
        return new ResponseResult<>(resultEnum.getCode(), resultEnum.getMsg(), null, token());
    }
}
