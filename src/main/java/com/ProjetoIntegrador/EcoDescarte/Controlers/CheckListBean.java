package com.ProjetoIntegrador.EcoDescarte.Controlers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.SessionScope;

import com.ProjetoIntegrador.EcoDescarte.Services.CheckListService;
import com.ProjetoIntegrador.EcoDescarte.Services.PontoColetaService;
import com.ProjetoIntegrador.EcoDescarte.models.CheckList;
import com.ProjetoIntegrador.EcoDescarte.models.Equipamento;
import com.ProjetoIntegrador.EcoDescarte.models.PontoDeColeta;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;

@Named("descarteBean")
@SessionScope
public class CheckListBean {
    
    @Autowired
    private CheckListService checkListService;

    @Autowired
    private PontoColetaService pontoColetaService;

    private List<Equipamento> equipamentosDisponiveis;
    private Long equipamentoSelecionadoId;
    private Equipamento equipamentoSelecionado;
    private List<CheckList> passosCheckList;
    private List<PontoDeColeta> pontosDeColeta;
    private boolean checklistConcluido;

    @PostConstruct
    public void init() {
        this.equipamentosDisponiveis = checkListService.listarEquip();
        this.pontosDeColeta = pontoColetaService.listarPontosAtivos(null);
    }

    public void carregarCheckList(){
        this.equipamentoSelecionado = null;
        this.checklistConcluido = false;

        if (equipamentoSelecionadoId != null && equipamentosDisponiveis != null) {
            for (Equipamento eq : equipamentosDisponiveis) {
                if (eq.getId().equals(equipamentoSelecionadoId)) {
                    this.equipamentoSelecionado = eq;
                    break;
                }
            }
        }

        this.passosCheckList = checkListService.buscarPassosPorEquipamento(equipamentoSelecionadoId);
        this.pontosDeColeta = pontoColetaService.listarPontosAtivos(equipamentoSelecionadoId);

        if (this.equipamentoSelecionado != null && (this.passosCheckList == null || this.passosCheckList.isEmpty())) {
            this.checklistConcluido = true;
        }
    }

    public void verificarChecklist() {
        if (passosCheckList == null || passosCheckList.isEmpty()) {
            this.checklistConcluido = false;
            return;
        }

        for (CheckList passo : passosCheckList) {
            if (!passo.isConcluido()) {
                this.checklistConcluido = false;
                return;
            }
        }

        this.checklistConcluido = true;
    }

    public String gerarLinkMaps(PontoDeColeta ponto){
        return pontoColetaService.gerarLinkRotaGoogleMaps(ponto);
    }

    public List<Equipamento> getEquipamentosDisponiveis() {
        return equipamentosDisponiveis;
    }

    public void setEquipamentosDisponiveis(List<Equipamento> equipamentosDisponiveis) {
        this.equipamentosDisponiveis = equipamentosDisponiveis;
    }

    public Long getEquipamentoSelecionadoId() {
        return equipamentoSelecionadoId;
    }

    public void setEquipamentoSelecionadoId(Long equipamentoSelecionadoId) {
        this.equipamentoSelecionadoId = equipamentoSelecionadoId;
    }

    public Equipamento getEquipamentoSelecionado() {
        return equipamentoSelecionado;
    }

    public void setEquipamentoSelecionado(Equipamento equipamentoSelecionado) {
        this.equipamentoSelecionado = equipamentoSelecionado;
    }

    public List<CheckList> getPassosCheckList() {
        return passosCheckList;
    }

    public void setPassosCheckList(List<CheckList> passosCheckList) {
        this.passosCheckList = passosCheckList;
    }

    public List<PontoDeColeta> getPontosDeColeta() {
        return pontosDeColeta;
    }

    public void setPontosDeColeta(List<PontoDeColeta> pontosDeColeta) {
        this.pontosDeColeta = pontosDeColeta;
    }

    public boolean isChecklistConcluido() {
        return checklistConcluido;
    }

    public void setChecklistConcluido(boolean checklistConcluido) {
        this.checklistConcluido = checklistConcluido;
    }
}

