package View;

import Model.Tabuleiro;
import Controller.GameController;
import java.awt.*;
import javax.swing.*;

public class TelaAtaques extends JFrame {
    private final Tabuleiro tabuleiro;
    private char jogadorAtual;
    private int ataquesRestantes;
    private JLabel resultadoLabel;
    private final GameController controller;

    public TelaAtaques(Tabuleiro tabuleiro, char jogadorAtual, GameController controller) {
        this.tabuleiro = tabuleiro;
        this.jogadorAtual = jogadorAtual;
        this.controller = controller;
        this.ataquesRestantes = 3; // Cada jogador tem 3 ataques por turno
        initUI();
    }

    private void initUI() {
        setTitle("Batalha Naval - Ataques");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel panelCentral = new JPanel(new GridLayout(1, 2));

        JPanel tabuleiroJogador1Panel = criarTabuleiroPanel('1');
        JPanel tabuleiroJogador2Panel = criarTabuleiroPanel('2');

        panelCentral.add(tabuleiroJogador1Panel);
        panelCentral.add(tabuleiroJogador2Panel);

        resultadoLabel = new JLabel("Jogador " + jogadorAtual + " - Ataques restantes: " + ataquesRestantes);
        resultadoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton passarVezButton = new JButton("Passar Vez");
        passarVezButton.setEnabled(false);
        passarVezButton.addActionListener(e -> trocarJogador());

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(resultadoLabel, BorderLayout.CENTER);
        southPanel.add(passarVezButton, BorderLayout.EAST);

        add(panelCentral, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel criarTabuleiroPanel(char jogador) {
        JPanel tabuleiroPanel = new JPanel(new GridLayout(15, 15));
        for (char i = 'A'; i <= 'O'; i++) {
            for (int j = 0; j < 15; j++) {
                JButton cellButton = new JButton();
                cellButton.setPreferredSize(new Dimension(30, 30));
                char linha = i;
                int coluna = j;
                if (jogador == '2') {
                    cellButton.addActionListener(e -> realizarAtaque(linha, coluna, cellButton));
                }
                tabuleiroPanel.add(cellButton);
            }
        }
        return tabuleiroPanel;
    }

    private void realizarAtaque(char linha, int coluna, JButton cellButton) {
        if (ataquesRestantes > 0) {
            String resultado = tabuleiro.atacar(linha, coluna, jogadorAtual == '1' ? '2' : '1');
            cellButton.setText(resultado.equals("Hit!") ? "X" : "O");
            cellButton.setEnabled(false);
            ataquesRestantes--;
            resultadoLabel.setText("Jogador " + jogadorAtual + " - Ataques restantes: " + ataquesRestantes);

            if (tabuleiro.verificarDerrota(jogadorAtual == '1' ? '2' : '1')) {
                JOptionPane.showMessageDialog(this, "Jogador " + jogadorAtual + " venceu!");
                int resposta = JOptionPane.showConfirmDialog(this, "Deseja iniciar uma nova partida?", "Fim de Jogo", JOptionPane.YES_NO_OPTION);
                if (resposta == JOptionPane.YES_OPTION) {
                    new TelaMenu(controller);
                    dispose();
                } else {
                    System.exit(0);
                }
            } else if (ataquesRestantes == 0) {
                JButton passarVezButton = (JButton) ((JPanel) getContentPane().getComponent(1)).getComponent(1);
                passarVezButton.setEnabled(true);
            }
        }
    }

    private void trocarJogador() {
        ataquesRestantes = 3;
        controller.alternarJogador();
        new TelaAtaques(tabuleiro, controller.getJogadorAtual(), controller);
        dispose();
    }
}
