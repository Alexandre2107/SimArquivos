package main;

import java.util.ArrayList;
import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import operatingSystem.Kernel;

/**
 * Kernel desenvolvido pelo aluno. Outras classes criadas pelo aluno podem ser
 * utilizadas, como por exemplo: - Arvores; - Filas; - Pilhas; - etc...
 *
 * @author nome do aluno...
 */
public class MyKernel implements Kernel {

    private Diretorio raiz;
    public Diretorio teste;
    private Diretorio diretorioAtual;
    private Diretorio diretorioCriacao;
    public String aux_cd;
    ArrayList<Arquivo> a = new ArrayList<Arquivo>();

    public MyKernel() {
        raiz = new Diretorio(null);
        raiz.setNome("/");
        diretorioAtual = raiz;
        diretorioCriacao = diretorioAtual;

        teste = new Diretorio(raiz);
        teste.setNome("teste");
        diretorioAtual.addFilho(teste);
        aux_cd = "";
    }

    public String ls(String parameters) {
        // variavel result deverah conter o que vai ser impresso na tela apos comando do
        // usuário
        String result = "";
        System.out.println("Chamada de Sistema: ls");
        System.out.println("\tParametros: " + parameters);
        String[] par = parameters.split("/");

        for (int i = 0; i < diretorioAtual.getFilhos().size(); i++) {
            if (!diretorioAtual.getFilhos().get(i).getNome().isEmpty()) {
                result += " " + diretorioAtual.getFilhos().get(i).getNome();
            }

            // implementacao "-l" e "-l /caminho"
        }

        for (int i = 0; i < diretorioAtual.getArquivos().size(); i++) {
            if (diretorioAtual.getArquivos().isEmpty()) {
                break;
            }
            if (!diretorioAtual.getArquivos().get(i).getNome().isEmpty()) {
                result += " " + diretorioAtual.getArquivos().get(i).getNome();
            }
            System.out.println("Arquivos: " + diretorioAtual.getArquivos().get(i).getPai().getNome());
        }

        if (par.length == 2) {

        }

        return result;
    }

    public String mkdir(String parameters) {
        String result = "";
        System.out.println("Chamada de Sistema: mkdir");
        System.out.println("\tParametros: " + parameters);

        // inicio da implementacao do aluno
        String[] par = parameters.split("/");
        System.out.println("inicio" + par[0] + "fim");
        if (par.length == 1) {
            for (int i = 0; i < diretorioAtual.getFilhos().size(); i++) {
                if (diretorioAtual.getFilhos().get(i).getNome().equals(parameters)) {
                    result = "Já existe um diretorio com esse nome neste caminho";
                    return result;
                }
            }
            Diretorio novoDiretorio = new Diretorio(diretorioAtual);
            diretorioAtual.addFilho(novoDiretorio);
            novoDiretorio.setNome(parameters);
        }
        if (par.length == 2) {
            for (int i = 0; i < diretorioAtual.getFilhos().size(); i++) {
                if (diretorioAtual.getFilhos().get(i).getNome().equals(par[1])) {
                    result = "Já existe um diretorio com esse nome neste caminho";
                    return result;
                }
            }
            Diretorio novoDiretorio = new Diretorio(diretorioAtual);
            diretorioAtual.addFilho(novoDiretorio);
            novoDiretorio.setNome(par[1]);
        }
        if (par.length > 2) {
            diretorioCriacao = diretorioAtual;
            for (int i = 1; i < par.length - 1; i++) {

                int au = 0;

                Diretorio auxiliar = diretorioCriacao;

                if (diretorioCriacao.getFilhos().isEmpty()) {
                    result = "Diretorio vazio";
                    return result;
                }
                for (int j = 0; j < diretorioCriacao.getFilhos().size(); j++) {
                    System.out.println("au:" + au + "auxiliar:" + auxiliar.getFilhos().size() + "par " + par[i]
                            + "array" + diretorioCriacao.getFilhos().get(j).getNome());
                    if (!(diretorioCriacao.getFilhos().get(j).getNome().equals(par[i]))) {
                        au += 1;
                    }
                    if (diretorioCriacao.getFilhos().get(j).getNome().equals(par[i])) {
                        diretorioCriacao = diretorioCriacao.getFilhos().get(j);
                    }
                }
                System.out.println(
                        "au:" + au + "auxiliar:" + auxiliar.getFilhos().size() + " criac" + diretorioCriacao.getNome());
                if (au == auxiliar.getFilhos().size()) {
                    result = "Diretorio inexisite";
                    return result;
                }

            }
            for (int i = 0; i < diretorioCriacao.getFilhos().size(); i++) {
                if (diretorioCriacao.getFilhos().get(i).getNome().equals(par[par.length - 1])) {
                    result = "Já existe um diretorio com esse nome neste caminho";
                    return result;
                }
            }
            Diretorio novoDiretorio = new Diretorio(diretorioCriacao);
            diretorioCriacao.addFilho(novoDiretorio);
            novoDiretorio.setNome(par[par.length - 1]);
            diretorioCriacao = diretorioAtual;
        }

        // fim da implementacao do aluno
        return result;
    }

