package com.demo.gestione_eventi.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FiltroAuthToken extends OncePerRequestFilter {

    @Autowired
    private JwtUtils utils;

    @Autowired
    private UserDetailsService userDetailsService;

    private String analizzaJwt(HttpServletRequest request) {
        String headAutenticazione = request.getHeader("Authorization");

        if (StringUtils.hasText(headAutenticazione) && headAutenticazione.startsWith("Bearer ")) {
            return headAutenticazione.substring(7);
        }

        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = analizzaJwt(request);

        if (jwt != null && utils.validazioneJwtToken(jwt)) {
            String username = utils.recuperoUsernameDaToken(jwt);
            UserDetails dettagliUtente = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken autenticazione =
                    new UsernamePasswordAuthenticationToken(
                            dettagliUtente, null, dettagliUtente.getAuthorities());
            autenticazione.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(autenticazione);
        }

        filterChain.doFilter(request, response);
    }
}

