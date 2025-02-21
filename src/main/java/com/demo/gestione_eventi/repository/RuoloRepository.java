package com.demo.gestione_eventi.repository;

import com.demo.gestione_eventi.enumerated.ERuolo;
import com.demo.gestione_eventi.model.Ruolo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RuoloRepository extends JpaRepository<Ruolo, Long> {
    Optional<Ruolo> findByNome(ERuolo nome);
}

