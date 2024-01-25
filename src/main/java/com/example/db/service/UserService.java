package com.example.db.service;

import com.example.db.entity.User;
import com.example.db.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository repository;

    //  private Long count = 0L;   ATAO

    public List<User> getUsers() {
        return repository.findAll().stream().toList();
    }

    public User addUser(User user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        repository.save(newUser);
        //   count += 1;   e o id crlh??
        return newUser;
    }

    public User getUserById(final Long userId) {
        return repository.getReferenceById(userId);
    }

    public User updateUser(final Long userId, User user) {
        repository.getReferenceById(userId).setUsername(user.getUsername());
        repository.getReferenceById(userId).setPassword(user.getPassword());
        return repository.getReferenceById(userId);

    }

    public void deleteUser(final Long userId) {
        repository.deleteById(userId);
    }

    public User updateUserDetail(final Long userId, User user) {

        User userUpdate = repository.getReferenceById(userId);

        if (user.getUsername() != null) userUpdate.setUsername(user.getUsername());
        if (user.getPassword() != null) userUpdate.setPassword(user.getPassword());

        repository.save(userUpdate);
        return repository.getReferenceById(userId);
    }


}
