package com.demo.gestione_eventi.model;

import com.demo.gestione_eventi.enumerated.ERuolo;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Ruolo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ERuolo nome;
}
