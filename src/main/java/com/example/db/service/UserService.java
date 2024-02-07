package com.example.db.service;

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
    private final static String errorUserNotFound = "User not found!";
    public List<User> getUsers() {
        return repository.findAll().stream().toList();
    }

    public User addUser(User user) {
        if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null) {
            throw new InvalidRequestException("Username, password and email cannot be empty");
        }
            return repository.save(user);
    }

    public User getUserById(final Long userId) {
        Optional<User> userAux = repository.findById(userId);
        if (userAux.isEmpty()) throw new UserNotFoundException(errorUserNotFound);
        return repository.getReferenceById(userId);
    }

    public User updatePutUser(final Long userId, User user) {
        Optional<User> userAux = repository.findById(userId);
        if (!userAux.isPresent()) {
            throw new UserNotFoundException(errorUserNotFound);
        }

        User userToUpdate = userAux.get();
        if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null || user.getAddress() == null) {
            throw new InvalidRequestException("Invalid body request. Username, Password, Email, and Address cannot be empty!");
        }
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setAddress(user.getAddress());
        userToUpdate.setEmail(user.getEmail());
        repository.save(userToUpdate);
        return userToUpdate;
    }

    public void deleteUser(final Long userId) {
        Optional<User> user = repository.findById(userId);
        if (!user.isPresent()) {
            throw new UserNotFoundException(errorUserNotFound);
        }
        repository.delete(user.get());
    }

    public User updatePatchUser(final Long userId, User user) {
        Optional<User> userAux = repository.findById(userId);
        if (userAux.isEmpty()) throw new UserNotFoundException(errorUserNotFound);
        if (user.getUsername() == null && user.getPassword() == null && user.getAddress() == null)
            throw new InvalidRequestException("Invalid body request. Username, Password and Email cannot be empty!");
        User updatedUser = userAux.get();
        if (user.getUsername() != null) updatedUser.setUsername(user.getUsername());
        if (user.getPassword() != null) updatedUser.setPassword(user.getPassword());
        if (user.getAddress() != null) updatedUser.setAddress(user.getAddress());
        repository.save(updatedUser);
        return updatedUser;

    }
}
