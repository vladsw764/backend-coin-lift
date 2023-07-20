package com.coinlift.userservice.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthenticationRequest {
    private String email;
    private String password;
}
