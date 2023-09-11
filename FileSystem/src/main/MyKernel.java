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
        // variável result deverá conter o que vai ser impresso na tela após o comando
        // do usuário
        String result = "";
        System.out.println("Chamada de Sistema: ls");
        System.out.println("\tParametros: " + parameters);

        // Tratamento da string de parâmetros
        String[] par = parameters.split(" ");

        // Verifica se a opção -l está presente nos parâmetros
        boolean listarDetalhes = false;
        String diretorioParaListar = "";

        if (par.length > 0) {
            if (par[0].equals("-l")) {
                listarDetalhes = true;
            } else {
                diretorioParaListar = par[0];
            }
        }

        // Lista o conteúdo do diretório atual ou do diretório especificado
        Diretorio diretorioLista = diretorioAtual;

        if (!diretorioParaListar.isEmpty()) {
            Diretorio novoDiretorio = diretorioAtual.buscaDiretorioPeloNome(diretorioParaListar);
            if (novoDiretorio != null) {
                diretorioLista = novoDiretorio;
            } else {
                result = diretorioParaListar + ": Diretório não existe.";
                return result;
            }
        }

        if (listarDetalhes) {
            for (Diretorio filho : diretorioLista.getFilhos()) {
                if (!filho.getNome().isEmpty()) {
                    result += filho.getPermissao() + " " + filho.getDataCriacaoFormatada() + " " + filho.getNome()
                            + "\n";
                }
            }
            for (Arquivo arquivo : diretorioLista.getArquivos()) {
                if (!arquivo.getNome().isEmpty()) {
                    result += arquivo.getPermissao() + " " + arquivo.getDataCriacao() + " " + arquivo.getNome() + "\n";
                }
            }
        } else {
            for (Diretorio filho : diretorioLista.getFilhos()) {
                if (!filho.getNome().isEmpty()) {
                    result += filho.getNome() + " ";
                }
            }
            for (Arquivo arquivo : diretorioLista.getArquivos()) {
                if (!arquivo.getNome().isEmpty()) {
                    result += arquivo.getNome() + " ";
                }
            }
        }

        return result;
    }

    public String mkdir(String parameters) {
        String result = "";
        System.out.println("Chamada de Sistema: mkdir");
        System.out.println("\tParametros: " + parameters);

        // Tratamento da string de parâmetros
        String[] par = parameters.split("/");

        // Diretório inicial
        Diretorio diretorioAtual = this.diretorioAtual;

        for (int i = 0; i < par.length; i++) {
            String dirName = par[i];
            if (diretorioAtual.buscaDiretorioPeloNome(dirName) != null) {
                result = "mkdir: " + dirName + ": Diretório já existe (Nenhum diretório foi criado).";
                break;
            }
            Diretorio novoDiretorio = new Diretorio(diretorioAtual);
            novoDiretorio.setNome(dirName);
            diretorioAtual.addFilho(novoDiretorio);
            diretorioAtual = novoDiretorio;
        }
        return result;
    }

    public String cd(String parameters) {
        String path = "";
        ArrayList<String> caminho = new ArrayList<>();

        // Variável result conterá a saída a ser impressa na tela após o comando do
        // usuário
        String result = "";
        System.out.println("Chamada de Sistema: cd");
        System.out.println("\tParametros: " + parameters);

        // Início da implementação do aluno

        if (parameters.startsWith("/")) {
            Diretorio novoDiretorio = encontraDiretorioPeloCaminhoAbsoluto(parameters);
            if (novoDiretorio != null) {
                diretorioAtual = novoDiretorio;
            } else {
                result = parameters + ": Diretório não existe.";
                return result;
            }
        } else {
            String[] pathParts = parameters.split("/");
            for (String part : pathParts) {
                if (part.equals(".")) {
                } else if (part.equals("..")) {
                    if (diretorioAtual.getPai() != null) {
                        diretorioAtual = diretorioAtual.getPai();
                    } else {
                        result = "Você já está na raiz";
                        return result;
                    }
                } else {
                    Diretorio proximoDiretorio = diretorioAtual.buscaDiretorioPeloNome(part);
                    if (proximoDiretorio != null) {
                        diretorioAtual = proximoDiretorio;
                    } else {
                        result = parameters + ": Diretório não existe.";
                        return result;
                    }
                }
            }
        }

        Diretorio aux = diretorioAtual;
        while (!aux.getNome().equals("/")) {
            caminho.add(aux.getNome());
            aux = aux.getPai();
        }

        for (int i = caminho.size() - 1; i >= 0; i--) {
            path += "/" + caminho.get(i);
        }

        // fazer o terminal printar tudo

        // Setando parte gráfica do diretório atual
        operatingSystem.fileSystem.FileSytemSimulator.currentDir = path + "/";
        // Fim da implementação do aluno
        return result;
    }

    private Diretorio encontraDiretorioPeloCaminhoAbsoluto(String path) {
        String[] dirs = path.split("/");
        Diretorio atual = raiz;

        for (int i = 1; i < dirs.length; i++) {
            String dir = dirs[i];
            Diretorio novoDiretorio = atual.buscaDiretorioPeloNome(dir);
            if (novoDiretorio != null) {
                atual = novoDiretorio;
            } else {
                return null;
            }
        }

        return atual;
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

            List<String> config = diretorioAtual.getArquivos().get(0).getConteudo();
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
