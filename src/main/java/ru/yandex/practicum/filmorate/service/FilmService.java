package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(long filmId, long userId) {
        Film film = filmStorage.getFilmById(filmId).orElseThrow();
        userStorage.getUserById(userId).orElseThrow(() -> new java.util.NoSuchElementException("Пользователь с id=" + userId + " не найден"));
        film.getLikes().add(userId);
        filmStorage.updateFilm(film);
    }

    public void removeLike(long filmId, long userId) {
        Film film = filmStorage.getFilmById(filmId).orElseThrow();
        userStorage.getUserById(userId).orElseThrow(() -> new java.util.NoSuchElementException("Пользователь с id=" + userId + " не найден"));
        film.getLikes().remove(userId);
        filmStorage.updateFilm(film);
    }

    public List<Film> getTopFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(f -> -f.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public java.util.Optional<Film> getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }
}
