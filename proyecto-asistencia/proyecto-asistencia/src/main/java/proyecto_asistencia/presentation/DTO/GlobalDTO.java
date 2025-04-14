package proyecto_asistencia.presentation.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class GlobalDTO {
    private String type;
    private Map<String, Object> data;
}
