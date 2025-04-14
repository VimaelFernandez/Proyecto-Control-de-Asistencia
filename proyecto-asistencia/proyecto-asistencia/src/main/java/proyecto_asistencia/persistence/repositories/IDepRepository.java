package proyecto_asistencia.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyecto_asistencia.persistence.entity.DepEntity;

import java.util.Optional;

@Repository
public interface IDepRepository extends JpaRepository<DepEntity, Long> {
    Optional<DepEntity> findByDepartment(String name);
}
