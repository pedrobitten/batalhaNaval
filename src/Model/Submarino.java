package Model;

public class Submarino extends Embarcacao {
    public Submarino(char direcao) {
        super(1, direcao); // Tamanho do submarino é 1
    }

    @Override
    public boolean posicionar(char linha, int coluna, int[][] tabuleiro) {
        int indice_linha = linha - 'A';

        if (tabuleiro[indice_linha][coluna] == 0) {
            tabuleiro[indice_linha][coluna] = 2; // O código '2' representa um submarino
            return true;
        }
        return false;
    }
}