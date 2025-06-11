package br.edu.ufrn.tads.eaj.suplementoseaj.controller;

import br.edu.ufrn.tads.eaj.suplementoseaj.domain.Suplemento;
import br.edu.ufrn.tads.eaj.suplementoseaj.service.SuplementoService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

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
    
     @GetMapping("/cadastro")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("suplemento", new Suplemento());
        model.addAttribute("imagens", suplementoService.listarImagensURIs());
        return "pages/cadastro";
    }

    @PostMapping("/salvar")
    public String salvarSuplemento(
            @Valid Suplemento suplemento,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("imagens", suplementoService.listarImagensURIs());
            model.addAttribute("modoEdicao", suplemento.getId() != null);
            return suplemento.getId() != null ? "pages/editar" : "pages/cadastro";
        }
        
        try {
            suplementoService.salvarSuplemento(suplemento);
            
            String mensagem = suplemento.getId() != null ? 
                "Suplemento atualizado com sucesso!" : 
                "Suplemento cadastrado com sucesso!";
            
            redirectAttributes.addFlashAttribute("mensagem", mensagem);
            return "redirect:/admin"; 
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao salvar suplemento: " + e.getMessage());
            model.addAttribute("modoEdicao", suplemento.getId() != null);
            return suplemento.getId() != null ? "pages/editar" : "pages/cadastro";
        }
    }

    @GetMapping("/editar")
    public String exibirFormularioEdicao(@RequestParam("suplementoId") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<Suplemento> suplementoOptional = suplementoService.buscarPorId(id);
            
            if (suplementoOptional.isPresent()) {
                model.addAttribute("suplemento", suplementoOptional.get());
                model.addAttribute("modoEdicao", true);
                model.addAttribute("imagens", suplementoService.listarImagensURIs());
                return "pages/cadastro";
            } else {
                redirectAttributes.addFlashAttribute("erro", "Suplemento não encontrado!");
                return "redirect:/admin";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao buscar suplemento: " + e.getMessage());
            return "redirect:/admin";
        }
    }

    @GetMapping("/deletar")
    public String deletarSuplemento(@RequestParam("suplementoId") Long id, RedirectAttributes redirectAttributes) {
        try {
            suplementoService.deletarSuplemento(id);
            redirectAttributes.addFlashAttribute("mensagem", "Suplemento deletado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao deletar suplemento: " + e.getMessage());
        }
        return "redirect:/admin";
    }
    
    @GetMapping("/restaurar")
    public String restaurarSuplemento(@RequestParam("suplementoId") Long id, RedirectAttributes redirectAttributes) {
        suplementoService.restaurarSuplemento(id);
        redirectAttributes.addFlashAttribute("mensagem", "Suplemento restaurado com sucesso!");
        return "redirect:/admin";
    }
    
}