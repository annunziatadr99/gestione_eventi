package com.demo.gestione_eventi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titolo;

    private String descrizione;

    private LocalDate data;

    private String luogo;

    private int postiDisponibili;

    @ManyToOne
    @JoinColumn(name = "creatore_id", nullable = false)
    private Utente creatore;
}

