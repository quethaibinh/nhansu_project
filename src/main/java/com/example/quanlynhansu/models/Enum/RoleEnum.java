package com.example.quanlynhansu.models.Enum;

import org.apache.catalina.Manager;

public enum RoleEnum {

    Admin ("admin"),
    HR ("HR"),
    Employee ("Nhân viên"),
    Manager ("Quản lý");

    private final String roleEnumName;

    RoleEnum(String roleEnumName) {
        this.roleEnumName = roleEnumName;
    }

    public  String getRoleEnumName(){
        return roleEnumName;
    }

}
