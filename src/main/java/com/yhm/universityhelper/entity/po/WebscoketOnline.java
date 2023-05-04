package com.yhm.universityhelper.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uh_webscokect_online")
@ApiModel(value = "WebscokectOnline对象", description = "")
public class WebscoketOnline implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("username")
    private String username;

    @TableField("isOnline")
    private Boolean isOnline;
    
    @TableField("onlineTime")
    private LocalDateTime onlineTime;
    
    public WebscoketOnline(String username, Boolean isOnline) {
        this.username = username;
        this.isOnline = isOnline;
        this.onlineTime = LocalDateTime.now();
    }
}
