package com.ProjetoIntegrador.EcoDescarte.Services;

import com.ProjetoIntegrador.EcoDescarte.models.Usuario;
import com.ProjetoIntegrador.EcoDescarte.Repositorios.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service 
public class CadastroService {
    
    @Autowired 
    private UsuarioRepository usuarioRepository;

    public void cadastrarUsuario(Usuario usuario){
        Optional<Usuario> existente = usuarioRepository.buscarEmail(usuario.getEmail());
        if(existente.isPresent()){
            throw new RuntimeException("Esse Email ja esta sendo usado!");
        }

        if (usuario.getRole() == null || usuario.getRole().isEmpty()) {
            usuario.setRole("ROLE_USER");
        }

        usuarioRepository.Insert(usuario);
    }
}
