package com.demo.gestione_eventi.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class RegistrazioneRequest {
    @NotBlank(message = "Username è obbligatorio")
    @Size(min = 3, max = 20, message = "Username deve essere tra 3 e 15 caratteri")
    private String username;

    @NotBlank(message = "Password è obbligatoria")
    @Size(min = 6, message = "Password deve essere almeno 6 caratteri")
    private String password;

    @NotBlank(message = "Cognome è obbligatorio")
    private String cognome;

    @NotBlank(message = "Email è obbligatoria")
    @Email(message = "Email non valida")
    private String email;

    private boolean isOrganizzatore = false;

    private Set<String> ruoli;

    public Set<String> getRuoli() {
        return ruoli;
    }
}


