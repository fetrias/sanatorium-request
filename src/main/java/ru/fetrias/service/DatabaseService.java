package ru.fetrias.service;

import ru.fetrias.repository.DatabaseRepository;

import java.sql.SQLException;
import java.util.List;

public class DatabaseService {
    private final DatabaseRepository databaseRepository;

    public DatabaseService(DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
    }

    public List<String> getTableNames() {
        try {
            return databaseRepository.getTableNames();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении таблиц базы данных", e);
        }
    }
}
