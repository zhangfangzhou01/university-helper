package com.yhm.universityhelper.util;

import cn.hutool.core.util.StrUtil;
import com.yhm.universityhelper.util.dfa.WordInfo;
import com.yhm.universityhelper.util.dfa.WordTree;
import org.springframework.core.io.ClassPathResource;
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
    private static final WordTree SENSITIVE_TREE = new WordTree();

    @PostConstruct
    private static void initResource() {
        List<String> sensitiveWords = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = new ClassPathResource("/static/sensitive_words.txt").getInputStream();
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
        SENSITIVE_TREE.clear();
        SENSITIVE_TREE.addWords(sensitiveWords);
    }

    public static List<String> getAllSensitive(String text) {
        return SENSITIVE_TREE.matchAll(text);
    }

    public static List<WordInfo> getAllSensitiveWithPos(String text) {
        return SENSITIVE_TREE.matchAllWithPos(text);
    }

    public static boolean containsSensitive(String text) {
        return SENSITIVE_TREE.matchFirst(text) != null;
    }

    public static String replaceSensitive(String content, char stopCharacter) {
        StringBuilder sb = new StringBuilder(content);
        List<String> sensitiveWords = SensitiveUtils.getAllSensitive(content);
        return sensitiveWords.stream().reduce(sb, (builder, sensitiveWord) -> builder.replace(content.indexOf(sensitiveWord), content.indexOf(sensitiveWord) + sensitiveWord.length(), StrUtil.repeat(stopCharacter, sensitiveWord.length())), StringBuilder::append).toString();
    }
}
