package com.example.quanlynhansu.converters;

import com.example.quanlynhansu.models.DTO.UpdateInfoOfEmployeeDTO;
import com.example.quanlynhansu.models.entity.AccountEntity;
import com.example.quanlynhansu.models.entity.EmployeeEntity;
import com.example.quanlynhansu.models.request.user.RegisterRequest;
import com.example.quanlynhansu.models.response.EmployeeResponse;
import com.example.quanlynhansu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeConverterEntityRequest {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder(12);

    public EmployeeEntity requestToEntity(RegisterRequest registerRequest) throws ParseException {
        EmployeeEntity employee = new EmployeeEntity();

        employee.setFullName(registerRequest.getFullName());
        employee.setEmail(registerRequest.getEmail());
        employee.setPhone(registerRequest.getPhone());
        employee.setAddress(registerRequest.getAddress());
        employee.setStatus(registerRequest.getStatus());
        employee.setBirthDate(sdf.parse(registerRequest.getBirthDate()));
        employee.setHireDate(sdf.parse(registerRequest.getHireDate()));

        List<AccountEntity> list = new ArrayList<>();
        AccountEntity account = new AccountEntity();
        account.setUsername(registerRequest.getEmail());
        account.setRole("Employee");
        account.setPassword(bcrypt.encode(registerRequest.getPhone()));
        account.setEmployee(employee);
        list.add(account);

        employee.setAccounts(list);

        return employee;
    }

    public EmployeeResponse entityToResponse(EmployeeEntity employeeEntity){
        EmployeeResponse employeeResponse = new EmployeeResponse();

        employeeResponse.setId(employeeEntity.getId());
        employeeResponse.setFullName(employeeEntity.getFullName());
        employeeResponse.setEmail(employeeEntity.getEmail());
        employeeResponse.setAddress(employeeEntity.getAddress());
        employeeResponse.setPhone(employeeEntity.getPhone());
        employeeResponse.setBirthDate(sdf.format(employeeEntity.getBirthDate()));
        employeeResponse.setHireDate(sdf.format(employeeEntity.getBirthDate()));
        employeeResponse.setStatus((employeeEntity.getStatus()));

        List<String> roles = new ArrayList<>();
        for(AccountEntity account: employeeEntity.getAccounts()){{
            roles.add(account.getRole());
        }}
        employeeResponse.setRole(roles);

        return employeeResponse;
    }

}
