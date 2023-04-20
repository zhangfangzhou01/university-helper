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

    // 关注
    boolean follow(String follower, String followed);

    // 取消关注
    boolean unfollow(String follower, String followed);

    // 获取关注列表
    List<String> getFollowedList(String username);

    // 获取粉丝列表
    List<String> getFollowerList(String username);

    // 获取关注数
    Long getFollowedCount(String username);

    // 获取粉丝数
    Long getFollowerCount(String username);

    // 拉黑
    boolean block(String blacker, String blacked);

    // 取消拉黑
    boolean unblock(String blacker, String blacked);

    // 获取黑名单
    List<String> getBlockedList(String username);

    // 获取黑名单数
    Long getBlockedCount(String username);
}
