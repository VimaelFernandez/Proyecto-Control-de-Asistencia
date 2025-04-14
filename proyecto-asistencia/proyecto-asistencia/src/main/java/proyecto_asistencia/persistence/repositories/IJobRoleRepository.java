package proyecto_asistencia.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyecto_asistencia.persistence.entity.JobRoleEntity;

import java.util.Optional;

@Repository
public interface IJobRoleRepository extends JpaRepository<JobRoleEntity, Long> {
    Optional<JobRoleEntity> findByJobRole(String name);
}

