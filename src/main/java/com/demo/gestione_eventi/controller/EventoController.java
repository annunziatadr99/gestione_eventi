package com.demo.gestione_eventi.controller;

import com.demo.gestione_eventi.model.Evento;
import com.demo.gestione_eventi.payload.request.EventoRequest;
import com.demo.gestione_eventi.security.services.UserDetailsImpl;
import com.demo.gestione_eventi.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventi")
public class EventoController {

    @Autowired
    EventoService eventoService;

    @PostMapping("/crea")
    public ResponseEntity<Evento> creaEvento(@RequestBody EventoRequest eventoRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long creatoreId = userDetails.getId();

        Evento nuovoEvento = eventoService.creaEvento(eventoRequest, creatoreId);
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

    @PostMapping("/{eventoId}/prenota")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Evento> prenotaPosto(@PathVariable Long eventoId, @RequestParam int postiPrenotati) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long utenteId = userDetails.getId();

        Evento evento = eventoService.prenotaPosto(eventoId, utenteId, postiPrenotati);
        return new ResponseEntity<>(evento, HttpStatus.OK);
    }

    @DeleteMapping("/{eventoId}/cancellaPrenotazione")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Evento> cancellaPrenotazione(@PathVariable Long eventoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long utenteId = userDetails.getId();

        Evento evento = eventoService.cancellaPrenotazione(eventoId, utenteId);
        return new ResponseEntity<>(evento, HttpStatus.OK);
    }
}
