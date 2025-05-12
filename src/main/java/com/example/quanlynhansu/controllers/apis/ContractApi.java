package com.example.quanlynhansu.controllers.apis;

import com.example.quanlynhansu.models.request.contract.ContractRequest;
import com.example.quanlynhansu.models.response.ContractResponse;
import com.example.quanlynhansu.services.ContractService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(value = "/contract")
public class ContractApi { // các api ở class này dành cho admin, HR

    @Autowired
    private ContractService contractService;

    @GetMapping("/select_by/{id}")
    public List<ContractResponse> selectById(@PathVariable Long id){
        return contractService.findById(id);
    }

    // api khởi tạo, gia hạn hợp đồng, ... => cập nhật bên workHistory.
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addContract(@RequestPart("metadata") ContractRequest contractRequest,
                                         @RequestPart("file") MultipartFile file) throws ParseException, MessagingException {
        return contractService.addContract(contractRequest, file);
    }

    @PutMapping("/update")
    public ContractResponse updateContract(@RequestBody ContractRequest contractRequest) throws ParseException {
        return contractService.updateContract(contractRequest);
    }

//    @PostMapping("/change")
//    public ContractResponse changeContract(ContractRequest contractRequest){
//        return null;
//    }

    // các api tự động gửi thông báo khi hợp đồng sắp hết hạn.

}
