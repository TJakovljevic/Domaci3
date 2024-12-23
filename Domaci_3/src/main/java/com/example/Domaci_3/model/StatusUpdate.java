package com.example.Domaci_3.model;

import lombok.Data;

@Data
public class StatusUpdate {

   private Long orderId;
   private String status;


    public StatusUpdate(Long orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }
}
