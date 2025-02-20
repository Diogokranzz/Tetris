
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Random;
import javax.swing.*;

public class TetrisBoard extends JPanel implements KeyListener {

    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 20;
    private final int BLOCK_SIZE = 30;

    private final Color[] COLORS = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.ORANGE};

    private final int[][] board = new int[BOARD_HEIGHT][BOARD_WIDTH]; // Tornando board final
    private int pieceX = BOARD_WIDTH / 2;
    private int pieceY = 0;
    private Color currentPieceColor;
    private boolean isFastDropping = false;
    private boolean gameOver = false;

    public TetrisBoard() {
        setFocusable(true);
        EventQueue.invokeLater(() -> {
            addKeyListener(this);
            Timer timer = new Timer(1000, e -> {
                if (!gameOver && !isFastDropping) {
                    if (!moveDown()) {
                        placePiece();
                        if (!isGameOver()) {
                            clearLines();
                            spawnPiece();
                        } else {
                            gameOver = true;
                            JOptionPane.showMessageDialog(this, "Game Over! Press 'R' to restart.");
                        }
                    }
                }
            });
            timer.start();
        });
        spawnPiece();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Desenha o fundo
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, BOARD_WIDTH * BLOCK_SIZE, BOARD_HEIGHT * BLOCK_SIZE);

        // Desenha o grid
        g.setColor(Color.GRAY);
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                g.drawRect(j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            }
        }

        // Desenha as peças fixas no tabuleiro
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (board[i][j] != 0) {
                    g.setColor(COLORS[board[i][j] - 1]);
                    g.fillRect(j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }

        // Desenha a peça ativa
        if (!gameOver) {
            g.setColor(currentPieceColor);
            g.fillRect(pieceX * BLOCK_SIZE, pieceY * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        } else {
            g.setColor(Color.WHITE);
            g.drawString("Game Over", 50, BOARD_HEIGHT * BLOCK_SIZE / 2);
        }
    }

    private boolean moveDown() {
        if (pieceY < BOARD_HEIGHT - 1 && board[pieceY + 1][pieceX] == 0) {
            pieceY++;
            repaint();
            return true;
        }
        return false;
    }

    private void placePiece() {
        for (int i = 0; i < COLORS.length; i++) {
            if (COLORS[i].equals(currentPieceColor)) {
                board[pieceY][pieceX] = i + 1;
                break;
            }
        }
    }

    private void clearLines() {
        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
            boolean lineIsFull = true;
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (board[i][j] == 0) {
                    lineIsFull = false;
                    break;
                }
            }
            if (lineIsFull) {
                for (int k = i; k > 0; k--) {
                    System.arraycopy(board[k - 1], 0, board[k], 0, BOARD_WIDTH);
                }
                Arrays.fill(board[0], 0);
                i++; // Para reavaliar a linha que desceu
            }
        }
    }

    private void spawnPiece() {
        pieceX = BOARD_WIDTH / 2;
        pieceY = 0;
        currentPieceColor = COLORS[new Random().nextInt(COLORS.length)];
        repaint();
    }

    private boolean isGameOver() {
        // Verifica se há espaço para spawnar uma nova peça
        for (int j = 0; j < BOARD_WIDTH; j++) {
            if (board[0][j] != 0) {
                return true;
            }
        }
        return false;
    }

    private void resetGame() {
        // Limpa o tabuleiro
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            Arrays.fill(board[i], 0);
        }
        // Reseta as variáveis de jogo
        gameOver = false;
        spawnPiece();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Não usamos este método
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT && pieceX > 0 && board[pieceY][pieceX - 1] == 0 && !gameOver) {
            pieceX--;
        } else if (keyCode == KeyEvent.VK_RIGHT && pieceX < BOARD_WIDTH - 1 && board[pieceY][pieceX + 1] == 0 && !gameOver) {
            pieceX++;
        } else if (keyCode == KeyEvent.VK_DOWN && !gameOver) {
            isFastDropping = true;
            while (moveDown()) {
            } // Move a peça para baixo até não poder mais
            placePiece();
            if (!isGameOver()) {
                clearLines();
                spawnPiece();
            } else {
                gameOver = true;
                JOptionPane.showMessageDialog(this, "Game Over! Press 'R' to restart.");
            }
        } else if (keyCode == KeyEvent.VK_R) {
            resetGame();
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            isFastDropping = false;
        }
    }
}
