package com.ProjetoIntegrador.EcoDescarte.Services;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ProjetoIntegrador.EcoDescarte.Repositorios.PontoColetaRepository;
import com.ProjetoIntegrador.EcoDescarte.models.PontoDeColeta;
import org.springframework.transaction.annotation.Transactional;

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

    public List<PontoDeColeta> listarTodos() {
        return pontoColetaRepository.listartodos();
    }

    @Transactional
    public void cadastrarPonto(PontoDeColeta ponto, List<Long> equipamentosIds) {
        if(ponto.getNomeLocal() == null || ponto.getNomeLocal().isBlank()) {
            throw new IllegalArgumentException("O nome do local não pode ser nulo ou vazio.");
        }
        if(ponto.getEndereco() == null || ponto.getEndereco().isBlank()) {
            throw new IllegalArgumentException("O endereço não pode ser nulo ou vazio.");
        }
        if(ponto.getCidade() == null || ponto.getCidade().isBlank()) {
            throw new IllegalArgumentException("A cidade não pode ser nula ou vazia.");
        }

        Long pontoId = pontoColetaRepository.inserir(ponto);

        if (equipamentosIds != null) {
            for(Long eqId : equipamentosIds) {
                pontoColetaRepository.vincularEquipamento(pontoId, eqId);
            }
        }
    }

    public void deletarPonto(Long id) {
        pontoColetaRepository.deletar(id);
    }

    public List<PontoDeColeta> listarPontos() {
        return pontoColetaRepository.listar();
    }
}
