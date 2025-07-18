package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getUserById_shouldReturnUser() throws Exception {
        User user = new User(1, "test@mail.com", "login", "name", LocalDate.of(2000, 1, 1));
        when(userService.getUserById(1)).thenReturn(Optional.of(user));
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@mail.com"));
    }

    @Test
    void addUser_shouldReturnCreatedUser() throws Exception {
        User user = new User(1, "test@mail.com", "login", "name", LocalDate.of(2000, 1, 1));
        when(userService.addUser(any(User.class))).thenReturn(user);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@mail.com"));
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        User user = new User(1, "test@mail.com", "login", "name", LocalDate.of(2000, 1, 1));
        when(userService.getUserById(1)).thenReturn(Optional.of(user));
        when(userService.updateUser(any(User.class))).thenReturn(user);
        mockMvc.perform(put("/users")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void addFriend_shouldReturnOk() throws Exception {
        doNothing().when(userService).addFriend(1, 2);
        mockMvc.perform(put("/users/1/friends/2"))
                .andExpect(status().isOk());
    }

    @Test
    void removeFriend_shouldReturnOk() throws Exception {
        doNothing().when(userService).removeFriend(1, 2);
        mockMvc.perform(delete("/users/1/friends/2"))
                .andExpect(status().isOk());
    }

    @Test
    void getFriends_shouldReturnListOfFriends() throws Exception {
        User user = new User(1, "test@mail.com", "login", "name", LocalDate.of(2000, 1, 1));
        user.getFriends().add(2L);
        User friend = new User(2, "friend@mail.com", "friend", "Friend", LocalDate.of(2000, 2, 2));
        when(userService.getUserById(1)).thenReturn(Optional.of(user));
        when(userService.getUserById(2)).thenReturn(Optional.of(friend));
        mockMvc.perform(get("/users/1/friends"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2));
    }

    @Test
    void getCommonFriends_shouldReturnListOfCommonFriends() throws Exception {
        User common = new User(3, "common@mail.com", "common", "Common", LocalDate.of(2000, 3, 3));
        when(userService.getCommonFriends(1, 2)).thenReturn(List.of(common));
        mockMvc.perform(get("/users/1/friends/common/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3));
    }
}
