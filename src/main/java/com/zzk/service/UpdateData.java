package com.zzk.service;

import com.zzk.domain.po.UserData;

import java.sql.SQLException;

public interface UpdateData {
    void updateData(UserData userData) throws SQLException;
}
