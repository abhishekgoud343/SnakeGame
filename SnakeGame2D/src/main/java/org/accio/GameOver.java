package org.accio;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;
import java.util.Objects;


public class GameOver extends JPanel implements ActionListener {
    CardLayout cardLayout;
    JPanel mainPanel;
    Board board;
    int B_WIDTH = SnakeGame.B_WIDTH, B_HEIGHT = SnakeGame.B_HEIGHT;
    int DOTS;
    int HIGHEST_SCORE;
    JLabel gameOverLabel, scoreLabel, hScoreLabel;
    JButton restartButton;
    File file;

    GameOver(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
    }

    void init(int DOTS, Board board) {
        this.DOTS = DOTS;
        this.board = board;

        String filePath = Objects.requireNonNull(getClass().getResource("/")).toString();
        file = new File(filePath.substring(6, filePath.length() - 15) + "src\\main\\resources\\HScore.txt");

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            HIGHEST_SCORE = Integer.parseInt(line);
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void screen() {
        this.setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setLayout(null);
        this.setVisible(true);

        //display game over msg
        String gameOverMSG = "Game Over!";

        Font medium = new Font("Helvetica", Font.BOLD, 20);
        FontMetrics fontMetrics1 = getFontMetrics(medium);

        gameOverLabel = new JLabel(gameOverMSG);
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setFont(medium);
        int gO_label_stringWidth = gameOverLabel.getFontMetrics(gameOverLabel.getFont()).stringWidth(gameOverLabel.getText());
        gameOverLabel.setBounds((B_WIDTH - fontMetrics1.stringWidth(gameOverMSG)) / 2, (int) (3.0 * B_HEIGHT / 8.0) - 30, gO_label_stringWidth, 20);
        this.add(gameOverLabel);

        //Restart button
        restartButton = new JButton("Restart Game");
        restartButton.setBackground(Color.WHITE);
        restartButton.setFont(new Font(restartButton.getFont().getFontName(), Font.PLAIN, restartButton.getFont().getSize() + 2));
        restartButton.setBounds(B_WIDTH / 2 - 70, B_HEIGHT / 2 - 30, 140, 24);
        restartButton.addActionListener(this);
        this.add(restartButton);

        //Score display
        int score = (DOTS - Board.INIT_DOTS) * 100;
        if (score > HIGHEST_SCORE) {
            HIGHEST_SCORE = score;
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.write("" + HIGHEST_SCORE);
                bw.close();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        String scoreMSG = "Score: " + score;
        String hScoreMSG = "Highest Score : " + HIGHEST_SCORE;

        Font small = new Font("Helvetica", Font.BOLD, 16);
        FontMetrics fontMetrics2 = getFontMetrics(small);

        scoreLabel = new JLabel(scoreMSG);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(small);
        int sLabel_stringWidth = scoreLabel.getFontMetrics(scoreLabel.getFont()).stringWidth(scoreLabel.getText());
        scoreLabel.setBounds((B_WIDTH - fontMetrics2.stringWidth(scoreMSG)) / 2, (int) (5.0 * B_HEIGHT / 8.0), sLabel_stringWidth, 16);
        this.add(scoreLabel);

        hScoreLabel = new JLabel(hScoreMSG);
        hScoreLabel.setForeground(Color.WHITE);
        hScoreLabel.setFont(small);
        int hSLabel_stringWidth = hScoreLabel.getFontMetrics(hScoreLabel.getFont()).stringWidth(hScoreLabel.getText());
        hScoreLabel.setBounds((B_WIDTH - fontMetrics2.stringWidth(hScoreMSG)) / 2, (int) (5.0 * B_HEIGHT / 8.0) + 32, hSLabel_stringWidth, 18);
        this.add(hScoreLabel);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == restartButton) {
            GameOver gameOver = new GameOver(cardLayout, mainPanel);
            board = new Board(cardLayout, mainPanel, gameOver);

            mainPanel.add(new Menu(cardLayout, mainPanel, board),"menu");
            mainPanel.add(board, "board");
            mainPanel.add(gameOver, "gameOver");

            cardLayout.show(mainPanel, "menu");
        }
    }
}