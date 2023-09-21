package org.accio;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class Menu extends JPanel implements ActionListener {
    int B_WIDTH = SnakeGame.B_WIDTH;
    int B_HEIGHT = SnakeGame.B_HEIGHT;
    CardLayout cardLayout;
    JPanel mainPanel;
    Board board;
    JButton easy;
    JButton medium;
    JButton hard;

    Menu(CardLayout cardLayout, JPanel mainPanel, Board board) {
        this.setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        this.setBackground(Color.ORANGE);
        this.setFocusable(true);
        this.setLayout(null);

        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.board = board;

        JLabel diff_label = new JLabel("Select Difficulty Level:");
        diff_label.setForeground(Color.BLACK);
        diff_label.setFont(new Font(diff_label.getFont().getFontName(), Font.BOLD, diff_label.getFont().getSize() + 4));
        int label_stringWidth = diff_label.getFontMetrics(diff_label.getFont()).stringWidth(diff_label.getText());
        diff_label.setBounds((B_WIDTH - label_stringWidth) / 2, B_HEIGHT / 2 - 70, label_stringWidth, 20);
        this.add(diff_label);

        easy = new JButton("Easy");
        easy.setBackground(Color.WHITE);
        easy.setFont(new Font(easy.getFont().getFontName(), Font.PLAIN, easy.getFont().getSize() + 2));
        easy.setBounds(B_WIDTH / 2 - 60, B_HEIGHT / 2 - 36, 110, 26);
        easy.addActionListener(this);
        this.add(easy);

        medium = new JButton("Medium");
        medium.setBackground(Color.WHITE);
        medium.setFont(new Font(medium.getFont().getFontName(), Font.PLAIN, medium.getFont().getSize() + 2));
        medium.setBounds(B_WIDTH / 2 - 60, B_HEIGHT / 2, 110, 26);
        medium.addActionListener(this);
        this.add(medium);

        hard = new JButton("Hard");
        hard.setBackground(Color.WHITE);
        hard.setFont(new Font(hard.getFont().getFontName(), Font.PLAIN, hard.getFont().getSize() + 2));
        hard.setBounds(B_WIDTH / 2 - 60, B_HEIGHT / 2 + 36, 110, 26);
        hard.addActionListener(this);
        this.add(hard);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == easy)
            board.setDELAY(200);
        else if (actionEvent.getSource() == medium)
            board.setDELAY(150);
        else if (actionEvent.getSource() == hard)
            board.setDELAY(100);

        cardLayout.show(mainPanel, "board");
        board.requestFocusInWindow();
    }
}