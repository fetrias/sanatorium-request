package ru.fetrias.repository;

import ru.fetrias.model.Request;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface RequestRepository {
    Request createRequest(Request request) throws SQLException;

    List<Request> getAllRequests() throws SQLException;

    Optional<Request> getRequestById(long id) throws SQLException;

    boolean changeRequest(Request request) throws SQLException;

    boolean deleteRequest(long id) throws SQLException;
}
