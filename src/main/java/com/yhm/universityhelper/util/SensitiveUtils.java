package com.yhm.universityhelper.util;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.yhm.universityhelper.util.dfa.WordInfo;
import com.yhm.universityhelper.util.dfa.WordTree;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import sun.misc.Unsafe;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
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

    @SneakyThrows
    public static String unsafeReplaceSensitive(String content, char stopCharacter) {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe)field.get(null);
        long offset = unsafe.objectFieldOffset(String.class.getDeclaredField("value"));
        char[] value = (char[])unsafe.getObject(content, offset);
        List<WordInfo> sensitiveWordInfos = SensitiveUtils.getAllSensitiveWithPos(content);
        if (sensitiveWordInfos == null || sensitiveWordInfos.size() == 0) {
            return content;
        }

        for (WordInfo sensitiveWordInfo : sensitiveWordInfos) {
            for (int i = 0; i < sensitiveWordInfo.getWord().length(); i++) {
                value[sensitiveWordInfo.getStart() + i] = stopCharacter;
            }
        }
        return content;
    }

    public static String replaceSensitive(String content, char stopCharacter) {
        StringBuilder sb = new StringBuilder(content);
        List<String> sensitiveWords = SensitiveUtils.getAllSensitive(content);
        return sensitiveWords.stream().reduce(sb, (builder, sensitiveWord) -> builder.replace(content.indexOf(sensitiveWord), content.indexOf(sensitiveWord) + sensitiveWord.length(), StrUtil.repeat(stopCharacter, sensitiveWord.length())), StringBuilder::append).toString();
    }
}
