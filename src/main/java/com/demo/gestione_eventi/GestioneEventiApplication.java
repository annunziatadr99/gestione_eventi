package com.demo.gestione_eventi;

import com.demo.gestione_eventi.enumerated.ERuolo;
import com.demo.gestione_eventi.model.Ruolo;
import com.demo.gestione_eventi.repository.RuoloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GestioneEventiApplication implements CommandLineRunner {

	@Autowired
	RuoloRepository ruoloRepository;

	public static void main(String[] args) {
		SpringApplication.run(GestioneEventiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (!ruoloRepository.findByNome(ERuolo.ROLE_USER).isPresent()) {
			Ruolo ruoloUser = new Ruolo();
			ruoloUser.setNome(ERuolo.ROLE_USER);
			ruoloRepository.save(ruoloUser);
		}

		if (!ruoloRepository.findByNome(ERuolo.ROLE_ORGANIZZATORE).isPresent()) {
			Ruolo ruoloOrganizzatore = new Ruolo();
			ruoloOrganizzatore.setNome(ERuolo.ROLE_ORGANIZZATORE);
			ruoloRepository.save(ruoloOrganizzatore);
		}
	}
}
