package ru.fetrias.service;

import ru.fetrias.model.Request;
import ru.fetrias.model.RequestStatus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StatisticsService {
    private final UserService userService;
    private final RequestService requestService;

    public StatisticsService(UserService userService, RequestService requestService) {
        this.userService = userService;
        this.requestService = requestService;
    }

    public Map<String, Long> getStatistics() {
        List<Request> requests = requestService.getAllRequests();
        Map<String, Long> statistics = new LinkedHashMap<>();

        statistics.put("Всего пользователей", (long) userService.getAllUsers().size());
        statistics.put("Всего заявок", (long) requests.size());
        statistics.put("Созданных", countByStatus(requests, RequestStatus.CREATED));
        statistics.put("На рассмотрении", countByStatus(requests, RequestStatus.UNDER_REVIEW));
        statistics.put("Согласованных", countByStatus(requests, RequestStatus.APPROVED));
        statistics.put("Отклоненных", countByStatus(requests, RequestStatus.REJECTED));
        statistics.put("Отмененных", countByStatus(requests, RequestStatus.CANCELLED));

        return statistics;
    }

    private long countByStatus(List<Request> requests, RequestStatus status) {
        return requests.stream()
                .filter(request -> request.getStatus() == status)
                .count();
    }
}
