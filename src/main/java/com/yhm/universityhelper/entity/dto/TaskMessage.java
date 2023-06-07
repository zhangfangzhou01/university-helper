package com.yhm.universityhelper.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskMessage {
    private Long taskId;
    private Integer taskState;
    private String type;
    private Long userId;
}
