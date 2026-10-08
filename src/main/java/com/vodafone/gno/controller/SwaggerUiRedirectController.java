package com.vodafone.gno.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerUiRedirectController {

    @GetMapping("/swagger-ui/technical-apis")
    public String technicalApisSwaggerUi() {
        return "redirect:/swagger-ui/index.html?urls.primaryName=technical-apis";
    }

    @GetMapping("/swagger-ui/demo-apis")
    public String demoApisSwaggerUi() {
        return "redirect:/swagger-ui/index.html?urls.primaryName=demo-apis";
    }
}

