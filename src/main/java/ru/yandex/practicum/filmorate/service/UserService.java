package ru.yandex.practicum.filmorate.service;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.NoSuchElementException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public void addFriend(long userId, long friendId) {
        User user = getUserByIdOrThrow(userId);
        User friend = getUserByIdOrThrow(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
    }

    public void removeFriend(long userId, long friendId) {
        User user = getUserByIdOrThrow(userId);
        User friend = getUserByIdOrThrow(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
    }

    public List<User> getCommonFriends(long userId, long otherId) {
        User user = getUserByIdOrThrow(userId);
        User other = getUserByIdOrThrow(otherId);
        return user.getFriends().stream()
                .filter(other.getFriends()::contains)
                .map(friendId -> userStorage.getUserById(friendId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User addUser(User user) {
        validateUser(user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        getUserByIdOrThrow(user.getId());
        validateUser(user);
        return userStorage.updateUser(user);
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Ошибка валидации email: {}", user.getEmail());
            throw new ru.yandex.practicum.filmorate.exception.ValidationException("Некорректный email");
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

    public java.util.Optional<User> getUserById(long id) {
        return userStorage.getUserById(id);
    }

    public List<User> getFriends(long userId) {
        User user = getUserByIdOrThrow(userId);
        return user.getFriends().stream()
                .map(friendId -> userStorage.getUserById(friendId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public User getUserByIdOrThrow(long id) {
        return userStorage.getUserById(id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с id=" + id + " не найден"));
    }
}
