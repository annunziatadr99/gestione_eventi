package com.demo.gestione_eventi.service;

import com.demo.gestione_eventi.model.Evento;
import com.demo.gestione_eventi.model.Prenotazione;
import com.demo.gestione_eventi.model.Utente;
import com.demo.gestione_eventi.repository.EventoRepository;
import com.demo.gestione_eventi.repository.PrenotazioneRepository;
import com.demo.gestione_eventi.repository.UtenteRepository;
import com.demo.gestione_eventi.payload.request.EventoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    @Autowired
    EventoRepository eventoRepository;

    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    PrenotazioneRepository prenotazioneRepository;

    public Evento creaEvento(EventoRequest eventoRequest, Long creatoreId) {
        Optional<Utente> creatore = utenteRepository.findById(creatoreId);
        if (creatore.isPresent()) {
            Evento evento = new Evento();
            evento.setTitolo(eventoRequest.getTitolo());
            evento.setDescrizione(eventoRequest.getDescrizione());
            evento.setData(eventoRequest.getData());
            evento.setLuogo(eventoRequest.getLuogo());
            evento.setPostiDisponibili(eventoRequest.getPostiDisponibili());
            evento.setCreatore(creatore.get());
            return eventoRepository.save(evento);
        } else {
            throw new RuntimeException("Creatore non trovato");
        }
    }

    public Evento modificaEvento(Long id, Evento eventoDetails) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));

        evento.setTitolo(eventoDetails.getTitolo());
        evento.setDescrizione(eventoDetails.getDescrizione());
        evento.setData(eventoDetails.getData());
        evento.setLuogo(eventoDetails.getLuogo());
        evento.setPostiDisponibili(eventoDetails.getPostiDisponibili());

        return eventoRepository.save(evento);
    }

    public void eliminaEvento(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));

        eventoRepository.delete(evento);
    }

    public List<Evento> trovaTuttiEventi() {
        return eventoRepository.findAll();
    }

    public List<Evento> trovaEventiPerCreatore(Long creatoreId) {
        return eventoRepository.findByCreatoreId(creatoreId);
    }

    public Evento prenotaPosto(Long eventoId, Long utenteId, int postiPrenotati) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));

        if (evento.getPostiDisponibili() < postiPrenotati) {
            throw new RuntimeException("Posti insufficienti");
        }

        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setEvento(evento);
        prenotazione.setUtente(utente);
        prenotazione.setPostiPrenotati(postiPrenotati);

        prenotazioneRepository.save(prenotazione);

        evento.setPostiDisponibili(evento.getPostiDisponibili() - postiPrenotati);
        return eventoRepository.save(evento);
    }

    public Evento cancellaPrenotazione(Long eventoId, Long utenteId) {
        Prenotazione prenotazione = prenotazioneRepository.findByEventoIdAndUtenteId(eventoId, utenteId)
                .orElseThrow(() -> new RuntimeException("Prenotazione non trovata"));

        Evento evento = prenotazione.getEvento();
        evento.setPostiDisponibili(evento.getPostiDisponibili() + prenotazione.getPostiPrenotati());

        prenotazioneRepository.delete(prenotazione);
        return eventoRepository.save(evento);
    }
}
