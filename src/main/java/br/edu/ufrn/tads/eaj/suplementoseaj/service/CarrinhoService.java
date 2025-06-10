package br.edu.ufrn.tads.eaj.suplementoseaj.service;

import br.edu.ufrn.tads.eaj.suplementoseaj.domain.Carrinho;
import br.edu.ufrn.tads.eaj.suplementoseaj.domain.CarrinhoItem;
import br.edu.ufrn.tads.eaj.suplementoseaj.domain.Suplemento;
import br.edu.ufrn.tads.eaj.suplementoseaj.domain.Usuario;
import br.edu.ufrn.tads.eaj.suplementoseaj.repository.CarrinhoItemRepository;
import br.edu.ufrn.tads.eaj.suplementoseaj.repository.CarrinhoRepository;
import br.edu.ufrn.tads.eaj.suplementoseaj.repository.SuplementoRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarrinhoService {

    @Autowired
    private CarrinhoRepository carrinhoRepository;
    
    @Autowired
    private SuplementoRepository suplementoRepository;
    
    @Autowired
    private CarrinhoItemRepository carrinhoItemRepository;


    @Transactional
    public Carrinho adicionarCarrinho(Long usuarioId, Long suplementoId, Integer quantidade) {
        Carrinho carrinho = carrinhoRepository.findByUsuarioId(1L);
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        
        if (carrinho == null) {
                
            carrinho = new Carrinho();
            carrinho.setUsuario(usuario);
            carrinho.setTotal(0.0);
            carrinho = carrinhoRepository.save(carrinho);
        }

        // Buscar o suplemento pelo ID
        Suplemento suplemento = suplementoRepository.findById(suplementoId)
                .orElseThrow(() -> new RuntimeException("Suplemento não encontrado"));

        // Verificar se o item já existe no carrinho
        CarrinhoItem itemExistente = null;
        for (CarrinhoItem item : carrinho.getItens()) {
            if (item.getSuplemento().getId().equals(suplementoId)) {
                itemExistente = item;
                break;
            }
        }

        if (itemExistente != null) {
            // Se o item já existe, aumenta a quantidade
            itemExistente.setQuantidade(itemExistente.getQuantidade() + quantidade);
        } else {
            // Se o item não existe, cria um novo
            CarrinhoItem novoItem = new CarrinhoItem();
            novoItem.setCarrinho(carrinho);
            novoItem.setSuplemento(suplemento);
            novoItem.setQuantidade(quantidade);
            carrinho.getItens().add(novoItem);
        }
        recalcularTotal(carrinho);
        
        return carrinhoRepository.save(carrinho);
    }


    private void recalcularTotal(Carrinho carrinho) {
        double total = carrinho.getItens().stream()
                .mapToDouble(item -> item.getSuplemento().getPreco() * item.getQuantidade())
                .sum();
        carrinho.setTotal(total);
    }

    @Transactional
    public Integer contarItensCarrinho(Long usuarioId) {
        Carrinho carrinho = carrinhoRepository.findByUsuarioId(usuarioId);
        if (carrinho == null) {
            return 0; // Se não houver carrinho, retorna 0
        }
        return carrinho.getItens().stream()
                .mapToInt(CarrinhoItem::getQuantidade)
                .sum();
    }


    public Carrinho getCarrinhoByUsuario(Long usuarioId) {
        Carrinho carrinho = carrinhoRepository.findByUsuarioId(usuarioId);
        if (carrinho == null) {
            // Se não houver carrinho, cria um novo
            carrinho = new Carrinho();
            Usuario usuario = new Usuario();
            usuario.setId(usuarioId);
            carrinho.setUsuario(usuario);
            carrinho.setTotal(0.0);
            carrinho = carrinhoRepository.save(carrinho);
        }
        return carrinho;
    }


    public Carrinho removerDoCarrinho(Long usuarioId, Long suplementoId) {
        Carrinho carrinho = carrinhoRepository.findByUsuarioId(usuarioId);
        if (carrinho == null) {
            throw new RuntimeException("Carrinho não encontrado para o usuário com ID: " + usuarioId);
        }

        // Encontrar o item no carrinho
        CarrinhoItem itemParaRemover = null;
        for (CarrinhoItem item : carrinho.getItens()) {
            if (item.getSuplemento().getId().equals(suplementoId)) {
                itemParaRemover = item;
                break;
            }
        }

        if (itemParaRemover != null) {
            // Remover o item do carrinho
            carrinho.getItens().remove(itemParaRemover);
            carrinhoItemRepository.delete(itemParaRemover);
            recalcularTotal(carrinho);
        } else {
            throw new RuntimeException("Item com ID " + suplementoId + " não encontrado no carrinho.");
        }

        return carrinhoRepository.save(carrinho);
    }


    public Carrinho atualizarQuantidade(Long usuarioId, Long suplementoId, Integer quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        Carrinho carrinho = carrinhoRepository.findByUsuarioId(usuarioId);
        if (carrinho == null) {
            throw new RuntimeException("Carrinho não encontrado para o usuário com ID: " + usuarioId);
        }

        // Encontrar o item no carrinho
        CarrinhoItem itemParaAtualizar = null;
        for (CarrinhoItem item : carrinho.getItens()) {
            if (item.getSuplemento().getId().equals(suplementoId)) {
                itemParaAtualizar = item;
                break;
            }
        }

        if (itemParaAtualizar != null) {
            // Atualizar a quantidade do item
            itemParaAtualizar.setQuantidade(quantidade);
            recalcularTotal(carrinho);
        } else {
            throw new RuntimeException("Item com ID " + suplementoId + " não encontrado no carrinho.");
        }

        return carrinhoRepository.save(carrinho);
    }


    public void limparCarrinho(Long usuarioId) {
        Carrinho carrinho = carrinhoRepository.findByUsuarioId(usuarioId);
        if (carrinho != null) {
            // Limpar os itens do carrinho
            carrinho.getItens().clear();
            carrinho.setTotal(0.0);
            carrinhoRepository.save(carrinho);
        } else {
            throw new RuntimeException("Carrinho não encontrado para o usuário com ID: " + usuarioId);
        }
    }


    public Object getSubtotalCarrinho(Long usuarioId) {
        Carrinho carrinho = carrinhoRepository.findByUsuarioId(usuarioId);
        if (carrinho == null) {
            return 0.0; 
        }
        return carrinho.getTotal();
    }
}