package com.example.quanlynhansu.controllers;

import com.example.quanlynhansu.models.response.ContractResponse;
import com.example.quanlynhansu.services.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/contract")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @GetMapping("/select")
    public List<ContractResponse> selectContract(){
        return contractService.selectContract();
    }

}
