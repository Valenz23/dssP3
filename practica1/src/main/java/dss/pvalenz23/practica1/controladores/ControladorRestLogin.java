package dss.pvalenz23.practica1.controladores;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class ControladorRestLogin {

    @PostMapping
    public ResponseEntity<String> login(@RequestParam("user") String user, @RequestParam("pass") String pass) {
                
        if ("admin".equals(user) && "admin".equals(pass)) {
            return ResponseEntity.ok("{\"role\": \"admin\", \"message\": \"Login correcto\"}");
        } else if ("user".equals(user) && "user".equals(pass)) {
            return ResponseEntity.ok("{\"role\": \"user\", \"message\": \"Login correcto\"}");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"role\": null, \"message\": \"Credenciales incorrectas\"}");
        }
    }
}
