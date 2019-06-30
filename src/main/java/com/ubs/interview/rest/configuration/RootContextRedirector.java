package com.ubs.interview.rest.configuration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RootContextRedirector {

    @RequestMapping("/")
    public String handleRootPage() {
        return "redirect:swagger-ui.html";
    }
}
