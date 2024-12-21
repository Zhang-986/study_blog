package com.zzk.service;

import com.zzk.domain.po.UserData;

import java.sql.SQLException;

public interface AddData {
    void addUser(UserData userData) throws SQLException;
}
