package org.accio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class LoadGame extends JPanel implements ActionListener {
    SnakeGame snakeGame;
    int B_WIDTH;
    int B_HEIGHT;
    CardLayout cardLayout;
    JPanel mainPanel;
    Board board;
    JButton start_game;
    JLabel pauseMSG;
    JButton easy, medium, hard;

    LoadGame(int B_WIDTH, int B_HEIGHT, CardLayout cardLayout, JPanel mainPanel, Board board) {
        this.B_WIDTH = B_WIDTH;
        this.B_HEIGHT = B_HEIGHT;

        this.setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        this.setBackground(Color.GREEN);
        this.setFocusable(true);
        this.setLayout(null);

        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.board = board;

        start_game = new JButton("Start New Game");
        this.add(start_game);
        start_game.setBackground(Color.WHITE);
        start_game.setForeground(Color.BLUE);
        start_game.setFont(new Font(start_game.getFont().getFontName(), Font.BOLD, 16));
        start_game.setBounds(B_WIDTH / 2 - 90, B_HEIGHT / 2 - 45, 180, 30);
        start_game.addActionListener(this);

        pauseMSG = new JLabel("Press SPACE BAR to Pause/Resume the Game");
        pauseMSG.setForeground(Color.BLACK);
        pauseMSG.setFont(new Font(pauseMSG.getFont().getFontName(), Font.PLAIN, pauseMSG.getFont().getSize() + 1));
        int label_stringWidth = pauseMSG.getFontMetrics(pauseMSG.getFont()).stringWidth(pauseMSG.getText());
        pauseMSG.setBounds((B_WIDTH - label_stringWidth) / 2, B_HEIGHT / 2 + 30, label_stringWidth, 18);
        this.add(pauseMSG);
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == start_game) {
            JButton button = (JButton) actionEvent.getSource();
            button.setVisible(false);
            pauseMSG.setVisible(false);

            JLabel diff_label = new JLabel("Select Difficulty Level:");
            diff_label.setForeground(Color.BLACK);
            diff_label.setFont(new Font(diff_label.getFont().getFontName(), Font.BOLD, diff_label.getFont().getSize() + 4));
            int label_stringWidth = diff_label.getFontMetrics(diff_label.getFont()).stringWidth(diff_label.getText());
            diff_label.setBounds((B_WIDTH - label_stringWidth) / 2, B_HEIGHT / 2 - 70, label_stringWidth, 20);
            this.add(diff_label);
            repaint();

            easy = new JButton("Easy");
            this.add(easy);
            easy.setBackground(Color.WHITE);
            easy.setFont(new Font(easy.getFont().getFontName(), Font.PLAIN, easy.getFont().getSize() + 2));
            easy.setBounds(B_WIDTH / 2 - 60, B_HEIGHT / 2 - 36, 110, 26);
            easy.addActionListener(this);

            medium = new JButton("Medium");
            this.add(medium);
            medium.setBackground(Color.WHITE);
            medium.setFont(new Font(medium.getFont().getFontName(), Font.PLAIN, medium.getFont().getSize() + 2));
            medium.setBounds(B_WIDTH / 2 - 60, B_HEIGHT / 2, 110, 26);
            medium.addActionListener(this);

            hard = new JButton("Hard");
            this.add(hard);
            hard.setBackground(Color.WHITE);
            hard.setFont(new Font(hard.getFont().getFontName(), Font.PLAIN, hard.getFont().getSize() + 2));
            hard.setBounds(B_WIDTH / 2 - 60, B_HEIGHT / 2 + 36, 110, 26);
            hard.addActionListener(this);
        }

        if (actionEvent.getSource() == easy) {
            try {
                board.setDELAY(300);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            cardLayout.show(mainPanel, "board");
        }
        else if (actionEvent.getSource() == medium) {
            try {
                board.setDELAY(200);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            cardLayout.show(mainPanel, "board");
        }
        else if (actionEvent.getSource() == hard) {
            try {
                board.setDELAY(150);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            cardLayout.show(mainPanel, "board");
//            board = new Board(B_WIDTH, B_HEIGHT, 100);
//            removeAll();
//            this.setVisible(false);
//            snakeGame.add(board);
//            revalidate();
//            repaint();
        }
    }
}