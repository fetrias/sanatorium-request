package ru.fetrias.ui;


import ru.fetrias.exception.BusinessException;
import ru.fetrias.exception.EntityNotFoundException;
import ru.fetrias.model.Request;
import ru.fetrias.model.RequestStatus;
import ru.fetrias.model.User;
import ru.fetrias.service.RequestService;
import ru.fetrias.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner;
    private final UserService userService;
    private final RequestService requestService;

    public ConsoleUI (UserService userService, RequestService requestService) {
        this.userService = userService;
        this.requestService = requestService;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        boolean running = true;

        while (running) {
            showMenu();

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> showUserMenu();
                case "2" -> showRequestMenu();
                case "3" -> System.out.println("Статистика");
                case "4" -> System.out.println("Экспорт данных");
                case "5" -> System.out.println("Таблицы базы данных");
                case "0" -> {
                    System.out.println("Завершение программы");
                    running = false;
                }
                default -> System.out.println("Выберите существующий пункт меню");
            }
        }
    }

    private void showMenu() {
        System.out.println("""
                ======================================== СИСТЕМА ЗАЯВОК НА ПУТЕВКИ ======================================== 
                1. Пользователи
                2. Заявки
                3. Статистика
                4. Экспорт данных
                5. Вывести таблицы базы данных
                0. Выход
                Выберите действие:""");
    }

    private void showUserMenu() {
        while (true) {
            System.out.println("""
                =============== ПОЛЬЗОВАТЕЛИ ===============
                1. Создать пользователя
                2. Вывести всех пользователей
                3. Найти пользователя по ID
                4. Найти пользователя по ФИО
                5. Изменить пользователя
                6. Удалить пользователя
                0. Вернуться в главное меню
                Выберите действие:
                """);

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> createUser();
                case "2" -> showAllUsers();
                case "3" -> showUserById();
                case "4" -> showUsersByFullName();
                case "5" -> changeUser();
                case "6" -> deleteUser();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Выберите существующий пункт меню");
            }
        }
    }

    private void createUser() {
        try {
            System.out.println("Введите ФИО пользователя:");
            String fullName = scanner.nextLine().trim();

            System.out.println("Введите дату рождения в формате ГГГГ-ММ-ДД:");
            LocalDate birthDate = LocalDate.parse(scanner.nextLine().trim());

            System.out.println("Введите номер телефона:");
            String phone = scanner.nextLine().trim();

            User user = new User(fullName, birthDate, phone);
            User createdUser = userService.createUser(user);

            System.out.println("Пользователь создан. ID: " + createdUser.getId());
        } catch (DateTimeParseException e) {
            System.out.println("Ошибка: дата должна быть в формате ГГГГ-ММ-ДД");
        } catch (BusinessException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
        }
    }

    private void showAllUsers() {
        try {
            List<User> users = userService.getAllUsers();

            if (users.isEmpty()) {
                System.out.println("Пользователей пока нет");
                return;
            }

            users.forEach(System.out::println);
        } catch (RuntimeException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
        }
    }

    private void showUserById() {
        try {
            System.out.println("Введите ID пользователя:");
            long id = Long.parseLong(scanner.nextLine().trim());

            User user = userService.getUserById(id);
            System.out.println(user);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть целым числом");
        } catch (EntityNotFoundException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
        }
    }

    private void showUsersByFullName() {
        try {
            System.out.println("Введите ФИО или его часть:");
            String fullName = scanner.nextLine().trim();

            List<User> users = userService.getUsersByFullName(fullName);

            if (users.isEmpty()) {
                System.out.println("Пользователи не найдены");
                return;
            }

            users.forEach(System.out::println);
        } catch (BusinessException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
        }
    }

    private void changeUser() {
        try {
            System.out.println("Введите ID пользователя:");
            long id = Long.parseLong(scanner.nextLine().trim());

            System.out.println("Введите новое ФИО:");
            String fullName = scanner.nextLine().trim();

            System.out.println("Введите новую дату рождения в формате ГГГГ-ММ-ДД:");
            LocalDate birthDate = LocalDate.parse(scanner.nextLine().trim());

            System.out.println("Введите новый номер телефона:");
            String phone = scanner.nextLine().trim();

            User user = new User(id, fullName, birthDate, phone);
            userService.changeUser(user);

            System.out.println("Пользователь изменен");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть целым числом");
        } catch (DateTimeParseException e) {
            System.out.println("Ошибка: дата должна быть в формате ГГГГ-ММ-ДД");
        } catch (BusinessException | EntityNotFoundException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
        }
    }

    private void deleteUser() {
        try {
            System.out.println("Введите ID пользователя:");
            long id = Long.parseLong(scanner.nextLine().trim());

            userService.deleteUser(id);
            System.out.println("Пользователь удален");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть целым числом");
        } catch (EntityNotFoundException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
        }
    }

    private void showRequestMenu() {
        while (true) {
            System.out.println("""
                    =============== ЗАЯВКИ ===============
                    1. Создать заявку
                    2. Вывести все заявки
                    3. Найти заявку по ID
                    4. Изменить заявку
                    5. Удалить заявку
                    6. Найти заявки по названию санатория
                    0. Вернуться в главное меню
                    Выберите действие:
                    """);

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> createRequest();
                case "2" -> showAllRequests();
                case "3" -> showRequestById();
                case "4" -> changeRequest();
                case "5" -> deleteRequest();
                case "6" -> showRequestsBySanatoriumName();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Выберите существующий пункт меню");
            }
        }
    }

    private void createRequest() {
        try {
            System.out.println("Введите ID пользователя:");
            long userId = Long.parseLong(scanner.nextLine().trim());

            System.out.println("Введите название санатория:");
            String sanatoriumName = scanner.nextLine().trim();

            System.out.println("Введите дату заезда в формате ГГГГ-ММ-ДД:");
            LocalDate checkInDate = LocalDate.parse(scanner.nextLine().trim());

            System.out.println("Введите длительность путевки в днях:");
            int durationDays = Integer.parseInt(scanner.nextLine().trim());

            Request request = new Request(
                    userId, sanatoriumName, checkInDate, durationDays, null, null
            );
            Request createdRequest = requestService.createRequest(request);

            System.out.println("Заявка создана. ID: " + createdRequest.getId());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID и длительность должны быть целыми числами");
        } catch (DateTimeParseException e) {
            System.out.println("Ошибка: дата должна быть в формате ГГГГ-ММ-ДД");
        } catch (BusinessException | EntityNotFoundException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
        }
    }

    private void showAllRequests() {
        try {
            List<Request> requests = requestService.getAllRequests();

            if (requests.isEmpty()) {
                System.out.println("Заявок пока нет");
                return;
            }

            requests.forEach(System.out::println);
        } catch (RuntimeException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
        }
    }

    private void showRequestById() {
        try {
            System.out.println("Введите ID заявки:");
            long id = Long.parseLong(scanner.nextLine().trim());

            Request request = requestService.getRequestById(id);
            System.out.println(request);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть целым числом");
        } catch (EntityNotFoundException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
        }
    }

    private void changeRequest() {
        try {
            System.out.println("Введите ID заявки:");
            long id = Long.parseLong(scanner.nextLine().trim());

            System.out.println("Введите ID пользователя:");
            long userId = Long.parseLong(scanner.nextLine().trim());

            System.out.println("Введите новое название санатория:");
            String sanatoriumName = scanner.nextLine().trim();

            System.out.println("Введите новую дату заезда в формате ГГГГ-ММ-ДД:");
            LocalDate checkInDate = LocalDate.parse(scanner.nextLine().trim());

            System.out.println("Введите новую длительность путевки в днях:");
            int durationDays = Integer.parseInt(scanner.nextLine().trim());

            System.out.println(
                    "Введите статус: CREATED, UNDER_REVIEW, APPROVED, REJECTED или CANCELLED"
            );
            RequestStatus status = RequestStatus.valueOf(
                    scanner.nextLine().trim().toUpperCase()
            );

            Request request = new Request(
                    id, userId, sanatoriumName, checkInDate, durationDays, null, status
            );
            requestService.changeRequest(request);

            System.out.println("Заявка изменена");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID и длительность должны быть целыми числами");
        } catch (DateTimeParseException e) {
            System.out.println("Ошибка: дата должна быть в формате ГГГГ-ММ-ДД");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: указан некорректный статус");
        } catch (BusinessException | EntityNotFoundException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
        }
    }

    private void deleteRequest() {
        try {
            System.out.println("Введите ID заявки:");
            long id = Long.parseLong(scanner.nextLine().trim());

            requestService.deleteRequest(id);
            System.out.println("Заявка удалена");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть целым числом");
        } catch (EntityNotFoundException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
        }
    }

    private void showRequestsBySanatoriumName() {
        try {
            System.out.println("Введите название санатория или его часть:");
            String sanatoriumName = scanner.nextLine().trim();

            List<Request> requests = requestService.searchBySanatoriumName(sanatoriumName);
            if (requests.isEmpty()) {
                System.out.println("Заявки не найдены");
                return;
            }

            requests.forEach(System.out::println);
        } catch (BusinessException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
        }
    }
}
