package com.example.quanlynhansu.services.impl;

import com.example.quanlynhansu.converters.EmployeeConverterDTOEntity;
import com.example.quanlynhansu.converters.EmployeeConverterEntityRequest;
import com.example.quanlynhansu.models.DTO.UpdateInfoOfEmployeeDTO;
import com.example.quanlynhansu.models.DTO.UpdatePasswordDTO;
import com.example.quanlynhansu.models.entity.AccountEntity;
import com.example.quanlynhansu.models.entity.EmployeeEntity;
import com.example.quanlynhansu.models.response.EmployeeResponse;
import com.example.quanlynhansu.repos.EmployeeRepo;
import com.example.quanlynhansu.repos.UserDetailsRepo;
import com.example.quanlynhansu.services.EmployeeService;
import com.example.quanlynhansu.services.securityService.InfoCurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private UserDetailsRepo userDetailsRepo;

    @Autowired
    private EmployeeConverterEntityRequest employeeConverterEntityRequest;

    @Autowired
    private EmployeeConverterDTOEntity employeeConverterDTOEntity;

    @Autowired
    private InfoCurrentUserService infoCurrentUserService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override
    public List<EmployeeResponse> finddAll() {

        List<EmployeeResponse> responses = new ArrayList<>();
        List<EmployeeEntity> employeeEntities = employeeRepo.findAll();
        if(employeeEntities != null){
            for(EmployeeEntity employee : employeeEntities){
                responses.add(employeeConverterEntityRequest.entityToResponse(employee));
            }
            return responses;
        }

        return null;
    }

    @Override // lấy thông tin người dùng hiện tại
    public EmployeeResponse findMyInfo() {

        String username = infoCurrentUserService.getCurrentUsername();
        EmployeeEntity employeeEntity = employeeRepo.findOneByEmail(username);
        if(employeeEntity != null) return employeeConverterEntityRequest.entityToResponse(employeeEntity);
        return null;

    }

    @Transactional
    @Override // cập nhật thông tin employee
    public UpdateInfoOfEmployeeDTO updateCurrentUserInfo(UpdateInfoOfEmployeeDTO updateInfoOfEmployeeDTO) throws ParseException {

        EmployeeEntity employeeEntity = employeeConverterDTOEntity.updateInfoToEntity(updateInfoOfEmployeeDTO);
        employeeRepo.save(employeeEntity);
        return updateInfoOfEmployeeDTO;

    }

    @Override // dữ liệu vào là mật khẩu cũ và mật khẩu mới
    public String updatePassword(UpdatePasswordDTO updatePasswordDTO) {

        String username = infoCurrentUserService.getCurrentUsername();
        AccountEntity account = userDetailsRepo.findByUsername(username);
        if(account != null){
            // matches so sánh mật khẩu chưa mã hóa với mật khẩu mã hóa matches(mk chưa mã hóa, mk đã mã hóa)
            if(!encoder.matches(updatePasswordDTO.getOldPassword(), account.getPassword())) {
                return "Mật khẩu xác nhật không giống mật khẩu cũ";
            }
            account.setPassword(encoder.encode(updatePasswordDTO.getNewPassword()));
            userDetailsRepo.save(account);

            return "success !!";
        }
        return "";
    }

}
