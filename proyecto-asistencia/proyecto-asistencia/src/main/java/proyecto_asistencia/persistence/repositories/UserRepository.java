package proyecto_asistencia.persistence.repositories;

import org.springframework.data.repository.CrudRepository;
import proyecto_asistencia.persistence.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
}
