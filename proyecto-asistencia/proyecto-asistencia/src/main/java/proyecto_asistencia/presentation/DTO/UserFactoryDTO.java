package proyecto_asistencia.presentation.DTO;

import proyecto_asistencia.presentation.DTO.interfaces.GlobalFactoryDTO;

public class UserFactoryDTO implements GlobalFactoryDTO {
    @Override
    public Object convert(GlobalDTO globalDTO) {
        return new UserDTO1(globalDTO);
    }
}
