package com.yhm.universityhelper.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.yhm.universityhelper.dao.RoleMapper;
import com.yhm.universityhelper.dao.UserMapper;
import com.yhm.universityhelper.dao.UserRoleMapper;
import com.yhm.universityhelper.entity.dto.LoginUser;
import com.yhm.universityhelper.entity.po.Role;
import com.yhm.universityhelper.entity.po.User;
import com.yhm.universityhelper.entity.po.UserRole;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    UserRoleMapper userRoleMapper;

    @Override
    public @NotNull UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectByUsername(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        List<GrantedAuthority> authorities = this.getUserAuthorities(user.getUserId());
        return new LoginUser(user.getUserId(), user.getUsername(), user.getPassword(), authorities);
    }

    public @NotNull List<GrantedAuthority> getUserAuthorities(Integer userId) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<UserRole> userRoles = userRoleMapper.listByUserId(userId);
        for (UserRole userRole : userRoles) {
            Role role = roleMapper.selectById(userRole.getRoleId());
            authorities.add(new SimpleGrantedAuthority(role.getRolename()));
        }
        return authorities;
    }
}
