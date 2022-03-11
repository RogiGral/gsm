package com.gymsystem.gms.service.Impl;

import com.gymsystem.gms.exceptions.model.EmailExistException;
import com.gymsystem.gms.exceptions.model.UsernameExistException;
import com.gymsystem.gms.model.User;
import com.gymsystem.gms.model.UserPrincipal;
import com.gymsystem.gms.repository.UserRepository;
import com.gymsystem.gms.service.LoginAttemptService;
import com.gymsystem.gms.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.gymsystem.gms.enumeration.Role.ROLE_USER;

@Service
@Transactional
@Qualifier("UserDetailService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private LoginAttemptService loginAttemptService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if(user == null){
            LOGGER.error("user not found by username: "+username);
            throw new UsernameNotFoundException("user not found by username: "+username);
        } else {
            validateLoginAttempt(user);
            user.setLastLoginDate(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.error("user found by username: "+username);
            return userPrincipal;
        }
    }

    private void validateLoginAttempt(User user) {
        String username = user.getUsername();
        if(user.isNotLocked()){
            if(loginAttemptService.hasExceededMaxAttempts(username)){
                user.setNotLocked(false);
            }
            else{
                user.setNotLocked(true);
            }
        }else {
            loginAttemptService.evicUserFromLoginAttemptCache(username);
        }
    }

    @Override
    public User register(String firstName, String lastName, String username, String email) throws UsernameNotFoundException, EmailExistException, UsernameExistException {
        validateNewUsernameAndEmail(StringUtils.EMPTY,username,email);
        User user = new User();
        user.setUserId(generateUserId());
        String password = generatePassword();
        String encodedPassword = encodedPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setJoinDate(new Date());
        user.setPassword(encodedPassword);
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(ROLE_USER.toString());
        user.setAuthorities(ROLE_USER.getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImageUrl());
        userRepository.save(user);
        LOGGER.info("New user password: "+password);
        return user;
    }

    private String getTemporaryProfileImageUrl() {
        return "https://www.google.com/url?sa=i&url=http%3A%2F%2Fclipart-library.com%2Fuser.html&psig=AOvVaw36fcZ3eV7pauGTTwWajuVS&ust=1646750759622000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCKiAsuOetPYCFQAAAAAdAAAAABAT";
    }

    private String encodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UsernameNotFoundException, UsernameExistException, EmailExistException {
        if (StringUtils.isNotEmpty(currentUsername)) {
            User currentUser = findUserByUsername(currentUsername);
            if (currentUser == null) {
                throw new UsernameNotFoundException("No user found by username " + currentUsername);
            }
            User userByNewUsername = findUserByUsername(newUsername);
            if (userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                throw new UsernameExistException("Username already exists");
            }
            User userByNewEmail = findUserByEmail(newEmail);
            if (userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistException("Username already exists");
            }
            return currentUser;
        } else {
            User userByNewUsername = userRepository.findUserByUsername(newUsername);
            if(userByNewUsername!=null){
                throw new UsernameExistException("Username already exists");
            }
            User userByNewEmail = userRepository.findUserByEmail(newEmail);
            if(userByNewEmail !=null){
                throw new EmailExistException("Email already exists");
            }
            return null;
        }
    }
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
