package com.example.db.dto;

import com.example.db.entity.Address;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserDto {
    private String username;
    private String email;
    private Address address;
}
