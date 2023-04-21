package com.yhm.universityhelper.util.dfa;

import java.util.Collection;
import java.util.List;

public final class SensitiveUtil {
    private static final WordTree SENSITIVE_TREE = new WordTree();

    public static void init(Collection<String> sensitiveWords) {
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
        return !SENSITIVE_TREE.matchAllWithPos(text).isEmpty();
    }
}
