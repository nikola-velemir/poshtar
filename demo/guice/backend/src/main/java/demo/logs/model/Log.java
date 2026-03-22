package demo.logs.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
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
