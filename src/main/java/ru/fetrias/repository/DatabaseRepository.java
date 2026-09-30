package ru.fetrias.repository;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseRepository {
    List<String> getTableNames() throws SQLException;
}
