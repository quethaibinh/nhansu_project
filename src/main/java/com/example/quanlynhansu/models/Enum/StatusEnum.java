package com.example.quanlynhansu.models.Enum;

public enum StatusEnum {

    Active ("Đang hoạt động"),
    Resigned ("Đã từ chức"),
    On_Leave ("Đang nghỉ phép");

    private final String statusName;
    StatusEnum(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}
