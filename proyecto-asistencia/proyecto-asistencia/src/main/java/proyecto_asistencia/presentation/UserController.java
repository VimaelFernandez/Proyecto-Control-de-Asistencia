package proyecto_asistencia.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto_asistencia.presentation.DTO.*;
import proyecto_asistencia.presentation.DTO.interfaces.GlobalFactoryDTO;
import proyecto_asistencia.service.UserService;

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
        return new ResponseEntity<>(userService.saveData(listDTOs), HttpStatus.OK);
    }

    //Find All
    @GetMapping("/findAll")
    ResponseEntity <List<UserDTO>> findByAll(){
        List<UserDTO> depDTOList = userService.findAll();

        if (depDTOList.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        else {
            return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
        }
    }


    //Find User by id
    @GetMapping("/find/{id}")
    ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findByIdUser(id), HttpStatus.OK);
    }

    //Delete User by id
    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> deleteById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.deleteByUser(id), HttpStatus.OK);
    }


}
