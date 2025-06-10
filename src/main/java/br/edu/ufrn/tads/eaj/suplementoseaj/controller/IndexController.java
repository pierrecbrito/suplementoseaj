package br.edu.ufrn.tads.eaj.suplementoseaj.controller;

import br.edu.ufrn.tads.eaj.suplementoseaj.domain.Suplemento;
import br.edu.ufrn.tads.eaj.suplementoseaj.repository.SuplementoRepository;
import br.edu.ufrn.tads.eaj.suplementoseaj.service.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    SuplementoRepository suplementoRepository;

    @Autowired
    CarrinhoService carrinhoService;

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("suplementos", suplementoRepository.findByIsDeletedIsNull());
        model.addAttribute("quantidadeItens", carrinhoService.getQuantidadeItens());
        return "index";
    }

    @GetMapping("/adicionarCarrinho/{id}")
    public String adicionarCarrinho(@PathVariable Long id) {
        carrinhoService.adicionarItem(id);
        return "redirect:/index";
    }

    @GetMapping("/verCarrinho")
    public String verCarrinho(Model model) {
        model.addAttribute("itens", carrinhoService.getItens());
        model.addAttribute("total", carrinhoService.getTotal());
        return "carrinho";
    }
}