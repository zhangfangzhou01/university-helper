package com.yhm.universityhelper.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    private String from;
    private String content;
    private String to;

    public Chat(String from, String content) {
        this.from = from;
        this.content = content;
    }
}
