package com.example.Domaci_3.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SearchOrderDto {

    private List<String> status;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private Long userId;

    @Override
    public String toString() {
        return "SearchOrderDto{" +
                "status=" + status +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", userId=" + userId +
                '}';
    }
}
