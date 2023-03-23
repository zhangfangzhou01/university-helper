package com.yhm.universityhelper.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yhm.universityhelper.entity.dto.ChatUser;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("uh_chat")
@ApiModel(value = "Chat", description = "")
public class Chat implements Serializable {
    private ChatUser from;
    private ChatUser to;
    @TableId(value = "chatId", type = IdType.AUTO)
    private Integer chatId;
    @TableField("fromUsername")
    private String fromUsername;
    @TableField("toUsername")
    private String toUsername;
    @TableField("content")
    private String content;

    public Chat(ChatUser from, String content) {
        this.from = from;
        this.content = content;
    }

    public Chat(ChatUser from, ChatUser to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.fromUsername = from.getUsername();
        this.toUsername = to.getUsername();
    }

    public Chat(String fromUsername, String toUsername, String content) {
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
        this.content = content;
    }
}
