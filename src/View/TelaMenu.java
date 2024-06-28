package View;

import java.io.File;
import javax.swing.*;

public class TelaMenu extends JFrame {
    public TelaMenu() {
        setTitle("Batalha Naval - Menu");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JButton newGameButton = new JButton("Novo Jogo");
        JButton continueGameButton = new JButton("Continuar Jogo");

        newGameButton.addActionListener(e -> {
            new TelaInicio();
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

        String jogador1 = "Jogador 1";
        String jogador2 = "Jogador 2";
        new TelaPosicionamento(jogador1, jogador2); // Passar o estado carregado
        dispose();
    }
}
