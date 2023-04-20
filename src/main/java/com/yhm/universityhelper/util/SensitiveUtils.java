package com.yhm.universityhelper.util;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.dfa.SensitiveUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class SensitiveUtils {
    @PostConstruct
    private static void initResource() {
        List<String> sensitiveWords = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = SensitiveUtils.class.getClassLoader().getResourceAsStream("static/sensitive_words.txt");
            ClassPathResource classPathResource = new ClassPathResource("static/sensitive_words.txt");
            inputStreamReader = new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(inputStreamReader);
            sensitiveWords = bufferedReader.lines().collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SensitiveUtil.init(sensitiveWords);
    }

    public static String replaceSensitiveWords(String content, char stopCharacter) {
        List<String> sensitiveWords = SensitiveUtil.getFindedAllSensitive(content);
        StringBuilder sb = new StringBuilder(content);
        sb = sensitiveWords.stream().reduce(sb, (s, s2) -> s.replace(s.indexOf(s2), s.indexOf(s2) + s2.length(), StringUtils.repeat(stopCharacter, s2.length())), (s, s2) -> s);
        return sb.toString();
    }
}
