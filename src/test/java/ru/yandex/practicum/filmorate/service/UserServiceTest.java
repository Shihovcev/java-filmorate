package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserService userService;
    private InMemoryUserStorage userStorage;

    @BeforeEach
    void setUp() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
    }

    @Test
    void testAddAndRemoveFriend() {
        User user1 = new User(1, "a@a.ru", "user1", "User One", LocalDate.of(2000, 1, 1));
        User user2 = new User(2, "b@b.ru", "user2", "User Two", LocalDate.of(2000, 2, 2));
        userStorage.addUser(user1);
        userStorage.addUser(user2);
        userService.addFriend(1, 2);
        assertTrue(userService.getUserById(1).get().getFriends().contains(2L));
        assertTrue(userService.getUserById(2).get().getFriends().contains(1L));
        userService.removeFriend(1, 2);
        assertFalse(userService.getUserById(1).get().getFriends().contains(2L));
        assertFalse(userService.getUserById(2).get().getFriends().contains(1L));
    }

    @Test
    void testGetCommonFriends() {
        User user1 = new User(1, "a@a.ru", "user1", "User One", LocalDate.of(2000, 1, 1));
        User user2 = new User(2, "b@b.ru", "user2", "User Two", LocalDate.of(2000, 2, 2));
        User user3 = new User(3, "c@c.ru", "user3", "User Three", LocalDate.of(2000, 3, 3));
        userStorage.addUser(user1);
        userStorage.addUser(user2);
        userStorage.addUser(user3);
        userService.addFriend(1, 3);
        userService.addFriend(2, 3);
        List<User> common = userService.getCommonFriends(1, 2);
        assertEquals(1, common.size());
        assertEquals(3, common.get(0).getId());
    }

    @Test
    void testCRUDUser() {
        User user = new User(1, "a@a.ru", "user1", "User One", LocalDate.of(2000, 1, 1));
        userStorage.addUser(user);
        assertEquals(user, userService.getUserById(1).get());
        user.setName("Updated Name");
        userService.updateUser(user);
        assertEquals("Updated Name", userService.getUserById(1).get().getName());
    }
}

