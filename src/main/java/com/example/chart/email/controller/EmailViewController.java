package com.example.chart.email.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/email")
public class EmailViewController {

    @GetMapping("/test")
    public String emailTestPage() {
        return "redirect:/email/email-test.html";
    }
} 