package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {
    private FilmService filmService;
    private InMemoryFilmStorage filmStorage;

    @BeforeEach
    void setUp() {
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage);
    }

    @Test
    void testAddAndRemoveLike() {
        Film film = new Film(1, "Film One", "desc", LocalDate.of(2000, 1, 1), 120.0);
        filmStorage.addFilm(film);
        filmService.addLike(1, 100L);
        assertTrue(filmService.getFilmById(1).get().getLikes().contains(100L));
        filmService.removeLike(1, 100L);
        assertFalse(filmService.getFilmById(1).get().getLikes().contains(100L));
    }

    @Test
    void testGetTopFilms() {
        Film film1 = new Film(1, "Film One", "desc", LocalDate.of(2000, 1, 1), 120.0);
        Film film2 = new Film(2, "Film Two", "desc", LocalDate.of(2000, 2, 2), 90.0);
        filmStorage.addFilm(film1);
        filmStorage.addFilm(film2);
        filmService.addLike(1, 100L);
        filmService.addLike(1, 101L);
        filmService.addLike(2, 102L);
        List<Film> top = filmService.getTopFilms(1);
        assertEquals(1, top.size());
        assertEquals(1, top.get(0).getId());
    }

    @Test
    void testCRUDFilm() {
        Film film = new Film(1, "Film One", "desc", LocalDate.of(2000, 1, 1), 120.0);
        filmStorage.addFilm(film);
        assertEquals(film, filmService.getFilmById(1).get());
        film.setName("Updated Film");
        filmService.updateFilm(film);
        assertEquals("Updated Film", filmService.getFilmById(1).get().getName());
    }
}

