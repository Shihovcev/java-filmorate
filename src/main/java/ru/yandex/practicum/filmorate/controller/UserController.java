package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        validateUser(user);
        User added = userService.addUser(user);
        log.info("Пользователь добавлен: {}", added);
        return added;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User updatedUser) {
        if (userService.getUserById(updatedUser.getId()).isEmpty()) {
            log.warn("Попытка обновить несуществующего пользователя с id={}", updatedUser.getId());
            throw new NoSuchElementException("Пользователь с id=" + updatedUser.getId() + " не найден");
        }
        validateUser(updatedUser);
        User updated = userService.updateUser(updatedUser);
        log.info("Пользователь обновлён: {}", updated);
        return updated;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        return userService.getUserById(id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с id=" + id + " не найден"));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с id=" + id + " не найден"));
        return user.getFriends().stream()
                .map(friendId -> userService.getUserById(friendId).orElse(null))
                .filter(u -> u != null)
                .toList();
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Ошибка валидации email: {}", user.getEmail());
            throw new ValidationException("Некорректный email");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Ошибка валидации login: {}", user.getLogin());
            throw new ValidationException("Некорректный логин");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(java.time.LocalDate.now())) {
            log.warn("Ошибка валидации даты рождения: {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

}
