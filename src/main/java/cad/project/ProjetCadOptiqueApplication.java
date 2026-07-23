package cad.project;

import cad.project.repositries.Mouvement_StockRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProjetCadOptiqueApplication {

	public static void main (String[] args) {
		SpringApplication.run(ProjetCadOptiqueApplication.class, args);
	}



}
