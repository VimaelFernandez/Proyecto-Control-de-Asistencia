package proyecto_asistencia.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto_asistencia.persistence.entity.AssistanceEntity;

public interface IAssistanceRepository extends JpaRepository <AssistanceEntity, Long>{
}
