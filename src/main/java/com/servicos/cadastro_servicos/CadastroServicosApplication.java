package com.servicos.cadastro_servicos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.servicos.cadastro_servicos.model")
public class CadastroServicosApplication {

	public static void main(String[] args) {
		SpringApplication.run(CadastroServicosApplication.class, args);
	}

}
