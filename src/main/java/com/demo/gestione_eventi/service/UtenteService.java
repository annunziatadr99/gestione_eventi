package com.demo.gestione_eventi.service;

import com.demo.gestione_eventi.enumerated.ERuolo;
import com.demo.gestione_eventi.exception.EmailDuplicateException;
import com.demo.gestione_eventi.exception.UsernameDuplicateException;

import com.demo.gestione_eventi.model.Ruolo;
import com.demo.gestione_eventi.model.Utente;
import com.demo.gestione_eventi.payload.request.RegistrazioneRequest;
import com.demo.gestione_eventi.repository.RuoloRepository;
import com.demo.gestione_eventi.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UtenteService {

    @Autowired
    UtenteRepository repoUtente;

    @Autowired
    RuoloRepository ruoloRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public String inserisciUtente(RegistrazioneRequest dto) throws UsernameDuplicateException, EmailDuplicateException {
        if (repoUtente.existsByUsername(dto.getUsername())) {
            throw new UsernameDuplicateException("Username già utilizzato");
        }
        if (repoUtente.existsByEmail(dto.getEmail())) {
            throw new EmailDuplicateException("Email già utilizzata");
        }

        Utente user = new Utente();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setCognome(dto.getCognome());
        user.setEmail(dto.getEmail());

        Set<String> strRoles = dto.getRuoli();
        Set<Ruolo> roles = new HashSet<>();

        if (strRoles == null) {
            Ruolo userRole = ruoloRepository.findByNome(ERuolo.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Errore: Ruolo non trovato."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if (role.equals("ROLE_ORGANIZZATORE")) {
                    Ruolo organizzatoreRole = ruoloRepository.findByNome(ERuolo.ROLE_ORGANIZZATORE)
                            .orElseThrow(() -> new RuntimeException("Errore: Ruolo non trovato."));
                    roles.add(organizzatoreRole);
                } else {
                    Ruolo userRole = ruoloRepository.findByNome(ERuolo.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Errore: Ruolo non trovato."));
                    roles.add(userRole);
                }
            });
        }

        user.setRuoli(roles);
        repoUtente.save(user);

        return "L'utente " + user.getUsername() + " è stato registrato con successo.";
    }
}

