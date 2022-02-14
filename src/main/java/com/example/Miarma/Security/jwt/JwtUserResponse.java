package com.example.Miarma.Security.jwt;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class JwtUserResponse {

    private String email;
    private String fullName;
    private String avatar;
    private String role;
    private String token;

}
