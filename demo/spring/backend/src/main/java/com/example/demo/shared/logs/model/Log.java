package com.example.demo.shared.logs.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@NoArgsConstructor
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column(name = "time", nullable = false)
    Instant time;
    @Column(name = "type", nullable = false)
    String type;

    public Log(Instant time, String type) {
        this.time = time;
        this.type = type;
    }
}
