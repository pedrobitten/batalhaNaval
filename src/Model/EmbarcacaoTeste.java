package Model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbarcacaoTeste {
    private int[][] tabuleiro;

    @Before
    public void setUp() {
        tabuleiro = new int[15][15]; // Tabuleiro vazio antes de cada teste
    }

    private class TestEmbarcacao extends Embarcacao {
        public TestEmbarcacao(int tamanho, char direcao) {
            super(tamanho, direcao);
        }

        @Override
        public boolean posicionar(char linha, int coluna, int[][] tabuleiro) {
            return checkAndPlace(linha, coluna, tabuleiro, 9); // 9 é um código para testes
        }
    }

    @Test
    public void testPosicionarValido() {
        Embarcacao embarcacao = new TestEmbarcacao(3, 'H');
        boolean resultado = embarcacao.posicionar('A', 0, tabuleiro);
        assertTrue("Embarcação deve ser posicionada corretamente.", resultado);
    }

    @Test
    public void testPosicionarInvalidoForaDoTabuleiro() {
        Embarcacao embarcacao = new TestEmbarcacao(3, 'H');
        boolean resultado = embarcacao.posicionar('A', 13, tabuleiro); // Deve falhar, pois sai do tabuleiro
        assertFalse("Embarcação não deve ser posicionada fora do tabuleiro.", resultado);
    }

    @Test
    public void testPosicionarSobreposicao() {
        tabuleiro[0][1] = 1; // Há uma embarcação na posição A2
        Embarcacao embarcacao = new TestEmbarcacao(3, 'H');
        boolean resultado = embarcacao.posicionar('A', 0, tabuleiro); // Tenta posicionar começando em A1
        assertFalse("Embarcação não deve sobrepor outra.", resultado);
    }

    // Testes específicos para cada tipo de embarcação
    @Test
    public void testCouracadoPosicionamentoValido() {
        Embarcacao couracado = new Couracado('H');
        assertTrue("Couracado deve ser posicionado corretamente.", couracado.posicionar('B', 0, tabuleiro));
    }

    @Test
    public void testCruzadorPosicionamentoForaDoTabuleiro() {
        Embarcacao cruzador = new Cruzador('H');
        assertFalse("Cruzador não deve ser posicionado fora do tabuleiro.", cruzador.posicionar('B', 12, tabuleiro));
    }

    @Test
    public void testDestroyerPosicionamentoSobreposicao() {
        tabuleiro[2][3] = 1; // Supõe uma parte de outro navio na posição C4
        Embarcacao destroyer = new Destroyer('H');
        assertFalse("Destroyer não deve sobrepor outro navio.", destroyer.posicionar('C', 2, tabuleiro));
    }

    @Test
    public void testHidroaviaoVerticalPosicionamentoValido() {
        Embarcacao hidroaviao = new Hidroaviao();
        assertTrue("Hidroavião deve ser posicionado corretamente.", hidroaviao.posicionar('C', 3, tabuleiro));
    }

    @Test
    public void testSubmarinoPosicionamentoValido() {
        Embarcacao submarino = new Submarino('H');
        assertTrue("Submarino não deve ser posicionado fora do tabuleiro.", submarino.posicionar('M', 14, tabuleiro));
    }

    @Test
    public void testVerificarTabuleiroPosVaziasAposPosicionamentos() {
        Embarcacao couracado = new Couracado('H');
        Embarcacao submarino = new Submarino('V');
        couracado.posicionar('A', 0, tabuleiro);
        submarino.posicionar('B', 0, tabuleiro);
        for (int i = 0; i < tabuleiro.length; i++) {
            for (int j = 0; j < tabuleiro[i].length; j++) {
                if ((i == 0 && j < 5) || (j == 0 && i == 1)) continue; // Ignora locais onde couracado e submarino estão posicionados
                assertEquals("Posição deve estar vazia", 0, tabuleiro[i][j]);
            }
        }
    }
}
