package com.example.Domaci_3.dto;

import com.example.Domaci_3.model.Status;
import com.example.Domaci_3.model.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ScheduleOrderDto {


    private Long id;
    private String name;
    private Status status;
    private boolean active;
    private User createdBy;
    private List<Long> dishes;
    private LocalDateTime scheduledTime;
}
