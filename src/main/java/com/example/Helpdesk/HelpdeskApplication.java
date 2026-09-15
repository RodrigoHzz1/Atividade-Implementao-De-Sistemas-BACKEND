package com.example.Helpdesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe bootstrap da aplicação Spring Boot.
 * É o ponto de entrada que inicia o backend do sistema de helpdesk.
 */
@SpringBootApplication
public class HelpdeskApplication {

    /**
     * Método principal que inicializa o contexto do Spring Boot.
     */
	public static void main(String[] args) {
		SpringApplication.run(HelpdeskApplication.class, args);
	}

}
