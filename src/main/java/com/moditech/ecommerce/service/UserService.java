package com.moditech.ecommerce.service;

import com.moditech.ecommerce.model.User;
import com.moditech.ecommerce.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User registerUser(User user) {
        User userByEmail = userRepository.findByEmail(user.getEmail());

        if (userByEmail != null) {
            log.warn("Username is already existing");
            return null;
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public User loginUser(String email, String password) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new Exception("User not found");
        }
        if (!encoder.matches(password, user.getPassword())) {
            throw new Exception("Invalid password");
        }
        return user;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getListUser() {
        return userRepository.findAll();
    }

    public void updatePassword(String email, User user) {
        User setUser = userRepository.findByEmail(email);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String encodedPassword = encoder.encode(user.getPassword());
        setUser.setPassword(encodedPassword);
        userRepository.save(setUser);
    }

    public User updateIsEnableUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setIsEnable(true);
            userRepository.save(user);
        }
        return user;
    }

    public User updateUser(String email, User user) {
        User newUser = userRepository.findByEmail(email);

        if (user != null && user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
            newUser.setImageUrl(user.getImageUrl());
        }

        if (!user.getFirstName().equals("") && user.getFirstName() != null) {
            newUser.setFirstName(user.getFirstName());
        }

        if (!user.getLastName().equals("") && user.getLastName() != null) {
            newUser.setLastName(user.getLastName());
        }

        if (!user.getContactNumber().equals("") && user.getContactNumber() != null) {
            newUser.setContactNumber(user.getContactNumber());
        }

        if (!user.getAddressLine1().equals("") && user.getAddressLine1() != null) {
            newUser.setAddressLine1(user.getAddressLine1());
        }

        if (!user.getAddressLine1().equals("") && user.getAddressLine1() != null) {
            newUser.setAddressLine1(user.getAddressLine1());
        }

        if (!user.getCity().equals("") && user.getCity() != null) {
            newUser.setCity(user.getCity());
        }

        if (!user.getCountry().equals("") && user.getCountry() != null) {
            newUser.setCountry(user.getCountry());
        }

        if (!user.getPostalCode().equals("") && user.getPostalCode() != null) {
            newUser.setPostalCode(user.getPostalCode());
        }

        return userRepository.save(newUser);
    }
}
