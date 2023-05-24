package com.yhm.universityhelper.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@EnableRabbit
@Configuration
public class RabbitConfig {
    public static final String TASK_AUTO_DELETE_QUEUE_NAME = "task_auto_delete_queue";
    public static final String TASK_AUTO_DELETE_EXCHANGE_NAME = "task_auto_delete_exchange";
    public static final String TASK_AUTO_DELETE_ROUTING_KEY = "task_auto_delete_routing_key";
    public static final String DLX_QUEUE_NAME = "dlx_queue";
    public static final String DLX_EXCHANGE_NAME = "dlx_exchange";
    public static final String DLX_ROUTING_KEY = "dlx_routing_key";

    @Value("${task.expire-time}")
    private Integer expireTime;

    /**
     * 死信队列
     */
    @Bean
    Queue dlxQueue() {
        return new Queue(DLX_QUEUE_NAME, true, false, false);
    }

    /**
     * 死信交换机
     */
    @Bean
    DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE_NAME, true, false);
    }

    /**
     * 绑定死信队列和死信交换机
     */
    @Bean
    Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue()).to(dlxExchange())
                .with(DLX_ROUTING_KEY);
    }

    /**
     * 普通消息队列
     */
    @Bean
    Queue taskAutoDeleteQueue() {
        Map<String, Object> args = new HashMap<>();
        //设置消息过期时间
        args.put("x-message-ttl", 1000L * expireTime);
        //设置死信交换机
        args.put("x-dead-letter-exchange", DLX_EXCHANGE_NAME);
        //设置死信 routing_key
        args.put("x-dead-letter-routing-key", DLX_ROUTING_KEY);
        return new Queue(TASK_AUTO_DELETE_QUEUE_NAME, true, false, false, args);
    }

    /**
     * 普通交换机
     */
    @Bean
    DirectExchange taskAutoDeleteExchange() {
        return new DirectExchange(TASK_AUTO_DELETE_EXCHANGE_NAME, true, false);
    }
    
    /**
     * 绑定普通队列和与之对应的交换机
     */
    @Bean
    Binding taskAutoDeleteBinding() {
        return BindingBuilder.bind(taskAutoDeleteQueue())
                .to(taskAutoDeleteExchange())
                .with(TASK_AUTO_DELETE_ROUTING_KEY);
    }
}