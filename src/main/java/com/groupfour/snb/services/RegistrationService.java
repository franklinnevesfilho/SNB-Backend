package com.groupfour.snb.services;

import com.groupfour.snb.models.User;
import com.groupfour.snb.security.registration.token.RegistrationToken;
import com.groupfour.snb.security.registration.token.RegistrationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Service
@Transactional
public class RegistrationService {
    private RegistrationTokenService tokenService;
    private EmailService emailService;
    private UserService userService;


    public String register(User request) {
        String result;
        if(isValidEmail(request.getUsername())){
            userService.addUser(request);
            UUID token = tokenService.createToken(request).getTokenId();
            emailService.sendVerificationEmail(token.toString(), request);
            result = "token: " + token;
        }else{
            result = "failed";
        }
        return result;
    }

    private boolean isValidEmail(String email){
        //TODO: email verification
        return true;
    }

    public RegistrationToken confirmToken(UUID token) {
        RegistrationToken confirmedToken = tokenService.getToken(token).get();
        if(confirmedToken.getConfirmedAt() != null){
            //TODO: handle registration exceptions
            System.out.println("token alreadyConfirmed");
        }
        // Set time confirmed
        confirmedToken.setConfirmedAt(LocalDateTime.now());
        // Checks if confirmation time is valid
        if(confirmedToken.getConfirmedAt().isAfter(confirmedToken.getExpiresAt())){
            // If the expiration is before now... it has already expired
            System.out.println("Token expired");
        }else{
            userService.activateUser(confirmedToken.getUser());
        }
        return confirmedToken;
    }
}