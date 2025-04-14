package proyecto_asistencia.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "job_role")
public class JobRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "job_role")
    private String jobRole;

    @ManyToOne()
    @JoinColumn(name = "id_department", nullable = false)
    private DepEntity depFromJobRole;
}
