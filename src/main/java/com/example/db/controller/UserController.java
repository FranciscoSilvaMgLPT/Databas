package com.example.db.controller;
import com.example.db.dto.UserDto;
import com.example.db.entity.User;
import com.example.db.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public List<User> getUsers() {
        return service.getUsers();
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addUser(user));
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable final Long userId) {
        return service.getUserById(userId);
    }
    @PutMapping("/{userId}")
    public UserDto updateUser(@PathVariable final Long userId, @RequestBody User user){
        return service.updatePutUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable final Long userId){
        service.deleteUser(userId);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUserDetail(@PathVariable final Long userId, @RequestBody User user){
        return service.updatePatchUser(userId,user);
    }


}
