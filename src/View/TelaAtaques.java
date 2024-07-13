package View;

import Controller.GameController;
import Model.Tabuleiro;
import java.awt.*;
import javax.swing.*;

public class TelaAtaques extends JFrame {
    private final Tabuleiro tabuleiro;
    private char jogadorAtual;
    private int ataquesRestantes;
    private JLabel resultadoLabel;
    private final GameController controller;
    private JButton[][] botoesTabuleiroP1; // Matriz de botões para atualizar a interface facilmente
    private JButton[][] botoesTabuleiroP2; // Matriz de botões para o tabuleiro do jogador 2
    private JButton passarVezButton;

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

        passarVezButton = new JButton("Passar Vez");
        passarVezButton.setEnabled(false);
        passarVezButton.addActionListener(e -> trocarJogador());

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(resultadoLabel, BorderLayout.CENTER);
        southPanel.add(passarVezButton, BorderLayout.EAST);

        add(panelCentral, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        setVisible(true);

        atualizarInteratividade();
    }

    private JPanel criarTabuleiroPanel(char jogador) {
        JPanel tabuleiroPanel = new JPanel(new GridLayout(15, 15));
        if (jogador == '1') {
            botoesTabuleiroP1 = new JButton[15][15]; // Inicializa a matriz de botões para o jogador 1
        } else {
            botoesTabuleiroP2 = new JButton[15][15]; // Inicializa a matriz de botões para o jogador 2
        }

        for (char i = 'A'; i <= 'O'; i++) {
            for (int j = 0; j < 15; j++) {
                JButton cellButton = new JButton();
                cellButton.setPreferredSize(new Dimension(30, 30));
                char linha = i;
                int coluna = j;
                if (jogador == '2') {
                    cellButton.addActionListener(e -> realizarAtaque(linha, coluna, cellButton, '2'));
                } else {
                    cellButton.addActionListener(e -> realizarAtaque(linha, coluna, cellButton, '1'));
                }
                if (jogador == '1') {
                    botoesTabuleiroP1[i - 'A'][j] = cellButton; // Armazena o botão na matriz do jogador 1
                } else {
                    botoesTabuleiroP2[i - 'A'][j] = cellButton; // Armazena o botão na matriz do jogador 2
                }
                tabuleiroPanel.add(cellButton);
            }
        }
        return tabuleiroPanel;
    }

    private void realizarAtaque(char linha, int coluna, JButton cellButton, char tabuleiroJogador) {
        if (ataquesRestantes > 0) {
            String resultado = tabuleiro.atacar(linha, coluna, tabuleiroJogador);
            cellButton.setText(resultado.equals("Hit!") ? "X" : "O");
            cellButton.setEnabled(false);
            if (resultado.equals("Hit!")) {
                cellButton.setBackground(Color.RED); // Acerto
            } else {
                cellButton.setBackground(Color.BLUE); // Erro
            }
            ataquesRestantes--;
            resultadoLabel.setText("Jogador " + jogadorAtual + " - Ataques restantes: " + ataquesRestantes);

            if (tabuleiro.jogadorPerdeu(tabuleiroJogador)) {
                JOptionPane.showMessageDialog(this, "Jogador " + jogadorAtual + " venceu!");
                int resposta = JOptionPane.showConfirmDialog(this, "Deseja iniciar uma nova partida?", "Fim de Jogo", JOptionPane.YES_NO_OPTION);
                if (resposta == JOptionPane.YES_OPTION) {
                    new TelaMenu(controller);
                    dispose();
                } else {
                    System.exit(0);
                }
            }

            if (ataquesRestantes == 0) {
                passarVezButton.setEnabled(true);
            }
        }
    }

    private void trocarJogador() {
        ataquesRestantes = 3;
        controller.alternarJogador();
        jogadorAtual = controller.getJogadorAtual(); // Atualiza o jogador atual
        resultadoLabel.setText("Jogador " + jogadorAtual + " - Ataques restantes: " + ataquesRestantes);
        passarVezButton.setEnabled(false);

        atualizarInteratividade();
    }

    private void atualizarInteratividade() {
        boolean jogador1Atacando = (jogadorAtual == '1');
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                botoesTabuleiroP1[i][j].setEnabled(false); // Desativa o tabuleiro do jogador 1
                botoesTabuleiroP2[i][j].setEnabled(false); // Desativa o tabuleiro do jogador 2
                if (jogador1Atacando) {
                    botoesTabuleiroP2[i][j].setEnabled(true); // Ativa o tabuleiro do jogador 2
                } else {
                    botoesTabuleiroP1[i][j].setEnabled(true); // Ativa o tabuleiro do jogador 1
                }
            }
        }
    }
}
