package proyecto_asistencia.service;

import lombok.*;
import org.hibernate.sql.exec.ExecutionException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proyecto_asistencia.persistence.entity.DepEntity;
import proyecto_asistencia.persistence.entity.JobRoleEntity;
import proyecto_asistencia.persistence.entity.UserEntity;
import proyecto_asistencia.persistence.repositories.IDepRepository;
import proyecto_asistencia.persistence.repositories.IJobRoleRepository;
import proyecto_asistencia.persistence.repositories.UserRepository;
import proyecto_asistencia.presentation.DTO.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IJobRoleRepository jobRoleRepository;

    @Autowired
    private IDepRepository depRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public List<Object> saveData(List<Object> listDTO) {
        DepDTO department = new DepDTO();
        JobRoleEntity jobRoleEntity = new JobRoleEntity();
        UserEntity userEntity = new UserEntity();
        for (Object objectDto : listDTO) {
            if (objectDto instanceof UserDTO1) {
                userEntity = modelMapper.map(objectDto, UserEntity.class);
            } else if (objectDto instanceof DepDTO) {
                department =(DepDTO) objectDto;
            } else if (objectDto instanceof JobRoleDTO) {
                jobRoleEntity.setJobRole(((JobRoleDTO) objectDto).getJobRole());
            } else {
                throw new IllegalArgumentException("Object is not existent");
            }
        }
        Optional <DepEntity> depEntityOptional = depRepository.findByDepartment(department.getDepartment());

        if (department.getDepartment() == null || department.getDepartment().isEmpty()) {
            throw new IllegalArgumentException("El departamento está vacío");
        } else {
            if (depEntityOptional.isPresent()){
                System.out.println("El departamento ya existe.");
            }
            else{
                DepEntity depEntity = new DepEntity();
                depEntity.setDepartment(department.getDepartment());
                depRepository.save(depEntity);
                depEntityOptional = depRepository.findByDepartment(department.getDepartment());
            }
        }

        if (jobRoleEntity.getJobRole() == null || jobRoleEntity.getJobRole().isEmpty()) {
            throw new IllegalArgumentException("El cargo está vacío");
        } else {
            Optional<JobRoleEntity> jobRoleEntityOptional = jobRoleRepository.findByJobRole(jobRoleEntity.getJobRole());
            if (jobRoleEntityOptional.isPresent()) {
                System.out.println("El cargo ya existe");
            } else {
                jobRoleEntity.setDepFromJobRole(depEntityOptional.get());
                jobRoleRepository.save(jobRoleEntity);
            }
        }

        if (userEntity.getName() == null || userEntity.getName().isEmpty()) {
            throw new IllegalArgumentException("El usuario está vacío");
        } else {
            Optional<UserEntity> userOptional = userRepository.findByName(userEntity.getName());
            if (userOptional.isPresent()) {
                throw new IllegalArgumentException("El usuario ya existe");
            } else {
                userEntity.setId(userRepository.encontrarMaximoId() + 1);
                userEntity.setDepFromUser(depEntityOptional.get());
                userRepository.save(userEntity);
            }
        }
        return listDTO;
    }

    @Transactional
    public List<UserDTO> findAll() {
        List<UserEntity> userEntities = userRepository.findAll();

        return IntStream.range(0, userEntities.size())
                .mapToObj(index -> {
                    UserEntity userEntity = userEntities.get(index);
                    UserDTO userDTO = modelMapper.map(userEntity, UserDTO.class);

                    if (userEntity.getDepFromUser() != null) {
                        userDTO.setDepartment(userEntity.getDepFromUser().getDepartment());

                        // Manejo seguro del rol por índice
                        List<JobRoleEntity> roles = userEntity.getDepFromUser().getJobRoleEntityList();
                        String jobRole = (roles != null && index < roles.size())
                                ? roles.get(index).getJobRole()
                                : "Rol no definido"; // Valor por defecto
                        userDTO.setJobRole(jobRole);
                    }

                    return userDTO;
                })
                .collect(Collectors.toList());
    }

    public UserDTO findByIdUser(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if (userEntity.isPresent()) {
            UserEntity currentUserEntity = userEntity.get();
            return modelMapper.map(currentUserEntity, UserDTO.class);
        } else {
            throw new ExecutionException("No existe en la base de datos");
        }
    }

    public String deleteByUser(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if (userEntity.isPresent()) {
            UserEntity currentUserEntity = userEntity.get();
            userRepository.deleteById(id);
            return "El usuario con id: " + id + " se ha borrado con exito";
        } else {
            return "El usuario no existe";
        }
    }

}
