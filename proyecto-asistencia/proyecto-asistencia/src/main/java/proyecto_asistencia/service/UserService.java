package proyecto_asistencia.service;

import lombok.*;
import org.apache.catalina.User;
import org.hibernate.sql.exec.ExecutionException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto_asistencia.persistence.entity.UserEntity;
import proyecto_asistencia.persistence.repositories.UserRepository;
import proyecto_asistencia.presentation.DTO.UserDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO saveUser(UserDTO userDTO) {
        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
        userEntity.setId(userRepository.encontrarMaximoId() + 1);
        userRepository.save(userEntity);
        return userDTO;
    }

    public List<UserDTO> findAll(){
        ModelMapper modelMapper = new ModelMapper();
        return userRepository.findAll().
                stream().map( userEntity -> modelMapper.map(userEntity, UserDTO.class)).
                collect(Collectors.toList());
    }

    public UserDTO findByIdUser (Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if (userEntity.isPresent()){
            UserEntity currentUserEntity = userEntity.get();
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(currentUserEntity, UserDTO.class);
        }
        else{
            throw new ExecutionException("No existe en la base de datos");
        }
    }

    public String deleteByUser (Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if (userEntity.isPresent()){
            UserEntity currentUserEntity = userEntity.get();
            userRepository.deleteById(id);
            return "El usuario con id: " + id + " se ha borrado con exito";
        }
        else {
            return "El usuario no existe";
        }
    }

}
