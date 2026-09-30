package ru.fetrias.repository;

import ru.fetrias.util.DatabaseManager;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcDatabaseRepository implements DatabaseRepository {
    @Override
    public List<String> getTableNames() throws SQLException {
        List<String> tableNames = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection()) {
            DatabaseMetaData metadata = connection.getMetaData();

            try (ResultSet resultSet = metadata.getTables(
                    null, "sanatorium", "%", new String[]{"TABLE"}
            )) {
                while (resultSet.next()) {
                    tableNames.add(resultSet.getString("TABLE_NAME"));
                }
            }
        }

        return tableNames.stream().sorted().toList();
    }
}
