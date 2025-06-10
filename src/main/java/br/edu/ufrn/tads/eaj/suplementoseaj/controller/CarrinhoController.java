package br.edu.ufrn.tads.eaj.suplementoseaj.controller;

import br.edu.ufrn.tads.eaj.suplementoseaj.domain.Carrinho;
import br.edu.ufrn.tads.eaj.suplementoseaj.service.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CarrinhoController {
    
    @Autowired
    private CarrinhoService carrinhoService;
    
    /**
     * Adiciona um item ao carrinho ou incrementa sua quantidade se já existir
     */
    @GetMapping("/adicionarCarrinho")
    public String adicionarItemCarrinho(
            @RequestParam Long suplementoId,
            @RequestParam(defaultValue = "1") Integer quantidade) {
        
        Carrinho carrinhoAtualizado = carrinhoService.adicionarCarrinho(1L, suplementoId, quantidade);
        return "redirect:/";
    }
    
    /**
     * Recupera o carrinho do usuário
     */
    @GetMapping("/verCarrinho")
    public ResponseEntity<Carrinho> getCarrinho(@PathVariable Long usuarioId) {
        Carrinho carrinho = carrinhoService.getCarrinhoByUsuario(usuarioId);
        if (carrinho == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(carrinho, HttpStatus.OK);
    }
    
    /**
     * Remove um item do carrinho
     */
    @DeleteMapping("/remover")
    public ResponseEntity<Carrinho> removerItemCarrinho(
            @RequestParam Long usuarioId,
            @RequestParam Long suplementoId) {
        
        Carrinho carrinhoAtualizado = carrinhoService.removerDoCarrinho(usuarioId, suplementoId);
        return new ResponseEntity<>(carrinhoAtualizado, HttpStatus.OK);
    }
    
    /**
     * Atualiza a quantidade de um item no carrinho
     */
    @PutMapping("/atualizarCarrinho")
    public ResponseEntity<Carrinho> atualizarQuantidade(
            @RequestParam Long usuarioId,
            @RequestParam Long suplementoId,
            @RequestParam Integer quantidade) {
        
        if (quantidade <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Carrinho carrinhoAtualizado = carrinhoService.atualizarQuantidade(usuarioId, suplementoId, quantidade);
        return new ResponseEntity<>(carrinhoAtualizado, HttpStatus.OK);
    }
    
    /**
     * Esvazia o carrinho
     */
    @DeleteMapping("/limparCarrinho/{usuarioId}")
    public ResponseEntity<Void> limparCarrinho(@PathVariable Long usuarioId) {
        carrinhoService.limparCarrinho(usuarioId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}