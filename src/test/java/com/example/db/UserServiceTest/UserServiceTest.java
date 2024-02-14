package com.example.db.UserServiceTest;

import com.example.db.entity.Address;
import com.example.db.entity.User;
import com.example.db.exceptions.UserNotFoundException;
import com.example.db.repository.UserRepository;
import com.example.db.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    User userTest = new User();

    @Test
    void testGetUsers() {
        when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));

        List<User> users = userService.getUsers();

        assertEquals(2, users.size());
    }

    @Test
    void addUPostUser() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("password");
        user.setEmail("test@example.com");

        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.addUser(user);

        assertNotNull(savedUser);
        assertEquals("test", savedUser.getUsername());
        assertEquals("password", savedUser.getPassword());
        assertEquals("test@example.com", savedUser.getEmail());
    }

    @Test
    void getUserById_UserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void getUserByIdSuccess() {
        User userTest1 = User.builder()
                .id(1L)
                .username("TestName")
                .password("TestPass")
                .email("test@email.com")
                .address(
                        Address.builder()
                                .country("Portugal")
                                .city("Porto")
                                .street("Rua do bacalhau")
                                .number(10)
                                .build()
                )
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(userTest1));
        Optional<User> user2 = userService.getUserById(1L);
        assertNotNull(user2);
        assertTrue(user2.isPresent());
    }

    @Test
    void updatePutUser_UserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.updatePutUser(1L, userTest));
    }

    @Test
    void updatePatchUser_UserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.updatePatchUser(1L, userTest));
    }

    @Test
    void updatePutUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("existing");
        user.setPassword("password");
        user.setEmail("existing@example.com");

        User updatedUser = User.builder()
                .username("updatedName")
                .password("updatedPass")
                .email("email@email.com")
                .address(Address.builder()
                        .country("Portugal")
                        .city("Porto")
                        .street("Rua das ruas")
                        .number(10)
                        .build()).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        User result = userService.updatePutUser(1L, updatedUser);

        assertEquals(updatedUser.getUsername(), result.getUsername());
        assertEquals(updatedUser.getPassword(), result.getPassword());
        assertEquals(updatedUser.getEmail(), result.getEmail());
        assertEquals(updatedUser.getAddress(), result.getAddress());
    }


    @Test
    void deleteUser() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("existing");
        existingUser.setPassword("password");
        existingUser.setEmail("existing@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository, times(1)).delete(existingUser);
    }

    @Test
    void updatePatchUser() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("existing");
        existingUser.setPassword("password");
        existingUser.setEmail("existing@example.com");

        User updatedUser = new User();
        updatedUser.setUsername("updated");
        updatedUser.setPassword("updatedPassword");
        updatedUser.setEmail("updated@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User result = userService.updatePatchUser(1L, updatedUser);

        assertEquals(updatedUser.getUsername(), result.getUsername());
        assertEquals(updatedUser.getPassword(), result.getPassword());
        assertEquals(existingUser.getEmail(), result.getEmail());
    }
}
