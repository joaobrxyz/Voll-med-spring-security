package med.voll.web_application.domain.usuario.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import med.voll.web_application.domain.RegraDeNegocioException;
import med.voll.web_application.domain.usuario.Usuario;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    private final JavaMailSender enviadorEmail;
    private static final String EMAIL_ORIGEM = "vollmed@email.com";
    private static final String NOME_ENVIADOR = "Clínica Voll Med";

    public static final String URL_SITE = "http://localhost:8080"; //"voll.med.com.br"

    public EmailService(JavaMailSender enviadorEmail) {
        this.enviadorEmail = enviadorEmail;
    }
    @Async
    private void enviarEmail(String emailUsuario, String assunto, String conteudo) {
        MimeMessage message = enviadorEmail.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(EMAIL_ORIGEM, NOME_ENVIADOR);
            helper.setTo(emailUsuario);
            helper.setSubject(assunto);
            helper.setText(conteudo, true);
        } catch(MessagingException | UnsupportedEncodingException e){
            throw new RegraDeNegocioException("Erro ao enviar email");
        }

        enviadorEmail.send(message);
    }

    public void enviarEmailSenha(Usuario usuario) {
        String assunto = "Aqui está seu link para alterar a senha";
        String conteudo = gerarConteudoEmail("Olá [[name]],<br>"
                + "Por favor clique no link abaixo para alterar a senha:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">ALTERAR</a></h3>"
                + "Obrigado,<br>"
                + "Clínica Voll Med.", usuario.getNome(), URL_SITE + "/recuperar-conta?codigo=" + usuario.getToken());

        enviarEmail(usuario.getUsername(), assunto, conteudo);
    }

    private String gerarConteudoEmail(String template, String nome, String url) {
        return template.replace("[[name]]", nome).replace("[[URL]]", url);
    }


    public void enviarEmailSenhaAleatoria(Usuario usuario, String senhaAleatoria) {
        String assunto = "Bem-vindo à Clínica Voll Med | Dados de Acesso ";
        String conteudo = gerarConteudoBoasVindas("Olá [[name]],<br>"
                + "Aqui estão suas informações de login <br>"
                + "<strong>Email:</strong> [[email]] <br>"
                + "<strong>Senha:</strong> [[senha]] <br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">ACESSAR SUA CONTA</a></h3>"
                + "Conte com nossa equipe para o que precisar!<br>"
                + "Obrigado,<br>"
                + "Clínica Voll Med.", usuario.getNome(), usuario.getUsername(), senhaAleatoria);

        enviarEmail(usuario.getUsername(), assunto, conteudo);

    }

    private String gerarConteudoBoasVindas(String template, String nome, String email, String senhaAleatoria) {
        return template.replace("[[name]]", nome)
                .replace("[[email]]", email)
                .replace("[[senha]]", senhaAleatoria)
                .replace("[[URL]]", URL_SITE);
    }
}