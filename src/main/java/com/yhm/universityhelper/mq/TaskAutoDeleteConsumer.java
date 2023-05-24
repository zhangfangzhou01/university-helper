package com.yhm.universityhelper.mq;

import cn.hutool.json.JSONObject;
import com.yhm.universityhelper.config.RabbitConfig;
import com.yhm.universityhelper.dao.TaskMapper;
import com.yhm.universityhelper.entity.po.Task;
import com.yhm.universityhelper.service.ChatService;
import com.yhm.universityhelper.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskAutoDeleteConsumer {
    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ChatService chatService;

    @Value("${task.expire-time}")
    private Integer expireTime;

    @RabbitListener(queues = RabbitConfig.DLX_QUEUE_NAME)
    public void handle(String msg) {
        final JSONObject jsonObject = JsonUtils.jsonToJsonObject(msg);
        final Long taskId = jsonObject.getLong("taskId");
        final Integer formerTaskState = jsonObject.getInt("taskState");
        final String type = jsonObject.getStr("type");
        final Long userId = jsonObject.getLong("userId");
        final Integer currentTaskState = taskMapper.selectTaskStateByTaskId(taskId);

        if (formerTaskState.equals(Task.NOT_TAKE) && "外卖".equals(type)) {
            if (currentTaskState.equals(Task.NOT_TAKE)) {
                taskMapper.deleteById(taskId);
                chatService.notificationByUserId(userId, "任务" + expireTime / 60 + "分钟未被接取, 已自动删除");
            }
        }
    }
}