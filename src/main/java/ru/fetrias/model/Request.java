package ru.fetrias.model;

import java.time.LocalDate;

public class Request {
    private long id;
    private long userId;
    private String sanatoriumName;
    private LocalDate checkInDate;
    private int durationDays;
    private LocalDate createDate;
    private RequestStatus status;

    public Request(long id, long userId, String sanatoriumName, LocalDate checkInDate,
                   int durationDays, LocalDate createDate, RequestStatus status) {
        this.id = id;
        this.userId = userId;
        this.sanatoriumName = sanatoriumName;
        this.checkInDate = checkInDate;
        this.durationDays = durationDays;
        this.createDate = createDate;
        this.status = status;
    }

    public Request(long userId, String sanatoriumName, LocalDate checkInDate,
                   int durationDays, LocalDate createDate, RequestStatus status) {
        this.userId = userId;
        this.sanatoriumName = sanatoriumName;
        this.checkInDate = checkInDate;
        this.durationDays = durationDays;
        this.createDate = createDate;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getSanatoriumName() {
        return sanatoriumName;
    }

    public void setSanatoriumName(String sanatoriumName) {
        this.sanatoriumName = sanatoriumName;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", userId=" + userId +
                ", sanatoriumName='" + sanatoriumName + '\'' +
                ", checkInDate=" + checkInDate +
                ", durationDays=" + durationDays +
                ", createDate=" + createDate +
                ", status=" + status +
                '}';
    }
}
