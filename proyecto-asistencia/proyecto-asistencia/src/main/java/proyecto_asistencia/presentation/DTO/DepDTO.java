package proyecto_asistencia.presentation.DTO;

import lombok.*;

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
