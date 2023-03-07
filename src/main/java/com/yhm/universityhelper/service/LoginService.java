package com.yhm.universityhelper.service;

public interface LoginService {
    boolean register(String username, String password);
    boolean changePassword(String username, String oldPassword, String newPassword);
}
