package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FilmController.class)
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FilmService filmService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getFilmById_shouldReturnFilm() throws Exception {
        Film film = new Film(1, "Film One", "desc", LocalDate.of(2000, 1, 1), 120.0);
        when(filmService.getFilmById(1)).thenReturn(Optional.of(film));
        mockMvc.perform(get("/films/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Film One"));
    }

    @Test
    void addFilm_shouldReturnCreatedFilm() throws Exception {
        Film film = new Film(1, "Film One", "desc", LocalDate.of(2000, 1, 1), 120.0);
        when(filmService.addFilm(any(Film.class))).thenReturn(film);
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Film One"));
    }

    @Test
    void updateFilm_shouldReturnUpdatedFilm() throws Exception {
        Film film = new Film(1, "Film One", "desc", LocalDate.of(2000, 1, 1), 120.0);
        when(filmService.getFilmById(1)).thenReturn(Optional.of(film));
        when(filmService.updateFilm(any(Film.class))).thenReturn(film);
        mockMvc.perform(put("/films")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void addLike_shouldReturnOk() throws Exception {
        doNothing().when(filmService).addLike(1, 100L);
        mockMvc.perform(put("/films/1/like/100"))
                .andExpect(status().isOk());
    }

    @Test
    void removeLike_shouldReturnOk() throws Exception {
        doNothing().when(filmService).removeLike(1, 100L);
        mockMvc.perform(delete("/films/1/like/100"))
                .andExpect(status().isOk());
    }

    @Test
    void getPopularFilms_shouldReturnListOfFilms() throws Exception {
        Film film1 = new Film(1, "Film One", "desc", LocalDate.of(2000, 1, 1), 120.0);
        Film film2 = new Film(2, "Film Two", "desc", LocalDate.of(2000, 2, 2), 90.0);
        when(filmService.getTopFilms(2)).thenReturn(List.of(film1, film2));
        mockMvc.perform(get("/films/popular?count=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }
}

