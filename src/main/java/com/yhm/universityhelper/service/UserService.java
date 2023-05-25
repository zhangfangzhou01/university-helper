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
    
    // 传用户名的接口
    boolean register(String username, String password);

    boolean changePassword(String username, String oldPassword, String newPassword);

    // 传用户id的接口
    boolean update(JSONObject json);

    boolean changeEmail(Long userId, String email);

    Map<String, Object> select(JSONArray json);

    Map<Long, List<String>> delete(Long userId);

    boolean ban(Long userId, boolean ban);

    boolean setRole(Long userId, String role);

    // 关注
    boolean follow(Long followerId, Long followedId);

    // 取消关注
    boolean unfollow(Long followerId, Long followedId);

    // 获取关注列表
    List<String> selectFollowedList(Long userId);

    // 获取粉丝列表
    List<String> selectFollowerList(Long userId);

    // 获取关注数
    Long selectFollowedCount(Long userId);

    // 获取粉丝数
    Long selectFollowerCount(Long userId);

    // 拉黑
    boolean block(Long blockerId, Long blockedId);

    // 取消拉黑
    boolean unblock(Long blockerId, Long blockedId);

    // 获取黑名单
    List<String> selectBlockedList(Long userId);

    // 获取黑名单数
    Long selectBlockedCount(Long userId);
    
    void sendEmailCode(String to);
}
