package com.zzk.service.impl;

import com.zzk.config.JdbcConfig;
import com.zzk.domain.po.UserData;
import com.zzk.service.UpdateData;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
@Slf4j
public class UpdateDataImpl implements UpdateData {
    @Override
    public void updateData(UserData userData) throws SQLException {
        Connection connection = JdbcConfig.getConnection();
        String sql = "update userdata set name = ?,age = ?,Date =? where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, userData.getName());
        preparedStatement.setInt(2,userData.getAge());
        preparedStatement.setDate(3, Date.valueOf(userData.getDate()));
        preparedStatement.setInt(4,userData.getId());
        preparedStatement.executeUpdate();
        log.info("{}",userData);
    }
}
