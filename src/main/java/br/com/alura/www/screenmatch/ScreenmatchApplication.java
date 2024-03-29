package br.com.alura.www.screenmatch;

import br.com.alura.www.screenmatch.principal.Principal;
import br.com.alura.www.screenmatch.service.MyProxySelector;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
		MyProxySelector.register("xx.xx.xx.xx", xxxx);
	}

	@Override
	public void run(String... args) {
		Principal principal = new Principal();
		principal.exibeMenu();
	}
}

