package com.example.quanlynhansu.converters;

import com.example.quanlynhansu.models.DTO.UpdateInfoOfEmployeeDTO;
import com.example.quanlynhansu.models.entity.AccountEntity;
import com.example.quanlynhansu.models.entity.EmployeeEntity;
import com.example.quanlynhansu.repos.EmployeeRepo;
import com.example.quanlynhansu.repos.UserDetailsRepo;
import com.example.quanlynhansu.services.UserService;
import com.example.quanlynhansu.services.securityService.InfoCurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeConverterDTOEntity {

    @Autowired
    private InfoCurrentUserService infoCurrentUserService;

    @Autowired
    private UserDetailsRepo userDetailsRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    // converter từ thông tin cập nhật employee đến dữ liệu truyền vào db
    public EmployeeEntity updateInfoToEntity(UpdateInfoOfEmployeeDTO updateInfoOfEmployeeDTO) throws ParseException {

        EmployeeEntity employee = new EmployeeEntity();
        employee.setId(updateInfoOfEmployeeDTO.getId()); // nếu là admin thì truyền id người cần update vào
        employee.setFullName(updateInfoOfEmployeeDTO.getFullName());
        employee.setEmail(updateInfoOfEmployeeDTO.getEmail());
        employee.setPhone(updateInfoOfEmployeeDTO.getPhone());
        employee.setAddress(updateInfoOfEmployeeDTO.getAddress());
        employee.setStatus(updateInfoOfEmployeeDTO.getStatus());
        employee.setBirthDate(sdf.parse(updateInfoOfEmployeeDTO.getBirthDate()));
        employee.setHireDate(sdf.parse(updateInfoOfEmployeeDTO.getHireDate()));

        // lấy id từ employee lúc chưa cập nhật
        String username = infoCurrentUserService.getCurrentUsername();
        EmployeeEntity oldEmployee = employeeRepo.findOneByEmail(username);
        employee.setId(oldEmployee.getId());

        //lấy danh sách tài khoản cũ và đổi lại username (email)
        List<AccountEntity> list = new ArrayList<>();
        AccountEntity account = userDetailsRepo.findByUsername(username);
        account.setEmployee(employee);
        account.setUsername(updateInfoOfEmployeeDTO.getEmail());
        list.add(account);
        employee.setAccounts(list);

        return employee;
    }


}
