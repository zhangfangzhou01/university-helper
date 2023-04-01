package com.yhm.universityhelper.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yhm.universityhelper.entity.dto.ChatUser;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uh_chat")
@ApiModel(value = "Chat", description = "")
public class Chat implements Serializable {
    private ChatUser from;

    private ChatUser to;

    @TableId(value = "chatId", type = IdType.AUTO)
    private Long chatId;

    @TableField("fromUsername")
    private String fromUsername;

    @TableField("toUsername")
    private String toUsername;

    @TableField("content")
    private String content;

    @TableField("time")
    private LocalDateTime time;

    public Chat(ChatUser from, String content) {
        this.from = from;
        this.content = content;
        this.time = LocalDateTime.now();
        this.fromUsername = from.getUsername();
        this.toUsername = "all";
    }

    public Chat(String fromUsername, String content) {
        this.fromUsername = fromUsername;
        this.toUsername = "all";
        this.content = content;
        this.time = LocalDateTime.now();
    }

    public Chat(ChatUser from, String content, LocalDateTime time) {
        this.from = from;
        this.content = content;
        this.time = time;
        this.fromUsername = from.getUsername();
        this.toUsername = "all";
    }

    public Chat(String fromUsername, String content, LocalDateTime time) {
        this.fromUsername = fromUsername;
        this.content = content;
        this.time = time;
        this.toUsername = "all";
    }

    public Chat(ChatUser from, ChatUser to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.fromUsername = from.getUsername();
        this.toUsername = to.getUsername();
        this.time = LocalDateTime.now();
    }

    public Chat(String fromUsername, String toUsername, String content) {
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
        this.content = content;
        this.time = LocalDateTime.now();
    }

    public Chat(ChatUser from, ChatUser to, String content, LocalDateTime time) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.fromUsername = from.getUsername();
        this.toUsername = to.getUsername();
        this.time = time;
    }

    public Chat(String fromUsername, String toUsername, String content, LocalDateTime time) {
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
        this.content = content;
        this.time = time;
    }
}
