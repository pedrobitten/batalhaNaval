package View;

import Model.Arma;
import Model.Tabuleiro;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.*;

public class TelaPosicionamento extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    private static final int TILE_SIZE = 30;
    private static final int GRID_SIZE = 15;
    private final JPanel armasPanel;
    private int[][] grid;
    private final int[][] gridJogador1;
    private boolean isSelected;
    private final ArrayList<Arma> armasDisponiveis;
    private Arma armaSelecionada;
    private int currentRotation; // Controle de rotação
    private final JButton passarVezButton;
    private boolean jogador1Posicionou;
    private final Tabuleiro tabuleiro;

    public TelaPosicionamento(String jogador1, String jogador2) {
        this.tabuleiro = new Tabuleiro();
        tabuleiro.criacaoTabuleiros();
        grid = new int[GRID_SIZE][GRID_SIZE];
        gridJogador1 = new int[GRID_SIZE][GRID_SIZE];
        isSelected = false;
        currentRotation = 0;
        jogador1Posicionou = false;

        setLayout(new BorderLayout());

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

        armasDisponiveis = new ArrayList<>();
        inicializarArmas();

        armasPanel.addMouseListener(this);

        JPanel tabuleiroPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                drawGrid(g2d);
                drawShips(g2d);
            }
        };

        tabuleiroPanel.setPreferredSize(new Dimension(GRID_SIZE * TILE_SIZE, GRID_SIZE * TILE_SIZE));
        tabuleiroPanel.addMouseListener(this);
        tabuleiroPanel.addMouseMotionListener(this);

        passarVezButton = new JButton("Passar Vez");
        passarVezButton.setEnabled(false);
        passarVezButton.addActionListener(e -> {
            if (!jogador1Posicionou) {
                JOptionPane.showMessageDialog(null, "Passe a vez para o próximo jogador.");
                salvarTabuleiroJogador1();
                grid = new int[GRID_SIZE][GRID_SIZE];
                inicializarArmas();
                passarVezButton.setEnabled(false);
                jogador1Posicionou = true;
                repaint();
            } else {
                JOptionPane.showMessageDialog(null, "Todos os jogadores posicionaram suas embarcações. Iniciar o jogo!");
                iniciarEtapaDeAtaques();
            }
        });

        add(armasPanel, BorderLayout.WEST);
        add(tabuleiroPanel, BorderLayout.CENTER);
        add(passarVezButton, BorderLayout.SOUTH);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
    }

    private void inicializarArmas() {
        armasDisponiveis.clear();

        int[][] offsetsHidroaviao = {{0, 0}, {-1, 1}, {1, 1}};
        for (int i = 0; i < 5; i++) {
            armasDisponiveis.add(new Arma(new Rectangle2D.Double(35 + i * 120, 10, TILE_SIZE, TILE_SIZE), 3, Color.YELLOW, offsetsHidroaviao));
        }

        for (int i = 0; i < 4; i++) {
            armasDisponiveis.add(new Arma(new Rectangle2D.Double(10 + i * 40, 90, TILE_SIZE, TILE_SIZE), 1, Color.WHITE, new int[][]{{0, 0}}));
        }

        for (int i = 0; i < 3; i++) {
            armasDisponiveis.add(new Arma(new Rectangle2D.Double(10 + i * 70, 130, TILE_SIZE * 2, TILE_SIZE), 2, Color.CYAN, new int[][]{{0, 0}, {1, 0}}));
        }

        for (int i = 0; i < 2; i++) {
            armasDisponiveis.add(new Arma(new Rectangle2D.Double(10 + i * 130, 170, TILE_SIZE * 4, TILE_SIZE), 4, Color.MAGENTA, new int[][]{{0, 0}, {1, 0}, {2, 0}, {3, 0}}));
        }

        armasDisponiveis.add(new Arma(new Rectangle2D.Double(10, 210, TILE_SIZE * 5, TILE_SIZE), 5, Color.BLACK, new int[][]{{0, 0}, {1, 0}, {2, 0}, {3, 0}, {4, 0}}));
    }

    private void salvarTabuleiroJogador1() {
        for (int i = 0; i < GRID_SIZE; i++) {
            System.arraycopy(grid[i], 0, gridJogador1[i], 0, GRID_SIZE);
        }
    }

    private void drawGrid(Graphics2D g2d) {
        for (int i = 0; i <= GRID_SIZE; i++) {
            g2d.drawLine(i * TILE_SIZE, 0, i * TILE_SIZE, GRID_SIZE * TILE_SIZE);
            g2d.drawLine(0, i * TILE_SIZE, GRID_SIZE * TILE_SIZE, i * TILE_SIZE);
        }
    }

    private void drawShips(Graphics2D g2d) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j] > 0) {
                    g2d.setColor(Color.ORANGE);
                    g2d.fill(new Rectangle2D.Double(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE));
                }
            }
        }
    }

    private void drawArmasDisponiveis(Graphics2D g2d) {
        for (Arma arma : armasDisponiveis) {
            if (arma.equals(armaSelecionada)) {
                g2d.setColor(Color.GREEN);
            } else {
                g2d.setColor(arma.color);
            }
            for (int[] offset : arma.offsets) {
                int x = offset[0];
                int y = offset[1];
                switch (currentRotation) {
                    case 90:
                        x = offset[1];
                        y = -offset[0];
                        break;
                    case 180:
                        x = -offset[0];
                        y = -offset[1];
                        break;
                    case 270:
                        x = -offset[1];
                        y = offset[0];
                        break;
                }
                g2d.fill(new Rectangle2D.Double(arma.shape.getX() + x * TILE_SIZE, arma.shape.getY() + y * TILE_SIZE, TILE_SIZE, TILE_SIZE));
            }
        }
    }

    private boolean canPlaceArma(int x, int y) {
        for (int[] offset : armaSelecionada.offsets) {
            int newX = x;
            int newY = y;
            switch (currentRotation) {
                case 90:
                    newX += offset[1];
                    newY -= offset[0];
                    break;
                case 180:
                    newX -= offset[0];
                    newY -= offset[1];
                    break;
                case 270:
                    newX -= offset[1];
                    newY += offset[0];
                    break;
                default:
                    newX += offset[0];
                    newY += offset[1];
                    break;
            }
            if (newX >= GRID_SIZE || newY >= GRID_SIZE || newX < 0 || newY < 0 || grid[newX][newY] > 0) {
                return false;
            }
        }
        return true;
    }

    private void highlightInvalidPosition(Graphics2D g2d, int x, int y) {
        g2d.setColor(Color.RED);
        for (int[] offset : armaSelecionada.offsets) {
            int newX = x;
            int newY = y;
            switch (currentRotation) {
                case 90:
                    newX += offset[1];
                    newY -= offset[0];
                    break;
                case 180:
                    newX -= offset[0];
                    newY -= offset[1];
                    break;
                case 270:
                    newX -= offset[1];
                    newY += offset[0];
                    break;
                default:
                    newX += offset[0];
                    newY += offset[1];
                    break;
            }
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
            int newX = x;
            int newY = y;
            switch (currentRotation) {
                case 90:
                    newX += offset[1];
                    newY -= offset[0];
                    break;
                case 180:
                    newX -= offset[0];
                    newY -= offset[1];
                    break;
                case 270:
                    newX -= offset[1];
                    newY += offset[0];
                    break;
                default:
                    newX += offset[0];
                    newY += offset[1];
                    break;
            }
            grid[newX][newY] = armaSelecionada.length;
        }

        armasDisponiveis.remove(armaSelecionada);
        armaSelecionada = null;
        isSelected = false;
        currentRotation = 0;
        repaint();
        JOptionPane.showMessageDialog(null, "Arma posicionada com sucesso.");
        verificarPosicionamentoCompleto();
    }

    private void iniciarEtapaDeAtaques() {
        JOptionPane.showMessageDialog(this, "Iniciando a etapa de ataques!");
        JFrame frame = new JFrame("Ataques - Jogador 1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new TelaAtaques(tabuleiro, '1'));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e) && isSelected) {
            currentRotation = (currentRotation + 90) % 360;
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point point = e.getPoint();
        boolean found = false;

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
            int x = e.getX() / TILE_SIZE;
            int y = e.getY() / TILE_SIZE;
            if (x < GRID_SIZE && y < GRID_SIZE) {
                isSelected = true;
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
