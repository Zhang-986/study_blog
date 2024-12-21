package com.zzk.service.impl;

import com.zzk.config.JdbcConfig;
import com.zzk.domain.po.UserData;
import com.zzk.service.AddData;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
@Slf4j
public class AddDataImpl implements AddData {
    @Override
    public void addUser(UserData userData) throws SQLException {
        Connection connection = JdbcConfig.getConnection();
        String sql = " insert into userdata (Name, Age, Date) values (?,?,?) ";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, userData.getName());
        preparedStatement.setInt(2, userData.getAge());
        preparedStatement.setDate(3, Date.valueOf(userData.getDate()));
        int i = preparedStatement.executeUpdate();
        if(i==1){
            log.info("数据添加成功---(userdata,{},{},{})",userData.getName(),userData.getAge(),userData.getDate());
        }
    }
}
