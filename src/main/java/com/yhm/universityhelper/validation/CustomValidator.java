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
    protected static final String CHINESE_REGEX = "([\\u4e00-\\u9fa5])";
    protected static final String NUMBER_REGEX = "(\\d+)";
    protected static final String ID_REGEX = "([1-9]\\d*)";
    protected static final String EMOJI_REGEX = "(?:[\\uD83C\\uDF00-\\uD83D\\uDDFF]|[\\uD83E\\uDD00-\\uD83E\\uDDFF]|[\\uD83D\\uDE00-\\uD83D\\uDE4F]|[\\uD83D\\uDE80-\\uD83D\\uDEFF]|[\\u2600-\\u26FF]\\uFE0F?|[\\u2700-\\u27BF]\\uFE0F?|\\u24C2\\uFE0F?|[\\uD83C\\uDDE6-\\uD83C\\uDDFF]{1,2}|[\\uD83C\\uDD70\\uD83C\\uDD71\\uD83C\\uDD7E\\uD83C\\uDD7F\\uD83C\\uDD8E\\uD83C\\uDD91-\\uD83C\\uDD9A]\\uFE0F?|[\\u0023\\u002A\\u0030-\\u0039]\\uFE0F?\\u20E3|[\\u2194-\\u2199\\u21A9-\\u21AA]\\uFE0F?|[\\u2B05-\\u2B07\\u2B1B\\u2B1C\\u2B50\\u2B55]\\uFE0F?|[\\u2934\\u2935]\\uFE0F?|[\\u3030\\u303D]\\uFE0F?|[\\u3297\\u3299]\\uFE0F?|[\\uD83C\\uDE01\\uD83C\\uDE02\\uD83C\\uDE1A\\uD83C\\uDE2F\\uD83C\\uDE32-\\uD83C\\uDE3A\\uD83C\\uDE50\\uD83C\\uDE51]\\uFE0F?|[\\u203C\\u2049]\\uFE0F?|[\\u25AA\\u25AB\\u25B6\\u25C0\\u25FB-\\u25FE]\\uFE0F?|[\\u00A9\\u00AE]\\uFE0F?|[\\u2122\\u2139]\\uFE0F?|\\uD83C\\uDC04\\uFE0F?|\\uD83C\\uDCCF\\uFE0F?|[\\u231A\\u231B\\u2328\\u23CF\\u23E9-\\u23F3\\u23F8-\\u23FA]\\uFE0F?)";
    protected static final String WORD_REGEX = "([\\u4e00-\\u9fa5_a-zA-Z0-9 \t]" + "|" + EMOJI_REGEX + ")";
    protected static final String LETTER_REGEX = "([\\u4e00-\\u9fa5_a-zA-Z])";
    protected static final String LINUX_PATH_REGEX = "^\\/([\\u4E00-\\u9FA5A-Za-z0-9_]+\\/?)+$";
    protected static final String WINDOWS_PATH_REGEX = "^[a-zA-z]:\\\\([\\u4E00-\\u9FA5A-Za-z0-9_\\s]+\\\\?)+$\n";
    protected static final String JSON_ARRAY_REGEX = "(\\[\\])|(^\\[(\\\"\\s*" + WORD_REGEX + "+\\s*\\\"|\\d+)(,\\s*(\\\"\\s*" + WORD_REGEX + "+\\s*\\\"|\\d+))*\\]$)";
    protected static final String ID_JSON_ARRAY_REGEX = "(\\[\\])|(^\\[(\\\"\\s*" + ID_REGEX + "+\\s*\\\"|\\d+)(,\\s*(\\\"\\s*" + ID_REGEX + "+\\s*\\\"|\\d+))*\\]$)";
    protected static final String JSON_OBJECT_REGEX = "(\\{\\})|(^\\{\\\"\\s*" + WORD_REGEX + "+\\s*\\\":\\s*(\\\"\\s*" + WORD_REGEX + "+\\s*\\\"|\\d+)(,\\s*\\\"\\s*" + WORD_REGEX + "+\\s*\\\":\\s*(\\\"\\s*" + WORD_REGEX + "+\\s*\\\"|\\d+))*\\}$)";
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
