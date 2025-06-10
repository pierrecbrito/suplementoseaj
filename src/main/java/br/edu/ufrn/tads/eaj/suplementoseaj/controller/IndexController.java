package br.edu.ufrn.tads.eaj.suplementoseaj.controller;

import br.edu.ufrn.tads.eaj.suplementoseaj.domain.Suplemento;
import br.edu.ufrn.tads.eaj.suplementoseaj.service.CarrinhoService;
import br.edu.ufrn.tads.eaj.suplementoseaj.service.SuplementoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {

    private final SuplementoService suplementoService;
    private final CarrinhoService carrinhoService;

    @Autowired
    public IndexController(SuplementoService suplementoService, CarrinhoService carrinhoService) {
        this.suplementoService = suplementoService;
        this.carrinhoService = carrinhoService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Suplemento> suplementos = suplementoService.listarSuplementosAtivos();
        model.addAttribute("suplementos", suplementos);
        
        Long usuarioId = 1L; 
        int quantidadeItensCarrinho = carrinhoService.contarItensCarrinho(usuarioId);
        model.addAttribute("quantidadeItensCarrinho", quantidadeItensCarrinho);

        model.addAttribute("carrinho", carrinhoService.getCarrinhoByUsuario(usuarioId));
        model.addAttribute("subtotalCarrinho", carrinhoService.getSubtotalCarrinho(usuarioId));
        
        return "pages/index";
    }
}