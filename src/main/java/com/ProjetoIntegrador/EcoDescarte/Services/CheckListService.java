package com.ProjetoIntegrador.EcoDescarte.Services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ProjetoIntegrador.EcoDescarte.Repositorios.CheckListRepository;
import com.ProjetoIntegrador.EcoDescarte.Repositorios.EquipamentoRepository;
import com.ProjetoIntegrador.EcoDescarte.models.CheckList;
import com.ProjetoIntegrador.EcoDescarte.models.Equipamento;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CheckListService {
    
    @Autowired 
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private CheckListRepository checkListRepository;

    public List<Equipamento> listarEquip(){
        return equipamentoRepository.listarTodos();
    }

    public List<CheckList> buscarPassosPorEquipamento(Long equipamentoId) {
        if (equipamentoId == null) {
            return Collections.emptyList();
        }
        return checkListRepository.buscarPorEquipID(equipamentoId);
    }

    @Transactional
    public void cadastrarEquipComChechList(Equipamento eq, List<String>instrucoes){
        if(eq.getNome() == null || eq.getNome().isBlank()){
             throw new RuntimeException("O nome do equipamento é obrigatório.");
        }

        if (eq.getCategoria() == null || eq.getCategoria().isBlank()) {
            throw new RuntimeException("A categoria do equipamento é obrigatória.");
        }

        Long eqId = equipamentoRepository.inserir(eq);

        if (instrucoes != null) {
            int ordem = 1;
            for (String texto : instrucoes) {
                if (texto != null && !texto.isBlank()) {
                    CheckList passo = new CheckList();
                    passo.setInstrucao(texto.trim());
                    passo.setOrdem(ordem++);
                    checkListRepository.inserir(eqId, passo);
                }
            }
        }
    }

    @Transactional
    public void deletarEquipamento(Long id) {
        checkListRepository.deletarPorEquipID(id);
        equipamentoRepository.deletar(id);
    }
}
