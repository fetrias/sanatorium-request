package ru.fetrias.service;

import ru.fetrias.exception.BusinessException;
import ru.fetrias.exception.EntityNotFoundException;
import ru.fetrias.model.User;
import ru.fetrias.repository.UserRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository repository;

    public UserService (UserRepository repository) {
        this.repository = repository;
    }

    public User createUser(User user) {
        validateUser(user);
        try {
            return repository.createUser(user);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании пользователя", e);
        }
    }

    public List<User> getAllUsers() {
        try {
            return repository.getAllUsers();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении пользователей", e);
        }
    }

    public User getUserById(long id) {
        try {
            Optional<User> result = repository.getUserById(id);
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Пользователь не найден");
            }
            return result.get();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении пользователя", e);
        }
    }

    public boolean changeUser(User user) {
        validateUser(user);
        try {
            boolean changed = repository.changeUser(user);
            if (!changed) {
                throw new EntityNotFoundException("Пользователь не найден");
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при изменении пользователя", e);
        }
    }

    public void deleteUser(long id) {
        try {
            boolean deleted = repository.deleteUser(id);
            if (!deleted) {
                throw new EntityNotFoundException("Пользователь не найден");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении пользователя", e);
        }
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new BusinessException("Пользователь не указан.");
        }
        if (user.getFullName() == null || user.getFullName().isBlank()) {
            throw new BusinessException("ФИО не может быть пустым.");
        }
        if (user.getBirthDate() == null) {
            throw new BusinessException("Дата не может быть пустой.");
        }
        if (user.getBirthDate().isAfter(LocalDate.now())) {
            throw new BusinessException("Дата не может быть из будущего.");
        }
        if (user.getPhone() == null || user.getPhone().isBlank()) {
            throw new BusinessException("Номер телефона не может быть пустым.");
        }
    }
}
