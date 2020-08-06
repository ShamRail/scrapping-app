package ru.study.scapping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/app")
public class IndexController {

    @GetMapping
    public String indexPage() {
        return "index";
    }

    @GetMapping("/redirect")
    public String redirect(@RequestParam("url") String url) {
        return "redirect:http://" + url;
    }

}
