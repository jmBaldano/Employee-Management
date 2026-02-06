package com.capstone2.employee_management_system.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PageController {

    @GetMapping("/")
    public String home(){
        return "redirect:/home.html";

    }

}
