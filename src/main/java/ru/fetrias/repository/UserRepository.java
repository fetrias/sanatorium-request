package ru.fetrias.repository;

import ru.fetrias.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User createUser(User user) throws SQLException;

    List<User> getAllUsers() throws SQLException;

    Optional<User> getUserById(long id) throws SQLException;

    boolean changeUser(User user) throws SQLException;

    boolean deleteUser(long id) throws SQLException;
}
