package org.accio;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Objects;
import javax.swing.*;


public class LoadGame extends JPanel implements ActionListener {
    int B_WIDTH = SnakeGame.B_WIDTH;
    int B_HEIGHT = SnakeGame.B_HEIGHT;
    CardLayout cardLayout;
    JPanel mainPanel;
    Menu menu;
    Board board;
    JButton start_game, clearScore;
    JLabel pauseMSG;

    LoadGame(CardLayout cardLayout, JPanel mainPanel, Board board) {
        this.setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        this.setBackground(Color.GREEN);
        this.setFocusable(true);
        this.setLayout(null);

        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.board = board;

        start_game = new JButton("Start New Game");
        start_game.setBackground(Color.WHITE);
        start_game.setForeground(Color.BLUE);
        start_game.setFont(new Font(start_game.getFont().getFontName(), Font.BOLD, 18));
        start_game.setBounds(B_WIDTH / 2 - 100, B_HEIGHT / 2 - 50, 200, 36);
        start_game.addActionListener(this);
        this.add(start_game);

        clearScore = new JButton("Reset Score");
        clearScore.setBackground(Color.BLACK);
        clearScore.setForeground(Color.RED);
        clearScore.setFont(new Font(clearScore.getFont().getFontName(), Font.PLAIN, 14));
        clearScore.setBounds(B_WIDTH / 2 - 65, B_HEIGHT / 2 + 10, 130, 30);
        clearScore.addActionListener(this);
        this.add(clearScore);

        pauseMSG = new JLabel("Press SPACE BAR to Pause/Resume the Game");
        pauseMSG.setForeground(Color.BLACK);
        int label_stringWidth = pauseMSG.getFontMetrics(pauseMSG.getFont()).stringWidth(pauseMSG.getText());
        pauseMSG.setBounds((B_WIDTH - label_stringWidth) / 2, B_HEIGHT / 2 + 85, label_stringWidth, pauseMSG.getFont().getSize());
        this.add(pauseMSG);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == start_game) {
            menu = new Menu(cardLayout, mainPanel, board);
            mainPanel.add(menu, "menu");

            cardLayout.show(mainPanel, "menu");
        }
        else if (actionEvent.getSource() == clearScore) {
            String sp = File.separator;
            String filePath = Objects.requireNonNull(getClass().getResource("/")).toString();
            File file = new File(filePath.substring(6, filePath.length() - 15) + "src" + sp + "main" + sp + "resources" + sp + "HScore.txt");

            try (
                    BufferedWriter bw = new BufferedWriter(new FileWriter(file))
            ) {
                bw.write("0");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }

            this.showToast();
        }
    }

    void showToast() {
        String s = "The score is reset!";
        Font small = new Font("Helvetica", Font.BOLD, 16);
        FontMetrics fontMetrics = getFontMetrics(small);
        int x = (B_WIDTH - fontMetrics.stringWidth(s)) / 2;
        int y = B_HEIGHT - 80;

        JWindow w = new JWindow();
        w.setBackground(new Color(0, 0, 0, 0));

        JPanel p = new JPanel() {
            public void paintComponent(Graphics g) {
                int wid = g.getFontMetrics().stringWidth(s);
                int hei = g.getFontMetrics().getHeight();

                // draw the boundary of the toast and fill it
                 g.setColor(Color.black);
                 g.fillRect(10, 10, wid + 30, hei + 10);
                 g.setColor(Color.black);
                 g.drawRect(10, 10, wid + 30, hei + 10);

                 // set the color of text
                 g.setColor(new Color(255, 255, 255, 240));
                 g.drawString(s, 25, 27);

                 int t = 250;

                 // draw the shadow of the toast
                 for (int i = 0; i < 4; i++) {
                     t -= 60;
                     g.setColor(new Color(0, 0, 0, t));
                     g.drawRect(10 - i, 10 - i, wid + 30 + i * 2, hei + 10 + i * 2);
                 }
            }
        };

        w.add(p);
        w.setLocation(x, y);
        w.setSize(300, 100);

        try {
            w.setOpacity(1);
            w.setVisible(true);

//            wait for some time
            Thread.sleep(800);

//            make the message disappear slowly
            for (float i = 1.0f; i > 0.2; i -= 0.1) {
                Thread.sleep(100);
                w.setOpacity(i);
            }

            // set the visibility to false
            w.setVisible(false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}