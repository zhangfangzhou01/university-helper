package com.yhm.universityhelper.validation;

import cn.hutool.core.exceptions.ValidateException;

public class Validator {
    protected static final String JSON_ARRAY_REGEX = "^\\[(\\\"\\w+\\\"|\\d+)(,\\s*(\\\"\\w+\\\"|\\d+))*\\]$";
    protected static final String JSON_OBJECT_REGEX = "^\\{\\\"\\w+\\\":\\s*(\\\"\\w+\\\"|\\d+)(,\\s*\\\"\\w+\\\":\\s*(\\\"\\w+\\\"|\\d+))*\\}$";
    protected static final String DATE_TIME_REGEX = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";


    protected static String validateBetween(String name, String value, int min, int max) {
        int length = value.length();
        if (length < min || length > max) {
            throw new ValidateException("参数" + name + "长度应在" + min + "-" + max + "之间");
        }
        return value;
    }

    protected static String validateNotEqual(String thisValue, String thatValue, String message) {
        if (thisValue.equals(thatValue)) {
            throw new ValidateException(message);
        }
        return thisValue;
    }
}
