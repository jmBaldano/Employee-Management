package com.capstone2.employee_management_system.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class PageController {

    @GetMapping("/")
    public String home(){
        return "redirect:/home.html";

    }
}
