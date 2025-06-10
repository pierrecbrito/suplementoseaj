package br.edu.ufrn.tads.eaj.suplementoseaj.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ufrn.tads.eaj.suplementoseaj.domain.Carrinho;
import br.edu.ufrn.tads.eaj.suplementoseaj.domain.CarrinhoItem;
import br.edu.ufrn.tads.eaj.suplementoseaj.repository.CarrinhoItemRepository;
import br.edu.ufrn.tads.eaj.suplementoseaj.repository.CarrinhoRepository;

@Service
public class CarrinhoService {
    private final CarrinhoRepository carrinhoRepository;
    private final CarrinhoItemRepository carrinhoItemRepository;

    public CarrinhoService(CarrinhoRepository carrinhoRepository, CarrinhoItemRepository carrinhoItemRepository) {
        this.carrinhoRepository = carrinhoRepository;
        this.carrinhoItemRepository = carrinhoItemRepository;
    }

    @Transactional
    public void adicionarItemAoCarrinho(Long usuarioId, Long produtoId) {
        Carrinho carrinho = carrinhoRepository.findByUsuarioIdAndAtivoTrue(usuarioId)
            .orElseGet(() -> {
                Carrinho novoCarrinho = new Carrinho();
                novoCarrinho.setUsuario(usuarioRepository.findById(usuarioId).orElseThrow());
                return carrinhoRepository.save(novoCarrinho);
            });

        CarrinhoItem item = carrinho.getItens().stream()
            .filter(i -> i.getProduto().getId().equals(produtoId))
            .findFirst()
            .orElseGet(() -> {
                CarrinhoItem novoItem = new CarrinhoItem();
                novoItem.setProduto(produtoRepository.findById(produtoId).orElseThrow());
                novoItem.setCarrinho(carrinho);
                return carrinhoItemRepository.save(novoItem);
            });

        item.setQuantidade(item.getQuantidade() + 1);
        carrinhoItemRepository.save(item);
    }

    public Integer contarItensCarrinho(Long usuarioId) {
        return carrinhoRepository.findByUsuarioIdAndAtivoTrue(usuarioId)
            .map(c -> c.getItens().stream()
                .mapToInt(CarrinhoItem::getQuantidade)
                .sum())
            .orElse(0);
    }
}