package Model;

public class Embarcacao {
    protected int tamanho;
    protected char direcao;
    protected int[][] partes;

    public Embarcacao(int tamanho, char direcao) {
        this.tamanho = tamanho;
        this.direcao = direcao;
        this.partes = new int[tamanho][2]; // Inicializa as partes da embarcação
    }

    public boolean posicionar(char linha, int coluna, int[][] tabuleiro) {
        int indiceLinha = linha - 'A';
        if (indiceLinha < 0 || indiceLinha >= tabuleiro.length || coluna < 0 || coluna >= tabuleiro[0].length) {
            return false; // Fora dos limites
        }

        // Verifica se pode posicionar a embarcação
        for (int[] parte : partes) {
            int x = indiceLinha + parte[0];
            int y = coluna + parte[1];
            if (x < 0 || x >= tabuleiro.length || y < 0 || y >= tabuleiro[0].length || tabuleiro[x][y] != 0) {
                return false; // Posição inválida ou já ocupada
            }
        }

        // Posiciona a embarcação
        for (int[] parte : partes) {
            int x = indiceLinha + parte[0];
            int y = coluna + parte[1];
            tabuleiro[x][y] = tamanho; // Marca a posição da embarcação com seu tamanho
        }
        return true;
    }

    protected boolean checkAndPlace(char linha, int coluna, int[][] tabuleiro, int codigo) {
        // Implementação para verificar e posicionar a embarcação
        // Isso é uma generalização do método posicionar
        int indiceLinha = linha - 'A';
        if (indiceLinha < 0 || indiceLinha >= tabuleiro.length || coluna < 0 || coluna >= tabuleiro[0].length) {
            return false; // Fora dos limites
        }

        for (int[] parte : partes) {
            int x = indiceLinha + parte[0];
            int y = coluna + parte[1];
            if (x < 0 || x >= tabuleiro.length || y < 0 || y >= tabuleiro[0].length || tabuleiro[x][y] != 0) {
                return false; // Posição inválida ou já ocupada
            }
        }

        for (int[] parte : partes) {
            int x = indiceLinha + parte[0];
            int y = coluna + parte[1];
            tabuleiro[x][y] = codigo;
        }
        return true;
    }
}
