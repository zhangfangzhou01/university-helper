package com.yhm.universityhelper.dao.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.yhm.universityhelper.entity.po.Post;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@EqualsAndHashCode(callSuper = true)
@Component
@Scope("prototype")
public class CustomPostWrapper extends CustomWrapper {
    private final QueryWrapper<Post> wrapper = new QueryWrapper<>();

    public LambdaQueryWrapper<Post> getLambdaQueryWrapper() {
        return wrapper.lambda();
    }
    
    public static OrderItem prioritySort() {
        return null;
    }

}
