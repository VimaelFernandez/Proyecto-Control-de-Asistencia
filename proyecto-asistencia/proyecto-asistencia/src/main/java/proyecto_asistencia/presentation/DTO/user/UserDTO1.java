package proyecto_asistencia.presentation.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import proyecto_asistencia.presentation.DTO.GlobalDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO1 {

    private Long id;
    private String name;
    private String lastName;

    public UserDTO1(GlobalDTO globalDTO){
        this.name =(String) globalDTO.getData().get("name");
        this.lastName =(String) globalDTO.getData().get("lastName");
    }
}
