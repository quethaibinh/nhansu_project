package com.example.quanlynhansu.services.impl;

import com.example.quanlynhansu.converters.EmployeeConverterEntityRequest;
import com.example.quanlynhansu.models.entity.AccountEntity;
import com.example.quanlynhansu.models.entity.EmployeeEntity;
import com.example.quanlynhansu.models.request.user.LoginRequest;
import com.example.quanlynhansu.models.request.user.RegisterRequest;
import com.example.quanlynhansu.repos.EmployeeRepo;
import com.example.quanlynhansu.repos.UserDetailsRepo;
import com.example.quanlynhansu.services.securityService.InfoCurrentUserService;
import com.example.quanlynhansu.services.securityService.JwtService;
import com.example.quanlynhansu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDetailsRepo userDetailsRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private EmployeeConverterEntityRequest employeeConverterEntityRequest;

    @Autowired
    private InfoCurrentUserService infoCurrentUserService;

    @Autowired
    private AuthenticationManager authentication;

    @Autowired
    private JwtService jwtService;

    private BCryptPasswordEncoder bcryp = new BCryptPasswordEncoder(12);

    @Override // tạo mới tài khoản và thông tin người dùng (admin)
    public AccountEntity register(RegisterRequest registerRequest) throws ParseException {

        EmployeeEntity employeeEntity = employeeConverterEntityRequest.requestToEntity(registerRequest);
        employeeRepo.save(employeeEntity);
        return employeeEntity.getAccounts().get(employeeEntity.getAccounts().size() - 1);

    }

//    @Override // lấy tài khoản người dùng hiện tại dựa vào username
//    public AccountEntity currentAccount() {
//        String username = infoCurrentUserService.getCurrentUsername();
//        return userDetailsRepo.findByUsername(username);
//    }

    @Override
    public String login(LoginRequest loginRequest) {

        Authentication auth;
        try{
            // dùng authenticationManager để spring security tự động xác thực tài khoản, mật khẩu và tự động lưu thông tin vào security context
            auth = authentication.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            return "fail !!" + e;
        }
        if(auth.isAuthenticated()){
            return jwtService.generateToken(loginRequest.getUsername());
        }
        return "fail !!";

    }

}
