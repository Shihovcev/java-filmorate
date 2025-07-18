package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(long id);

    Optional<User> getUserById(long id);

    List<User> getAllUsers();
}

