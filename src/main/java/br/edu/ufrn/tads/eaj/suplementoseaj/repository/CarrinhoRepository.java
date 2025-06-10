package br.edu.ufrn.tads.eaj.suplementoseaj.repository;

import br.edu.ufrn.tads.eaj.suplementoseaj.domain.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    @Query("SELECT c FROM Carrinho c WHERE c.usuario.id = :usuarioId")
    Carrinho findByUsuarioId(@Param("usuarioId") Long usuarioId);
}