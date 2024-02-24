package com.example.db.UserControllerTest;

import com.example.db.entity.Address;
import com.example.db.entity.User;
import com.example.db.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters=false)
@SpringBootTest
class UserControllerTest{


    @MockBean
    private UserRepository userRepositoryMock;
    @Autowired
    private MockMvc mockmvc;
    private final User userTest1 = User.builder()
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
    private final User userTest2 = User.builder()
            .id(2L)
            .username("TestName1")
            .password("TestPass1")
            .email("test1@email.com")
            .build();
    private final User userTest3 = User.builder()
            .id(3L)
            .username("TestName2")
            .password("TestPass2")
            .email("test2@email.com")
            .build();

    @BeforeEach
    public void setup() {

        ArrayList<User> usersListMock = new ArrayList<>(Arrays.asList(userTest1, userTest2, userTest3));
        Mockito.mock(UserRepository.class);
        Mockito.when(userRepositoryMock.findAll()).thenReturn(usersListMock);
        Mockito.when(userRepositoryMock.findById(userTest1.getId())).thenReturn(Optional.of(userTest1));
        Mockito.when(userRepositoryMock.save(userTest1)).thenReturn(userTest1);
    }

    @Test
    void getUsersShouldReturnListUser() throws Exception {

        mockmvc.perform(MockMvcRequestBuilders
                        .get("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath(("$[0].username"), is("TestName")))
                .andExpect(jsonPath(("$[0].password"), is("TestPass")))
                .andExpect(jsonPath(("$[0].email"), is("test@email.com")));
    }

    @Test
    void getUserByIdShouldReturnUser() throws Exception {

        mockmvc.perform(MockMvcRequestBuilders
                        .get("/user/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(("$.username"), is("TestName")))
                .andExpect(jsonPath(("$.email"), is("test@email.com")));
    }

    @Test
    void deleteUserShouldReduceSize() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders
                        .delete("/user/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void updatePutUserShouldReturnUser() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User updatedUser = User.builder()
                .username("TestNameUpdate")
                .password("TestPassUpdate")
                .email("EMAILDONTUPDATE@email.com")
                .address(
                        Address.builder()
                                .country("Portugal dos pequenos")
                                .city("Vila do conde")
                                .street("Rua do touro")
                                .number(11)
                                .build())
                .build();

        Mockito.when(userRepositoryMock.findById(userTest1.getId())).thenReturn(Optional.of(userTest1));
        Mockito.when(userRepositoryMock.save(updatedUser)).thenReturn(updatedUser);
        mockmvc.perform(MockMvcRequestBuilders
                        .put("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(("$.username"), is("TestNameUpdate")))
                .andExpect(jsonPath("$.address.country", is("Portugal dos pequenos")))
                .andExpect(jsonPath("$.address.city", is("Vila do conde")))
                .andExpect(jsonPath("$.address.street", is("Rua do touro")))
                .andExpect(jsonPath("$.address.number", is(11)));
    }

    @Test
    void updatePatchUserShouldReturnUser() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User updatedUser = User.builder()
                .username("TestNameUpdatePatch")
                .build();

        Mockito.when(userRepositoryMock.findById(userTest1.getId())).thenReturn(Optional.of(userTest1));
        Mockito.when(userRepositoryMock.save(updatedUser)).thenReturn(updatedUser);
        mockmvc.perform(MockMvcRequestBuilders
                        .patch("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(("$.username"), is("TestNameUpdatePatch")));
    }

    @Test
    void addUserReturnUser() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User addUser = User.builder()
                .id(4L)
                .username("TestNamePost")
                .password("TestPassPost")
                .email("post@post.com")
                .build();
        Mockito.when(userRepositoryMock.save(addUser)).thenReturn(addUser);

        mockmvc.perform(MockMvcRequestBuilders
                        .post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(("$.username"), is("TestNamePost")))
                .andExpect(jsonPath(("$.email"), is("post@post.com")));
    }


    @Test
    void addUserErrorNoField() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User addUser = User.builder()
                .id(4L)
                .build();
        mockmvc.perform(MockMvcRequestBuilders
                        .post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePatchUserErrorNotFound() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User updatedUser = User.builder()
                .build();
        Mockito.when(userRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        Mockito.when(userRepositoryMock.save(updatedUser)).thenReturn(updatedUser);
        mockmvc.perform(MockMvcRequestBuilders
                        .patch("/user/8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUserErrorNotFound() throws Exception {

        Long id = 7L;
        Mockito.when(userRepositoryMock.findById(id)).thenReturn(Optional.empty());

        mockmvc.perform(MockMvcRequestBuilders
                        .delete("/user/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePutErrorEmptyFields() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User updatedUser = User.builder()
                .build();

        mockmvc.perform(MockMvcRequestBuilders
                        .put("/user/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePatchErrorEmptyFields() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User updatedUser = User.builder()
                .build();

        mockmvc.perform(MockMvcRequestBuilders
                        .patch("/user/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUserByIdErrorUserNotFound() throws Exception {
        mockmvc.perform(MockMvcRequestBuilders
                        .get("/user/{id}",-99)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
