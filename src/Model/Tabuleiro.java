package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Tabuleiro {
    private final int[][] tabuleiro_P1 = new int[15][15];
    private final int[][] tabuleiro_P2 = new int[15][15];
    private final List<Observer> observers = new ArrayList<>();

    public void criacaoTabuleiros() {
        for (int[] row : tabuleiro_P1) {
            Arrays.fill(row, 0);
        }
        for (int[] row : tabuleiro_P2) {
            Arrays.fill(row, 0);
        }
        notifyObservers(); // Notifica os observadores após a criação dos tabuleiros
    }

    public void salvarTabuleiroJogador1(int[][] vetor) {
        for (int i = 0; i < 15; i++) {
            System.arraycopy(vetor[i], 0, tabuleiro_P1[i], 0, 15);
        }
    }

    public void salvarTabuleiroJogador2(int[][] vetor) {
        for (int i = 0; i < 15; i++) {
            System.arraycopy(vetor[i], 0, tabuleiro_P2[i], 0, 15);
        }
    }

    public void imprimeTabuleiroP1() {
        System.out.println("\tTabuleiro P1");
        imprimirTabuleiro(tabuleiro_P1);
    }

    public void imprimeTabuleiroP2() {
        System.out.println("\tTabuleiro P2");
        imprimirTabuleiro(tabuleiro_P2);
    }

    private void imprimirTabuleiro(int[][] tabuleiro) {
        for (int i = 0; i < tabuleiro.length; i++) {
            System.out.printf("%c ", 'A' + i);
            for (int cell : tabuleiro[i]) {
                System.out.printf("%d ", cell);
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean insereEmbarcacao(Embarcacao embarcacao, char linha, int coluna, char jogador) {
        int[][] tabuleiro = (jogador == '1') ? tabuleiro_P1 : tabuleiro_P2;
        boolean sucesso = embarcacao.posicionar(linha, coluna, tabuleiro, embarcacao.tamanho); // Usar tamanho como código
        if (sucesso) {
            notifyObservers(); // Notifica os observadores após a inserção da embarcação
        }
        return sucesso;
    }

    public String atacar(char linha, int coluna, char jogador) {
        int[][] tabuleiroAlvo = (jogador == '1') ? tabuleiro_P2 : tabuleiro_P1;
        int indice_linha = linha - 'A';

        if (tabuleiroAlvo[indice_linha][coluna] > 0) {
            tabuleiroAlvo[indice_linha][coluna] = -1; // Marca como atingido
            notifyObservers(); // Notifica os observadores após o ataque
            if (verificarDerrota(jogador)) {
                return "Hit! Player " + jogador + " wins!";
            }
            return "Hit!";
        } else if (tabuleiroAlvo[indice_linha][coluna] == 0) {
            tabuleiroAlvo[indice_linha][coluna] = -2; // Marca como tiro na água
            notifyObservers(); // Notifica os observadores após o ataque
            return "Miss!";
        }
        return "Already Hit!";
    }

    public boolean verificarDerrota(char jogador) {
        int[][] tabuleiro = (jogador == '1') ? tabuleiro_P2 : tabuleiro_P1;
        for (int[] row : tabuleiro) {
            for (int cell : row) {
                if (cell > 0) {
                    return false; // Ainda existem partes de navios não atingidos
                }
            }
        }
        return true; // Todos os navios foram destruídos
    }

    public boolean jogadorPerdeu(char jogador) {
        return verificarDerrota(jogador);
    }

    public void salvarEstado(String arquivo) {
        try (PrintWriter out = new PrintWriter(new FileWriter(arquivo))) {
            for (int[] row : tabuleiro_P1) {
                for (int cell : row) {
                    out.print(cell + " ");
                }
                out.println();
            }
            out.println(); // Separar os tabuleiros
            for (int[] row : tabuleiro_P2) {
                for (int cell : row) {
                    out.print(cell + " ");
                }
                out.println();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar o estado do jogo: " + e.getMessage());
        }
    }

    public void carregarEstado(String arquivo) {
        try (Scanner scanner = new Scanner(new File(arquivo))) {
            for (int i = 0; i < tabuleiro_P1.length; i++) {
                for (int j = 0; j < tabuleiro_P1[i].length; j++) {
                    if (scanner.hasNextInt()) {
                        tabuleiro_P1[i][j] = scanner.nextInt();
                    }
                }
            }
            for (int i = 0; i < tabuleiro_P2.length; i++) {
                for (int j = 0; j < tabuleiro_P2[i].length; j++) {
                    if (scanner.hasNextInt()) {
                        tabuleiro_P2[i][j] = scanner.nextInt();
                    }
                }
            }
            notifyObservers(); // Notifica os observadores após carregar o estado
        } catch (FileNotFoundException e) {
            System.err.println("Erro ao carregar o estado do jogo: Arquivo não encontrado.");
        }
    }

    public int[][] getEstado(char jogador) {
        return (jogador == '1') ? tabuleiro_P1 : tabuleiro_P2;
    }

    public int getPosicao(char linha, int coluna, char jogador) {
        int[][] tabuleiro = (jogador == '1') ? tabuleiro_P1 : tabuleiro_P2;
        int indice_linha = linha - 'A';
        return tabuleiro[indice_linha][coluna];
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
