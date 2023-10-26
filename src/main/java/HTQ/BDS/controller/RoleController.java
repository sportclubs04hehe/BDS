package HTQ.BDS.controller;

import HTQ.BDS.entity.Roles;
import HTQ.BDS.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("${api.prefix}/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService service;

    @GetMapping
    public ResponseEntity<?> getRoles(){
        try{
            List<Roles> roles = service.getAllRoles();
            if(roles.isEmpty()){
                return new ResponseEntity<>(NO_CONTENT);
            }
            return new ResponseEntity<>(roles, OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
