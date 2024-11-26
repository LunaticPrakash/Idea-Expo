package com.ideaexpobackend.idea_expo_backend.services;

import com.ideaexpobackend.idea_expo_backend.annotations.LogMethodExecutionTime;
import com.ideaexpobackend.idea_expo_backend.exceptions.CustomBadCredentialsException;
import com.ideaexpobackend.idea_expo_backend.exceptions.CustomBadRequestException;
import com.ideaexpobackend.idea_expo_backend.exceptions.CustomRuntimeException;
import com.ideaexpobackend.idea_expo_backend.models.LoginResponse;
import com.ideaexpobackend.idea_expo_backend.models.User;
import com.ideaexpobackend.idea_expo_backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger("com.ideaexpobackend");

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @LogMethodExecutionTime
    public User registerUser(User user) throws Exception {
        if(user == null){
            throw new Exception("User can't be null");
        }
        Map<String, String> fieldsMap = new HashMap<>();
        fieldsMap.put("First Name", user.getFirstName());
        fieldsMap.put("Email", user.getEmail());
        fieldsMap.put("Password", user.getPassword());
        String nullAndEmptyFieldValidatorResponse = nullAndEmptyFieldValidator(fieldsMap);
        String passwordValidatorResponse = passwordValidator(user.getPassword());

        if(nullAndEmptyFieldValidatorResponse.length() > 0){
            throw new Exception(nullAndEmptyFieldValidatorResponse);
        }

        if(passwordValidatorResponse.length() > 0){
            throw new Exception(passwordValidatorResponse);
        }

        User alreadyExistingUser = this.userRepository.findByEmail(user.getEmail());
        if(alreadyExistingUser != null){
            logger.error("Failed in saving user details. ERROR: User already exists");
            throw new Exception("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User registeredUser = userRepository.save(user);
        return  registeredUser;

    }

    @LogMethodExecutionTime
    public LoginResponse loginUser(String email, String password){
        Map<String, String> fieldsMap = new HashMap<>();
        fieldsMap.put("Email", email);
        fieldsMap.put("Password", password);
        String nullAndEmptyFieldValidatorResponse = nullAndEmptyFieldValidator(fieldsMap);

        if(nullAndEmptyFieldValidatorResponse.length() > 0){
            throw new CustomBadRequestException(nullAndEmptyFieldValidatorResponse);
        }
        try {
            logger.info("Trying to authenticate user");
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            logger.info("Authentication successful");
        } catch (DisabledException e) {
            logger.error("Authentication failed with exception: {}\n{}", e.getMessage(), Arrays.toString(e.getStackTrace()));
            throw new DisabledException(e.getMessage());
        } catch (BadCredentialsException e) {
            logger.error("Authentication failed with exception: {}\n{}", e.getMessage(), Arrays.toString(e.getStackTrace()));
            throw new CustomBadCredentialsException(e.getMessage());
        } catch (RuntimeException e) {
            logger.error("Authentication failed with exception: {}\n{}", e.getMessage(), Arrays.toString(e.getStackTrace()));
            throw new CustomRuntimeException(e.getMessage());
        }

        User user = userDetailsService.loadUserByUsername(email);
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        logger.info("Current Logged in User: {}", user.getEmail());
        return new LoginResponse(accessToken, refreshToken);
    }

    @LogMethodExecutionTime
    public User getUser(Long userId) throws Exception {
        Optional<User> userOptional = this.userRepository.findById(userId);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            return user ;
        }
        throw new Exception("User with ID: " + userId + " doesn't exist");
    }

    @LogMethodExecutionTime
    private String passwordValidator(String password) {
        StringBuilder response = new StringBuilder();

        if (password == null || password.length() <= 8) {
            response.append("Password must be more than 8 characters long");
        } else {
            boolean hasLower = false;
            boolean hasUpper = false;
            boolean hasSpecial = false;
            boolean hasDigit = false;
            for (char c : password.toCharArray()) {
                if (Character.isLowerCase(c)) {
                    hasLower = true;
                } else if (Character.isUpperCase(c)) {
                    hasUpper = true;
                } else if (Character.isDigit(c)) {
                    hasDigit = true;
                } else if (!Character.isLetterOrDigit(c)) {
                    hasSpecial = true;
                }
            }
            if (!hasLower || !hasUpper || !hasDigit || !hasSpecial) {
                response.append("Password must contain at least one lowercase, one uppercase, one digit and one special character");
            }
        }
        return response.toString();
    }

    @LogMethodExecutionTime
    private String nullAndEmptyFieldValidator(Map<String, String> fieldsMap) {
        StringBuilder response = new StringBuilder();
        for (Map.Entry<String, String> entry : fieldsMap.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isBlank()) {
                response.append(entry.getKey()).append(", ");
            }
        }
        if (response.length() > 0) {
            response.delete(response.length() - 2, response.length());
            response.append(" can't be null");
        }
        return response.toString();
    }
}