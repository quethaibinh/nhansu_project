package com.example.quanlynhansu.services;

import com.example.quanlynhansu.models.DTO.UpdateInfoOfEmployeeDTO;
import com.example.quanlynhansu.models.DTO.UpdatePasswordDTO;
import com.example.quanlynhansu.models.response.EmployeeResponse;

import java.text.ParseException;
import java.util.List;

public interface EmployeeService {

    List<EmployeeResponse> finddAll();
    EmployeeResponse findMyInfo();
    UpdateInfoOfEmployeeDTO updateCurrentUserInfo(UpdateInfoOfEmployeeDTO updateInfoOfEmployeeDTO) throws ParseException;
    String updatePassword(UpdatePasswordDTO updatePasswordDTO);

}
