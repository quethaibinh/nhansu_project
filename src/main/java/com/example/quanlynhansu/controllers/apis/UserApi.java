package com.example.quanlynhansu.controllers.apis;

import com.example.quanlynhansu.models.entity.AccountEntity;
import com.example.quanlynhansu.models.request.user.LoginRequest;
import com.example.quanlynhansu.models.request.user.RegisterRequest;
import com.example.quanlynhansu.models.request.user.UpgradeRoleRequest;
import com.example.quanlynhansu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/user")
public class UserApi {

    @Autowired
    private UserService userService;

    // chỉ admin mới có quyền
    @PostMapping("/register")
    public AccountEntity register(@RequestBody RegisterRequest registerRequest) throws ParseException {
        AccountEntity account = userService.register(registerRequest);
        return account;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest){
        return userService.login(loginRequest);
    }

    @PutMapping("/upgrade_role") // admin upgrade role for user
    public AccountEntity upgradeRole(UpgradeRoleRequest upgradeRoleRequest){
        return null;
    }

}
