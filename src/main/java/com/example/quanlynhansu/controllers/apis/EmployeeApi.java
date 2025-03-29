package com.example.quanlynhansu.controllers.apis;

import com.example.quanlynhansu.models.DTO.UpdateInfoOfEmployeeDTO;
import com.example.quanlynhansu.models.DTO.UpdatePasswordDTO;
import com.example.quanlynhansu.models.response.EmployeeResponse;
import com.example.quanlynhansu.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;

@RestController
@RequestMapping("/employee")
public class EmployeeApi {

    @Autowired
    private EmployeeService employeeService;

    @PutMapping("/update_current_user_info") // nếu là admin thì truyền cả id người cần update, không thì để id=null
    public UpdateInfoOfEmployeeDTO update_info(@RequestBody UpdateInfoOfEmployeeDTO updateInfoOfEmployeeDTO) throws ParseException {
        return employeeService.updateCurrentUserInfo(updateInfoOfEmployeeDTO);
    }

    @PutMapping("/update_password") // truyền mật khẩu cũ và mật khẩu mới
    public String updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO){
        return employeeService.updatePassword(updatePasswordDTO);
    }

    @PutMapping("/update_avatar")
    public ResponseEntity<?> updataAvatar(@RequestParam("file") MultipartFile file){
        return ResponseEntity.ok(employeeService.updateAvatar(file));
    }

}
