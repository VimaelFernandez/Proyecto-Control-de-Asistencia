package proyecto_asistencia.presentation.DTO.factories;

import proyecto_asistencia.presentation.DTO.GlobalDTO;
import proyecto_asistencia.presentation.DTO.user.UserDTO1;
import proyecto_asistencia.presentation.DTO.interfaces.GlobalFactoryDTO;

public class UserFactoryDTO implements GlobalFactoryDTO {
    @Override
    public Object convert(GlobalDTO globalDTO) {
        return new UserDTO1(globalDTO);
    }
}
