package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class TelaPosicionamento extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    private static final int TILE_SIZE = 30; // Tamanho de cada quadrado do tabuleiro
    private static final int GRID_SIZE = 15; // Tamanho do tabuleiro 15x15
    private String jogador1, jogador2;
    private JPanel armasPanel;
    private int[][] grid; // Matriz para armazenar o estado das células do tabuleiro
    private int[][] gridJogador1; // Matriz para armazenar o estado do tabuleiro do jogador 1
    private boolean isSelected;
    private int selectedX, selectedY; // Coordenadas da célula selecionada
    private ArrayList<Arma> armasDisponiveis; // Lista de armas disponíveis
    private Arma armaSelecionada; // Arma selecionada
    private boolean horizontal; // Orientação da arma
    private JButton passarVezButton;
    private boolean jogador1Posicionou; // Flag para verificar se o jogador 1 já posicionou suas embarcações

    
    public TelaPosicionamento(String jogador1, String jogador2) {
        this.jogador1 = jogador1;
        this.jogador2 = jogador2;
        grid = new int[GRID_SIZE][GRID_SIZE];
        gridJogador1 = new int[GRID_SIZE][GRID_SIZE];
        isSelected = false;
        horizontal = true;
        jogador1Posicionou = false;

        setLayout(new BorderLayout());

        // Parte que referencia a área das embarcações
        armasPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                drawArmasDisponiveis(g2d);
            }
        };
        armasPanel.setPreferredSize(new Dimension(600, GRID_SIZE * TILE_SIZE));
        armasPanel.setBackground(Color.LIGHT_GRAY);

        // Inicializar as armas disponíveis
        armasDisponiveis = new ArrayList<>();
        inicializarArmas();

        armasPanel.addMouseListener(this);

        JPanel tabuleiroPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                drawGrid(g2d); // Desenha tabuleiro
                drawShips(g2d); // Desenha as embarcações no tabuleiro
            }
        };

        tabuleiroPanel.setPreferredSize(new Dimension(GRID_SIZE * TILE_SIZE, GRID_SIZE * TILE_SIZE)); // Insere o tabuleiro
        tabuleiroPanel.addMouseListener(this);
        tabuleiroPanel.addMouseMotionListener(this);

        passarVezButton = new JButton("Passar Vez");
        passarVezButton.setEnabled(false); // Desabilitado até que todas as armas sejam posicionadas
        passarVezButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!jogador1Posicionou) {
                    // Jogador 1 posicionou todas as embarcações, salvar tabuleiro e limpar para o jogador 2
                    JOptionPane.showMessageDialog(null, "Passe a vez para o próximo jogador.");
                    salvarTabuleiroJogador1();
                    grid = new int[GRID_SIZE][GRID_SIZE]; // Limpar o tabuleiro
                    inicializarArmas(); // Recarregar as armas para o segundo jogador
                    passarVezButton.setEnabled(false);
                    jogador1Posicionou = true;
                    repaint();
                } else {
                    // Jogador 2 posicionou todas as embarcações, iniciar o jogo
                    JOptionPane.showMessageDialog(null, "Todos os jogadores posicionaram suas embarcações. Iniciar o jogo!");
                }
            }
        });

        add(armasPanel, BorderLayout.WEST); // Adiciona o painel das armas no oeste
        add(tabuleiroPanel, BorderLayout.CENTER); // Adiciona o painel do tabuleiro no centro
        add(passarVezButton, BorderLayout.SOUTH); // Adiciona o painel de passar a vez no sul
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
    }

    private void inicializarArmas() {
        armasDisponiveis.clear();

        // Desenhar hidroaviões
        int[][] offsetsHidroaviao = {{0, 0}, {-1, 1}, {1, 1}};
        for (int i = 0; i < 5; i++) {
            armasDisponiveis.add(new Arma(new Rectangle2D.Double(35 + i * 120, 10, TILE_SIZE, TILE_SIZE), 3, Color.YELLOW, offsetsHidroaviao));
        }

        // Desenhar submarinos
        for (int i = 0; i < 4; i++) {
            armasDisponiveis.add(new Arma(new Rectangle2D.Double(10 + i * 40, 90, TILE_SIZE, TILE_SIZE), 1, Color.WHITE, new int[][]{{0, 0}}));
        }

        // Desenhar destroyers
        for (int i = 0; i < 3; i++) {
            armasDisponiveis.add(new Arma(new Rectangle2D.Double(10 + i * 70, 130, TILE_SIZE * 2, TILE_SIZE), 2, Color.CYAN, new int[][]{{0, 0}, {1, 0}}));
        }

        // Desenhar cruzadores
        for (int i = 0; i < 2; i++) {
            armasDisponiveis.add(new Arma(new Rectangle2D.Double(10 + i * 130, 170, TILE_SIZE * 4, TILE_SIZE), 4, Color.MAGENTA, new int[][]{{0, 0}, {1, 0}, {2, 0}, {3, 0}}));
        }

        // Desenhar couraçado
        armasDisponiveis.add(new Arma(new Rectangle2D.Double(10, 210, TILE_SIZE * 5, TILE_SIZE), 5, Color.BLACK, new int[][]{{0, 0}, {1, 0}, {2, 0}, {3, 0}, {4, 0}}));
    }

    private void salvarTabuleiroJogador1() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                gridJogador1[i][j] = grid[i][j];
            }
        }
    }

    private void drawGrid(Graphics2D g2d) {
        for (int i = 0; i <= GRID_SIZE; i++) {
            g2d.drawLine(i * TILE_SIZE, 0, i * TILE_SIZE, GRID_SIZE * TILE_SIZE);
            g2d.drawLine(0, i * TILE_SIZE, GRID_SIZE * TILE_SIZE, i * TILE_SIZE);
        }
    }

    private void drawShips(Graphics2D g2d) {
        // Desenhar as armas (embarcações) no tabuleiro
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j] > 0) { // Célula ocupada por uma embarcação
                    g2d.setColor(Color.ORANGE);
                    g2d.fill(new Rectangle2D.Double(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE));
                }
            }
        }
    }

    
    private void drawArmasDisponiveis(Graphics2D g2d) {
        for (Arma arma : armasDisponiveis) {
            if (arma.equals(armaSelecionada)) {
                g2d.setColor(Color.GREEN); // Cor para indicar arma selecionada
            } else {
                g2d.setColor(arma.color); // Cor específica da arma
            }
            for (int[] offset : arma.offsets) {
                g2d.fill(new Rectangle2D.Double(arma.shape.getX() + offset[0] * TILE_SIZE, arma.shape.getY() + offset[1] * TILE_SIZE, TILE_SIZE, TILE_SIZE));
            }
        }
    }

    private boolean canPlaceArma(int x, int y) {
        for (int[] offset : armaSelecionada.offsets) {
            int newX = x + (horizontal ? offset[0] : offset[1]);
            int newY = y + (horizontal ? offset[1] : offset[0]);
            if (newX >= GRID_SIZE || newY >= GRID_SIZE || newX < 0 || newY < 0 || grid[newX][newY] > 0) {
                return false;
            }
        }
        return true;
    }

    private void highlightInvalidPosition(Graphics2D g2d, int x, int y) {
        g2d.setColor(Color.RED);
        for (int[] offset : armaSelecionada.offsets) {
            int newX = x + (horizontal ? offset[0] : offset[1]);
            int newY = y + (horizontal ? offset[1] : offset[0]);
            g2d.fillRect(newX * TILE_SIZE, newY * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }

    private void verificarPosicionamentoCompleto() {
        if (armasDisponiveis.isEmpty()) {
            passarVezButton.setEnabled(true);
        }
    }

    private void posicionarArma(int x, int y) {
        if (!canPlaceArma(x, y)) {
            Graphics2D g2d = (Graphics2D) getGraphics();
            highlightInvalidPosition(g2d, x, y);
            JOptionPane.showMessageDialog(null, "Não é possível posicionar a arma aqui.");
            return;
        }

        for (int[] offset : armaSelecionada.offsets) {
            int newX = x + (horizontal ? offset[0] : offset[1]);
            int newY = y + (horizontal ? offset[1] : offset[0]);
            grid[newX][newY] = armaSelecionada.length;
        }

        armasDisponiveis.remove(armaSelecionada);
        armaSelecionada = null;
        isSelected = false;
        repaint();
        JOptionPane.showMessageDialog(null, "Arma posicionada com sucesso.");
        verificarPosicionamentoCompleto();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e) && isSelected) {
            // Implementar a lógica de rotação das armas
            horizontal = !horizontal;
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point point = e.getPoint();
        boolean found = false;

        // Verificar se o clique foi em uma arma disponível
        for (Arma arma : armasDisponiveis) {
            if (arma.shape.contains(point) && e.getSource() == armasPanel) {
                armaSelecionada = arma;
                isSelected = true;
                repaint();
                JOptionPane.showMessageDialog(null, "Arma disponível selecionada.");
                found = true;
                break;
            }
        }

        if (!found && e.getSource() != armasPanel) {
            // Verificar se o clique foi sobre o tabuleiro
            int x = e.getX() / TILE_SIZE;
            int y = e.getY() / TILE_SIZE;
            if (x < GRID_SIZE && y < GRID_SIZE) {
                isSelected = true;
                selectedX = x;
                selectedY = y;
                if (armaSelecionada != null) {
                    posicionarArma(x, y);
                }
                JOptionPane.showMessageDialog(null, "Posição do tabuleiro selecionada (" + x + ", " + y + ").");
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            // Confirmar o posicionamento da arma
            armaSelecionada = null;
            isSelected = false;
            repaint();
            JOptionPane.showMessageDialog(null, "Posicionamento da arma cancelado.");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
