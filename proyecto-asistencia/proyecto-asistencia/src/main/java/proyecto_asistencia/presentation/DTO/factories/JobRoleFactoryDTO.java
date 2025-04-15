package proyecto_asistencia.presentation.DTO.factories;

import proyecto_asistencia.presentation.DTO.GlobalDTO;
import proyecto_asistencia.presentation.DTO.jobrole.JobRoleDTO;
import proyecto_asistencia.presentation.DTO.interfaces.GlobalFactoryDTO;

public class JobRoleFactoryDTO implements GlobalFactoryDTO {
    @Override
    public Object convert(GlobalDTO globalDTO) {
        return new JobRoleDTO(globalDTO);
    }
}
