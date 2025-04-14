package proyecto_asistencia.presentation.DTO;

import proyecto_asistencia.presentation.DTO.interfaces.GlobalFactoryDTO;

public class DepFactoryDTO implements GlobalFactoryDTO {
    @Override
    public Object convert(GlobalDTO globalDTO) {
        return new DepDTO(globalDTO);
    }
}
