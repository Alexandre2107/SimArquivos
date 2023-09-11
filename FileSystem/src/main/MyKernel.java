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
        String result = "";
        System.out.println("Chamada de Sistema: ls");
        System.out.println("\tParametros: " + parameters);
        String[] par = parameters.split(" ");

        Diretorio dir = diretorioAtual;

        if (par.length == 0) {
            encontrarDiretorio(par[0], dir);
            result = listarConteudoDiretorio(dir);
        } else if (par.length == 1) {
            if (par[0].equals("-l")) {
                result = listarConteudoDetalhadoDiretorio(dir);
            } else {
                dir = encontrarDiretorio(par[0], dir);
                if (dir != null) {
                    result = listarConteudoDiretorio(dir);
                } else {
                    result = par[0] + ": Diretório não existe.";
                }
            }
        } else if (par.length == 2 && par[0].equals("-l")) {
            dir = encontrarDiretorio(par[1], dir);
            if (dir != null) {
                result = listarConteudoDetalhadoDiretorio(dir);
            } else {
                result = par[1] + ": Diretório não existe.";
            }
        } else {
            result = "Número de parâmetros inválido. Uso correto: ls [-l] [caminho]";
        }

        return result;
    }

    private String listarConteudoDiretorio(Diretorio diretorio) {
        String conteudo = "";
        for (Diretorio filho : diretorio.getFilhos()) {
            conteudo += " " + filho.getNome();
        }
        for (Arquivo arquivo : diretorio.getArquivos()) {
            conteudo += " " + arquivo.getNome();
        }
        return conteudo;
    }

    private String listarConteudoDetalhadoDiretorio(Diretorio diretorio) {
        String conteudo = "";
        for (Diretorio filho : diretorio.getFilhos()) {
            conteudo += " " + filho.getPermissao() + " " + filho.getDataCriacaoFormatada() + " " + filho.getNome()
                    + "\n";
        }
        for (Arquivo arquivo : diretorio.getArquivos()) {
            conteudo += " " + arquivo.getPermissao() + " " + arquivo.getDataCriacao() + " " + arquivo.getNome() + "\n";
        }
        return conteudo;
    }

    private Diretorio encontrarDiretorio(String caminho, Diretorio dir) {
        String[] partes = caminho.split("/");
        // Loop pelas partes do caminho
        for (String parte : partes) {
            if (parte.isEmpty()) {
                continue;
            }
            dir = dir.buscaDiretorioPeloNome(parte);

            if (dir == null) {
                return null;
            }
        }

        return dir;
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
            System.out.println(diretorioAtual.getNome());
            for (String part : pathParts) {
                Diretorio aux = diretorioAtual.buscaDiretorioPeloNome(part);
                if (part.equals(".")) {
                } else if (part.equals("..")) {
                    if (diretorioAtual.getPai() != null) {
                        diretorioAtual = diretorioAtual.getPai();
                    } else {
                        result = "Você já está na raiz";
                        return result;
                    }
                }
                if (aux == null) {
                    result = parameters + ": Diretório não existe.";
                    return result;
                } else {
                    diretorioAtual = aux;
                }

                // } else {
                // Diretorio proximoDiretorio = diretorioAtual.buscaDiretorioPeloNome(part);
                // if (proximoDiretorio != null) {
                // diretorioAtual = proximoDiretorio;
                // } else {
                // result = parameters + ": Diretório não existe.";
                // return result;
                // }
                // }
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

        Diretorio atual = this.diretorioAtual;

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
        String result = "";
        System.out.println("Chamada de Sistema: rmdir");
        System.out.println("\tParametros: " + parameters);

        // Quebre o caminho em partes usando "/"
        String[] parts = parameters.split("/");

        // Comece na raiz
        Diretorio currentDir = raiz;

        // Percorra as partes do caminho
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];

            // Verifique se a parte atual está vazia
            if (currentDir.getFilhos().isEmpty()) {
                result = "Diretório vazio";
                return result;
            }

            // Encontre o filho com o nome da parte atual
            Diretorio childDir = null;
            for (Diretorio dir : currentDir.getFilhos()) {
                if (dir.getNome().equals(part)) {
                    childDir = dir;
                    break;
                }
            }
            // Se não encontrou o filho, o diretório não existe
            if (childDir == null) {
                result = "Diretório não existe";
                return result;
            }

            // Se chegou à última parte do caminho, remova o diretório se estiver vazio
            if (i == parts.length - 1) {
                if (!childDir.getFilhos().isEmpty() || !childDir.getArquivos().isEmpty()) {
                    result = "Diretório não está vazio";
                    return result;
                }

                currentDir.removeFilho(childDir);
            }

            currentDir = childDir; // Vá para o próximo diretório
        }

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
        String caminhoAb = "";

        for (int i = 1; i < caminho.length - 1; i++) {
            caminhoAb += "/" + caminho[i];
        }
        System.out.println("Comando" + caminhoAb);

        Diretorio novo = encontraDiretorioPeloCaminhoAbsolutoAlternativo(caminhoAb);
        if (novo != null) {
            if (comando.length < 2) {
                result = "É necessario passar um conteudo no arquivo";
                return result;
            }
            if (comando.length >= 1) {

                String conteudo = comando[1];
                System.out.println(conteudo);
                String nome = caminho[caminho.length - 1];

                if (verificaNome(nome)) {
                    nome = nome + ".txt";
                } else {
                    result = "Erro no comando inserido";
                    return result;
                }
                for (int i = 0; i < novo.getArquivos().size(); i++) {
                    if (novo.getArquivos().get(i).getNome().equals(nome)) {
                        result = "Já existe um arquivo com esse nome neste caminho";
                        return result;
                    }
                }
                String[] cont = conteudo.split("\\\\n");
                ArrayList<String> armazena = new ArrayList<>();
                for (int i = 0; i < cont.length; i++) {
                    System.out.println(cont[i]);
                    armazena.add(cont[i]);
                }
                System.out.println("Atual" + novo.getNome());
                Arquivo novoArquivo = new Arquivo(novo);
                novoArquivo.setNome(nome);
                novoArquivo.setConteudo(armazena);
                novo.addArquivo(novoArquivo);

                result = "Arquivo criado com sucesso";

            }
        } else {
            result = "Diretorio nao existe";
        }

        // fim da implementacao do aluno

        return result;
    }

    private Diretorio encontraDiretorioPeloCaminhoAbsolutoAlternativo(String path) {
        String[] dirs = path.split("/");
        Diretorio atual = this.diretorioAtual;

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
        String result = "";
        System.out.println("Chamada de Sistema: cat");
        System.out.println("\tParametros: " + parameters);

        // Início da implementação do aluno
        String[] parts = parameters.split("/");

        // Verifique se o parâmetro foi passado corretamente

        // Comece na raiz
        Diretorio currentDir = raiz;

        // Percorra as partes do caminho, exceto a última que é o nome do arquivo
        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];

            // Encontre o filho com o nome da parte atual
            Diretorio childDir = null;
            for (Diretorio dir : currentDir.getFilhos()) {
                if (dir.getNome().equals(part)) {
                    childDir = dir;
                    break;
                }
            }

            // Se não encontrou o filho, o diretório não existe
            if (childDir == null) {
                result = "cat: Diretório não encontrado: " + parameters;
                return result;
            }

            currentDir = childDir; // Vá para o próximo diretório
        }

        // Agora, currentDir é o diretório que deve conter o arquivo
        String fileName = parts[parts.length - 1];

        // Procure o arquivo no diretório atual
        Arquivo arquivo = null;
        for (Arquivo file : currentDir.getArquivos()) {
            if (file.getNome().equals(fileName)) {
                arquivo = file;
                break;
            }
        }

        // Se não encontrou o arquivo, retorne um erro
        if (arquivo == null) {
            result = "cat: Arquivo não encontrado: " + parameters;
            return result;
        }

        // Leitura e concatenação do conteúdo do arquivo
        List<String> conteudo = arquivo.getConteudo();
        for (String linha : conteudo) {
            result += linha + "\n";
        }

        // Fim da implementação do aluno
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
