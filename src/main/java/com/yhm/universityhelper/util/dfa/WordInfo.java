package com.yhm.universityhelper.util.dfa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Component
public class WordInfo {
    private String word;
    private int start;
}
