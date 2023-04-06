package com.yhm.universityhelper.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yhm.universityhelper.entity.po.User;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yhm
 * @since 2023-02-26
 */

public interface UserService extends IService<User> {
    boolean update(JSONObject json);

    Map<String, Object> select(JSONArray json);

    Map<Long, List<String>> delete(String username);

    boolean register(String username, String password);

    boolean changePassword(String username, String oldPassword, String newPassword);

    boolean ban(String username, boolean ban);

    boolean setRole(String username, String role);
}
