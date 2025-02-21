package com.demo.gestione_eventi.repository;

import com.demo.gestione_eventi.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Long> {

    Optional<Utente> findByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

