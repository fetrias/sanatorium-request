# Система заявок на путевки в санаторий

## Требования

- Java 21
- PostgreSQL
- Maven

## Создание базы данных

Открыть терминал в папке проекта и выполнить:

```powershell
createdb -U postgres sanatorium_request
psql -U postgres -d sanatorium_request -f ".\database.sql"
psql -U postgres -d sanatorium_request -f ".\test-data.sql"
```

Пароль PostgreSQL по умолчанию: `1234`.

Если используется другой пользователь или пароль, изменить настройки в файле:

`src/main/java/ru/fetrias/util/DatabaseManager.java`

## Запуск

1. Открыть проект в IntelliJ IDEA.
2. Обновить Maven-проект.
3. Запустить метод `main` из файла `src/main/java/ru/fetrias/Main.java`.

## Экспорт

Выбрать пункт `4. Экспорт данных` в главном меню.

Файл `sanatorium-data.xlsx` будет создан в корне проекта.
