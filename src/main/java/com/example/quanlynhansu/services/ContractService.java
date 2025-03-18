package com.example.quanlynhansu.services;

import com.example.quanlynhansu.models.request.contract.ContractRequest;
import com.example.quanlynhansu.models.response.ContractResponse;

import java.text.ParseException;
import java.util.List;

public interface ContractService {

    ContractResponse addContract(ContractRequest contractRequest) throws ParseException;
    List<ContractResponse> selectContract();
    List<ContractResponse> findById(Long id);
    ContractResponse updateContract(ContractRequest contractRequest) throws ParseException;

}
