package org.accio;

import javax.swing.*;
import java.awt.*;


public class SnakeGame extends JFrame {
    static final int B_WIDTH = 480, B_HEIGHT = 480; //Board width and board height
    CardLayout cardLayout;
    JPanel mainPanel;
    LoadGame load;
    Board board;
    GameOver gameOver;

    SnakeGame() {
        this.setTitle("Snake Game 2D");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        gameOver = new GameOver(cardLayout, mainPanel);
        board = new Board(cardLayout, mainPanel, gameOver);
        load = new LoadGame(cardLayout, mainPanel, board);

        mainPanel.add(load, "load");
        mainPanel.add(board, "board");
        mainPanel.add(gameOver, "gameOver");

        this.add(mainPanel);

        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SnakeGame snakeGame = new SnakeGame();
    }
}