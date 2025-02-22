package com.demo.gestione_eventi.security.services;

import com.demo.gestione_eventi.model.Utente;
import com.demo.gestione_eventi.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UtenteRepository repoUser;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Utente> utente = repoUser.findByUsername(username);
        Utente user = utente.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));


        return UserDetailsImpl.costruisciDettagli(user);
    }
}
