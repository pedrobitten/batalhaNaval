package Model;

public class Couracado extends Embarcacao {
    public Couracado(char direcao) {
        super(5, direcao); // Tamanho do couraçado é 5
    }

    @Override
    public boolean posicionar(char linha, int coluna, int[][] tabuleiro) {
        return checkAndPlace(linha, coluna, tabuleiro, 1); // O código '1' representa um couraçado
    }
}
