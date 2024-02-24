package com.example.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {
    @Column
    private String country;

    @Column
    private String city;

    @Column
    private String street;

    @Column
    private Integer number;
}
