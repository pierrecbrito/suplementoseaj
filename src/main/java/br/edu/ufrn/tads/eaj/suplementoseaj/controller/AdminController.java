package br.edu.ufrn.tads.eaj.suplementoseaj.controller;

import br.edu.ufrn.tads.eaj.suplementoseaj.domain.Suplemento;
import br.edu.ufrn.tads.eaj.suplementoseaj.service.SuplementoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {

    private final SuplementoService suplementoService;

    @Autowired
    public AdminController(SuplementoService suplementoService) {
        this.suplementoService = suplementoService;
    }

    @GetMapping("/admin")
    public String adminPanel(Model model) {
        List<Suplemento> suplementos = suplementoService.listarTodosSuplementos();
        model.addAttribute("suplementos", suplementos);
        
        return "pages/admin";
    }
    
    @GetMapping("/admin/deletar")
    public String deletarSuplemento(@RequestParam("id") Long id) {
        suplementoService.deletarSuplemento(id);
        return "redirect:/admin";
    }
    
    @GetMapping("/admin/restaurar")
    public String restaurarSuplemento(@RequestParam("id") Long id) {
        suplementoService.restaurarSuplemento(id);
        return "redirect:/admin";
    }
    
    @GetMapping("/admin/editar")
    public String editarSuplementoForm(@RequestParam("id") Long id, Model model) {
        model.addAttribute("suplemento", suplementoService.buscarPorId(id).orElse(null));
        return "editar-suplemento";
    }
}