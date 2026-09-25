package ru.fetrias.repository;

import ru.fetrias.model.User;
import ru.fetrias.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcUserRepository implements UserRepository{

    @Override
    public User createUser(User user) throws SQLException {
        String sql = """
            INSERT INTO users (full_name, birth_date, phone)
            VALUES (?, ?, ?)
            RETURNING id
            """;
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getFullName());
            statement.setObject(2, user.getBirthDate());
            statement.setString(3, user.getPhone());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user.setId(resultSet.getLong("id"));
                    return user;
                }
            }
        }
        throw new SQLException("Не удалось создать пользователя");
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        String sql = """
                SELECT * FROM users
                """;
        List<User> users = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    User user = new User(
                            resultSet.getLong("id"),
                            resultSet.getString("full_name"),
                            resultSet.getObject("birth_date", LocalDate.class),
                            resultSet.getString("phone")
                    );
                    users.add(user);
                }
            }
        }
        return users;
    }

    @Override
    public Optional<User> getUserById(long id) throws SQLException {
        String sql = """
                SELECT * FROM users WHERE id = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User(
                            resultSet.getLong("id"),
                            resultSet.getString("full_name"),
                            resultSet.getObject("birth_date", LocalDate.class),
                            resultSet.getString("phone")
                    );
                    return Optional.of(user);
                }
            }

        }
        return Optional.empty();
    }

    @Override
    public boolean changeUser(User user) throws SQLException {
        String sql = """
                UPDATE users
                SET full_name = ?, birth_date = ?, phone = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getFullName());
            statement.setObject(2, user.getBirthDate());
            statement.setString(3, user.getPhone());
            statement.setLong(4, user.getId());

            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteUser(long id) throws SQLException {
        String sql = """
                DELETE FROM users WHERE id = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            return statement.executeUpdate() > 0;
        }
    }
}
