package com.demo.gestione_eventi.repository;

import com.demo.gestione_eventi.model.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    List<Prenotazione> findByUtenteId(Long utenteId);
    List<Prenotazione> findByEventoId(Long eventoId);
    Optional<Prenotazione> findByEventoIdAndUtenteId(Long eventoId, Long utenteId);
}
