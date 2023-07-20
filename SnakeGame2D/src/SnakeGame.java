import javax.swing.*;


public class SnakeGame extends JFrame {
    int B_WIDTH = 480;
    int B_HEIGHT = 480;
    LoadGame game;

    SnakeGame() {
        this.setTitle("Snake Game 2D");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        game = new LoadGame(B_WIDTH, B_HEIGHT, this);
        this.add(game);

        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SnakeGame snakeGame = new SnakeGame();
    }
}