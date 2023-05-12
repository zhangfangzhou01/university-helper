package com.yhm.universityhelper.validation;

import cn.hutool.core.exceptions.ValidateException;
import com.yhm.universityhelper.dao.UserMapper;
import com.yhm.universityhelper.entity.po.UserRole;
import com.yhm.universityhelper.util.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomValidator {
    
    protected static final String CHINESE_REGEX = "[\\u4e00-\\u9fa5]";
    
    protected static final String WORD_REGEX = "[\\u4e00-\\u9fa5_a-zA-Z0-9]";
    
    protected static final String LETTER_REGEX = "[\\u4e00-\\u9fa5a-zA-Z]";
    protected static final String LINUX_PATH_REGEX = "^\\/([\\u4E00-\\u9FA5A-Za-z0-9_]+\\/?)+$";
    protected static final String WINDOWS_PATH_REGEX = "^[a-zA-z]:\\\\([\\u4E00-\\u9FA5A-Za-z0-9_\\s]+\\\\?)+$\n";
    protected static final String JSON_ARRAY_REGEX = "(\\[\\])|(^\\[(\\\"\\s*[" + LETTER_REGEX + "]+\\s*\\\"|\\d+)(,\\s*(\\\"\\s*[" + LETTER_REGEX + "]+\\s*\\\"|\\d+))*\\]$)";

    protected static final String JSON_OBJECT_REGEX = "(\\{\\})|(^\\{\\\"\\s*[" + WORD_REGEX + "]+\\s*\\\":\\s*(\\\"\\s*[" + WORD_REGEX + "]+\\s*\\\"|\\d+)(,\\s*\\\"\\s*[" + WORD_REGEX + "]+\\s*\\\":\\s*(\\\"\\s*[" + WORD_REGEX + "]+\\s*\\\"|\\d+))*\\}$)";

    protected static final String LINUX_PATH_JSON_ARRAY_REGEX = "(\\[\\])|(^\\[(\\\"\\/([\\u4E00-\\u9FA5A-Za-z0-9_]+\\/?)+\\\")(,\\s*(\\\"\\/([\\u4E00-\\u9FA5A-Za-z0-9_]+\\/?)+\\\"))*\\]$)";
    
    protected static final String WINDOWS_PATH_JSON_ARRAY_REGEX = "(\\[\\])|(^\\[(\\\"[a-zA-z]:\\\\([\\u4E00-\\u9FA5A-Za-z0-9_\\s]+\\\\?)+\\\")(,\\s*(\\\"[a-zA-z]:\\\\([\\u4E00-\\u9FA5A-Za-z0-9_\\s]+\\\\?)+\\\"))*\\]$)";
    
    protected static final String DATE_TIME_REGEX = "^\\d{4}[\\-|\\/]\\d{2}[\\-|\\/]\\d{2}[ T]\\d{2}:\\d{2}:\\d{2}$";

    

    protected static String validateBetween(String name, String value, long min, long max) {
        int length = value.length();
        if (length < min || length > max) {
            throw new ValidateException("参数" + name + "长度应在" + min + "-" + max + "之间");
        }
        return value;
    }
    
    protected static String validateBetween(String name, String value, double min, double max) {
        int length = value.length();
        if (length < min || length > max) {
            throw new ValidateException("参数" + name + "长度应在" + min + "-" + max + "之间");
        }
        return value;
    }

    protected static Integer validateBetween(String name, int value, int min, int max) {
        if (value < min || value > max) {
            throw new ValidateException("参数" + name + "应在" + min + "-" + max + "之间");
        }
        return value;
    }

    protected static Long validateBetween(String name, long value, long min, long max) {
        if (value < min || value > max) {
            throw new ValidateException("参数" + name + "应在" + min + "-" + max + "之间");
        }
        return value;
    }

    protected static Double validateBetween(String name, double value, double min, double max) {
        if (value < min || value > max) {
            throw new ValidateException("参数" + name + "应在" + min + "-" + max + "之间");
        }
        return value;
    }

    protected static String validateNotEqual(String thisValue, String thatValue, String message) {
        if (thisValue.equals(thatValue)) {
            throw new ValidateException(message);
        }
        return thisValue;
    }
    
    protected static String validateLinuxPath(String name, String value) {
        if (!value.matches(LINUX_PATH_REGEX)) {
            throw new ValidateException("参数" + name + "不是合法的Linux路径");
        }
        return value;
    }
    
    protected static String validateWindowsPath(String name, String value) {
        if (!value.matches(WINDOWS_PATH_REGEX)) {
            throw new ValidateException("参数" + name + "不是合法的Windows路径");
        }
        return value;
    }

    public static void auth(String username, int specificAuthority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        switch (specificAuthority) {
            case UserRole.USER_CAN_CHANGE_SELF:
                if ((!roles.contains("ROLE_ADMIN")) && (!authentication.getName().equals(username))) {
                    throw new RuntimeException("用户没有权限这么做");
                }
                break;
            case UserRole.USER_CAN_CHANGE_NOBODY:
                if (!roles.contains("ROLE_ADMIN")) {
                    throw new RuntimeException("用户没有权限这么做");
                }
                break;
            default:
                throw new RuntimeException("用户没有权限这么做");
        }
    }

    public static void auth(long userId, int specificAuthority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        switch (specificAuthority) {
            case UserRole.USER_CAN_CHANGE_SELF:
                if ((!roles.contains("ROLE_ADMIN")) && (!authentication.getName().equals(BeanUtils.getBean(UserMapper.class).selectUsernameByUserId(userId)))) {
                    throw new RuntimeException("用户没有权限这么做");
                }
                break;
            case UserRole.USER_CAN_CHANGE_NOBODY:
                if (!roles.contains("ROLE_ADMIN")) {
                    throw new RuntimeException("用户没有权限这么做");
                }
                break;
            default:
                throw new RuntimeException("用户没有权限这么做");
        }
    }
}
