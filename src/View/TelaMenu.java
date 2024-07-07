package View;

import java.io.File;
import javax.swing.*;
import Controller.GameController;
import Model.Tabuleiro;

public class TelaMenu extends JFrame {
    private GameController controller;

    public TelaMenu(GameController controller) {
        this.controller = controller;
        setTitle("Batalha Naval - Menu");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JButton newGameButton = new JButton("Novo Jogo");
        JButton continueGameButton = new JButton("Continuar Jogo");

        newGameButton.addActionListener(e -> {
            new TelaInicio(controller);
            dispose();
        });

        continueGameButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                carregarJogo(selectedFile);
            }
        });

        panel.add(newGameButton);
        panel.add(continueGameButton);

        add(panel);
        setVisible(true);
    }

    private void carregarJogo(File file) {
        JOptionPane.showMessageDialog(this, "Carregando o jogo do arquivo: " + file.getAbsolutePath());

        Tabuleiro tabuleiro = new Tabuleiro();
        tabuleiro.carregarEstado(file.getAbsolutePath());
        controller.iniciarAtaques();
        dispose();
    }
}
