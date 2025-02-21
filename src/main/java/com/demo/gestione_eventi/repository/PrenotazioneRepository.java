package com.demo.gestione_eventi.repository;

import com.demo.gestione_eventi.model.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {

    Optional<Prenotazione> findByEventoIdAndUtenteId(Long eventoId, Long utenteId);
}
