package com.example.Miarma.dto;

import com.example.Miarma.models.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter {

    public GetUserDto convertUserEntityToGetUserDto(UserEntity user) {
        return GetUserDto.builder()
                .avatar(user.getAvatar())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .build();
    }

}
