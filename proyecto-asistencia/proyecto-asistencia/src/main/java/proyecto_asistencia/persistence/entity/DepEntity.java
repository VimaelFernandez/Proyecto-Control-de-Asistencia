package proyecto_asistencia.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany (mappedBy = "userDepartment", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    List<UserEntity> userList;

    @OneToMany (mappedBy = "jobRoleDepartment", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    List<JobRoleEntity> jobRoleList;
}