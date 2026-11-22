package com.ProjetoIntegrador.EcoDescarte.Controlers;

import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;

import com.ProjetoIntegrador.EcoDescarte.models.Usuario;
import com.ProjetoIntegrador.EcoDescarte.Services.AutentificacaoService;
import com.ProjetoIntegrador.EcoDescarte.Services.CadastroService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.SessionScope;
import jakarta.inject.Named;

@Named("usuarioBean")
@SessionScope
public class UsuarioBean {
    
    @Autowired
    private AutentificacaoService autentificacaoService;

    @Autowired
    private CadastroService cadastroService;

    private Usuario usuario = new Usuario();
    private Usuario usuarioLogado;

    public String logar(){
        Usuario encontrado = autentificacaoService.autenticarSenha(usuario.getEmail(), usuario.getSenha());

        if(encontrado != null){
            this.usuarioLogado = encontrado;

            if("ROLE_ADMIN".equals(encontrado.getRole())){
                return "/admin/painel.xhtml?faces-redirect=true";
            }else{
                return "/index.xhtml?faces-redirect=true";
            }
        }else{
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "E-mail ou senha inválidos."));
            return null;
        }  
    }

    public String cadastrar(){
        try{
            cadastroService.cadastrarUsuario(usuario);
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Conta criada com sucesso. Faça login."));
            usuario = new Usuario();
            return "login.xhtml?faces-redirect=true";
        }catch(Exception e){
            e.printStackTrace();
            Throwable causa = e;
            while (causa.getCause() != null && causa.getCause() != causa) {
                causa = causa.getCause();
            }
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro:", causa.getMessage()));
            return null;
        }
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Usuario getUsuarioLogado() { return usuarioLogado; }
    public void setUsuarioLogado(Usuario usuarioLogado) { this.usuarioLogado = usuarioLogado; }
}

