package com.demo.gestione_eventi.controller;

import com.demo.gestione_eventi.model.Evento;
import com.demo.gestione_eventi.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventi")
public class EventoController {

    @Autowired
    EventoService eventoService;

    @PostMapping("/crea")
    @PreAuthorize("hasRole('ROLE_ORGANIZZATORE')")
    public ResponseEntity<Evento> creaEvento(@Validated @RequestBody Evento evento, @RequestParam Long creatoreId) {
        Evento nuovoEvento = eventoService.creaEvento(evento, creatoreId);
        return new ResponseEntity<>(nuovoEvento, HttpStatus.CREATED);
    }

    @PutMapping("/modifica/{id}")
    @PreAuthorize("hasRole('ROLE_ORGANIZZATORE')")
    public ResponseEntity<Evento> modificaEvento(@PathVariable Long id, @Validated @RequestBody Evento eventoDetails) {
        Evento eventoAggiornato = eventoService.modificaEvento(id, eventoDetails);
        return new ResponseEntity<>(eventoAggiornato, HttpStatus.OK);
    }

    @DeleteMapping("/elimina/{id}")
    @PreAuthorize("hasRole('ROLE_ORGANIZZATORE')")
    public ResponseEntity<Void> eliminaEvento(@PathVariable Long id) {
        eventoService.eliminaEvento(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/tutti")
    public ResponseEntity<List<Evento>> trovaTuttiEventi() {
        List<Evento> eventi = eventoService.trovaTuttiEventi();
        return new ResponseEntity<>(eventi, HttpStatus.OK);
    }

    @GetMapping("/creatore/{creatoreId}")
    public ResponseEntity<List<Evento>> trovaEventiPerCreatore(@PathVariable Long creatoreId) {
        List<Evento> eventi = eventoService.trovaEventiPerCreatore(creatoreId);
        return new ResponseEntity<>(eventi, HttpStatus.OK);
    }

    @PostMapping("/prenota/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> prenotaPosto(@PathVariable Long id, @RequestParam Long utenteId, @RequestParam int postiPrenotati) {
        eventoService.prenotaPosto(id, utenteId, postiPrenotati);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/cancellaPrenotazione/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> cancellaPrenotazione(@PathVariable Long id, @RequestParam Long utenteId) {
        eventoService.cancellaPrenotazione(id, utenteId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
