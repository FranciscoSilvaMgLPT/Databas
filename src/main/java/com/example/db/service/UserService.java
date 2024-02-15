package com.example.db.service;

import com.example.db.dto.UserDto;
import com.example.db.entity.User;
import com.example.db.exceptions.InvalidRequestException;
import com.example.db.exceptions.UserNotFoundException;
import com.example.db.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository repository;
    private static final String ERROR_USER_NOT_FOUND = "User not found!";
    public List<User> getUsers() {
        return repository.findAll().stream().toList();
    }

    public UserDto addUser(User user) {
        if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null) {
            throw new InvalidRequestException("Username, password and email cannot be empty");
        }
        repository.save(user);
            return UserDto.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .address(user.getAddress())
                    .build();
    }

    public UserDto getUserById(final Long userId) {
        Optional<User> userAux = repository.findById(userId);
        if (userAux.isEmpty()) throw new UserNotFoundException(ERROR_USER_NOT_FOUND);
        return UserDto.builder()
                .username(userAux.get().getUsername())
                .email(userAux.get().getEmail())
                .address(userAux.get().getAddress())
                .build();
    }

    public UserDto updatePutUser(final Long userId, User user) {
        Optional<User> userAux = repository.findById(userId);
        if (userAux.isEmpty()) {
            throw new UserNotFoundException(ERROR_USER_NOT_FOUND);
        }

        User userToUpdate = userAux.get();
        if (user.getUsername() == null || user.getPassword() == null || user.getAddress() == null) {
            throw new InvalidRequestException("Invalid body request. Username, Password and Address cannot be empty!");
        }
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setAddress(user.getAddress());
        repository.save(userToUpdate);
        return UserDto.builder()
                .username(userAux.get().getUsername())
                .address(userAux.get().getAddress())
                .build();
    }

    public void deleteUser(final Long userId) {
        Optional<User> user = repository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(ERROR_USER_NOT_FOUND);
        }
        repository.delete(user.get());
    }

    public UserDto updatePatchUser(final Long userId, User user) {
        Optional<User> userAux = repository.findById(userId);
        if (userAux.isEmpty()) throw new UserNotFoundException(ERROR_USER_NOT_FOUND);
        if (user.getUsername() == null && user.getPassword() == null && user.getAddress() == null)
            throw new InvalidRequestException("Invalid body request. Username, Password and Email cannot be empty!");
        User updatedUser = userAux.get();
        if (user.getUsername() != null) updatedUser.setUsername(user.getUsername());
        if (user.getPassword() != null) updatedUser.setPassword(user.getPassword());
        if (user.getAddress() != null) updatedUser.setAddress(user.getAddress());
        repository.save(updatedUser);
        return UserDto.builder()
                .username(userAux.get().getUsername())
                .email(userAux.get().getEmail())
                .address(userAux.get().getAddress())
                .build();

    }
}
