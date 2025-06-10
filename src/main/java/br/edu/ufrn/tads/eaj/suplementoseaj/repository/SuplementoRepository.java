package br.edu.ufrn.tads.eaj.suplementoseaj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ufrn.tads.eaj.suplementoseaj.domain.Suplemento;

public interface SuplementoRepository extends JpaRepository<Suplemento, Long> {
    List<Suplemento> findByIsDeletedIsNull();
}
