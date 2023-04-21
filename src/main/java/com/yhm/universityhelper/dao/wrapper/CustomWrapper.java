package com.yhm.universityhelper.dao.wrapper;

import cn.hutool.extra.tokenizer.Word;
import cn.hutool.extra.tokenizer.engine.jieba.JiebaEngine;
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
import java.util.stream.StreamSupport;

@Component
public abstract class CustomWrapper {
    protected static List<String> STOP_WORDS = null;
    protected final static JiebaEngine JIEBA = new JiebaEngine();

    public static String fuzzyQuery(String field, String keyword) {
        return StreamSupport.stream(JIEBA.parse(keyword).spliterator(), true).map(Word::getText).filter(token -> !STOP_WORDS.contains(token)).map(token -> "(case when " + field + " like \"%" + token + "%\" then 1 else 0 end)").collect(Collectors.joining(" + "));
    }

    @PostConstruct
    private static void initResource() {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = new ClassPathResource("/static/stopwords.txt").getInputStream();
            inputStreamReader = new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(inputStreamReader);
            STOP_WORDS = bufferedReader.lines().collect(Collectors.toList());
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
    }
}