    public String cd(String parameters) {
        // variavel result deverah conter o que vai ser impresso na tela apos comando do
        // usuário
        String result = "";
        String currentDir = "";
        System.out.println("Chamada de Sistema: cd");
        System.out.println("\tParametros: " + parameters);
        int au = 0;
        Diretorio auxiliar = diretorioAtual;

        // inicio da implementacao do aluno

        if (diretorioAtual.getFilhos().isEmpty() && !parameters.equals("..")) {
            result = "Diretorio vazio";
            return result;
        }
        for (int i = 0; i < diretorioAtual.getFilhos().size(); i++) {

            if (!(diretorioAtual.getFilhos().get(i).getNome().equals(parameters))) {
                au += 1;
            }
            if (diretorioAtual.getFilhos().get(i).getNome().equals(parameters)) {
                aux_cd += "/" + diretorioAtual.getFilhos().get(i).getNome();
                diretorioAtual = diretorioAtual.getFilhos().get(i);
                currentDir = aux_cd;

            }

        }

        if (au == auxiliar.getFilhos().size() && !parameters.equals("..")) {
            result = "Diretorio inexisite";
            return result;
        }
        if (parameters.equals("..") && diretorioAtual.getNome().equals(raiz.getNome())) {
            result = "Você já está na raiz";
            return result;
        }
        if (parameters.equals("..")) {
            String[] cur = aux_cd.split("/");
            aux_cd = "";
            System.out.println("t" + cur[0]);
            for (int i = 1; i < cur.length - 1; i++) {
                System.out.println(cur[i]);
                aux_cd += "/" + cur[i];
            }
            diretorioAtual = diretorioAtual.getPai();
            currentDir = aux_cd;
        }
        System.out.println("auxcd" + aux_cd);

        // setando parte gráfica do diretorio atual
        operatingSystem.fileSystem.FileSytemSimulator.currentDir = currentDir;

        // fim da implementacao do aluno
        return result;
    }

    public String rmdir(String parameters) {
        // variavel result deverah conter o que vai ser impresso na tela apos comando do
        // usuário
        String result = "";
        System.out.println("Chamada de Sistema: rmdir");
        System.out.println("\tParametros: " + parameters);

        // inicio da implementacao do aluno
        String[] par = parameters.split("/");

        Diretorio auxiliar = diretorioAtual;
        System.out.println(auxiliar.getFilhos().size());
        if (par.length == 1) {
            int au = 0;
            for (int i = 0; i < diretorioAtual.getFilhos().size(); i++) {
                if (diretorioAtual.getFilhos().isEmpty()) {
                    result = "Diretorio vazio";
                    return result;
                }
                System.out.println(diretorioAtual.getFilhos().get(i).getNome() + "" + parameters);
                if (!(diretorioAtual.getFilhos().get(i).getNome().equals(parameters))) {
                    au += 1;
                }
                if (!diretorioAtual.getFilhos().get(i).getFilhos().isEmpty()
                        || !diretorioAtual.getFilhos().get(i).getArquivos().isEmpty()) {
                    result = "Diretorio não está vazio";
                    return result;
                }
                if (diretorioAtual.getFilhos().get(i).getNome().equals(parameters)) {
                    System.out.println(diretorioAtual.getFilhos().get(i).getNome());
                    diretorioAtual.removeFilho(diretorioAtual.getFilhos().get(i));
                    System.out.println(auxiliar.getFilhos().size());
                    break;
                }

                System.out.println(
                        "au:" + au + "auxiliar:" + auxiliar.getFilhos().size() + " criac" + diretorioCriacao.getNome());
                if (au == auxiliar.getFilhos().size()) {
                    result = "Diretorio não existe";
                    return result;
                }

            }
        }
        if (par.length == 2) {
            int au = 0;
            for (int i = 0; i < diretorioAtual.getFilhos().size(); i++) {
                if (!(diretorioAtual.getFilhos().get(i).getNome().equals(par[par.length - 1]))) {
                    au += 1;
                }
                if (!diretorioAtual.getFilhos().get(i).getFilhos().isEmpty()
                        || !diretorioAtual.getFilhos().get(i).getArquivos().isEmpty()) {
                    result = "Diretorio não está vazio";
                    return result;
                }
                if (diretorioAtual.getFilhos().get(i).getNome().equals(par[par.length - 1])) {
                    System.out.println(diretorioAtual.getFilhos().get(i).getNome());
                    diretorioAtual.removeFilho(diretorioAtual.getFilhos().get(i));
                    System.out.println(auxiliar.getFilhos().size());
                    break;
                }
                System.out.println(
                        "au:" + au + "auxiliar:" + auxiliar.getFilhos().size() + " criac" + diretorioCriacao.getNome());
                if (au == auxiliar.getFilhos().size()) {
                    result = "Diretorio não existe";
                    return result;
                }
            }
        }

        // implementacao da remocao do caminho absoluto

        // fim da implementacao do aluno
        return result;
    }

