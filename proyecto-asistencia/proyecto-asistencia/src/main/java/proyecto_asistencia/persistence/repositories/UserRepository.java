package proyecto_asistencia.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import proyecto_asistencia.persistence.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT COALESCE(MAX(e.id), 0) FROM UserEntity e")
    Long encontrarMaximoId();
}
