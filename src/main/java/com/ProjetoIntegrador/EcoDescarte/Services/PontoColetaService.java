package com.ProjetoIntegrador.EcoDescarte.Services;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ProjetoIntegrador.EcoDescarte.Repositorios.PontoColetaRepository;
import com.ProjetoIntegrador.EcoDescarte.models.PontoDeColeta;

@Service
public class PontoColetaService {
    @Autowired
    private PontoColetaRepository pontoColetaRepository;

    public List<PontoDeColeta> listarPontosAtivos(Long equipId) {
        if (equipId != null) {
            return pontoColetaRepository.buscarPorEquip(equipId);
        }
        return pontoColetaRepository.listar();
    }

    public String gerarLinkRotaGoogleMaps(PontoDeColeta ponto) {
        if (ponto == null) {
            return "#";
        }

        StringBuilder destino = new StringBuilder();
        if (ponto.getEndereco() != null && !ponto.getEndereco().isBlank()) {
            destino.append(ponto.getEndereco());
        }
        if (ponto.getBairro() != null && !ponto.getBairro().isBlank()) {
            if (destino.length() > 0) destino.append(", ");
            destino.append(ponto.getBairro());
        }
        if (ponto.getCidade() != null && !ponto.getCidade().isBlank()) {
            if (destino.length() > 0) destino.append(", ");
            destino.append(ponto.getCidade());
        }

        String encoded = URLEncoder.encode(destino.toString(), StandardCharsets.UTF_8);
        return "https://www.google.com/maps/dir/?api=1&destination=" + encoded;
    }
}
