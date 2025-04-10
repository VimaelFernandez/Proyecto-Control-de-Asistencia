package proyecto_asistencia.presentation;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto_asistencia.presentation.DTO.UserDTO;
import proyecto_asistencia.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    //Create user
    @PostMapping("/create")
    ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.saveUser(userDTO), HttpStatus.OK);
    }

    //Find All
    @GetMapping("/findAll")
    ResponseEntity <List<UserDTO>> findByAll(){
        List<UserDTO> userDTOList = userService.findAll();
        if (userDTOList.isEmpty()){
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