    public String cp(String parameters) {
        // variavel result deverah conter o que vai ser impresso na tela apos comando do
        // usuário
        String result = "";
        System.out.println("Chamada de Sistema: cp");
        System.out.println("\tParametros: " + parameters);

        // inicio da implementacao do aluno
        // fim da implementacao do aluno
        return result;
    }

    public String mv(String parameters) {
        // variavel result deverah conter o que vai ser impresso na tela apos comando do
        // usuário
        String result = "";
        System.out.println("Chamada de Sistema: mv");
        System.out.println("\tParametros: " + parameters);

        // inicio da implementacao do aluno
        // fim da implementacao do aluno
        return result;
    }

    public String rm(String parameters) {
        // variavel result deverah conter o que vai ser impresso na tela apos comando do
        // usuário
        String result = "";
        System.out.println("Chamada de Sistema: rm");
        System.out.println("\tParametros: " + parameters);

        // inicio da implementacao do aluno
        // fim da implementacao do aluno
        return result;
    }

    public String chmod(String parameters) {
        // variavel result deverah conter o que vai ser impresso na tela apos comando do
        // usuário
        String result = "";
        System.out.println("Chamada de Sistema: chmod");
        System.out.println("\tParametros: " + parameters);

        // inicio da implementacao do aluno
        // fim da implementacao do aluno
        return result;
    }

    public String createfile(String parameters) {
        // variavel result deverah conter o que vai ser impresso na tela apos comando do
        // usuário
        String result = "";
        System.out.println("Chamada de Sistema: createfile");
        System.out.println("\tParametros: " + parameters);

        // inicio da implementacao do aluno
        String[] comando = parameters.split(".txt");
        String[] caminho = comando[0].split("/");
        if (diretorioAtual.getNome().equals(raiz.getNome())) {
            result = "Você Não pode criar na raiz";
            return result;
        }

        if (comando.length < 2) {
            result = "É necessario passar um conteudo no arquivo";
            return result;
        }
        if (comando.length >= 1) {

            String conteudo = comando[1];
            System.out.println(conteudo);
            String nome = caminho[caminho.length - 1];

            System.out.println("ksksk" + conteudo);

            if (verificaNome(nome)) {
                nome = nome + ".txt";
            } else {
                result = "Erro no comando inserido";
                return result;
            }
            System.out.println(nome);
            for (int i = 0; i < diretorioAtual.getArquivos().size(); i++) {
                if (diretorioAtual.getArquivos().get(i).getNome().equals(nome)) {
                    result = "Já existe um arquivo com esse nome neste caminho";
                    return result;
                }
            }
            String[] cont = conteudo.split(" ");
            ArrayList<String> armazena = new ArrayList<>();
            for (int i = 0; i < cont.length; i++) {
                System.out.println(cont[i]);
                armazena.add(cont[i]);
            }
            Arquivo novoArquivo = new Arquivo(diretorioAtual);
            novoArquivo.setNome(nome);
            novoArquivo.setConteudo(armazena);
            diretorioAtual.addArquivo(novoArquivo);

            result = "Arquivo criado com sucesso";

        }
        // fim da implementacao do aluno

        return result;
    }

