package br.edu.ufrn.tads.eaj.suplementoseaj.controller;

import br.edu.ufrn.tads.eaj.suplementoseaj.domain.Carrinho;
import br.edu.ufrn.tads.eaj.suplementoseaj.domain.Suplemento;
import br.edu.ufrn.tads.eaj.suplementoseaj.service.SuplementoService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {

    private final SuplementoService suplementoService;

    @Autowired
    public IndexController(SuplementoService suplementoService) {
        this.suplementoService = suplementoService;
    }

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        List<Suplemento> suplementos = suplementoService.listarSuplementosAtivos();
        model.addAttribute("suplementos", suplementos);

        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new Carrinho();
            session.setAttribute("carrinho", carrinho);
        }
        
        model.addAttribute("quantidadeItensCarrinho", carrinho.getItens().stream()
                .mapToInt(item -> item.getQuantidade())
                .sum());

        model.addAttribute("carrinho", carrinho);
        model.addAttribute("subtotalCarrinho", carrinho.getItens().stream()
                .mapToDouble(item -> item.getSuplemento().getPreco() * item.getQuantidade())
                .sum());
        
        return "pages/index";
    }
}