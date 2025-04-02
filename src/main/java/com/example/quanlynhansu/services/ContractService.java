package com.example.quanlynhansu.services;

import com.example.quanlynhansu.models.request.contract.ContractRequest;
import com.example.quanlynhansu.models.response.ContractResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

public interface ContractService {

    ResponseEntity<?> addContract(ContractRequest contractRequest, MultipartFile file) throws ParseException;
    List<ContractResponse> selectContract();
    List<ContractResponse> findById(Long id);
    ContractResponse updateContract(ContractRequest contractRequest) throws ParseException;

}
