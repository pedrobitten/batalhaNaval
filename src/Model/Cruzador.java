package Model;

public class Cruzador extends Embarcacao {
    public Cruzador(char direcao) {
        super(4, direcao);
    }

    @Override
    public boolean posicionar(char linha, int coluna, int[][] tabuleiro) {
        return checkAndPlace(linha, coluna, tabuleiro, 3); // O código '3' representa um cruzador
    }
}