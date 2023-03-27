package com.yhm.universityhelper.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhm.universityhelper.dao.UsertaketaskMapper;
import com.yhm.universityhelper.entity.po.Usertaketask;
import com.yhm.universityhelper.service.UsertaketaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 任务与其接受者的表 服务实现类
 * </p>
 *
 * @author yhm
 * @since 2023-03-04
 */

@Transactional
@Service
public class UsertaketaskServiceImpl extends ServiceImpl<UsertaketaskMapper, Usertaketask> implements UsertaketaskService {
}
