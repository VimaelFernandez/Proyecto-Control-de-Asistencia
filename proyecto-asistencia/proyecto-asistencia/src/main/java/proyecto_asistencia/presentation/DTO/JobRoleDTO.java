package proyecto_asistencia.presentation.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobRoleDTO{

    private Long id;
    private String jobRole;

    public JobRoleDTO(GlobalDTO globalDTO){
     this.jobRole = (String) globalDTO.getData().get("role");
    }
}
