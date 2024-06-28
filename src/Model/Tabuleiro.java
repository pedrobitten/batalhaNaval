package Model;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Tabuleiro {
    private final int[][] tabuleiro_P1 = new int[15][15];
    private final int[][] tabuleiro_P2 = new int[15][15];

    public void criacaoTabuleiros() {
        for (int[] row : tabuleiro_P1) {
            Arrays.fill(row, 0);
        }
        for (int[] row : tabuleiro_P2) {
            Arrays.fill(row, 0);
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
        return embarcacao.posicionar(linha, coluna, tabuleiro);
    }

    public String atacar(char linha, int coluna, char jogador) {
        int[][] tabuleiroAlvo = (jogador == '1') ? tabuleiro_P2 : tabuleiro_P1;
        int indice_linha = linha - 'A';

        if (tabuleiroAlvo[indice_linha][coluna] > 0) {
            tabuleiroAlvo[indice_linha][coluna] = -1; // Marca como atingido
            return "Hit!";
        } else if (tabuleiroAlvo[indice_linha][coluna] == 0) {
            tabuleiroAlvo[indice_linha][coluna] = -2; // Marca como tiro na água
            return "Miss!";
        }
        return "Already Hit!";
    }

    public boolean verificarDerrota(char jogador) {
        int[][] tabuleiro = (jogador == '1') ? tabuleiro_P1 : tabuleiro_P2;
        for (int[] row : tabuleiro) {
            for (int cell : row) {
                if (cell > 0) {
                    return false; // Ainda existem partes de navios não atingidos
                }
            }
        }
        return true; // Todos os navios foram destruídos
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
            for (int[] row : tabuleiro_P1) {
                for (int j = 0; j < row.length; j++) {
                    if (scanner.hasNextInt()) {
                        row[j] = scanner.nextInt();
                    }
                }
            }
            for (int[] row : tabuleiro_P2) {
                for (int j = 0; j < row.length; j++) {
                    if (scanner.hasNextInt()) {
                        row[j] = scanner.nextInt();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Erro ao carregar o estado do jogo: Arquivo não encontrado.");
        }
    }
}
