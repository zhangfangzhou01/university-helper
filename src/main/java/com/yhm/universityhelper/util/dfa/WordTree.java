package com.yhm.universityhelper.util.dfa;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class WordTree extends HashMap<Character, WordTree> {
    private final Set<Character> endCharacterSet = new HashSet<>();
    private final Filter<Character> charFilter = StopChar::isNotStopChar;

    public WordTree() {
    }

    public void addWords(Collection<String> words) {
        if (!(words instanceof Set)) {
            words = new HashSet<>(words);
        }
        for (String word : words) {
            addWord(word);
        }
    }

    public void addWords(String... words) {
        HashSet<String> wordsSet = CollectionUtil.newHashSet(words);
        for (String word : wordsSet) {
            addWord(word);
        }
    }

    public void addWord(String word) {
        final Filter<Character> charFilter = this.charFilter;
        WordTree parent = null;
        WordTree current = this;
        WordTree child;
        char currentChar = 0;
        int length = word.length();
        for (int i = 0; i < length; i++) {
            currentChar = word.charAt(i);
            if (charFilter.accept(currentChar)) {//只处理合法字符
                child = current.get(currentChar);
                if (child == null) {
                    //无子类，新建一个子节点后存放下一个字符
                    child = new WordTree();
                    current.put(currentChar, child);
                }
                parent = current;
                current = child;
            }
        }
        if (null != parent) {
            parent.setEnd(currentChar);
        }
    }
    
    public List<WordInfo> matchAllWithPos(String text) {
        if (null == text) {
            return null;
        }

        List<WordInfo> foundWords = new ArrayList<>();
        WordTree current = this;
        int length = text.length();
        final Filter<Character> charFilter = this.charFilter;
        //存放查找到的字符缓存。完整出现一个词时加到foundWords中，否则清空
        final StrBuilder wordBuffer = StrUtil.strBuilder();
        char currentChar;
        for (int i = 0; i < length; i++) {
            wordBuffer.reset();
            for (int j = i; j < length; j++) {
                currentChar = text.charAt(j);
                if (!charFilter.accept(currentChar)) {
                    if (wordBuffer.length() > 0) {
                        //作为关键词中间的停顿词被当作关键词的一部分被返回
                        wordBuffer.append(currentChar);
                    } else {
                        //停顿词做为关键词的第一个字符时需要跳过
                        i++;
                    }
                    continue;
                } else if (!current.containsKey(currentChar)) {
                    //非关键字符被整体略过，重新以下个字符开始检查
                    break;
                }
                wordBuffer.append(currentChar);
                if (current.isEnd(currentChar)) {
                    //到达单词末尾，关键词成立，从此词的下一个位置开始查找
                    foundWords.add(new WordInfo(wordBuffer.toString(), i));
                    // 跳过匹配到的词
                    i = j;
                    // 当遇到第一个结尾标记就结束本轮匹配
                    break;
                }
                current = current.get(currentChar);
                if (null == current) {
                    break;
                }
            }
            current = this;
        }
        return foundWords;
    }
    
    public List<String> matchAll(String text) {
        if (null == text) {
            return null;
        }

        List<String> foundWords = new ArrayList<>();
        WordTree current = this;
        int length = text.length();
        final Filter<Character> charFilter = this.charFilter;
        //存放查找到的字符缓存。完整出现一个词时加到foundWords中，否则清空
        final StrBuilder wordBuffer = StrUtil.strBuilder();
        char currentChar;
        for (int i = 0; i < length; i++) {
            wordBuffer.reset();
            for (int j = i; j < length; j++) {
                currentChar = text.charAt(j);
                if (!charFilter.accept(currentChar)) {
                    if (wordBuffer.length() > 0) {
                        //作为关键词中间的停顿词被当作关键词的一部分被返回
                        wordBuffer.append(currentChar);
                    } else {
                        //停顿词做为关键词的第一个字符时需要跳过
                        i++;
                    }
                    continue;
                } else if (!current.containsKey(currentChar)) {
                    //非关键字符被整体略过，重新以下个字符开始检查
                    break;
                }
                wordBuffer.append(currentChar);
                if (current.isEnd(currentChar)) {
                    //到达单词末尾，关键词成立，从此词的下一个位置开始查找
                    foundWords.add(wordBuffer.toString());
                    i = j;
                    break;
                }
                current = current.get(currentChar);
                if (null == current) {
                    break;
                }
            }
            current = this;
        }
        return foundWords;
    }
    
    public String matchFirst(String text) {
        if (null == text) {
            return null;
        }

        WordTree current = this;
        int length = text.length();
        final Filter<Character> charFilter = this.charFilter;
        //存放查找到的字符缓存。完整出现一个词时加到foundWords中，否则清空
        final StrBuilder wordBuffer = StrUtil.strBuilder();
        char currentChar;
        for (int i = 0; i < length; i++) {
            wordBuffer.reset();
            for (int j = i; j < length; j++) {
                currentChar = text.charAt(j);
                if (!charFilter.accept(currentChar)) {
                    if (wordBuffer.length() > 0) {
                        //作为关键词中间的停顿词被当作关键词的一部分被返回
                        wordBuffer.append(currentChar);
                    } else {
                        //停顿词做为关键词的第一个字符时需要跳过
                        i++;
                    }
                    continue;
                } else if (!current.containsKey(currentChar)) {
                    //非关键字符被整体略过，重新以下个字符开始检查
                    break;
                }
                wordBuffer.append(currentChar);
                if (current.isEnd(currentChar)) {
                    //到达单词末尾，关键词成立，从此词的下一个位置开始查找
                    return wordBuffer.toString();
                }
                current = current.get(currentChar);
                if (null == current) {
                    break;
                }
            }
            current = this;
        }
        return null;
    }

    private boolean isEnd(Character c) {
        return this.endCharacterSet.contains(c);
    }

    private void setEnd(Character c) {
        if (null != c) {
            this.endCharacterSet.add(c);
        }
    }
}
