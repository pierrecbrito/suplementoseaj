package br.edu.ufrn.tads.eaj.suplementoseaj.controller;

import br.edu.ufrn.tads.eaj.suplementoseaj.domain.Carrinho;
import br.edu.ufrn.tads.eaj.suplementoseaj.domain.CarrinhoItem;
import br.edu.ufrn.tads.eaj.suplementoseaj.domain.Suplemento;
import br.edu.ufrn.tads.eaj.suplementoseaj.service.SuplementoService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class CarrinhoController {

    @Autowired
    private SuplementoService suplementoService;

    @GetMapping("/adicionarCarrinho")
    public String adicionarAoCarrinho(
            @RequestParam("suplementoId") Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            Optional<Suplemento> suplementoOpt = suplementoService.buscarPorId(id);
            
            if (suplementoOpt.isPresent()) {
                Suplemento suplemento = suplementoOpt.get();
                
                Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
                
                if (carrinho == null) {
                    carrinho = new Carrinho();
                    session.setAttribute("carrinho", carrinho);
                }
                
                boolean itemExistente = false;
                
                for (CarrinhoItem item : carrinho.getItens()) {
                    if (item.getSuplemento().getId().equals(suplemento.getId())) {
                        item.setQuantidade(item.getQuantidade() + 1);
                        itemExistente = true;
                        break;
                    }
                }
                
                if (!itemExistente) {
                    carrinho.getItens().add(new CarrinhoItem(suplemento, 1));
                }
                
                double total = 0.0;
                for (CarrinhoItem item : carrinho.getItens()) {
                    total += item.getSuplemento().getPreco() * item.getQuantidade();
                }
                carrinho.setTotal(total);
                
                redirectAttributes.addFlashAttribute("mensagem", 
                        "Produto adicionado ao carrinho com sucesso!");
                
            } else {
                redirectAttributes.addFlashAttribute("erro", 
                        "Produto não encontrado. ID: " + id);
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", 
                    "Erro ao adicionar produto ao carrinho: " + e.getMessage());
        }
        
        return "redirect:/";
    }
    


    @GetMapping("/verCarrinho")
    public String verCarrinho(@AuthenticationPrincipal UserDetails userDetails, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
        
        if (carrinho == null || carrinho.getItens().isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Carrinho vazio.");
            return "redirect:/";
        }

        
        model.addAttribute("quantidadeItensCarrinho", carrinho.getItens().stream()
                .mapToInt(item -> item.getQuantidade())
                .sum());
        model.addAttribute("carrinho", carrinho);
        model.addAttribute("subtotalCarrinho", carrinho.getItens().stream()
                .mapToDouble(item -> item.getSuplemento().getPreco() * item.getQuantidade())
                .sum());
        

        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
        }
        
        return "pages/carrinho";
    }
    
    @GetMapping("/finalizarCompra")
    public String finalizarCompra(HttpSession session, RedirectAttributes redirectAttributes) {
        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
        
        if (carrinho == null || carrinho.getItens().isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Carrinho vazio.");
            return "redirect:/";
        }

        session.removeAttribute("carrinho");
        redirectAttributes.addFlashAttribute("mensagem", "Compra finalizada com sucesso! Obrigado pela preferência.");
        
        return "redirect:/";
    }
}