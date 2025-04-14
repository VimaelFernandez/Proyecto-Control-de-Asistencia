package proyecto_asistencia.presentation.DTO;

import proyecto_asistencia.presentation.DTO.interfaces.GlobalFactoryDTO;

public class JobRoleFactoryDTO implements GlobalFactoryDTO {
    @Override
    public Object convert(GlobalDTO globalDTO) {
        return new JobRoleDTO(globalDTO);
    }
}
