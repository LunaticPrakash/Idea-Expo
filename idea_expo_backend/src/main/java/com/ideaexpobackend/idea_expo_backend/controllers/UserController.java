package com.ideaexpobackend.idea_expo_backend.controllers;

import com.ideaexpobackend.idea_expo_backend.models.LoginRequest;
import com.ideaexpobackend.idea_expo_backend.models.LoginResponse;
import com.ideaexpobackend.idea_expo_backend.models.User;
import com.ideaexpobackend.idea_expo_backend.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/auth/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) throws Exception {
        User registeredUser = userService.registerUser(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping(value = "/auth/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) throws Exception {
        LoginResponse loginResponse = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) throws Exception {
        User user = this.userService.getUser(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
