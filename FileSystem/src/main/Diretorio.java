/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Douglas
 */
public class Diretorio {

    private Diretorio pai;
    private ArrayList<Diretorio> filhos;
    private ArrayList<Arquivo> arquivos;

    private Date dataCriacao;
    private String dataCriacaoFormatada;
    private String nome;
    private String permissao;

    public Diretorio(Diretorio pai) {
        this.pai = pai;
        this.dataCriacao = new Date(System.currentTimeMillis());
        this.permissao = "drwxrwxrwx";
        SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy HH:mm");
        this.dataCriacaoFormatada = sdf.format(dataCriacao);
        this.filhos = new ArrayList<>();
        this.arquivos = new ArrayList<>();
    }

    public Diretorio buscaDiretorioPeloNome(String nome) {

        if (nome.equals(".")) {
            return this;
        } else if (nome.equals("..")) {
            return this.pai;
        } else {
            for (Diretorio dir : filhos) {
                if (dir.getNome().equals(nome)) {
                    return dir;
                }
            }
        }
        return null;

    }

    public void addFilho(Diretorio filho) {
        filhos.add(filho);
    }

    public void removeFilho(Diretorio filho) {
        filhos.remove(filho);
    }

    public void addArquivo(Arquivo arquivo) {
        arquivos.add(arquivo);
    }

    public void removeArquivo (Arquivo arquivo){
        arquivos.remove(arquivo);
    }

    public Arquivo getArquivoPorNome(String nomeArquivo) {
        for (Arquivo arquivo : arquivos) {
            if (arquivo.getNome().equals(nomeArquivo)) {
                return arquivo; // Encontrou o arquivo pelo nome
            }
        }
        return null;
    }

    public Diretorio getPai() {
        return pai;
    }

    public void setPai(Diretorio pai) {
        this.pai = pai;
    }

    public ArrayList<Diretorio> getFilhos() {
        return filhos;
    }

    public void setFilhos(ArrayList<Diretorio> filhos) {
        this.filhos = filhos;
    }

    public ArrayList<Arquivo> getArquivos() {
        return arquivos;
    }

    public void setArquivos(ArrayList<Arquivo> arquivos) {
        this.arquivos = arquivos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPermissao() {
        return permissao;
    }

    public void setPermissao(String permissao) {
        this.permissao = permissao;
    }

    public String getDataCriacaoFormatada() {
        return dataCriacaoFormatada;
    }

    public void setDataCriacaoFormatada(String dataCriacaoFormatada) {
        this.dataCriacaoFormatada = dataCriacaoFormatada;
    }

    public Diretorio getDiretorioFilhoPorNome(String parte) {
        return null;
    }

}