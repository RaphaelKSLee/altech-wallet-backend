package dev.raphaellee.altechwalletbackend;

import org.springframework.boot.SpringApplication;

public class TestAltechWalletBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(AltechWalletBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
