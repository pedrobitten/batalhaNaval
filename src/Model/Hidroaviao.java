package Model;

public class Hidroaviao extends Embarcacao {
    public Hidroaviao() {
        super(3, 'H'); // Inicialmente disposto horizontalmente
    }
    
    public Hidroaviao(char direcao) {
        super(3, direcao); // Construtor que aceita a direção
    }

    @Override
    public boolean posicionar(char linha, int coluna, int[][] tabuleiro) {
        return checkAndPlace(linha, coluna, tabuleiro, 2); // O código '2' representa um hidroavião
    }
}
