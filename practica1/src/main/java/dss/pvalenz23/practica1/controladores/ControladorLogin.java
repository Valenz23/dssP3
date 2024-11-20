package dss.pvalenz23.practica1.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("login")
public class ControladorLogin {

    @GetMapping
    public String login() {
        return "login";
    }

}
