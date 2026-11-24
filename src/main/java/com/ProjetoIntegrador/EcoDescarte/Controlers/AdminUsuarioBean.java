package com.ProjetoIntegrador.EcoDescarte.Controlers;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ProjetoIntegrador.EcoDescarte.Services.CadastroService;
import com.ProjetoIntegrador.EcoDescarte.models.Usuario;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("adminUsuarioBean")
@ViewScoped
public class AdminUsuarioBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private CadastroService cadastroService;

    @Autowired
    private UsuarioBean usuarioBean;

    private Usuario novoAdmin = new Usuario();
    private List<Usuario> usuarios;

    @PostConstruct
    public void init() {
        recarregar();
    }

    public void recarregar() {
        this.usuarios = cadastroService.listarUsuario();
    }

    public void cadastrarAdmin() {
        try {
            cadastroService.cadastrarAdmim(novoAdmin);
            novoAdmin = new Usuario();
            recarregar();
            adicionarMensagem(FacesMessage.SEVERITY_INFO, "Sucesso!", "Administrador cadastrado com sucesso.");
        } catch (Exception e) {
            adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Erro:", extrairCausa(e));
        }
    }

    public void deletarUsuario(Long id) {
        try {
            if (usuarioBean.getUsuarioLogado() != null && id.equals(usuarioBean.getUsuarioLogado().getId())) {
                adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Ação bloqueada:", "Você não pode excluir a sua própria conta.");
                return;
            }
            cadastroService.deletarUsuario(id);
            recarregar();
            adicionarMensagem(FacesMessage.SEVERITY_INFO, "Sucesso!", "Usuário removido com sucesso.");
        } catch (Exception e) {
            adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Erro:", extrairCausa(e));
        }
    }

    private void adicionarMensagem(FacesMessage.Severity severity, String resumo, String detalhe) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, resumo, detalhe));
    }

    private String extrairCausa(Throwable e) {
        Throwable causa = e;
        while (causa.getCause() != null && causa.getCause() != causa) {
            causa = causa.getCause();
        }
        return causa.getMessage();
    }

    public Usuario getNovoAdmin() {
        return novoAdmin;
    }

    public void setNovoAdmin(Usuario novoAdmin) {
        this.novoAdmin = novoAdmin;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
