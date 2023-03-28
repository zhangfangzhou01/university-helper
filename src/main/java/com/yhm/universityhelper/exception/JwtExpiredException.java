package com.yhm.universityhelper.exception;

import io.jsonwebtoken.JwtException;

public class JwtExpiredException extends JwtException {
    public JwtExpiredException() {
        super("token 已过期");
    }

    public JwtExpiredException(String msg) {
        super(msg);
    }

    public JwtExpiredException(String msg, Throwable e) {
        super(msg, e);
    }
}
