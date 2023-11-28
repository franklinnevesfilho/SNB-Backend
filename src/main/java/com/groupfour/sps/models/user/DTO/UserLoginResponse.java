package com.groupfour.sps.models.user.DTO;

import com.groupfour.sps.models.user.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserLoginResponse {
     private UserProfile user;
     private String jwt;
}
