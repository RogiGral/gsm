package com.gymsystem.gms.service;

import com.gymsystem.gms.exceptions.model.EmailExistException;
import com.gymsystem.gms.exceptions.model.UsernameExistException;
import com.gymsystem.gms.model.User;

import java.util.List;

public interface UserService {

    User register(String firstName, String lastName, String username, String email) throws EmailExistException, UsernameExistException;
    List<User> getUsers();
    User findUserByUsername(String username);
    User findUserByEmail(String email);
}