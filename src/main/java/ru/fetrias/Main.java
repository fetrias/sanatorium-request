package ru.fetrias;

import ru.fetrias.repository.DatabaseRepository;
import ru.fetrias.repository.JdbcDatabaseRepository;
import ru.fetrias.repository.JdbcRequestRepository;
import ru.fetrias.repository.JdbcUserRepository;
import ru.fetrias.repository.RequestRepository;
import ru.fetrias.repository.UserRepository;
import ru.fetrias.service.DatabaseService;
import ru.fetrias.service.ExportService;
import ru.fetrias.service.RequestService;
import ru.fetrias.service.StatisticsService;
import ru.fetrias.service.UserService;
import ru.fetrias.ui.ConsoleUI;
import ru.fetrias.util.ExcelExporter;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new JdbcUserRepository();
        RequestRepository requestRepository = new JdbcRequestRepository();
        DatabaseRepository databaseRepository = new JdbcDatabaseRepository();

        UserService userService = new UserService(userRepository);
        RequestService requestService = new RequestService(requestRepository, userRepository);
        DatabaseService databaseService = new DatabaseService(databaseRepository);
        StatisticsService statisticsService = new StatisticsService(userService, requestService);
        ExportService exportService = new ExportService(
                userService, requestService, new ExcelExporter()
        );

        ConsoleUI consoleUI = new ConsoleUI(
                userService, requestService, statisticsService, exportService, databaseService
        );
        consoleUI.run();
    }
}
