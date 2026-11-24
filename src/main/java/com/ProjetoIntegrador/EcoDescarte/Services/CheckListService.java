package com.ProjetoIntegrador.EcoDescarte.Services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ProjetoIntegrador.EcoDescarte.Repositorios.CheckListRepository;
import com.ProjetoIntegrador.EcoDescarte.Repositorios.EquipamentoRepository;
import com.ProjetoIntegrador.EcoDescarte.models.CheckList;
import com.ProjetoIntegrador.EcoDescarte.models.Equipamento;

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
}
