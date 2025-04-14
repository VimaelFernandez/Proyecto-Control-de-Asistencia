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
@Table(name = "department")
public class DepEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String department;

    @OneToMany (mappedBy = "depFromUser", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<UserEntity> userEntityList;

    @OneToMany (mappedBy = "depFromJobRole", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<JobRoleEntity> jobRoleEntityList;
}