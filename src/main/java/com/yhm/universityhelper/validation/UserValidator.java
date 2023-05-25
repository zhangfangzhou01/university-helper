package com.yhm.universityhelper.validation;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.lang.Validator;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yhm.universityhelper.dao.BlacklistMapper;
import com.yhm.universityhelper.dao.FollowMapper;
import com.yhm.universityhelper.dao.UserMapper;
import com.yhm.universityhelper.dao.UserRoleMapper;
import com.yhm.universityhelper.entity.po.Blacklist;
import com.yhm.universityhelper.entity.po.Follow;
import com.yhm.universityhelper.entity.po.User;
import com.yhm.universityhelper.entity.po.UserRole;
import com.yhm.universityhelper.util.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserValidator extends CustomValidator {
    public static void register(String username, String password) {
        Optional.ofNullable(username)
                .map(u -> Validator.validateNumber(u, "用户名不合法"))
                .map(u -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUsername, u)))
                .map(exists -> Validator.validateFalse(exists, "用户名已存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户名"));

        Optional.ofNullable(password)
                .map(p -> Validator.validateNotEmpty(p, "密码不能为空"))
                .map(p -> CustomValidator.validateBetween("password", p, 6, 255))
                .orElseThrow(() -> new ValidateException("必须提供密码"));
    }

    public static void changePassword(String username, String oldPassword, String newPassword) {
        Optional.ofNullable(username)
                .map(u -> Validator.validateNumber(u, "用户名不合法"))
                .map(u -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUsername, u)))
                .map(exists -> Validator.validateTrue(exists, "用户名不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户名"));

        Optional.ofNullable(oldPassword)
                .map(p -> Validator.validateNotEmpty(p, "旧密码不能为空"))
                .map(p -> CustomValidator.validateBetween("oldPassword", p, 6, 255))
                .orElseThrow(() -> new ValidateException("必须提供旧密码"));

        Optional.ofNullable(newPassword)
                .map(p -> Validator.validateNotEmpty(p, "新密码不能为空"))
                .map(p -> CustomValidator.validateBetween("newPassword", p, 6, 255))
                .map(p -> CustomValidator.validateNotEqual(p, oldPassword, "新密码不能与旧密码相同"))
                .map(p -> Validator.validateEqual(Boolean.TRUE, BeanUtils.getBean(BCryptPasswordEncoder.class).matches(oldPassword, BeanUtils.getBean(UserMapper.class).selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username)).getPassword()), "旧密码错误"))
                .orElseThrow(() -> new ValidateException("必须提供新密码"));
    }


    public static void update(JSONObject user) {
        Optional.ofNullable(user.getLong("userId"))
                .map(id -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, id)))
                .map(exists -> Validator.validateTrue(exists, "用户不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户id"));

        Optional.ofNullable(user.getStr("username"))
                .map(username -> Validator.validateNull(username, "禁止修改用户名"));

        Optional.ofNullable(user.getStr("description"))
                .map(description -> CustomValidator.validateBetween("description", description, 0, 255));

        Optional.ofNullable(user.getStr("location"))
                .map(location -> CustomValidator.validateBetween("location", location, 0, 255));

        Optional.ofNullable(user.getStr("avatar"))
                .map(avatar -> CustomValidator.validateLinuxPath("avatar", avatar));

        Optional.ofNullable(user.getStr("phone"))
                .map(phone -> Validator.validateMobile(phone, "手机号不合法"));

        Optional.ofNullable(user.getStr("email"))
                .map(email -> Validator.validateEmail(email, "邮箱不合法"))
                .map(email -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getEmail, email)))
                .map(exists -> Validator.validateTrue(exists, "邮箱已被注册"));

        Optional.ofNullable(user.getStr("sex"))
                .map(sex -> Validator.validateMatchRegex("[男女]", sex, "性别只能是男或女"));

        Optional.ofNullable(user.getStr("password"))
                .map(password -> Validator.validateNull(password, "禁止修改密码"));

        Optional.ofNullable(user.getStr("nickname"))
                .map(nickname -> CustomValidator.validateBetween("nickname", nickname, 0, 255));

        Optional.ofNullable(user.getStr("school"))
                .map(school -> CustomValidator.validateBetween("school", school, 0, 255));

        Optional.ofNullable(user.getInt("score"))
                .map(score -> Validator.validateNull(score, "禁止修改积分"));

        Optional.ofNullable(user.getBool("banned"))
                .map(banned -> Validator.validateNull(banned, "禁止修改封禁状态"));

        Optional.ofNullable(user.getInt("passwordErrorCount"))
                .map(passwordErrorCount -> Validator.validateNull(passwordErrorCount, "禁止修改密码错误次数"));

        Optional.ofNullable(user.getStr("unlockTime"))
                .map(unlockTime -> Validator.validateNull(unlockTime, "禁止修改解封时间"));

        Optional.ofNullable(user.getStr("createTime"))
                .map(createTime -> Validator.validateNull(createTime, "禁止修改创建时间"));

        Optional.ofNullable(user.getStr("email"))
                .map(emailCode -> Validator.validateNull(emailCode, "禁止修改邮箱"));
    }

    public static void changeEmail(Long userId, String email) {
        Optional.ofNullable(userId)
                .map(id -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, id)))
                .map(exists -> Validator.validateTrue(exists, "用户不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户id"));
        
        Optional.ofNullable(email)
                .map(e -> Validator.validateEmail(e, "邮箱不合法"))
                .map(e -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getEmail, e)))
                .map(exists -> Validator.validateFalse(exists, "邮箱已被注册"))
                .orElseThrow(() -> new ValidateException("必须提供邮箱"));
    }

    public static void ban(Long userId, boolean banned) {
        Optional.ofNullable(userId)
                .map(id -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, id)))
                .map(exists -> Validator.validateTrue(exists, "用户不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户id"));
    }

    public static void delete(Long userId) {
        Optional.ofNullable(userId)
                .map(id -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, id)))
                .map(exists -> Validator.validateTrue(exists, "用户不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户id"));
    }

    public static void setRole(Long userId, String role) {
        Optional.ofNullable(userId)
                .map(id -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, id)))
                .map(exists -> Validator.validateTrue(exists, "用户不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户id"));

        Optional.ofNullable(role)
                .map(r -> Validator.validateMatchRegex("^(admin|user)$", r, "角色名不合法"))
                .orElseThrow(() -> new ValidateException("必须提供角色名"));

        Validator.validateTrue(BeanUtils.getBean(UserRoleMapper.class).exists(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId)), "用户角色不存在");
    }

    public static void select(JSONArray userIds) {
        Optional.ofNullable(userIds)
                .map(u -> Validator.validateMatchRegex(ID_JSON_ARRAY_REGEX, u.toString(), "用户id列表不合法"))
                .orElseThrow(() -> new ValidateException("必须提供用户id列表"));
    }

    public static void follow(Long followerId, Long followedId) {
        Optional.ofNullable(followerId)
                .map(id -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, id)))
                .map(exists -> Validator.validateTrue(exists, "关注者不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户id"));
        
        Optional.ofNullable(followedId)
                .map(id -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, id)))
                .map(exists -> Validator.validateTrue(exists, "被关注者不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户id"));

        Validator.validateFalse(followerId.equals(followedId), "不能关注自己");
        Validator.validateFalse(BeanUtils.getBean(FollowMapper.class).exists(new LambdaQueryWrapper<Follow>().eq(Follow::getFollowerId, followerId).eq(Follow::getFollowedId, followedId)), "已经关注该用户");
    }

    public static void unfollow(Long followerId, Long followedId) {
        Optional.ofNullable(followerId)
                .map(id -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, id)))
                .map(exists -> Validator.validateTrue(exists, "关注者不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户id"));
        
        Optional.ofNullable(followedId)
                .map(id -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, id)))
                .map(exists -> Validator.validateTrue(exists, "被关注者不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户id"));

        Validator.validateFalse(followerId.equals(followedId), "不能取关自己");
        Validator.validateTrue(BeanUtils.getBean(FollowMapper.class).exists(new LambdaQueryWrapper<Follow>().eq(Follow::getFollowerId, followerId).eq(Follow::getFollowedId, followedId)), "未关注该用户");
    }

    public static void selectFollowedList(Long userId) {
        Optional.ofNullable(userId)
                .map(id -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, id)))
                .map(exists -> Validator.validateTrue(exists, "用户不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户id"));
    }

    public static void selectFollowerList(Long userId) {
        Optional.ofNullable(userId)
                .map(id -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, id)))
                .map(exists -> Validator.validateTrue(exists, "用户不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户id"));
    }

    public static void selectFollowedCount(Long userId) {
        Optional.ofNullable(userId)
                .map(id -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, id)))
                .map(exists -> Validator.validateTrue(exists, "用户不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户id"));
    }

    public static void selectFollowerCount(Long userId) {
        Optional.ofNullable(userId)
                .map(id -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, id)))
                .map(exists -> Validator.validateTrue(exists, "用户不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户id"));
    }

    public static void block(Long blockerId, Long blockedId) {
        Optional.ofNullable(blockerId)
                .map(id -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, id)))
                .map(exists -> Validator.validateTrue(exists, "拉黑者不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户id"));
        
        Optional.ofNullable(blockedId)
                .map(id -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, id)))
                .map(exists -> Validator.validateTrue(exists, "被拉黑者不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户id"));

        Validator.validateFalse(blockerId.equals(blockedId), "禁止拉黑自己");
        Validator.validateFalse(BeanUtils.getBean(BlacklistMapper.class).exists(new LambdaQueryWrapper<Blacklist>().eq(Blacklist::getBlockerId, blockerId).eq(Blacklist::getBlockedId, blockedId)), "已经屏蔽该用户");
    }

    public static void unblock(Long blockerId, Long blockedId) {
        Optional.ofNullable(blockerId)
                .map(id -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, id)))
                .map(exists -> Validator.validateTrue(exists, "拉黑者不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户id"));
        
        Optional.ofNullable(blockedId)
                .map(id -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, id)))
                .map(exists -> Validator.validateTrue(exists, "被拉黑者不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户id"));

        Validator.validateFalse(blockerId.equals(blockedId), "禁止解除拉黑自己");
        Validator.validateTrue(BeanUtils.getBean(BlacklistMapper.class).exists(new LambdaQueryWrapper<Blacklist>().eq(Blacklist::getBlockerId, blockerId).eq(Blacklist::getBlockedId, blockedId)), "未屏蔽该用户");
    }

    public static void selectBlockedList(Long userId) {
        Optional.ofNullable(userId)
                .map(id -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, id)))
                .map(exists -> Validator.validateTrue(exists, "用户不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户id"));
    }

    public static void selectBlockedCount(Long userId) {
        Optional.ofNullable(userId)
                .map(id -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUserId, id)))
                .map(exists -> Validator.validateTrue(exists, "用户不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户id"));
    }

    public static void sendEmailCode(String email) {
        Optional.ofNullable(email)
                .map(e -> Validator.validateEmail(e, "邮箱不合法"))
                .orElseThrow(() -> new ValidateException("必须提供邮箱"));
    }
}
