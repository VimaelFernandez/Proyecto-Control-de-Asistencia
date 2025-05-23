package proyecto_asistencia.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    private Long id;
    private String name;

    @Column(name = "last_name")
    private String lastName;
//    private String code;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private DepEntity userDepartment;

    @OneToMany (fetch = FetchType.LAZY, mappedBy = "user")
    private List<AssistanceEntity> assistance;
}
