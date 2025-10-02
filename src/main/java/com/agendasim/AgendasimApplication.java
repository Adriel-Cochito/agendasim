package com.agendasim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AgendasimApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgendasimApplication.class, args);

        // BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // String senhaCriptografada = encoder.encode("asd123");
        // System.out.println(senhaCriptografada);
	}

}
