package proyecto_asistencia.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "assistance")
public class AssistanceEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;

    @Column(name="departure_time")
    private LocalDateTime departureTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "user_id", nullable = false)
    private UserEntity user;
}