package HTQ.BDS.controller;

import HTQ.BDS.dtos.UserDTO;
import HTQ.BDS.dtos.UserLoginDTO;
import HTQ.BDS.entity.User;
import HTQ.BDS.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("${api.prefix}/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result
    ){
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream().map(
                                FieldError::getDefaultMessage
                        ).toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }

            User user = service.createUser(userDTO);
            return new ResponseEntity<>(user,CREATED);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody UserLoginDTO loginDTO,
            BindingResult result
    ){
        // kiểm tra thông tin đăng nhập và sinh token
        // Trả về token trong response
        try{
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream().map(
                                FieldError::getDefaultMessage
                        ).toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }

            String token = service.login(loginDTO.getUsernames(), loginDTO.getPassword());

            return new ResponseEntity<>(token,OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
