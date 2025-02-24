package com.demo.gestione_eventi.payload.request;

import lombok.Data;

import java.util.Date;

@Data
public class EventoRequest {
    private String titolo;
    private String descrizione;
    private Date data;
    private String luogo;
    private int postiDisponibili;
}
