package com.moisesvn.carteira_vacinacao_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CarteiraVacinacaoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarteiraVacinacaoApiApplication.class, args);
	}

}
