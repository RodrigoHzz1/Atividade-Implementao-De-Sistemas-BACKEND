package com.example.Helpdesk.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Welcome {


    @GetMapping
    public String welcome() {
        return "Aplicação Helpdesk rodando com sucesso!";
    }


    @GetMapping("/devs")
    public String dev() {
        return "Desenvolvedores: Rodrigo Costa, Bruno Machado, Juan França e Kauã Victor";
    }
}