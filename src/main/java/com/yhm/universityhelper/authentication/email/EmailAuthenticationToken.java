package com.yhm.universityhelper.authentication.email;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class EmailAuthenticationToken extends AbstractAuthenticationToken {
    private Object principal;
    private Object credentials;
    
    public EmailAuthenticationToken() {
        super(null);
    }
    
    public EmailAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
        this.credentials = null;
        setAuthenticated(false);
    }

    public EmailAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = null;
        super.setAuthenticated(true);
    }

    public EmailAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }
}
