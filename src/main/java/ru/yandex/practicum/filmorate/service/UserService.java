package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(long userId, long friendId) {
        User user = userStorage.getUserById(userId).orElseThrow();
        User friend = userStorage.getUserById(friendId).orElseThrow();
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
    }

    public void removeFriend(long userId, long friendId) {
        User user = userStorage.getUserById(userId).orElseThrow();
        User friend = userStorage.getUserById(friendId).orElseThrow();
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
    }

    public List<User> getCommonFriends(long userId, long otherId) {
        User user = userStorage.getUserById(userId).orElseThrow();
        User other = userStorage.getUserById(otherId).orElseThrow();
        Set<Long> commonIds = user.getFriends();
        commonIds.retainAll(other.getFriends());
        return commonIds.stream()
                .map(id -> userStorage.getUserById(id).orElse(null))
                .filter(u -> u != null)
                .collect(Collectors.toList());
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public java.util.Optional<User> getUserById(long id) {
        return userStorage.getUserById(id);
    }
}
