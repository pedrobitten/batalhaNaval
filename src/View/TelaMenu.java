package View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class TelaMenu extends JFrame {
    public TelaMenu() {
        setTitle("Batalha Naval - Menu");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JButton newGameButton = new JButton("Novo Jogo");
        JButton continueGameButton = new JButton("Continuar Jogo");

        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TelaInicio();
                dispose();
            }
        });

        continueGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    // Lógica para carregar o estado do jogo do arquivo selecionado
                    carregarJogo(selectedFile);
                }
            }
        });

        panel.add(newGameButton);
        panel.add(continueGameButton);

        add(panel);
        setVisible(true);
    }

    private void carregarJogo(File file) {
        // Lógica para carregar o estado do jogo do arquivo selecionado
        // Por exemplo, você pode ler o arquivo e inicializar o estado do jogo
        JOptionPane.showMessageDialog(this, "Carregando o jogo do arquivo: " + file.getAbsolutePath());
        // Exemplo de lógica para iniciar a tela de posicionamento com estado carregado
        // Isso deve ser substituído pela lógica real de carregamento
        String jogador1 = "Jogador 1"; // Substituir pela lógica real de carregamento
        String jogador2 = "Jogador 2"; // Substituir pela lógica real de carregamento
        new TelaPosicionamento(jogador1, jogador2); // Passar o estado carregado
        dispose();
    }

}
