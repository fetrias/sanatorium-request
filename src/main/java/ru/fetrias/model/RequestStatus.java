package ru.fetrias.model;

public enum RequestStatus {
    CREATED,
    UNDER_REVIEW,
    APPROVED,
    REJECTED,
    CANCELLED;

    @Override
    public String toString() {
        if (this == CREATED) {
            return "создана";
        }
        if (this == UNDER_REVIEW) {
            return "на рассмотрении";
        }
        if (this == APPROVED) {
            return "согласована";
        }
        if (this == REJECTED) {
            return "отклонена";
        }
        return "отменена";
    }
}
