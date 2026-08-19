package com.luis.documentrag;

import org.springframework.boot.SpringApplication;

public class TestDocumentRagApplication {

	public static void main(String[] args) {
		SpringApplication.from(DocumentRagApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
