package com.yhm.universityhelper.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhm.universityhelper.dao.TaskTagsMapper;
import com.yhm.universityhelper.entity.po.TaskTags;
import com.yhm.universityhelper.service.TaskTagsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yhm
 * @since 2023-03-26
 */

@Transactional
@Service
public class TaskTagsServiceImpl extends ServiceImpl<TaskTagsMapper, TaskTags> implements TaskTagsService {

}
