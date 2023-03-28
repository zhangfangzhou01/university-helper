package com.yhm.universityhelper.exception;

import com.yhm.universityhelper.entity.vo.ResultEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class GlobalException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private ResultEnum resultEnum;

    public GlobalException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.resultEnum = resultEnum;
    }

    public GlobalException(ResultEnum resultEnum, String message) {
        super(message);
        this.resultEnum = resultEnum;
        this.resultEnum.setMsg(message);
    }
}
