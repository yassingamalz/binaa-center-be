package com.novavista.binaa.center.enums;

public enum NotificationType {
    SESSION("جلسة"),
    PAYMENT("دفع"),
    ASSESSMENT("تقييم"),
    CASE("حالة"),
    SYSTEM("النظام");

    private final String label;

    NotificationType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
