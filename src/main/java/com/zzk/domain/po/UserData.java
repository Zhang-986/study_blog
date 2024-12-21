package com.zzk.domain.po;


import lombok.Data;

import java.time.LocalDate;


@Data
public class UserData {
    private Integer Id;
    private String Name;      // 姓名
    private int Age;          // 年龄
    private LocalDate Date;        // 时间

    // 带参构造函数
    public UserData( Integer id,String Name, int Age, LocalDate Date) {
        this.Id =  id;
        this.Name = Name;
        this.Age = Age;
        this.Date = Date;
    }
}
