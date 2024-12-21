package com.zzk.service.impl;

import com.zzk.config.JdbcConfig;
import com.zzk.service.DeleteData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteDataImpl implements DeleteData {
    @Override
    public void deleteData(Integer id) throws SQLException {
        Connection connection = JdbcConfig.getConnection();
        String sql = "delete from userData where ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,id);
        preparedStatement.executeUpdate();

    }
}
