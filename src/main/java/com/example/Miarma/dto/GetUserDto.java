package com.example.Miarma.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetUserDto {

    private String avatar;
    private String fullName;
    private String email;
    private String role;

}
