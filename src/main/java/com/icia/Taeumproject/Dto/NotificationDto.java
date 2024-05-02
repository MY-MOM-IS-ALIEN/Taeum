package com.icia.Taeumproject.Dto;

import java.sql.Timestamp; // 올바른 import 문 사용

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class NotificationDto {
    private int notificationId;
    private String role;
    private String message;
    private Timestamp CREATED_AT; // 올바른 Timestamp 사용
}
