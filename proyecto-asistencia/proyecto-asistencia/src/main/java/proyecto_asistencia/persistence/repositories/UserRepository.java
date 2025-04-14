package proyecto_asistencia.persistence.repositories;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import proyecto_asistencia.persistence.entity.UserEntity;

import java.util.Optional;
import java.util.OptionalInt;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT COALESCE(MAX(e.id), 0) FROM UserEntity e")
    Long encontrarMaximoId();

    Optional<UserEntity> findByName (String name);
}
