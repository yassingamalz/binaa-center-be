package com.novavista.binaa.center.enums;

import lombok.Getter;

@Getter
public enum NotificationStatus {
    UNREAD("غير مقروء"),
    READ("مقروء");

    private final String label;

    NotificationStatus(String label) {
        this.label = label;
    }

}
