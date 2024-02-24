package com.example.db.UserServiceTest;

import com.example.db.dto.UserDto;
import com.example.db.entity.Address;
import com.example.db.entity.User;
import com.example.db.exceptions.UserNotFoundException;
import com.example.db.repository.UserRepository;
import com.example.db.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
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

    UserDto userTest = UserDto.builder().build();

    @Test
    void testGetUsers() {
        ArrayList<User> dtoList = new ArrayList<>(Arrays.asList(User.builder().build(),User.builder().build()));
        when(userRepository.findAll()).thenReturn(dtoList);


        List<UserDto> usersList = userService.getUsers();

        assertEquals(2, dtoList.size());
    }

    @Test
    void addPostUser() {
        UserDto userDto = UserDto.builder()
                .username("test")
                .password("password")
                .email("test@example.com")
                .build();

        User user = User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto addedUser = userService.addUser(userDto);

        assertNotNull(addedUser);
        assertEquals("test", addedUser.getUsername());
        assertEquals("test@example.com", addedUser.getEmail());
    }

    @Test
    void getUserById_UserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(-10L));
    }

    @Test
    void getUserByIdSuccess() {
        User userTest1 = User.builder().username("TestName").password("TestPass").email("test@email.com").address(Address.builder().country("Portugal").city("Porto").street("Rua do bacalhau").number(10).build()).build();

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(userTest1));
        UserDto user2 = userService.getUserById(1L);
        assertNotNull(user2);
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
    void deleteUser_UserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void updatePutUser() {
        UserDto userDto = UserDto.builder().username("existingName").password("existingPassword").email("existing@example.com").build();

        UserDto updatedUser = UserDto.builder().username("updatedName").password("updatedPass").email("emailDoesntUpdate!!!@email.com").address(Address.builder().country("Portugal").city("Porto").street("Rua das ruas").number(10).build()).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(convertUserDtoToUser(userDto)));
        when(userRepository.save(convertUserDtoToUser(updatedUser))).thenReturn(convertUserDtoToUser(updatedUser));

        UserDto result = userService.updatePutUser(1L, updatedUser);

        assertEquals(updatedUser.getUsername(), result.getUsername());
        assertEquals(updatedUser.getPassword(), result.getPassword());
        assertEquals(updatedUser.getEmail(), result.getEmail());
        assertEquals(updatedUser.getAddress(), result.getAddress());
    }

    @Test
    void deleteUser() {
        UserDto existingUser = UserDto.builder().username("existing").password("password").email("existing@example.com").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(convertUserDtoToUser(existingUser)));

        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository, times(1)).delete(convertUserDtoToUser(existingUser));
    }

    @Test
    void updatePatchUser() {
        UserDto existingUser = UserDto.builder().username("existing").password("password").email("existing@example.com").build();

        UserDto updatedUser = UserDto.builder().username("updated").password("PasswordDoesntUpdate").email("updated@example.com").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(convertUserDtoToUser(existingUser)));
        when(userRepository.save(convertUserDtoToUser(existingUser))).thenReturn(convertUserDtoToUser(existingUser));

        UserDto result = userService.updatePatchUser(1L, updatedUser);

        assertEquals(updatedUser.getUsername(), result.getUsername());
    }

    public User convertUserDtoToUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        return user;
    }

}
