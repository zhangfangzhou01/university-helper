package com.yhm.universityhelper.mq;

import com.yhm.universityhelper.config.RabbitConfig;
import com.yhm.universityhelper.dao.TaskMapper;
import com.yhm.universityhelper.entity.dto.TaskMessage;
import com.yhm.universityhelper.entity.po.Task;
import com.yhm.universityhelper.service.ChatService;
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
    public void handle(TaskMessage taskMessage) {
        final Long taskId = taskMessage.getTaskId();
        final Integer formerTaskState = taskMessage.getTaskState();
        final String type = taskMessage.getType();
        final Long userId = taskMessage.getUserId();
        final Integer currentTaskState = taskMapper.selectTaskStateByTaskId(taskId);
    
        if (formerTaskState.equals(Task.NOT_TAKE) && "外卖".equals(type)) {
            if (currentTaskState.equals(Task.NOT_TAKE)) {
                taskMapper.deleteById(taskId);
                chatService.notificationByUserId(userId, "任务" + expireTime / 60 + "分钟未被接取, 已自动删除");
            }
        }
    }
}