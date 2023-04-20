package com.yhm.universityhelper.dao.wrapper;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.extra.tokenizer.Word;
import cn.hutool.extra.tokenizer.engine.jieba.JiebaEngine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class CustomWrapper {
    protected final static List<String> STOP_WORDS = new BufferedReader(new InputStreamReader(new ClassPathResource("static/stopwords.txt").getStream(), StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
    protected final static JiebaEngine JIEBA = new JiebaEngine();
    protected final static StringBuilder STRING_BUILDER = new StringBuilder();

    public static String fuzzyQuery(String field, String keyword) {
        return StreamSupport.stream(JIEBA.parse(keyword).spliterator(), true).map(Word::getText).filter(token -> !STOP_WORDS.contains(token)).map(token -> "(case when " + field + " like \"%" + token + "%\" then 1 else 0 end)").collect(Collectors.joining(" + "));
    }
}
