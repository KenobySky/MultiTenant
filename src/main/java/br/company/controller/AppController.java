package br.company.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @GetMapping(value = {"/", "/index", "/index.html"})
    public String viewAdminHomePage() {
        return "index";
    }

}
