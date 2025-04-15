package proyecto_asistencia.presentation.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCompleteDTO {
    private Long id;
    private String name;
    private String lastName;
    private String department;
    private String jobRole;
//    private String code;
}
