package med.voll.web_application.controller;

import org.springframework.ui.Model;
import med.voll.web_application.domain.RegraDeNegocioException;
import med.voll.web_application.domain.usuario.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EsqueciMinhaSenhaController {
    public static final String FORMULARIO_RECUPERACAO_SENHA = "autenticacao/formulario-recuperacao-senha";
    private final UsuarioService service;

    public EsqueciMinhaSenhaController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping("esqueci-minha-senha")
    public String carregarPaginaEsqueciMinhaSenha() {
        return FORMULARIO_RECUPERACAO_SENHA;
    }

    @PostMapping("esqueci-minha-senha")
    public String enviarTokenEmail(@ModelAttribute("email") String email, Model model){
        try {
            service.enviarToken(email);
            return "redirect:esqueci-minha-senha?verificar";
        } catch (RegraDeNegocioException e){
            model.addAttribute("erro", e.getMessage());
            return FORMULARIO_RECUPERACAO_SENHA;
        }
    }
}