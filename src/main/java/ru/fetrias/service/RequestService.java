package ru.fetrias.service;

import ru.fetrias.exception.BusinessException;
import ru.fetrias.exception.EntityNotFoundException;
import ru.fetrias.model.Request;
import ru.fetrias.model.RequestStatus;
import ru.fetrias.repository.RequestRepository;
import ru.fetrias.repository.UserRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    public RequestService(RequestRepository requestRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    public Request createRequest(Request request) {
        validateRequest(request);
        ensureUserExists(request.getUserId());
        request.setCreateDate(LocalDate.now());
        request.setStatus(RequestStatus.CREATED);

        try {
            return requestRepository.createRequest(request);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании заявки", e);
        }
    }

    public List<Request> getAllRequests() {
        try {
            return requestRepository.getAllRequests();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении заявок", e);
        }
    }

    public Request getRequestById(long id) {
        try {
            Optional<Request> result = requestRepository.getRequestById(id);
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Заявка не найдена");
            }
            return result.get();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении заявки", e);
        }
    }

    public boolean changeRequest(Request request) {
        validateRequest(request);
        ensureUserExists(request.getUserId());

        Request existingRequest = getRequestById(request.getId());
        validateStatusTransition(existingRequest.getStatus(), request.getStatus());
        request.setCreateDate(existingRequest.getCreateDate());

        try {
            boolean changed = requestRepository.changeRequest(request);
            if (!changed) {
                throw new EntityNotFoundException("Заявка не найдена");
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при изменении заявки", e);
        }
    }

    public void deleteRequest(long id) {
        try {
            boolean deleted = requestRepository.deleteRequest(id);
            if (!deleted) {
                throw new EntityNotFoundException("Заявка не найдена");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении заявки", e);
        }
    }

    private void validateRequest(Request request) {
        if (request == null) {
            throw new BusinessException("Заявка не указана.");
        }
        if (request.getSanatoriumName() == null || request.getSanatoriumName().isBlank()) {
            throw new BusinessException("Название санатория не может быть пустым.");
        }
        if (request.getCheckInDate() == null) {
            throw new BusinessException("Дата заезда не указана.");
        }
        if (request.getCheckInDate().isBefore(LocalDate.now())) {
            throw new BusinessException("Дата заезда не может быть в прошлом.");
        }
        if (request.getDurationDays() <= 0 || request.getDurationDays() > 30) {
            throw new BusinessException("Длительность путевки должна быть от 1 до 30 дней.");
        }
    }

    private void ensureUserExists(long userId) {
        try {
            if (userRepository.getUserById(userId).isEmpty()) {
                throw new EntityNotFoundException("Пользователь не найден");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при проверке пользователя", e);
        }
    }

    private void validateStatusTransition(RequestStatus currentStatus, RequestStatus newStatus) {
        if (newStatus == null) {
            throw new BusinessException("Статус заявки не указан.");
        }
        if (currentStatus == newStatus) {
            return;
        }

        boolean allowed = switch (currentStatus) {
            case CREATED -> newStatus == RequestStatus.UNDER_REVIEW
                    || newStatus == RequestStatus.CANCELLED;
            case UNDER_REVIEW -> newStatus == RequestStatus.APPROVED
                    || newStatus == RequestStatus.REJECTED
                    || newStatus == RequestStatus.CANCELLED;
            case APPROVED -> newStatus == RequestStatus.CANCELLED;
            case REJECTED, CANCELLED -> false;
        };

        if (!allowed) {
            throw new BusinessException(
                    "Нельзя изменить статус с " + currentStatus + " на " + newStatus
            );
        }
    }
}
