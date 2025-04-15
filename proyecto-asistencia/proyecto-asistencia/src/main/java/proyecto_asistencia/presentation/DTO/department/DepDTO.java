package proyecto_asistencia.presentation.DTO.department;

import lombok.*;
import proyecto_asistencia.presentation.DTO.GlobalDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepDTO {

    private Long id;
    private String department;

    public DepDTO (GlobalDTO globalDTO){
        this.department = (String) globalDTO.getData().get("department");
    }
}
