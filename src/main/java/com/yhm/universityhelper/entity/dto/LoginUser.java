package com.yhm.universityhelper.entity.dto;

import cn.hutool.core.lang.Assert;
import com.yhm.universityhelper.util.IpUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class LoginUser implements UserDetails {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(LoginUser.class);
    private Long userId;
    private String password;
    private String username;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private int passwordErrorCount;
    private LocalDateTime unlockTime;
    private String region;

    public LoginUser(Long userId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this(userId, username, password, true, true, true, true, authorities);
        HttpServletRequest request = ((ServletRequestAttributes)Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        this.region = IpUtils.getRegion(request);
    }

    public LoginUser(Long userId, String username, String password, boolean banned, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.enabled = !banned;
        this.accountNonExpired = true;
        this.credentialsNonExpired = true;
        this.accountNonLocked = this.isAccountNonLocked();
        this.authorities = authorities;
        HttpServletRequest request = ((ServletRequestAttributes)Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        this.region = IpUtils.getRegion(request);
    }

    public LoginUser(Long userId, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        Assert.isTrue(username != null && (!"".equals(username)) && password != null, "Cannot pass null or empty values to constructor");
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = authorities;
        HttpServletRequest request = ((ServletRequestAttributes)Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        this.region = IpUtils.getRegion(request);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
