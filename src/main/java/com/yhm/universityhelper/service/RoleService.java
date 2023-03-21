package com.yhm.universityhelper.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yhm.universityhelper.entity.po.Role;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yhm
 * @since 2023-02-26
 */

@Transactional
public interface RoleService extends IService<Role> {

}
