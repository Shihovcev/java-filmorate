package ru.yandex.practicum.filmorate.service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void addLike(long filmId, long userId) {
        Film film = getFilmByIdOrThrow(filmId);
        userStorage.getUserById(userId).orElseThrow(() -> new NoSuchElementException("Пользователь с id=" + userId + " не найден"));
        film.getLikes().add(userId);
        filmStorage.updateFilm(film);
    }

    public void removeLike(long filmId, long userId) {
        Film film = getFilmByIdOrThrow(filmId);
        userStorage.getUserById(userId).orElseThrow(() -> new NoSuchElementException("Пользователь с id=" + userId + " не найден"));
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
        validateFilm(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        getFilmByIdOrThrow(film.getId());
        validateFilm(film);
        return filmStorage.updateFilm(film);
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Ошибка валидации: название фильма не может быть пустым");
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.warn("Ошибка валидации: описание превышает 200 символов");
            throw new ValidationException("Описание не должно превышать 200 символов");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(java.time.LocalDate.of(1895, 12, 28))) {
            log.warn("Ошибка валидации: некорректная дата релиза: {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895");
        }
        if (film.getDuration() == null || film.getDuration() <= 0) {
            log.warn("Ошибка валидации: продолжительность должна быть положительной, сейчас: {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }

    public java.util.Optional<Film> getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }

    public Film getFilmByIdOrThrow(long id) {
        return filmStorage.getFilmById(id)
                .orElseThrow(() -> new NoSuchElementException("Фильм с id=" + id + " не найден"));
    }
}
