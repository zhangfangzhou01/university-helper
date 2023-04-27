package com.yhm.universityhelper.util;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
public class SqlUtils {
    public static String getSql(AbstractWrapper queryWrapper) {
        String targetSql = queryWrapper.getTargetSql();
        Map<String, Object> paramNameValuePairs = queryWrapper.getParamNameValuePairs();
        Collection<Object> values = paramNameValuePairs.values();
        for (Object value : values) {
            //替换?为 'value'
            targetSql = targetSql.replace("?", "'" + value + "'");
        }
        return targetSql;
    }
}
