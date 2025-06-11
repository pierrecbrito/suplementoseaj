package br.edu.ufrn.tads.eaj.suplementoseaj.controller;

import br.edu.ufrn.tads.eaj.suplementoseaj.domain.Usuario;
import br.edu.ufrn.tads.eaj.suplementoseaj.service.UsuarioService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class AutenticacaoController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String login() {
        return "pages/login";
    }
    
    @GetMapping("/cadusuario")
    public String formCadastroUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "pages/cadastro-usuario";
    }
    
    @PostMapping("/salvarusuario")
    public String salvarUsuario(@Valid Usuario usuario, 
                               BindingResult result, 
                               RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "pages/cadastro-usuario";
        }
        
        if (usuarioService.usuarioExiste(usuario.getUsername())) {
            redirectAttributes.addFlashAttribute("mensagemErro", 
                "Nome de usuário já existe!");
            return "redirect:/cadusuario";
        }
        
        usuarioService.salvar(usuario);
        redirectAttributes.addFlashAttribute("mensagemSucesso", 
            "Cadastro realizado com sucesso! Faça login para continuar.");
        
        return "redirect:/login";
    }
    
    @GetMapping("/acesso-negado")
    public String acessoNegado() {
        return "acesso-negado";
    }
}