package ru.fetrias.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.fetrias.model.Request;
import ru.fetrias.model.User;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ExcelExporter {
    public void export(List<User> users, List<Request> requests, Path path) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             OutputStream outputStream = Files.newOutputStream(path)) {
            createUsersSheet(workbook, users);
            createRequestsSheet(workbook, requests);
            workbook.write(outputStream);
        }
    }

    private void createUsersSheet(Workbook workbook, List<User> users) {
        Sheet sheet = workbook.createSheet("Пользователи");
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("ФИО");
        header.createCell(2).setCellValue("Дата рождения");
        header.createCell(3).setCellValue("Телефон");

        for (int index = 0; index < users.size(); index++) {
            User user = users.get(index);
            Row row = sheet.createRow(index + 1);
            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getFullName());
            row.createCell(2).setCellValue(user.getBirthDate().toString());
            row.createCell(3).setCellValue(user.getPhone());
        }

        autoSizeColumns(sheet, 4);
    }

    private void createRequestsSheet(Workbook workbook, List<Request> requests) {
        Sheet sheet = workbook.createSheet("Заявки");
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("ID пользователя");
        header.createCell(2).setCellValue("Санаторий");
        header.createCell(3).setCellValue("Дата заезда");
        header.createCell(4).setCellValue("Длительность");
        header.createCell(5).setCellValue("Дата создания");
        header.createCell(6).setCellValue("Статус");

        for (int index = 0; index < requests.size(); index++) {
            Request request = requests.get(index);
            Row row = sheet.createRow(index + 1);
            row.createCell(0).setCellValue(request.getId());
            row.createCell(1).setCellValue(request.getUserId());
            row.createCell(2).setCellValue(request.getSanatoriumName());
            row.createCell(3).setCellValue(request.getCheckInDate().toString());
            row.createCell(4).setCellValue(request.getDurationDays());
            row.createCell(5).setCellValue(request.getCreateDate().toString());
            row.createCell(6).setCellValue(request.getStatus().toString());
        }

        autoSizeColumns(sheet, 7);
    }

    private void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int column = 0; column < columnCount; column++) {
            sheet.autoSizeColumn(column);
        }
    }
}
