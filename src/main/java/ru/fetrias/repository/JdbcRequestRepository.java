package ru.fetrias.repository;

import ru.fetrias.model.Request;
import ru.fetrias.model.RequestStatus;
import ru.fetrias.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcRequestRepository implements RequestRepository{
    @Override
    public Request createRequest(Request request) throws SQLException {
        String sql = """
                INSERT INTO requests
                    (user_id, sanatorium_name, check_in_date, duration_days, create_date, status)
                VALUES (?, ?, ?, ?, ?, CAST(? AS sanatorium.request_status))
                RETURNING id
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, request.getUserId());
            statement.setString(2, request.getSanatoriumName());
            statement.setObject(3, request.getCheckInDate());
            statement.setInt(4, request.getDurationDays());
            statement.setObject(5, request.getCreateDate());
            statement.setString(6, request.getStatus().name());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    request.setId(resultSet.getLong("id"));
                    return request;
                }
            }
        }

        throw new SQLException("Не удалось создать заявку");
    }

    @Override
    public List<Request> getAllRequests() throws SQLException {
        String sql = """
                SELECT * from requests
                """;
        List<Request> requests = new ArrayList<>();

        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Request request = new Request(
                            resultSet.getLong("id"),
                            resultSet.getLong("user_id"),
                            resultSet.getString("sanatorium_name"),
                            resultSet.getObject("check_in_date", LocalDate.class),
                            resultSet.getInt("duration_days"),
                            resultSet.getObject("create_date", LocalDate.class),
                            RequestStatus.valueOf(resultSet.getString("status"))
                    );
                    requests.add(request);
                }
            }
        }
        return requests;
    }

    @Override
    public Optional<Request> getRequestById(long id) throws SQLException {
        String sql = """
                SELECT * FROM requests WHERE id = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Request request = new Request(
                            resultSet.getLong("id"),
                            resultSet.getLong("user_id"),
                            resultSet.getString("sanatorium_name"),
                            resultSet.getObject("checkin_date", LocalDate.class),
                            resultSet.getInt("duration_days"),
                            resultSet.getObject("create_date", LocalDate.class),
                            RequestStatus.valueOf(resultSet.getString("status"))
                    );
                    return Optional.of(request);
                }
            }

        }
        return Optional.empty();
    }

    @Override
    public boolean changeRequest(Request request) throws SQLException {
        String sql = """
                UPDATE requests
                SET user_id = ?, sanatorium_name = ?, check_in_date = ?,
                duration_days = ?, create_date = ?,
                status = CAST(? AS sanatorium.request_status)
                WHERE id = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, request.getUserId());
            statement.setString(2, request.getSanatoriumName());
            statement.setObject(3, request.getCheckInDate());
            statement.setInt(4, request.getDurationDays());
            statement.setObject(5, request.getCreateDate());
            statement.setString(6, request.getStatus().name());
            statement.setLong(7, request.getId());

            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteRequest(long id) throws SQLException {
        String sql = """
                DELETE FROM requests WHERE id = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            return statement.executeUpdate() > 0;
        }
    }
}
