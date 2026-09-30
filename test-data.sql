INSERT INTO sanatorium.users (full_name, birth_date, phone)
VALUES
    ('Иванов Иван Иванович', DATE '1985-03-12', '+79990000001'),
    ('Петрова Анна Сергеевна', DATE '1992-07-24', '+79990000002'),
    ('Сидоров Алексей Олегович', DATE '1978-11-05', '+79990000003'),
    ('Кузнецова Мария Павловна', DATE '1989-01-17', '+79990000004'),
    ('Смирнов Дмитрий Андреевич', DATE '1995-09-30', '+79990000005')
ON CONFLICT (phone) DO NOTHING;

WITH request_data (phone, sanatorium_name, check_in_date, duration_days, create_date, status) AS (
    VALUES
        ('+79990000001', 'Сосновый бор', DATE '2026-10-10', 14, DATE '2026-09-01', 'CREATED'::sanatorium.request_status),
        ('+79990000002', 'Белые ночи', DATE '2026-10-15', 10, DATE '2026-09-02', 'UNDER_REVIEW'::sanatorium.request_status),
        ('+79990000003', 'Горный воздух', DATE '2026-10-20', 21, DATE '2026-09-03', 'APPROVED'::sanatorium.request_status),
        ('+79990000004', 'Родник', DATE '2026-10-25', 7, DATE '2026-09-04', 'REJECTED'::sanatorium.request_status),
        ('+79990000005', 'Зеленая роща', DATE '2026-11-01', 14, DATE '2026-09-05', 'CANCELLED'::sanatorium.request_status),
        ('+79990000001', 'Морской берег', DATE '2026-11-08', 12, DATE '2026-09-06', 'UNDER_REVIEW'::sanatorium.request_status),
        ('+79990000002', 'Сосновый бор', DATE '2026-11-15', 18, DATE '2026-09-07', 'APPROVED'::sanatorium.request_status),
        ('+79990000003', 'Лесная поляна', DATE '2026-11-22', 9, DATE '2026-09-08', 'CREATED'::sanatorium.request_status),
        ('+79990000004', 'Солнечный', DATE '2026-12-01', 16, DATE '2026-09-09', 'APPROVED'::sanatorium.request_status),
        ('+79990000005', 'Жемчужина', DATE '2026-12-10', 20, DATE '2026-09-10', 'CANCELLED'::sanatorium.request_status)
)
INSERT INTO sanatorium.requests
    (user_id, sanatorium_name, check_in_date, duration_days, create_date, status)
SELECT
    users.id,
    request_data.sanatorium_name,
    request_data.check_in_date,
    request_data.duration_days,
    request_data.create_date,
    request_data.status
FROM request_data
JOIN sanatorium.users AS users ON users.phone = request_data.phone
WHERE NOT EXISTS (
    SELECT 1
    FROM sanatorium.requests AS requests
    WHERE requests.user_id = users.id
      AND requests.sanatorium_name = request_data.sanatorium_name
      AND requests.check_in_date = request_data.check_in_date
);
