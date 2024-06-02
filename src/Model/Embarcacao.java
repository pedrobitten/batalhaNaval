package Model;

public abstract class Embarcacao {
    protected int tamanho;
    protected int[] posicoes;
    protected char direcao; // 'H' para horizontal, 'V' para vertical

    public Embarcacao(int tamanho, char direcao) {
        this.tamanho = tamanho;
        this.posicoes = new int[tamanho];
        this.direcao = direcao;
    }

    public abstract boolean posicionar(char linha, int coluna, int[][] tabuleiro);

    // Método auxiliar para verificar e posicionar embarcações linearmente
    protected boolean checkAndPlace(char linha, int coluna, int[][] tabuleiro, int code) {
        int indice_linha = linha - 'A';
        for (int i = 0; i < this.tamanho; i++) {
            int delta = this.direcao == 'H' ? coluna + i : indice_linha + i;
            if ((this.direcao == 'H' && (delta >= tabuleiro[0].length || tabuleiro[indice_linha][delta] != 0))
                    || (this.direcao == 'V' && (delta >= tabuleiro.length || tabuleiro[delta][coluna] != 0))) {
                return false;
            }
        }

        for (int i = 0; i < this.tamanho; i++) {
            if (this.direcao == 'H') {
                tabuleiro[indice_linha][coluna + i] = code;
            } else {
                tabuleiro[indice_linha + i][coluna] = code;
            }
        }
        return true;
    }
}