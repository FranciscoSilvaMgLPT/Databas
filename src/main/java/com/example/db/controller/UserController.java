package com.example.db.controller;
import com.example.db.entity.User;
import com.example.db.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<User> addUser(@RequestBody User user) {
        var newUser = service.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newUser);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable final Long userId) {
        return service.getUserById(userId);
    }
    @PutMapping("/{userId}")
    public User updateUser(@PathVariable final Long userId, @RequestBody User user){
        return service.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable final Long userId){
        service.deleteUser(userId);
    }

    @PatchMapping("/{userId}")
    public User updateUserDetail(@PathVariable final Long userId, @RequestBody User user){
        return service.updateUserDetail(userId,user);
    }


}
