package com.example.db.dto;

import com.example.db.entity.Address;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Address address;
}
