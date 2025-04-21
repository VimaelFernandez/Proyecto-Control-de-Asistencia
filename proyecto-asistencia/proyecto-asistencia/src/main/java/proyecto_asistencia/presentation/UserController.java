package proyecto_asistencia.presentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto_asistencia.presentation.DTO.*;
import proyecto_asistencia.presentation.DTO.factories.DepFactoryDTO;
import proyecto_asistencia.presentation.DTO.factories.JobRoleFactoryDTO;
import proyecto_asistencia.presentation.DTO.factories.UserFactoryDTO;
import proyecto_asistencia.presentation.DTO.user.UserCompleteDTO;
import proyecto_asistencia.presentation.DTO.interfaces.GlobalFactoryDTO;
import proyecto_asistencia.service.UserService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    //Create user
    @PostMapping("/create")
    ResponseEntity <List<Object>> createUser(@RequestBody List<Map<String, Object>> dataUser) {
        Map<String, GlobalFactoryDTO > factories = Map.of(
                "role", new JobRoleFactoryDTO(),
                "department", new DepFactoryDTO(),
                "user", new UserFactoryDTO()
        );
        List <Object> listDTOs = new ArrayList<>();

        for ( Map<String, Object> objectDTO : dataUser){
            GlobalDTO globalDTO = new GlobalDTO();
            globalDTO.setType((String) objectDTO.get("type"));
            globalDTO.setData(objectDTO);

            GlobalFactoryDTO factory = factories.get(globalDTO.getType());
            Object dto = factory.convert(globalDTO);

            listDTOs.add(dto);
        }
        return new ResponseEntity<>(userService.saveUserInfo(listDTOs), HttpStatus.OK);
    }

    //Find a UserComplete List
    @GetMapping("/findAll")
    ResponseEntity <List<UserCompleteDTO>> findByAll(){
        List<UserCompleteDTO> depDTOList = userService.findAllUsersWithCompleteInfo();

        if (depDTOList.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        else {
            return new ResponseEntity<>(userService.findAllUsersWithCompleteInfo(), HttpStatus.OK);
        }
    }

    //Find User by id
    @GetMapping("/find/{id}")
    ResponseEntity<UserCompleteDTO> findById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findByIdUser(id), HttpStatus.OK);
    }

    //Delete User by id
    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> deleteById(@PathVariable Long id) {
        System.out.println("Identificador recibido: "+ id);
        return new ResponseEntity<>(userService.deleteByUser(id), HttpStatus.OK);
    }
}
