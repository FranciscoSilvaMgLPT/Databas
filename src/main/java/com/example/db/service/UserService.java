package com.example.db.service;

import com.example.db.dto.UserDto;
import com.example.db.entity.Address;
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

    public List<UserDto> getUsers() {
        return repository.findAll().stream()
                .map(e -> UserDto.builder()
                        .id(e.getId())
                        .username(e.getUsername())
                        .password(e.getPassword())
                        .email(e.getEmail())
                        .address(e.getAddress())
                        .build())
                .toList();
    }

    public UserDto addUser(UserDto userDto) {
        if (userDto.getUsername() == null || userDto.getPassword() == null || userDto.getEmail() == null) {
            throw new InvalidRequestException("Username, password and email cannot be empty");
        }
        repository.save(User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .address(Address.builder()
                        .country(userDto.getAddress().getCountry())
                        .city(userDto.getAddress().getCity())
                        .street(userDto.getAddress().getStreet())
                        .number(userDto.getAddress().getNumber()).build())
                .build());
        return UserDto.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .address(userDto.getAddress())
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

    public UserDto updatePutUser(final Long userId, UserDto userDto) {
        Optional<User> userAux = repository.findById(userId);
        if (userAux.isEmpty()) {
            throw new UserNotFoundException(ERROR_USER_NOT_FOUND);
        }

        User userToUpdate = userAux.get();
        if (userDto.getUsername() == null || userDto.getEmail() == null || userDto.getPassword() == null || userDto.getAddress() == null) {
            throw new InvalidRequestException("Invalid body request. Username, Password, Email and Address cannot be empty!");
        }
        userToUpdate.setUsername(userDto.getUsername());
        userToUpdate.setPassword(userDto.getPassword());
        userToUpdate.setAddress(userDto.getAddress());
        userToUpdate.setEmail(userDto.getEmail());
        repository.save(userToUpdate);
        return userDto;
    }

    public void deleteUser(final Long userId) {
        Optional<User> user = repository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(ERROR_USER_NOT_FOUND);
        }
        repository.delete(user.get());
    }

    public UserDto updatePatchUser(final Long userId, UserDto userDto) {
        Optional<User> userAux = repository.findById(userId);
        if (userAux.isEmpty()) throw new UserNotFoundException(ERROR_USER_NOT_FOUND);
        if (userDto.getUsername() == null && userDto.getPassword() == null && userDto.getAddress() == null)
            throw new InvalidRequestException("Invalid body request. Username, Password and Email cannot be empty!");
        User updatedUser = userAux.get();
        if (userDto.getUsername() != null) updatedUser.setUsername(userDto.getUsername());
        if (userDto.getPassword() != null) updatedUser.setPassword(userDto.getPassword());
        if (userDto.getAddress() != null) updatedUser.setAddress(userDto.getAddress());
        repository.save(updatedUser);
        return userDto;

    }
}
