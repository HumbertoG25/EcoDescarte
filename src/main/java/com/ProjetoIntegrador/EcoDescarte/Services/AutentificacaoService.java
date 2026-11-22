package com.ProjetoIntegrador.EcoDescarte.Services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ProjetoIntegrador.EcoDescarte.models.Usuario;
import com.ProjetoIntegrador.EcoDescarte.Repositorios.UsuarioRepository;

@Service 
public class AutentificacaoService {
    
    @Autowired 
    private UsuarioRepository usuarioRepository;

    public Usuario autenticarSenha(String email, String senhaForm){
        Optional<Usuario> optUsuario = usuarioRepository.buscarEmail(email);

        if(optUsuario.isPresent()){
            Usuario usuario = optUsuario.get();

            if(senhaForm.equals(usuario.getSenha())){
                return usuario;
            }
        }
        return null;
    }
}
