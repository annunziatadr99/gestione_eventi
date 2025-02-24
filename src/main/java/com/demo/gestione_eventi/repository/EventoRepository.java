package com.demo.gestione_eventi.repository;

import com.demo.gestione_eventi.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByCreatoreId(Long creatoreId);
}
