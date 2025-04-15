package proyecto_asistencia.presentation.DTO.factories;

import proyecto_asistencia.presentation.DTO.GlobalDTO;
import proyecto_asistencia.presentation.DTO.department.DepDTO;
import proyecto_asistencia.presentation.DTO.interfaces.GlobalFactoryDTO;

public class DepFactoryDTO implements GlobalFactoryDTO {
    @Override
    public Object convert(GlobalDTO globalDTO) {
        return new DepDTO(globalDTO);
    }
}
