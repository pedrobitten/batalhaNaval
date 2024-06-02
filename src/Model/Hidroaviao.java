package Model;

public class Hidroaviao extends Embarcacao {
    public Hidroaviao() {
        super(3, 'H'); // Inicialmente disposto horizontalmente
    }

    @Override
    public boolean posicionar(char linha, int coluna, int[][] tabuleiro) {
        int indice_linha = linha - 'A';

        if (indice_linha + 1 < tabuleiro.length && coluna - 1 >= 0 && coluna + 1 < tabuleiro[0].length) {
            if (tabuleiro[indice_linha][coluna] == 0 && tabuleiro[indice_linha + 1][coluna - 1] == 0
                    && tabuleiro[indice_linha + 1][coluna + 1] == 0) {
                tabuleiro[indice_linha][coluna] = 5; // Centro
                tabuleiro[indice_linha + 1][coluna - 1] = 5; // Esquerda
                tabuleiro[indice_linha + 1][coluna + 1] = 5; // Direita
                return true;
            }
        }
        return false;
    }
}