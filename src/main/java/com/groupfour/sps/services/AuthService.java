package com.groupfour.sps.services;

import com.groupfour.sps.models.user.*;
import com.groupfour.sps.models.user.DTO.UserLogin;
import com.groupfour.sps.models.user.DTO.UserRegistration;
import com.groupfour.sps.repositories.UserRepository;
import com.groupfour.sps.utils.security.AuthProvider;
import com.groupfour.sps.models.user.SecurityUser;
import com.groupfour.sps.utils.responses.Response;
import com.groupfour.sps.utils.security.tokens.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * <h1>AuthService</h1>
 * This class handles all operations for authentication and registration
 * @author Franklin Neves Filho
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService extends MainService{

    private final AuthProvider loginManager;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final RegistrationTokenService registrationService;
    private final JwtTokenService jwtTokenService;
    private final static String LOGIN_ERROR = "Email or password have been inputted incorrectly.";
    /**
     * This method is called once the user has entered their info and is ready to confirm their account
     * @param registerUser A DTO consisting of firstname, lastname, email and password.
     * @return A response saying the email, to activate the account, has been sent successfully.
     */
    public Response registerUser(UserRegistration registerUser){
        // Authorities list for users
        Set<Role> authorities = new HashSet<>();
        //Todo: retrieve what role you want to assign the user
        Role userRole = roleService.getRoleByAuthority("USER");
        authorities.add(userRole);
        return registrationService.registerUser(
                userRepository.save(
                        User.builder()
                                .firstName(registerUser.getFirstName())
                                .lastName(registerUser.getLastName())
                                .email(registerUser.getEmail())
                                .password(passwordEncoder.encode(registerUser.getPassword()))
                                .authorities(authorities)
                                .build()
                ));
    }
    /***
     * This method is called when a user would like to log-in.
     * It generated a session token used for every other http call to ensure the users credentials.
     * It creates a new SecurityUser using the id from the foundUser and the credentials from the userLogin
     * and authenticates the user.
     * @param userLogin This is a DTO consisting of only email and password.
     * @return This will return a response either the logged-in user or a list of errors
     */
    public Response loginUser(UserLogin userLogin) {
        String jwtToken = "";
        Optional<User> foundUser = userRepository.findByEmail(userLogin.getEmail());
        Response response = Response.builder().build();
        List<String> errors = new LinkedList<>();
        if(foundUser.isPresent() && foundUser.get().isEnabled()) {
             SecurityUser secUser = SecurityUser.builder()
                     .user(User.builder()
                             .id(foundUser.get().getId())
                             .email(userLogin.getEmail())
                             .password(userLogin.getPassword())
                             .build())
                     .build();
            if (loginManager.authenticate(secUser).isAuthenticated()) {
                secUser = SecurityUser.builder().user(foundUser.get()).build();

                jwtToken = jwtTokenService.generateJwt(secUser);
            } else {
                log.warn("user was not authenticated");
                errors.add(LOGIN_ERROR);
            }
        }
        else {
            errors.add(LOGIN_ERROR);
        }
        response.setNode(mapToJson(jwtToken));
        response.setErrors(errors);
        return response;
    }

    /**
     * @param token The registration tokenId is passed and confirmed
     * @return a response with the userRegistrationResponse in the node
     */
    public Response confirmAccount(String token) {
        return registrationService.verifyRegistrationToken(token);
    }
}