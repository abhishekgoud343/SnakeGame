package org.accio;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;


public class SnakeGame extends JFrame {
    int B_WIDTH = 480;
    int B_HEIGHT = 480;
    CardLayout cardLayout;
    JPanel mainPanel;
    LoadGame menu;
    Board board;

    SnakeGame() throws URISyntaxException, IOException {
        this.setTitle("Snake Game 2D");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        board = new Board(B_WIDTH, B_HEIGHT);
        menu = new LoadGame(B_WIDTH, B_HEIGHT, cardLayout, mainPanel, board);
        mainPanel.add(menu, "menu");

        mainPanel.add(board, "board");


//        game = new LoadGame(B_WIDTH, B_HEIGHT, this);
//        this.add(game);

        this.add(mainPanel);
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        SnakeGame snakeGame = new SnakeGame();
    }
}