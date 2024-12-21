package com.zzk;
import com.zzk.domain.po.UserData;
import com.zzk.service.AddData;
import com.zzk.service.UpdateData;
import com.zzk.service.impl.AddDataImpl;
import com.zzk.service.impl.UpdateDataImpl;

import java.sql.SQLException;
import java.time.LocalDate;

public class Application {
    public static void main(String[] args) throws SQLException {
        UpdateData updateData = new UpdateDataImpl();
        updateData.updateData(new UserData(1,"zzk",16,LocalDate.now()));
    }
}
