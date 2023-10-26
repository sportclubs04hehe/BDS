package HTQ.BDS.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("${api.prefix}/hi")
@RestController
public class XinChao {
    @GetMapping
    public String hi(){
        return "hi";
    }
}
