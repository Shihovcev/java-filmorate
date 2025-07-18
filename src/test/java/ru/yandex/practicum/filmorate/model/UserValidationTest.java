package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class UserValidationTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldFailValidationForBlankEmail() {
        User user = new User(1, "", "login", "Имя", LocalDate.of(2000, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldFailValidationForInvalidEmail() {
        User user = new User(1, "not-an-email", "login", "Имя", LocalDate.of(2000, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldFailValidationForBlankLogin() {
        User user = new User(1, "user@mail.com", "", "Имя", LocalDate.of(2000, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("login")));
    }

    @Test
    void shouldFailValidationForLoginWithSpaces() {
        User user = new User(1, "user@mail.com", "лог ин", "Имя", LocalDate.of(2000, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("login")));
    }

    @Test
    void shouldFailValidationForNullBirthday() {
        User user = new User(1, "user@mail.com", "login", "Имя", null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("birthday")));
    }

    @Test
    void shouldFailValidationForFutureBirthday() {
        User user = new User(1, "user@mail.com", "login", "Имя", LocalDate.now().plusDays(1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("birthday")));
    }

    @Test
    void shouldPassValidationForValidUser() {
        User user = new User(1, "user@mail.com", "login", "Имя", LocalDate.of(2000, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldAllowNullOrAnyName() {
        User user = new User(1, "user@mail.com", "login", null, LocalDate.of(2000, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        // name не должен валидироваться, так как нет ограничений
        assertTrue(violations.isEmpty());

        user = new User(1, "user@mail.com", "login", "", LocalDate.of(2000, 1, 1));
        violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldSetNameToLoginIfNameIsNullOrBlank() {
        User user = new User(1, "user@mail.com", "common", null, LocalDate.of(2000, 1, 1));
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        assertEquals("common", user.getName());

        user = new User(1, "user@mail.com", "common", "", LocalDate.of(2000, 1, 1));
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        assertEquals("common", user.getName());
    }
}
