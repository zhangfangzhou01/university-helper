package com.yhm.universityhelper.exception;

import io.jsonwebtoken.JwtException;

public class JwtAbnormalException extends JwtException {
    public JwtAbnormalException() {
        super("token 异常");
    }

    public JwtAbnormalException(String message) {
        super(message);
    }

    public JwtAbnormalException(String message, Throwable cause) {
        super(message, cause);
    }
}
