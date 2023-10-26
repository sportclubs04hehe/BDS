package HTQ.BDS.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/admin")
@RequiredArgsConstructor
public class AdminController {
    @GetMapping
    public ResponseEntity<?> hi(){
        return ResponseEntity.ok("Xin chao Admin");
    }
}
