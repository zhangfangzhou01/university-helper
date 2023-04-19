package com.yhm.universityhelper.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhm.universityhelper.dao.PostMapper;
import com.yhm.universityhelper.entity.po.Post;
import com.yhm.universityhelper.service.ForumService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 论坛表，存储论坛信息 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-04-19
 */
@Service
public class ForumServiceImpl extends ServiceImpl<PostMapper, Post> implements ForumService {

}
