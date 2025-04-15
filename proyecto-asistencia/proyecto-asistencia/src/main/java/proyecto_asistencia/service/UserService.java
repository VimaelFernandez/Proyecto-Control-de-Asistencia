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
import proyecto_asistencia.presentation.DTO.department.DepDTO;
import proyecto_asistencia.presentation.DTO.jobrole.JobRoleDTO;
import proyecto_asistencia.presentation.DTO.user.UserCompleteDTO;
import proyecto_asistencia.presentation.DTO.user.UserDTO1;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<Object> saveUserInfo(List<Object> listDTO) {
        DepDTO department = new DepDTO();
        UserDTO1 userDTO = new UserDTO1();
        JobRoleDTO jobRoleDTO = new JobRoleDTO();
        for (Object objectDto : listDTO) {
            if (objectDto instanceof UserDTO1) {
                userDTO = modelMapper.map(objectDto, UserDTO1.class);
            } else if (objectDto instanceof DepDTO) {
                department = (DepDTO) objectDto;
            } else if (objectDto instanceof JobRoleDTO) {
                jobRoleDTO = ((JobRoleDTO) objectDto);
            } else {
                throw new IllegalArgumentException("Object is not existent");
            }
        }

        Optional<DepEntity> depEntityOptional = depRepository.findByDepartment(department.getDepartment());
        if (department.getDepartment() == null || department.getDepartment().isEmpty()) {
            throw new IllegalArgumentException("El departamento está vacío");
        } else {
            if (depEntityOptional.isPresent()) {
                System.out.println("El departamento ya existe.");
            } else {
                DepEntity depEntity = modelMapper.map(department, DepEntity.class);
                depRepository.save(depEntity);
                depEntityOptional = depRepository.findByDepartment(department.getDepartment());
            }
        }

        if (jobRoleDTO.getJobRole() == null || jobRoleDTO.getJobRole().isEmpty()) {
            throw new IllegalArgumentException("El cargo está vacío");
        } else {
            Optional<JobRoleEntity> jobRoleEntityOptional = jobRoleRepository.findByJobRole(jobRoleDTO.getJobRole());
            if (jobRoleEntityOptional.isPresent()) {
                System.out.println("El cargo ya existe");
            } else {
                JobRoleEntity jobRoleEntity = modelMapper.map(jobRoleDTO, JobRoleEntity.class);
                if (depEntityOptional.isPresent()) {
                    jobRoleEntity.setJobRoleDepartment(depEntityOptional.get());
                } else {
                    throw new IllegalArgumentException("Department is not found");
                }
                jobRoleRepository.save(jobRoleEntity);
            }
        }

        if (userDTO.getName() == null || userDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("El usuario está vacío");
        } else {
            UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
            userEntity.setId(userRepository.encontrarMaximoId() + 1);
            if (depEntityOptional.isPresent()) {
                userEntity.setUserDepartment(depEntityOptional.get());
            } else {
                throw new IllegalArgumentException("Department not found");
            }
            userRepository.save(userEntity);
        }
        return listDTO;
    }

    @Transactional(readOnly = true)
    public List<UserCompleteDTO> findAllUsersWithCompleteInfo() {
        return userRepository.findAllUsersWithDepartmentAndJobRole().stream()
                .map(this::mapToCompleteDTO)
                .collect(Collectors.toList());
    }

    private UserCompleteDTO mapToCompleteDTO(UserEntity user) {
        UserCompleteDTO dto = modelMapper.map(user, UserCompleteDTO.class);
        if (user.getUserDepartment() != null) {
            dto.setDepartment(user.getUserDepartment().getDepartment());

            // Tomamos el primer job role (asumiendo 1:1)
            if (!user.getUserDepartment().getJobRoleList().isEmpty()) {
                dto.setJobRole(user.getUserDepartment().getJobRoleList().get(0).getJobRole());
            }
        }
        return dto;
    }

    public UserCompleteDTO findByIdUser(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if (userEntity.isPresent()) {
            UserEntity currentUserEntity = userEntity.get();
            return modelMapper.map(currentUserEntity, UserCompleteDTO.class);
        } else {
            throw new ExecutionException("No existe en la base de datos");
        }
    }

    @Transactional
    public String deleteByUser(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if (userEntity.isPresent()) {
            DepEntity department = userEntity.get().getUserDepartment();
            if (department != null) {
                department.getUserList().remove(userRepository.findById(id).get());
            }
            userRepository.deleteById(id);
            return "El usuario con id: " + id + " se ha borrado con exito";
        } else {
            return "El usuario no existe";
        }
    }

}
