package com.ProjetoIntegrador.EcoDescarte.Controlers;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.annotation.SessionScope;

import com.ProjetoIntegrador.EcoDescarte.Services.AutentificacaoService;
import com.ProjetoIntegrador.EcoDescarte.Services.CadastroService;
import com.ProjetoIntegrador.EcoDescarte.models.Usuario;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;

@Named("usuarioBean")
@SessionScope
public class UsuarioBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(UsuarioBean.class);

    private final AutentificacaoService autentificacaoService;
    private final CadastroService cadastroService;

    private Usuario usuario = new Usuario();
    private Usuario usuarioLogado;

    public UsuarioBean(AutentificacaoService autentificacaoService, CadastroService cadastroService) {
        this.autentificacaoService = autentificacaoService;
        this.cadastroService = cadastroService;
    }

    public String logar() {
        try {
            Usuario encontrado = autentificacaoService.autenticarSenha(usuario.getEmail(), usuario.getSenha());

            if (encontrado != null) {
                return iniciarSessao(encontrado);
            }

            adicionarErro("Não foi possível entrar", "E-mail ou senha inválidos.");
            return null;
        } catch (Exception e) {
            log.error("Erro ao autenticar usuário: {}", extrairMensagemCausa(e), e);
            adicionarErro("Erro ao conectar", "Não foi possível realizar o login. Tente novamente.");
            return null;
        }
    }

    public String cadastrar() {
        try {
            cadastroService.cadastrarUsuario(usuario);
            adicionarInfo("Sucesso!", "Conta criada com sucesso. Faça login.");
            usuario = new Usuario();
            return "/login.xhtml?faces-redirect=true";
        } catch (Exception e) {
            log.error("Erro ao cadastrar usuário: {}", extrairMensagemCausa(e), e);
            adicionarErro("Erro:", extrairMensagemCausa(e));
            return null;
        }
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    public String abrirInicio() {
        if (isAdmin()) {
            return "/admin/painel.xhtml?faces-redirect=true";
        }
        if (isUser()) {
            return "/index.xhtml?faces-redirect=true";
        }
        return "/login.xhtml?faces-redirect=true";
    }

    private String iniciarSessao(Usuario encontrado) {
        this.usuarioLogado = encontrado;
        this.usuario = new Usuario();

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
        session.setAttribute("usuarioLogado", encontrado);

        return abrirInicio();
    }

    private void adicionarErro(String titulo, String detalhe) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, titulo, detalhe));
    }

    private void adicionarInfo(String titulo, String detalhe) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, titulo, detalhe));
    }

    private String extrairMensagemCausa(Throwable e) {
        Throwable causa = e;
        while (causa.getCause() != null && causa.getCause() != causa) {
            causa = causa.getCause();
        }
        return causa.getMessage();
    }

    // Métodos auxiliares para verificar o perfil do usuário logado
    public boolean isLogado() {
        return usuarioLogado != null;
    }

    public boolean isAdmin() {
        return isLogado() && "ROLE_ADMIN".equalsIgnoreCase(usuarioLogado.getRole());
    }

    public boolean isUser() {
        return isLogado() && !isAdmin();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }
}
