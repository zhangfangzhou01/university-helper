package com.yhm.universityhelper.util;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.dfa.SensitiveUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SensitiveUtils {
    @PostConstruct
    private static void initResource() {
        final List<String> sensitiveWords = new BufferedReader(new InputStreamReader(new ClassPathResource("static/sensitive_words.txt").getStream(), StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
        SensitiveUtil.init(sensitiveWords);
    }

    public static String replaceSensitiveWords(String content, char stopCharacter) {
        List<String> sensitiveWords = SensitiveUtil.getFindedAllSensitive(content);
        StringBuilder sb = new StringBuilder(content);
        sb = sensitiveWords.stream().reduce(sb, (s, s2) -> s.replace(s.indexOf(s2), s.indexOf(s2) + s2.length(), StringUtils.repeat(stopCharacter, s2.length())), (s, s2) -> s);
        return sb.toString();
    }
}
