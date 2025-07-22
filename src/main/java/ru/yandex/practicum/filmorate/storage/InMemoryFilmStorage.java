package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long nextId = 1;

    @Override
    public Film addFilm(Film film) {
        film.setId((int) nextId++);
        films.put((long) film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey((long) film.getId())) {
            throw new NoSuchElementException("Фильм с id=" + film.getId() + " не найден");
        }
        films.put((long) film.getId(), film);
        return film;
    }

    @Override
    public void deleteFilm(long id) {
        films.remove(id);
    }

    @Override
    public Optional<Film> getFilmById(long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }
}

