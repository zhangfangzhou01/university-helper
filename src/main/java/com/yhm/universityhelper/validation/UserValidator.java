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

import java.util.Optional;

public class UserValidator extends CustomValidator {

    public static void update(JSONObject user) {
        Optional.ofNullable(user.getLong("userId"))
                .map(userId -> Validator.validateNull(userId, "禁止修改用户ID"));

        Optional.ofNullable(user.getStr("username"))
                .map(username -> Validator.validateNumber(username, "用户名不合法"))
                .map(username -> BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUsername, username)))
                .map(exists -> Validator.validateTrue(exists, "用户名不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户名"));

        Optional.ofNullable(user.getStr("avatar"))
                .map(avatar -> Validator.validateUrl(avatar, "头像地址应为URL"));

        Optional.ofNullable(user.getStr("description"))
                .map(description -> CustomValidator.validateBetween("description", description, 0, 255));

        Optional.ofNullable(user.getStr("location"))
                .map(location -> CustomValidator.validateBetween("location", location, 0, 255));

        Optional.ofNullable(user.getStr("phone"))
                .map(phone -> Validator.validateMobile(phone, "手机号不合法"));

        Optional.ofNullable(user.getStr("email"))
                .map(email -> Validator.validateEmail(email, "邮箱不合法"));

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
    }

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

    public static void ban(String username, boolean banned) {
        Optional.ofNullable(username)
                .map(u -> Validator.validateNumber(u, "用户名不合法"))
                .map(u -> Validator.validateNotNull(BeanUtils.getBean(UserMapper.class).selectByUsername(u), "用户名不存在"))
                .map(user -> Validator.validateTrue(user.getBanned() != banned, "用户已经处于该状态"))
                .orElseThrow(() -> new ValidateException("必须提供用户名"));
    }

    public static void delete(String username) {
        Optional.ofNullable(username)
                .map(u -> Validator.validateNumber(u, "用户名不合法"))
                .map(u -> Validator.validateTrue(BeanUtils.getBean(UserMapper.class).exists(new LambdaQueryWrapper<User>().eq(User::getUsername, u)), "用户名不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户名"));
    }

    public static void setRole(String username, String role) {
        Optional.ofNullable(username)
                .map(u -> Validator.validateNumber(u, "用户名不合法"))
                .map(u -> Validator.validateNotNull(BeanUtils.getBean(UserMapper.class).selectByUsername(u), "用户名不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户名"));

        Optional.ofNullable(role)
                .map(r -> Validator.validateMatchRegex("^(admin|user)$", r, "角色名不合法"))
                .orElseThrow(() -> new ValidateException("必须提供角色名"));

        Validator.validateTrue(BeanUtils.getBean(UserRoleMapper.class).exists(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, BeanUtils.getBean(UserMapper.class).selectUserIdByUsername(username))), "用户不存在该角色");
    }

    public static void select(JSONArray usernames) {
        Optional.ofNullable(usernames)
                .map(u -> Validator.validateMatchRegex(JSON_ARRAY_REGEX, u.toString(), "用户名列表不合法"))
                .orElseThrow(() -> new ValidateException("必须提供用户名列表"));
    }

    public static void follow(String follower, String followed) {
        Optional.ofNullable(follower)
                .map(u -> Validator.validateNumber(u, "用户名不合法"))
                .map(u -> Validator.validateNotNull(BeanUtils.getBean(UserMapper.class).selectByUsername(u), "用户名不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户名"));

        Optional.ofNullable(followed)
                .map(u -> Validator.validateNumber(u, "用户名不合法"))
                .map(u -> Validator.validateNotNull(BeanUtils.getBean(UserMapper.class).selectByUsername(u), "用户名不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户名"));

        Long followerId = BeanUtils.getBean(UserMapper.class).selectUserIdByUsername(follower);
        Long followedId = BeanUtils.getBean(UserMapper.class).selectUserIdByUsername(followed);

        Validator.validateFalse(BeanUtils.getBean(FollowMapper.class).exists(new LambdaQueryWrapper<Follow>().eq(Follow::getFollowerId, followerId).eq(Follow::getFollowedId, followedId)), "已经关注该用户");
    }

    public static void unfollow(String follower, String followed) {
        Optional.ofNullable(follower)
                .map(u -> Validator.validateNumber(u, "用户名不合法"))
                .map(u -> Validator.validateNotNull(BeanUtils.getBean(UserMapper.class).selectByUsername(u), "用户名不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户名"));

        Optional.ofNullable(followed)
                .map(u -> Validator.validateNumber(u, "用户名不合法"))
                .map(u -> Validator.validateNotNull(BeanUtils.getBean(UserMapper.class).selectByUsername(u), "用户名不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户名"));

        Long followerId = BeanUtils.getBean(UserMapper.class).selectUserIdByUsername(follower);
        Long followedId = BeanUtils.getBean(UserMapper.class).selectUserIdByUsername(followed);

        Validator.validateTrue(BeanUtils.getBean(FollowMapper.class).exists(new LambdaQueryWrapper<Follow>().eq(Follow::getFollowerId, followerId).eq(Follow::getFollowedId, followedId)), "未关注该用户");
    }

    public static void getFollowedList(String username) {
        Optional.ofNullable(username)
                .map(u -> Validator.validateNumber(u, "用户名不合法"))
                .map(u -> Validator.validateNotNull(BeanUtils.getBean(UserMapper.class).selectByUsername(u), "用户名不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户名"));
    }

    public static void getFollowerList(String username) {
        Optional.ofNullable(username)
                .map(u -> Validator.validateNumber(u, "用户名不合法"))
                .map(u -> Validator.validateNotNull(BeanUtils.getBean(UserMapper.class).selectByUsername(u), "用户名不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户名"));
    }

    public static void getFollowedCount(String username) {
        Optional.ofNullable(username)
                .map(u -> Validator.validateNumber(u, "用户名不合法"))
                .map(u -> Validator.validateNotNull(BeanUtils.getBean(UserMapper.class).selectByUsername(u), "用户名不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户名"));
    }

    public static void getFollowerCount(String username) {
        Optional.ofNullable(username)
                .map(u -> Validator.validateNumber(u, "用户名不合法"))
                .map(u -> Validator.validateNotNull(BeanUtils.getBean(UserMapper.class).selectByUsername(u), "用户名不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户名"));
    }

    public static void block(String blocker, String blocked) {
        Optional.ofNullable(blocker)
                .map(u -> Validator.validateNumber(u, "用户名不合法"))
                .map(u -> Validator.validateNotNull(BeanUtils.getBean(UserMapper.class).selectByUsername(u), "用户名不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户名"));

        Optional.ofNullable(blocked)
                .map(u -> Validator.validateNumber(u, "用户名不合法"))
                .map(u -> Validator.validateNotNull(BeanUtils.getBean(UserMapper.class).selectByUsername(u), "用户名不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户名"));

        Long blockerId = BeanUtils.getBean(UserMapper.class).selectUserIdByUsername(blocker);
        Long blockedId = BeanUtils.getBean(UserMapper.class).selectUserIdByUsername(blocked);

        Validator.validateFalse(BeanUtils.getBean(BlacklistMapper.class).exists(new LambdaQueryWrapper<Blacklist>().eq(Blacklist::getBlockerId, blockerId).eq(Blacklist::getBlockedId, blockedId)), "已经屏蔽该用户");
    }

    public static void unblock(String blocker, String blocked) {
        Optional.ofNullable(blocker)
                .map(u -> Validator.validateNumber(u, "用户名不合法"))
                .map(u -> Validator.validateNotNull(BeanUtils.getBean(UserMapper.class).selectByUsername(u), "用户名不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户名"));

        Optional.ofNullable(blocked)
                .map(u -> Validator.validateNumber(u, "用户名不合法"))
                .map(u -> Validator.validateNotNull(BeanUtils.getBean(UserMapper.class).selectByUsername(u), "用户名不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户名"));

        Long blockerId = BeanUtils.getBean(UserMapper.class).selectUserIdByUsername(blocker);
        Long blockedId = BeanUtils.getBean(UserMapper.class).selectUserIdByUsername(blocked);

        Validator.validateTrue(BeanUtils.getBean(BlacklistMapper.class).exists(new LambdaQueryWrapper<Blacklist>().eq(Blacklist::getBlockerId, blockerId).eq(Blacklist::getBlockedId, blockedId)), "未屏蔽该用户");
    }

    public static void getBlockedList(String username) {
        Optional.ofNullable(username)
                .map(u -> Validator.validateNumber(u, "用户名不合法"))
                .map(u -> Validator.validateNotNull(BeanUtils.getBean(UserMapper.class).selectByUsername(u), "用户名不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户名"));
    }

    public static void getBlockerList(String username) {
        Optional.ofNullable(username)
                .map(u -> Validator.validateNumber(u, "用户名不合法"))
                .map(u -> Validator.validateNotNull(BeanUtils.getBean(UserMapper.class).selectByUsername(u), "用户名不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户名"));
    }

    public static void getBlockedCount(String username) {
        Optional.ofNullable(username)
                .map(u -> Validator.validateNumber(u, "用户名不合法"))
                .map(u -> Validator.validateNotNull(BeanUtils.getBean(UserMapper.class).selectByUsername(u), "用户名不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户名"));
    }

    public static void getBlockerCount(String username) {
        Optional.ofNullable(username)
                .map(u -> Validator.validateNumber(u, "用户名不合法"))
                .map(u -> Validator.validateNotNull(BeanUtils.getBean(UserMapper.class).selectByUsername(u), "用户名不存在"))
                .orElseThrow(() -> new ValidateException("必须提供用户名"));
    }
}
