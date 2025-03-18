package com.example.quanlynhansu.services;

import com.example.quanlynhansu.models.entity.AccountEntity;
import com.example.quanlynhansu.models.request.user.LoginRequest;
import com.example.quanlynhansu.models.request.user.RegisterRequest;

import java.text.ParseException;

public interface UserService {

    String login(LoginRequest loginRequest);
    AccountEntity register(RegisterRequest registerRequest) throws ParseException;
//    AccountEntity currentAccount();
}
