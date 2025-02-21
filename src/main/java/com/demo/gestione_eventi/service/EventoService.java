package com.demo.gestione_eventi.service;

import com.demo.gestione_eventi.model.Evento;
import com.demo.gestione_eventi.model.Prenotazione;
import com.demo.gestione_eventi.model.Utente;
import com.demo.gestione_eventi.repository.EventoRepository;
import com.demo.gestione_eventi.repository.PrenotazioneRepository;
import com.demo.gestione_eventi.repository.UtenteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventoService {

    @Autowired
    EventoRepository eventoRepository;

    @Autowired
    PrenotazioneRepository prenotazioneRepository;

    @Autowired
    UtenteRepository utenteRepository;

    public Evento creaEvento(Evento evento, Long creatoreId) {
        Optional<Utente> creatore = utenteRepository.findById(creatoreId);
        if (creatore.isPresent()) {
            evento.setCreatore(creatore.get());
            return eventoRepository.save(evento);
        } else {
            throw new RuntimeException("Creatore non trovato");
        }
    }

    public Evento modificaEvento(Long eventoId, Evento eventoDetails) {
        Evento evento = eventoRepository.findById(eventoId).orElseThrow(() -> new RuntimeException("Evento non trovato"));
        evento.setTitolo(eventoDetails.getTitolo());
        evento.setDescrizione(eventoDetails.getDescrizione());
        evento.setData(eventoDetails.getData());
        evento.setLuogo(eventoDetails.getLuogo());
        evento.setPostiDisponibili(eventoDetails.getPostiDisponibili());
        return eventoRepository.save(evento);
    }

    public void eliminaEvento(Long eventoId) {
        Evento evento = eventoRepository.findById(eventoId).orElseThrow(() -> new RuntimeException("Evento non trovato"));
        eventoRepository.delete(evento);
    }

    public List<Evento> trovaEventiPerCreatore(Long creatoreId) {
        return eventoRepository.findByCreatoreId(creatoreId);
    }

    public List<Evento> trovaTuttiEventi() {
        return eventoRepository.findAll();
    }

    public Optional<Evento> trovaEventoPerId(Long eventoId) {
        return eventoRepository.findById(eventoId);
    }

    public void prenotaPosto(Long eventoId, Long utenteId, int postiPrenotati) {
        Evento evento = eventoRepository.findById(eventoId).orElseThrow(() -> new RuntimeException("Evento non trovato"));
        Utente utente = utenteRepository.findById(utenteId).orElseThrow(() -> new RuntimeException("Utente non trovato"));

        if (evento.getPostiDisponibili() >= postiPrenotati) {
            evento.setPostiDisponibili(evento.getPostiDisponibili() - postiPrenotati);
            eventoRepository.save(evento);

            Prenotazione prenotazione = new Prenotazione();
            prenotazione.setUtente(utente);
            prenotazione.setEvento(evento);
            prenotazione.setPostiPrenotati(postiPrenotati);

            prenotazioneRepository.save(prenotazione);
        } else {
            throw new RuntimeException("Posti esauriti");
        }
    }

    public void cancellaPrenotazione(Long eventoId, Long utenteId) {
        Evento evento = eventoRepository.findById(eventoId).orElseThrow(() -> new RuntimeException("Evento non trovato"));
        Prenotazione prenotazione = prenotazioneRepository.findByEventoIdAndUtenteId(eventoId, utenteId)
                .orElseThrow(() -> new RuntimeException("Prenotazione non trovata"));

        evento.setPostiDisponibili(evento.getPostiDisponibili() + prenotazione.getPostiPrenotati());
        eventoRepository.save(evento);

        prenotazioneRepository.delete(prenotazione);
    }
}
