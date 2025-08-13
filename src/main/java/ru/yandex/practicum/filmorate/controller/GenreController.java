package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreRepository genreRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable int id) {
        log.info("Получение жанра по id={}", id);
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "Жанр с id = " + id + " не найден"
                ));
        return ResponseEntity.ok(genre);
    }

    @GetMapping
    public ResponseEntity<List<Genre>> getAllGenres() {
        log.info("Получение всех жанров");
        List<Genre> genres = genreRepository.findAll();
        return ResponseEntity.ok(genres);
    }
}
