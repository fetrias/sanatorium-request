package ru.fetrias;

import ru.fetrias.repository.JdbcRequestRepository;
import ru.fetrias.repository.JdbcUserRepository;
import ru.fetrias.repository.RequestRepository;
import ru.fetrias.repository.UserRepository;
import ru.fetrias.service.RequestService;
import ru.fetrias.service.UserService;
import ru.fetrias.ui.ConsoleUI;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new JdbcUserRepository();
        RequestRepository requestRepository = new JdbcRequestRepository();

        UserService userService = new UserService(userRepository);
        RequestService requestService = new RequestService(requestRepository, userRepository);

        ConsoleUI consoleUI = new ConsoleUI(userService, requestService);
        consoleUI.run();
    }
}