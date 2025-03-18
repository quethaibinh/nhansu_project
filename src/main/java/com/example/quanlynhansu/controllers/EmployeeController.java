package com.example.quanlynhansu.controllers;

import com.example.quanlynhansu.models.response.EmployeeResponse;
import com.example.quanlynhansu.services.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // chỉ admin
    @GetMapping("/all")
    public List<EmployeeResponse> findAll(){
        return employeeService.finddAll();
    }

    @GetMapping("/current_user_info") // hiển thị thông tin người dùng hiện tại
    public EmployeeResponse findMyInfo(){
        return employeeService.findMyInfo();
    }

}