    public boolean verificaNome(String nome) {

        if (nome.contains(".")) {
            return false;
        } else if (nome.trim().equals("")) {
            return false;
        } else if ((Character) nome.charAt(0) == '-') {
            return false;
        }
        return true;
    }

    public String cat(String parameters) {
        // variavel result deverah conter o que vai ser impresso na tela apos comando do
        // usuário
        String result = "";
        System.out.println("Chamada de Sistema: cat");
        System.out.println("\tParametros: " + parameters);

        // inicio da implementacao do aluno
        String[] par = parameters.split("/");
        System.out.println("inicio" + par[0] + "fim");
        if (par.length == 1) {
            List<String> config = new ArrayList<>() ;
            for (int i = 0; i < diretorioAtual.getArquivos().size(); i++) {
                if (diretorioAtual.getArquivos().get(i).getNome().equals(parameters)) {
                    config = diretorioAtual.getArquivos().get(i).getConteudo();
                }
            }
            for (int i = 0; i < config.size(); i++) {
                result += config.get(i) + "\n";
                System.out.println("\n");

            }
        }
        if (par.length == 2) {
            List<String> config = FileManager.stringReader(par[1]);
            for (int i = 0; i < config.size(); i++) {
                result += config.get(i) + "\n";
                System.out.println("\n");
            }
        }
        if (par.length > 2) {
            diretorioCriacao = diretorioAtual;
            List<String> config = FileManager.stringReader(par[1]);
            for (int i = 1; i < par.length - 1; i++) {

                int au = 0;

                Diretorio auxiliar = diretorioCriacao;

                if (diretorioCriacao.getFilhos().isEmpty()) {
                    result = "Diretorio vazio";
                    return result;
                }
                for (int j = 0; j < diretorioCriacao.getFilhos().size(); j++) {
                    System.out.println("au:" + au + "auxiliar:" + auxiliar.getFilhos().size() + "par " + par[i]
                            + "array" + diretorioCriacao.getFilhos().get(j).getNome());
                    if (!(diretorioCriacao.getFilhos().get(j).getNome().equals(par[i]))) {
                        au += 1;
                    }
                    if (diretorioCriacao.getFilhos().get(j).getNome().equals(par[i])) {
                        diretorioCriacao = diretorioCriacao.getFilhos().get(j);
                    }
                }
                System.out.println(
                        "au:" + au + "auxiliar:" + auxiliar.getFilhos().size() + " criac" + diretorioCriacao.getNome());
                if (au == auxiliar.getFilhos().size()) {
                    result = "Diretorio inexisite";
                    return result;
                }
            }
            for (int i = 0; i < config.size(); i++) {
                result += config.get(i) + "\n";
                System.out.println("\n");
            }
        }

        // fim da implementacao do aluno
        return result;
    }

    public String batch(String parameters) {
        // variavel result deverah conter o que vai ser impresso na tela apos comando do
        // usuário
        String result = "";
        System.out.println("Chamada de Sistema: batch");
        System.out.println("\tParametros: " + parameters);

        // inicio da implementacao do aluno
        // fim da implementacao do aluno
        return result;
    }

    public String dump(String parameters) {
        // variavel result deverah conter o que vai ser impresso na tela apos comando do
        // usuário
        String result = "";
        System.out.println("Chamada de Sistema: dump");
        System.out.println("\tParametros: " + parameters);

        // inicio da implementacao do aluno
        // fim da implementacao do aluno
        return result;
    }

    public String info() {
        // variavel result deverah conter o que vai ser impresso na tela apos comando do
        // usuário
        String result = "";
        System.out.println("Chamada de Sistema: info");
        System.out.println("\tParametros: sem parametros");

        // nome do aluno
        String name = "";
        // numero de matricula
        String registration = "";
        // versao do sistema de arquivos
        String version = "0.5";

        result += "Nome do Aluno:        " + name;
        result += "\nMatricula do Aluno:   " + registration;
        result += "\nVersao do Kernel:     " + version;

        return result;
    }

}
