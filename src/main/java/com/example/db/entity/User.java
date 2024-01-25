package com.example.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "users")

public class User {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;


}
