package com.demo.gestione_eventi.security.jwt;

import com.demo.gestione_eventi.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpiration;

    public String creaJwtToken(Authentication autenticazione) {
        UserDetailsImpl utentePrincipal = (UserDetailsImpl) autenticazione.getPrincipal();
        return Jwts.builder()
                .setSubject(utentePrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration))
                .signWith(recuperoChiave(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String recuperoUsernameDaToken(String token) {
        return Jwts.parserBuilder().setSigningKey(recuperoChiave()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validazioneJwtToken(String token) {
        Jwts.parserBuilder().setSigningKey(recuperoChiave()).build().parseClaimsJws(token);
        return true;
    }

    private Key recuperoChiave() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
