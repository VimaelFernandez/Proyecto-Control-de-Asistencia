package proyecto_asistencia.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne()
    @JoinColumn(name = "id_department", nullable = false)
    private DepEntity depFromUser;
}
