package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {

    private final MpaRepository mpaRepository;

    @GetMapping
    public ResponseEntity<List<Mpa>> getAllMpa() {
        log.info("Получение всех MPA рейтингов");
        List<Mpa> mpas = mpaRepository.findAll();
        return ResponseEntity.ok(mpas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mpa> getMpaById(@PathVariable int id) {
        log.info("Получение MPA рейтинга по id={}", id);
        Mpa mpa = mpaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "MPA рейтинг с id = " + id + " не найден"
                ));
        return ResponseEntity.ok(mpa);
    }
}
