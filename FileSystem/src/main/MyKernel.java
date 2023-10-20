package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private Diretorio diretorioAtual;
    public String aux_cd;
    ArrayList<Arquivo> a = new ArrayList<Arquivo>();

    public MyKernel() {
        raiz = new Diretorio(null);
        raiz.setNome("/");
        diretorioAtual = raiz;

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
        if (parameters.startsWith("./")) {
            for (int i = 1; i < par.length; i++) {
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
        } else if (parameters.startsWith("/")) {
            for (int i = 1; i < par.length; i++) {
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

        } else {
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
                if (part.equals("..")) {
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
        ArrayList<String> dirs = new ArrayList<String>(Arrays.asList(path.split("/")));
        dirs.removeIf(dir -> dir.isEmpty());
        if (path.endsWith(".txt")) {
            dirs.remove(dirs.size() - 1);
        }
        Diretorio currentDir = (path.startsWith("/")) ? raiz : diretorioAtual;

        

        for (int i = 0; i < dirs.size(); i++) {
            String dir = dirs.get(i);
            currentDir = currentDir.buscaDiretorioPeloNome(dir);
            if (currentDir == null) {
                return null;
            }
        }

        return currentDir;
    }

    public String rmdir(String parameters) {
        String result = "";
        System.out.println("Chamada de Sistema: rmdir");
        System.out.println("\tParametros: " + parameters);

        // Quebre o caminho em partes usando "/"
        String[] parts = parameters.split("/");
        Diretorio currentDir = raiz;

        // Percorra as partes do caminho
        if (parameters.startsWith("/")) {
            for (int i = 1; i < parts.length; i++) {
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
        } else if (parameters.startsWith("./")) {
            for (int i = 1; i < parts.length; i++) {
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
        } else {
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
        }

        return result;
    }

    public String cp(String parameters) {
        String result = "";
        System.out.println("Chamada de Sistema: cp");
        System.out.println("\tParametros: " + parameters);

        String[] parts = parameters.split(" ");
        int numParams = parts.length;

        if (numParams == 3) {
            boolean recursive = false;
            String sourcePath = parts[1];
            String destPath = parts[2];

            if (parts[0].equals("-R")) {
                recursive = true;
            } else {
                result = "Erro: parâmetros inválidos";
                return result;
            }

            if (recursive) {
                Diretorio sourceDir = encontraDiretorioPeloCaminhoAbsoluto(sourcePath);
                if (sourceDir != null) {
                    if (sourceDir.getArquivos().isEmpty() && sourceDir.getFilhos().isEmpty()) {
                        // O diretório de origem está vazio
                        result = "cp: " + sourcePath + ": Diretório de origem está vazio. (Nada foi copiado)";
                    } else {
                        // Encontre ou crie o diretório de destino
                        Diretorio destDir = encontrarOuCriarDiretorio(destPath);
                        // Copie o conteúdo recursivamente
                        copiarDiretorioRecursivamente(sourceDir, destDir);
                    }
                } else {
                    result = "cp: " + sourcePath + ": Diretório de origem não existe. (Nada foi copiado)";
                }
            }
        } else if (numParams == 2) {
            String sourcePath = parts[0];
            String destPath = parts[1];

            Arquivo sourceFile = encontrarArquivo(sourcePath);
            if (sourceFile != null) {
                // Encontre ou crie o arquivo de destino
                Arquivo destFile = encontrarOuCriarArquivo(destPath);
                // Copie o conteúdo do arquivo
                destFile.setConteudo(sourceFile.getConteudo());
            } else {
                result = "cp: " + sourcePath + ": Arquivo de origem não existe. (Nada foi copiado)";
            }
        } else {
            result = "Erro: número incorreto de parâmetros";
        }

        return result;
    }

    private Diretorio encontrarOuCriarDiretorio(String caminho) {
        String[] partes = caminho.split("/");
        Diretorio diretorioAtual = this.diretorioAtual;

        for (String parte : partes) {
            if (parte.isEmpty()) {
                // Pular partes vazias, por exemplo, quando há barras duplas no caminho
                continue;
            }

            // Verifique se o diretório já existe
            Diretorio proximoDir = diretorioAtual.getDiretorioFilhoPorNome(parte);

            if (proximoDir == null) {
                // O diretório não existe, então crie-o
                proximoDir = new Diretorio(diretorioAtual);
                proximoDir.setNome(parte);
                diretorioAtual.addFilho(proximoDir);
            }

            diretorioAtual = proximoDir;
        }

        return diretorioAtual;
    }

    private void copiarDiretorioRecursivamente(Diretorio sourceDir, Diretorio destDir) {
        for (Diretorio filho : sourceDir.getFilhos()) {
            // Para cada filho do diretório de origem, crie um diretório correspondente no
            // diretório de destino
            Diretorio novoDestDir = new Diretorio(destDir);
            novoDestDir.setNome(filho.getNome());
            destDir.addFilho(novoDestDir);

            // Recursivamente copie o conteúdo desse filho
            copiarDiretorioRecursivamente(filho, novoDestDir);
        }

        for (Arquivo arquivo : sourceDir.getArquivos()) {
            // Para cada arquivo no diretório de origem, crie um arquivo correspondente no
            // diretório de destino
            Arquivo novoArquivo = new Arquivo(destDir);
            novoArquivo.setNome(arquivo.getNome());
            destDir.addArquivo(novoArquivo);
        }
    }

    private Arquivo encontrarOuCriarArquivo(String caminho) {
        String[] partes = caminho.split("/");
        String nomeArquivo = partes[partes.length - 1];
        String caminhoDiretorio = caminho.substring(0, caminho.length() - nomeArquivo.length());
        Diretorio diretorioPai = encontrarOuCriarDiretorio(caminhoDiretorio);
        Arquivo arquivo = diretorioPai.getArquivoPorNome(nomeArquivo);

        if (arquivo == null) {
            // O arquivo não existe, então crie-o
            arquivo = new Arquivo(diretorioPai);
            arquivo.setNome(nomeArquivo);
            diretorioPai.addArquivo(arquivo);
        }

        return arquivo;
    }

    private Arquivo encontrarArquivo(String caminho) {

        Diretorio currentDir = (caminho.startsWith("/")) ? raiz : diretorioAtual;
        String[] partes = caminho.split("/");

        // Itere através das partes do caminho
        for (int i = 0; i < partes.length - 1; i++) {
            String parte = partes[i];

            if (parte.isEmpty()) {
                // Ignorar partes vazias, como resultado de caminhos como "//"
                continue;
            }

            // Verifique se o diretório atual tem uma entrada para a parte atual do caminho
            currentDir = currentDir.buscaDiretorioPeloNome(parte);

            if (currentDir == null) {
                // Se o arquivo não foi encontrado, retorne null
                return null;
            }

            // Atualize o diretório atual para a próxima parte

        }
        Arquivo arquivo = currentDir.getArquivoPorNome(partes[partes.length - 1]);
        // Retorna o arquivo encontrado no caminho
        return arquivo;
    }

   public String mv(String parameter) {
        String[] parameters = parameter.split(" ");

        boolean sourceIsRoot = parameters[0].startsWith("/");
        boolean destinationIsRoot = parameters[1].startsWith("/");

        ArrayList<String> source = new ArrayList<String>(Arrays.asList(parameters[0].split("/")));
        source.removeIf(e -> e.isEmpty());
        ArrayList<String> destination = new ArrayList<String>(Arrays.asList(parameters[1].split("/")));
        destination.removeIf(e -> e.isEmpty());

        boolean sourceFile = (parameters[0].endsWith(".txt")) ? source.get(source.size() - 1).endsWith(".txt") : false;
        boolean destinationFile = (parameters[1].endsWith(".txt")) ? destination.get(destination.size() - 1).endsWith(".txt") : false;
        boolean rename = false;

        String oldName = source.remove(source.size() - 1);

        

        Diretorio dirSource = (sourceIsRoot) ? raiz : diretorioAtual;
        Diretorio dirDestination = (destinationIsRoot) ? raiz : diretorioAtual;

        if (!sourceFile) {
            for (String name : source) {
                dirSource = dirSource.buscaDiretorioPeloNome(name);
                if (dirSource == null)
                    return "Diretório não existe";
            }
            for (int i = 0; i < destination.size(); i++) {
                if (destination.size() > 1 || destination.get(i).equals("..") || destination.get(i).equals(".")
                        || destinationIsRoot) {
                    dirDestination = dirDestination.buscaDiretorioPeloNome(destination.get(i));
                    if (i == destination.size() - 2
                            && (dirDestination.buscaDiretorioPeloNome(destination.get(i + 1)) == null)) {
                        rename = dirSource == dirDestination;
                        break;
                    } else if (dirDestination == null) {
                        return "Diretório de destino não existe";
                    }
                } else {
                    Diretorio aux = dirDestination.buscaDiretorioPeloNome(destination.get(i));
                    if (aux != null) {
                        dirDestination = aux;
                    } else {
                        rename = true;
                    }
                }
            }

        }

        String newName = (destinationFile || rename) ? destination.remove(destination.size() - 1) : "";

        if (sourceFile) {
            for (String name : source) {
                dirSource = dirSource.buscaDiretorioPeloNome(name);
                if (dirSource == null)
                    return "Diretório não existe";
            }

            for (String name : destination) {
                dirDestination = dirDestination.buscaDiretorioPeloNome(name);
                if (dirDestination == null)
                    return "Diretório de destino não existe";
            }

            Arquivo aux = dirSource.getArquivoPorNome(oldName);
            if (aux == null)
                return "Arquivo não existe!";

            ArrayList<Arquivo> sourceFiles = dirSource.getArquivos();
            sourceFiles.remove(sourceFiles.indexOf(aux));

            if ((newName.equals("") && dirDestination.getArquivoPorNome(oldName) != null)
                    || !newName.equals("") && dirDestination.getArquivoPorNome(newName) != null) {
                dirSource.addArquivo(aux);
                return "Já existe um arquivo com o mesmo nome na pasta de destino";
            }

            if (!newName.isEmpty())
                aux.setNome(newName);

            dirDestination.addArquivo(aux);
        } else {
            Diretorio aux = dirSource.buscaDiretorioPeloNome(oldName);
            if (aux == null)
                return "Diretório não existe";
            ArrayList<Diretorio> sourceDirectory = dirSource.getFilhos();
            sourceDirectory.remove(sourceDirectory.indexOf(aux));

            if ((newName.equals("") && dirDestination.buscaDiretorioPeloNome(oldName) != null)
                    || !newName.equals("") && dirDestination.buscaDiretorioPeloNome(newName) != null) {
                dirSource.addFilho(dirDestination);
                return "Já existe uma pasta com o mesmo nome na pasta de destino";
            }

            if (rename)
                aux.setNome(newName);
            dirDestination.addFilho(aux);
        }
        return "";
    }

    public String rm(String parameters) { // precisa corrigir alguns erros de caminhos absolutos
        String result = "";
        System.out.println("Chamada de Sistema: rm");
        System.out.println("\tParametros: " + parameters);

        // inicio da implementacao do aluno
        Diretorio currentDir = this.diretorioAtual;
        String[] parts = parameters.split(" ");
        boolean recursive = false;
        String path = "";

        if (parts.length == 1) {
            path = parts[0];
        } else if (parts.length == 2 && parts[0].equals("-R")) {
            recursive = true;
            path = parts[1];
        } else {
            result = "Erro: parâmetros inválidos";
            return result;
        }

        String[] pathParts = path.split("/");

        if (recursive) {
            if (path.startsWith("/")) {
            currentDir = raiz;
            for (int i = 1; i < pathParts.length; i++) {
                String part = pathParts[i];

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
                if (i == pathParts.length - 1) {
                    if (!childDir.getFilhos().isEmpty() || !childDir.getArquivos().isEmpty()) {
                        result = "Diretório não está vazio";
                        return result;
                    }

                    currentDir.removeFilho(childDir);
                }

                currentDir = childDir; // Vá para o próximo diretório
            }
        } else if (path.startsWith("./")) {
            for (int i = 1; i < pathParts.length; i++) {
                String part = pathParts[i];

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
                if (i == pathParts.length - 1) {
                    if (!childDir.getFilhos().isEmpty() || !childDir.getArquivos().isEmpty()) {
                        result = "Diretório não está vazio";
                        return result;
                    }

                    currentDir.removeFilho(childDir);
                }

                currentDir = childDir; // Vá para o próximo diretório
            }
        } else {
            for (int i = 0; i < pathParts.length; i++) {
                String part = pathParts[i];

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
                if (i == pathParts.length - 1) {
                    if (!childDir.getFilhos().isEmpty() || !childDir.getArquivos().isEmpty()) {
                        result = "Diretório não está vazio";
                        return result;
                    }

                    currentDir.removeFilho(childDir);
                }

                currentDir = childDir; // Vá para o próximo diretório
            }
        }
        } else {
            Diretorio diretorioAtual = encontraDiretorioPeloCaminhoAbsoluto(path);
            Arquivo arquivoRemover = diretorioAtual.getArquivoPorNome(pathParts[pathParts.length - 1]);

            if (arquivoRemover != null) {
                // Remove o arquivo do diretório atual
                diretorioAtual.removeArquivo(arquivoRemover);
            } else {
                result = "rm: " + path + ": Arquivo não existe. (Nada foi removido)";
            }
        }
        return result;
    }

    public String chmod(String parameters) {
        // Variável result conterá o que será impresso na tela após o comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: chmod");
        System.out.println("\tParametros: " + parameters);

        // Divide os parâmetros com base nos espaços
        String[] params = parameters.split(" ");
        String newPermission;

        // Verifica a opção de recursividade
        boolean recursive = false;
        int startIndex = 0;
        if (params[0].equals("-R")) {
            recursive = true;
            startIndex = 1;
        } 

        int permissions = Integer.parseInt(params[startIndex]);
        String octal = Integer.toString(permissions);
        String path = params[startIndex + 1];

        if (recursive) {
            Diretorio targetDir = encontraDiretorioPeloCaminhoAbsoluto(path);
            newPermission = converteCHMOD(octal.split(""));
            if (targetDir == null) {
                result = "Erro: diretório não encontrado";
            } else {
                aplicarPermissoesRecursivamente(targetDir, newPermission);
                result = "Permissões alteradas com sucesso";
            }
        } else {
            Arquivo targetFile = encontrarArquivo(path);
            newPermission = converteCHMOD(octal.split(""));
            if (targetFile != null) {
                targetFile.setPermissao(newPermission);
                result = "Permissões do arquivo alteradas com sucesso";
            } else {
                Diretorio targetDir = encontraDiretorioPeloCaminhoAbsoluto(path);
                newPermission = converteCHMOD(octal.split(""));

                if (targetDir != null) {
                    targetDir.setPermissao(newPermission);
                    result = "Permissões do diretório alteradas com sucesso";
                } else {
                    result = "Erro: arquivo ou diretório não encontrado";
                }
            }
        }
        return result;
    }

    public String converteCHMOD(String[] chmod) {
        String permissao = "";
        if (chmod.length == 3) {
            for (String position : chmod) {
                if (position.equals("0")) {
                    permissao = permissao + "---";
                } else if (position.equals("1")) {
                    permissao = permissao + "--x";
                } else if (position.equals("2")) {
                    permissao = permissao + "-w-";
                } else if (position.equals("3")) {
                    permissao = permissao + "-wx";
                } else if (position.equals("4")) {
                    permissao = permissao + "r--";
                } else if (position.equals("5")) {
                    permissao = permissao + "r-x";
                } else if (position.equals("6")) {
                    permissao = permissao + "rw-";
                } else if (position.equals("7")) {
                    permissao = permissao + "rwx";
                }
            }

        }

        return permissao;
    }

    private void aplicarPermissoesRecursivamente(Diretorio directory, String permissions) {

        directory.setPermissao(permissions);

        for (Arquivo file : directory.getArquivos()) {
            file.setPermissao(permissions);
        }

        for (Diretorio subDir : directory.getFilhos()) {
            aplicarPermissoesRecursivamente(subDir, permissions);
        }
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

        Diretorio novo = encontraDiretorioPeloCaminhoAbsoluto(caminhoAb);
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
        Diretorio currentDir = this.diretorioAtual;

        // Percorra as partes do caminho, exceto a última que é o nome do arquivo
        if (parameters.startsWith("/")) {
            currentDir = raiz;
            for (int i = 1; i < parts.length - 1; i++) {
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
        } else if (parameters.startsWith("./")) {
            for (int i = 1; i < parts.length - 1; i++) {
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
        } else if (parts.length > 1) {
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

        }

        // Agora, currentDir é o diretório que deve conter o arquivo
        String fileName = parts[parts.length - 1];

        // Procure o arquivo no diretório atual
        Arquivo arquivo = currentDir.getArquivoPorNome(fileName);
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
        int i;
        String comando, parametros, caminho;
        caminho = parameters;

        List<String> config = FileManager.stringReader(caminho);
        for (i = 0; i < config.size(); i++) {
            comando = config.get(i).substring(0, config.get(i).indexOf(" "));
            parametros = config.get(i).substring(config.get(i).indexOf(" ") + 1);
            if (comando.equals("ls")) {
                ls(parametros);
                result = "Comandos Executados.";
            } else if (comando.equals("mkdir")) {
                mkdir(parametros);
                result = "Comandos Executados.";
            } else if (comando.equals("cd")) {
                cd(parametros);
                result = "Comandos Executados.";
            } else if (comando.equals("rmdir")) {
                rmdir(parametros);
                result = "Comandos Executados.";
            } else if (comando.equals("cp")) {
                cp(parametros);
                result = "Comandos Executados.";
            } else if (comando.equals("mv")) {
                mv(parametros);
                result = "Comandos Executados.";
            } else if (comando.equals("rm")) {
                rm(parametros);
                result = "Comandos Executados.";
            } else if (comando.equals("chmod")) {
                chmod(parametros);
                result = "Comandos Executados.";
            } else if (comando.equals("createfile")) {
                createfile(parametros);
                result = "Comandos Executados.";
            } else if (comando.equals("cat")) {
                cat(parametros);
                result = "Comandos Executados.";
            } else if (comando.equals("batch")) {
                batch(parametros);
                result = "Comandos Executados.";
            } else if (comando.equals("dump")) {
                dump(parametros);
                result = "Comandos Executados.";
            } else {
                result = "Arquivo não existe.";
            }
        }
        //fim da implementacao do aluno
        return result;
    }

    public String dump(String parameters) {
        // variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: dump");
        System.out.println("\tParametros: " + parameters);

        // inicio da implementacao do aluno
        String outputPath = parameters;

        try(BufferedWriter write = new BufferedWriter(new FileWriter(outputPath))) {
            String script = generateDumpScript(raiz);
            write.write(script);
            result = "Arquivo de dump criado com sucesso";
        } catch (IOException e) {
            result = "Erro: não foi possível escrever no arquivo";
            return result;
        }
        // fim da implementacao do aluno
        return result;
    }

    private String generateDumpScript(Diretorio dir){
        String script = "";
        script += "mkdir " + dir.getCaminhoAbsoluto() + "\n";
        script += "chmod 777 " + dir.getCaminhoAbsoluto() + "\n";

        for (Diretorio subDir : dir.getFilhos()) {
            script += generateDumpScript(subDir);
        }

        for (Arquivo file : dir.getArquivos()) {
            script += "createfile " + file.getCaminhoAbsoluto() + " " + file.getConteudo() + "\n";
            script += "chmod 777 " + file.getCaminhoAbsoluto() + "\n";
        }

        return script;
    }

    public String info() {

        String result = "";
        System.out.println("Chamada de Sistema: info");
        System.out.println("\tParametros: sem parametros");

        String name = "";
        String registration = "";
        String version = "0.5";

        result += "Nome do Aluno:        " + name;
        result += "\nMatricula do Aluno:   " + registration;
        result += "\nVersao do Kernel:     " + version;

        return result;
    }

}
