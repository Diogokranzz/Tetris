
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import javax.swing.*;

public class Tetris extends JFrame {

    public Tetris() {
        initUI();
    }

    private void initUI() {
        TetrisBoard board = new TetrisBoard();

        // Definindo o tamanho preferido do TetrisBoard
        board.setPreferredSize(new Dimension(300, 600)); // Agora Dimension está disponível

        // Adicionando o TetrisBoard a um JPanel centralizado
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS)); // Layout vertical
        gamePanel.add(Box.createVerticalGlue()); // Espaço vazio no topo
        gamePanel.add(board);
        gamePanel.add(Box.createVerticalGlue()); // Espaço vazio embaixo

        add(gamePanel);

        setTitle("Tetris");

        // Calculate window size and position
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int windowWidth = 400;
        int windowHeight = 700;
        int x = (screenSize.width - windowWidth) / 2;
        int y = (screenSize.height - windowHeight) / 2;

        setSize(windowWidth, windowHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(x, y);

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Tetris game = new Tetris();
            game.setVisible(true);
        });
    }
}
