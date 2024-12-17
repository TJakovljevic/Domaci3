package com.example.Domaci_3.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "error_message")
public class ErrorMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne()
    @JoinColumn(name="order_id", nullable = false)
    private Order orderEntity;

    @Column(nullable = false)
    private String messageDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;


}
