package br.edu.ufrn.tads.eaj.suplementoseaj.repository;

import br.edu.ufrn.tads.eaj.suplementoseaj.domain.CarrinhoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrinhoItemRepository extends JpaRepository<CarrinhoItem, Long> {
    // Métodos customizados podem ser adicionados aqui
}