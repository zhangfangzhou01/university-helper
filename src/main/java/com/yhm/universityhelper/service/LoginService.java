package com.yhm.universityhelper.service;

import org.springframework.transaction.annotation.Transactional;

public interface LoginService {
    boolean register(String username, String password);

    boolean changePassword(String username, String oldPassword, String newPassword);
}
