package ru.fetrias.service;

import ru.fetrias.util.ExcelExporter;

import java.io.IOException;
import java.nio.file.Path;

public class ExportService {
    private final UserService userService;
    private final RequestService requestService;
    private final ExcelExporter excelExporter;

    public ExportService(UserService userService, RequestService requestService,
                         ExcelExporter excelExporter) {
        this.userService = userService;
        this.requestService = requestService;
        this.excelExporter = excelExporter;
    }

    public Path exportData() {
        Path path = Path.of("sanatorium-data.xlsx").toAbsolutePath();

        try {
            excelExporter.export(
                    userService.getAllUsers(),
                    requestService.getAllRequests(),
                    path
            );
            return path;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при экспорте данных", e);
        }
    }
}
