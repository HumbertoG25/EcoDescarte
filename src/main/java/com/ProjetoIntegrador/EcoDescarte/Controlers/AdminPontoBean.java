package com.ProjetoIntegrador.EcoDescarte.Controlers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ProjetoIntegrador.EcoDescarte.Services.CheckListService;
import com.ProjetoIntegrador.EcoDescarte.Services.PontoColetaService;
import com.ProjetoIntegrador.EcoDescarte.models.Equipamento;
import com.ProjetoIntegrador.EcoDescarte.models.PontoDeColeta;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("adminPontoBean")
@ViewScoped
public class AdminPontoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private PontoColetaService pontoColetaService;

    @Autowired
    private CheckListService checkListService;

    private PontoDeColeta novoPonto = new PontoDeColeta();
    private List<Long> equipamentosAceitosIds = new ArrayList<>();
    private List<PontoDeColeta> pontosDeColeta;
    private List<Equipamento> equipamentosDisponiveis;

    @PostConstruct
    public void init() {
        recarregar();
    }

    public void recarregar() {
        this.pontosDeColeta = pontoColetaService.listarTodos();
        this.equipamentosDisponiveis = checkListService.listarEquip();
    }

    public void cadastrarPonto() {
        try {
            pontoColetaService.cadastrarPonto(novoPonto, equipamentosAceitosIds);
            novoPonto = new PontoDeColeta();
            equipamentosAceitosIds = new ArrayList<>();
            recarregar();
            adicionarMensagem(FacesMessage.SEVERITY_INFO, "Sucesso!", "Ponto de coleta cadastrado com sucesso.");
        } catch (Exception e) {
            adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Erro:", extrairCausa(e));
        }
    }

    public void deletarPonto(Long id) {
        try {
            pontoColetaService.deletarPonto(id);
            recarregar();
            adicionarMensagem(FacesMessage.SEVERITY_INFO, "Sucesso!", "Ponto de coleta removido com sucesso.");
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

    public PontoDeColeta getNovoPonto() {
        return novoPonto;
    }

    public void setNovoPonto(PontoDeColeta novoPonto) {
        this.novoPonto = novoPonto;
    }

    public List<Long> getEquipamentosAceitosIds() {
        return equipamentosAceitosIds;
    }

    public void setEquipamentosAceitosIds(List<Long> equipamentosAceitosIds) {
        this.equipamentosAceitosIds = equipamentosAceitosIds;
    }

    public List<PontoDeColeta> getPontosDeColeta() {
        return pontosDeColeta;
    }

    public void setPontosDeColeta(List<PontoDeColeta> pontosDeColeta) {
        this.pontosDeColeta = pontosDeColeta;
    }

    public List<Equipamento> getEquipamentosDisponiveis() {
        return equipamentosDisponiveis;
    }

    public void setEquipamentosDisponiveis(List<Equipamento> equipamentosDisponiveis) {
        this.equipamentosDisponiveis = equipamentosDisponiveis;
    }
}
