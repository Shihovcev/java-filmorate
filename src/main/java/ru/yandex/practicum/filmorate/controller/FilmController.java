package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
public final class FilmController {

    private final FilmService filmService;

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable int id) {
        log.info("Получение фильма по id={}", id);
        return ResponseEntity.ok(filmService.getFilmById(id));
    }

    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody final Film film) {
        log.info("Добавление нового фильма: {}", film.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(filmService.createFilm(film));
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody final Film film) {
        log.info("Обновление фильма с id={}", film.getId());
        return ResponseEntity.ok(filmService.updateFilm(film));
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        log.info("Получение всех фильмов");
        return ResponseEntity.ok(filmService.getAllFilms());
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Получение {} популярных фильмов", count);
        return ResponseEntity.ok(filmService.getTopFilms(count));
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Добавление лайка пользователем {} фильму {}", userId, id);
        filmService.addLike(id, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> removeLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Удаление лайка пользователем {} фильму {}", userId, id);
        filmService.removeLike(id, userId);
        return ResponseEntity.ok().build();
    }

}
