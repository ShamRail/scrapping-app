package ru.study.scapping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping
    public String redirectToApp() {
        return "redirect:/app";
    }

}
