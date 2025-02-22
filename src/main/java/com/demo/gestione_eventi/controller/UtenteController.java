package com.demo.gestione_eventi.controller;

import com.demo.gestione_eventi.exception.EmailDuplicateException;
import com.demo.gestione_eventi.exception.UsernameDuplicateException;
import com.demo.gestione_eventi.payload.request.LoginRequest;
import com.demo.gestione_eventi.payload.request.RegistrazioneRequest;
import com.demo.gestione_eventi.payload.response.JwtResponse;
import com.demo.gestione_eventi.security.jwt.JwtUtils;
import com.demo.gestione_eventi.security.services.UserDetailsImpl;
import com.demo.gestione_eventi.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UtenteController {

    @Autowired
    UtenteService utenteService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/registrazione")
    public ResponseEntity<String> registraUtente(@Validated @RequestBody RegistrazioneRequest registrazioneRequest, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errori = new StringBuilder("Errori di validazione:\n");
            for (ObjectError errore : result.getAllErrors()) {
                errori.append(errore.getDefaultMessage()).append("\n");
            }
            return new ResponseEntity<>(errori.toString(), HttpStatus.BAD_REQUEST);
        }

        try {
            String messaggio = utenteService.inserisciUtente(registrazioneRequest);
            return new ResponseEntity<>(messaggio, HttpStatus.OK);
        } catch (UsernameDuplicateException | EmailDuplicateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest loginRequest, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errori = new StringBuilder("Errori di validazione:\n");
            for (ObjectError errore : result.getAllErrors()) {
                errori.append(errore.getDefaultMessage()).append("\n");
            }
            return new ResponseEntity<>(errori.toString(), HttpStatus.BAD_REQUEST);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.creaJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> ruoli = userDetails.getAuthorities().stream()
                .map(item -> ((GrantedAuthority) item).getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(userDetails.getUsername(), userDetails.getId(),
                userDetails.getEmail(), ruoli, jwt));
    }
}
