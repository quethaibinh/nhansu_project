package com.example.quanlynhansu.services;

import com.example.quanlynhansu.models.DTO.UpdateInfoOfEmployeeDTO;
import com.example.quanlynhansu.models.DTO.UpdatePasswordDTO;
import com.example.quanlynhansu.models.response.EmployeeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.multi.MultiPanelUI;
import java.text.ParseException;
import java.util.List;

public interface EmployeeService {

    List<EmployeeResponse> finddAll();
    EmployeeResponse findMyInfo();
    UpdateInfoOfEmployeeDTO updateCurrentUserInfo(UpdateInfoOfEmployeeDTO updateInfoOfEmployeeDTO) throws ParseException;
    String updatePassword(UpdatePasswordDTO updatePasswordDTO);
    ResponseEntity<?> updateAvatar(MultipartFile file);
    ResponseEntity<?> findListChatEmployee();
    void deleteEmployee(Long id);

}
