package com.demo.gestione_eventi.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private String username;
    private Long id;
    private String email;
    private List<String> ruoli;
    private String token;
    private String type = "Bearer ";

    public JwtResponse(String username, Long id, String email, List<String> ruoli, String token) {
        this.username = username;
        this.id = id;
        this.email = email;
        this.ruoli = ruoli;
        this.token = token;
    }
}


