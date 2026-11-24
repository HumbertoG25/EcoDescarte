package com.ProjetoIntegrador.EcoDescarte.Controlers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ProjetoIntegrador.EcoDescarte.Services.CheckListService;
import com.ProjetoIntegrador.EcoDescarte.models.Equipamento;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("adminEquipamentoBean")
@ViewScoped
public class AdminEquipamentoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private CheckListService checkListService;

    @Autowired
    private CheckListBean checkListBean;

    private Equipamento novoEquipamento = new Equipamento();
    private String novaInstrucao;
    private List<String> instrucoesTemp = new ArrayList<>();
    private List<Equipamento> equipamentos;

    @PostConstruct
    public void init() {
        recarregar();
    }

    public void recarregar() {
        this.equipamentos = checkListService.listarEquip();
    }

    public void adicionarInstrucao() {
        if (novaInstrucao != null && !novaInstrucao.isBlank()) {
            instrucoesTemp.add(novaInstrucao.trim());
            novaInstrucao = "";
        }
    }

    public void removerInstrucao(int index) {
        if (index >= 0 && index < instrucoesTemp.size()) {
            instrucoesTemp.remove(index);
        }
    }

    public void cadastrarEquipamento() {
        try {
            checkListService.cadastrarEquipComChechList(novoEquipamento, instrucoesTemp);
            novoEquipamento = new Equipamento();
            novaInstrucao = "";
            instrucoesTemp = new ArrayList<>();
            recarregar();
            checkListBean.recarregarEquipamentos();
            adicionarMensagem(FacesMessage.SEVERITY_INFO, "Sucesso!", "Equipamento e checklist cadastrados com sucesso.");
        } catch (Exception e) {
            adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Erro:", extrairCausa(e));
        }
    }

    public void deletarEquipamento(Long id) {
        try {
            checkListService.deletarEquipamento(id);
            recarregar();
            checkListBean.recarregarEquipamentos();
            adicionarMensagem(FacesMessage.SEVERITY_INFO, "Sucesso!", "Equipamento removido com sucesso.");
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

    public Equipamento getNovoEquipamento() {
        return novoEquipamento;
    }

    public void setNovoEquipamento(Equipamento novoEquipamento) {
        this.novoEquipamento = novoEquipamento;
    }

    public String getNovaInstrucao() {
        return novaInstrucao;
    }

    public void setNovaInstrucao(String novaInstrucao) {
        this.novaInstrucao = novaInstrucao;
    }

    public List<String> getInstrucoesTemp() {
        return instrucoesTemp;
    }

    public void setInstrucoesTemp(List<String> instrucoesTemp) {
        this.instrucoesTemp = instrucoesTemp;
    }

    public List<Equipamento> getEquipamentos() {
        return equipamentos;
    }

    public void setEquipamentos(List<Equipamento> equipamentos) {
        this.equipamentos = equipamentos;
    }
}
