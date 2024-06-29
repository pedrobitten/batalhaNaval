package View;

import Model.Tabuleiro;
import java.awt.*;
import javax.swing.*;

public class TelaAtaques extends JPanel {
    private final Tabuleiro tabuleiro;
    private char jogadorAtual;

    public TelaAtaques(Tabuleiro tabuleiro, char jogadorAtual) {
        this.tabuleiro = tabuleiro;
        this.jogadorAtual = jogadorAtual;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel ataquePanel = new JPanel(new GridLayout(15, 15));
        for (char i = 'A'; i <= 'O'; i++) {
            for (int j = 0; j < 15; j++) {
                JButton cellButton = new JButton();
                cellButton.setPreferredSize(new Dimension(30, 30));
                char linha = i;
                int coluna = j;
                cellButton.addActionListener(e -> realizarAtaque(linha, coluna, cellButton));
                ataquePanel.add(cellButton);
            }
        }

        add(ataquePanel, BorderLayout.CENTER);
    }

    private void realizarAtaque(char linha, int coluna, JButton cellButton) {
        String resultado = tabuleiro.atacar(linha, coluna, jogadorAtual == '1' ? '2' : '1');
        cellButton.setText(resultado.equals("Hit!") ? "X" : "O");
        cellButton.setEnabled(false);

        if (tabuleiro.verificarDerrota(jogadorAtual == '1' ? '2' : '1')) {
            JOptionPane.showMessageDialog(this, "Jogador " + jogadorAtual + " venceu!");
            int resposta = JOptionPane.showConfirmDialog(this, "Deseja iniciar uma nova partida?", "Fim de Jogo", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                new TelaMenu();
                SwingUtilities.getWindowAncestor(this).dispose();
            } else {
                System.exit(0);
            }
        } else {
            trocarJogador();
        }
    }

    private void trocarJogador() {
        jogadorAtual = (jogadorAtual == '1') ? '2' : '1';
        JFrame frame = new JFrame("Ataques - Jogador " + jogadorAtual);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new TelaAtaques(tabuleiro, jogadorAtual));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }
}
