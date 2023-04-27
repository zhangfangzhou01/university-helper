package com.yhm.universityhelper.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.yhm.universityhelper.dao.RoleMapper;
import com.yhm.universityhelper.dao.UserMapper;
import com.yhm.universityhelper.dao.UserRoleMapper;
import com.yhm.universityhelper.entity.dto.LoginUser;
import com.yhm.universityhelper.entity.po.Role;
import com.yhm.universityhelper.entity.po.User;
import com.yhm.universityhelper.entity.po.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public LoginUser loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectByUsername(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        List<GrantedAuthority> authorities = this.getUserAuthorities(user.getUserId());
        return new LoginUser(user.getUserId(), user.getUsername(), user.getPassword(), user.getBanned(), authorities);
    }
    
    public LoginUser loadUserByEmail(String email) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        List<GrantedAuthority> authorities = this.getUserAuthorities(user.getUserId());
        return new LoginUser(user.getUserId(), user.getUsername(), user.getPassword(), user.getBanned(), authorities);
    }
    
    public LoginUser loadUserByUser(User user) throws UsernameNotFoundException {
        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        List<GrantedAuthority> authorities = this.getUserAuthorities(user.getUserId());
        return new LoginUser(user.getUserId(), user.getUsername(), user.getPassword(), user.getBanned(), authorities);
    }

    public List<GrantedAuthority> getUserAuthorities(Long userId) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<UserRole> userRoles = userRoleMapper.listByUserId(userId);  // 会出现一个用户既是用户也是管理员的情况
        for (UserRole userRole : userRoles) {
            Role role = roleMapper.selectById(userRole.getRoleId());
            authorities.add(new SimpleGrantedAuthority(role.getRolename()));
        }
        return authorities;
    }
}
