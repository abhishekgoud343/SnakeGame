package org.accio;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import javax.swing.*;


public class Board extends JPanel implements ActionListener {
    int B_WIDTH;
    int B_HEIGHT;
    int DOT_SIZE = 10;
    int MAX_DOTS;
    static final int INIT_DOTS = 4;
    int DOTS;
    int[] x;
    int[] y;
    int apple_x, apple_y;
    Image body, head, apple;
    Timer timer;
    int DELAY;
    boolean leftDirection = true, rightDirection = false, upDirection = false, downDirection = false;
    boolean inGame = true;
    boolean isPaused = false;
    int HIGHEST_SCORE;
    File file = new File("src/main/resources/HScore.txt");
//    InputStream inputStream = this.getClass().getResourceAsStream("/HScore.txt");
    JButton restartButton;

    Board(int B_WIDTH, int B_HEIGHT) throws IOException {
        TAdapter tAdapter = new TAdapter();
        this.addKeyListener(tAdapter);

        this.setFocusable(true);
        this.setLayout(null);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            HIGHEST_SCORE = Integer.parseInt(line);
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        try (
//                InputStream is = this.getClass().getResource("src/main/resources/HScore.txt").openStream();
//                InputStreamReader isr = new InputStreamReader(is);
//                BufferedReader br = new BufferedReader(isr);
//        ) {
//            String line = br.readLine();
//            HIGHEST_SCORE = Integer.parseInt(line);
//        }

        this.B_WIDTH = B_WIDTH;
        this.B_HEIGHT = B_HEIGHT;

        MAX_DOTS = (B_WIDTH * B_HEIGHT) / (DOT_SIZE * DOT_SIZE);

        x = new int[MAX_DOTS];
        y = new int[MAX_DOTS];
    }

    void setDELAY(int DELAY) throws IOException {
        this.DELAY = DELAY;
        this.setBackground(Color.BLACK);
        this.initGame();
        this.loadImages(getClass().getResource("/dot.png"), getClass().getResource("/head.png"), getClass().getResource("/apple.png"));
    }

    //Initialize game
    public void initGame() {
        DOTS = INIT_DOTS;

        //Initialize snake position
        x[0] = 100;
        y[0] = 100;
        for (int i = 1; i < DOTS; ++i) {
            x[i] = x[0] + DOT_SIZE * i;
            y[i] = y[0];
        }

        //Initialize apple's position
        this.locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    //Load images from resources folder into Image objects
    public void loadImages(URL url1, URL url2, URL url3) {
        if (url1 != null) {
            ImageIcon bodyIcon = new ImageIcon(url1);
            body = bodyIcon.getImage();
        }

        if (url2 != null) {
            ImageIcon headIcon = new ImageIcon(url2);
            head = headIcon.getImage();
        }

        if (url3 != null) {
            ImageIcon appleIcon = new ImageIcon(url3);
            apple = appleIcon.getImage();
        }
    }

    //draw images at snake's and apple's positions
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.doDrawing(g);
    }

    //draw image
    public void doDrawing(Graphics g) {
        if (inGame) {
            g.drawImage(apple, apple_x, apple_y, this);

            for (int i = 0; i < DOTS; ++i) {
                if (i == 0)
                    g.drawImage(head, x[0], y[0], this);

                else
                    g.drawImage(body, x[i], y[i], this);
            }
        }
        else {
            gameOver(g);
            timer.stop();
        }
    }

    public void locateApple() {
        apple_x = (int) (Math.random() * 39) * DOT_SIZE;
        apple_y = (int) (Math.random() * 39) * DOT_SIZE;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (inGame) {
            this.checkCollision();
            this.checkApple();
            this.move();
            repaint();
        }

        if (actionEvent.getSource() == restartButton) {
            JButton button = (JButton) actionEvent.getSource();
            button.setVisible(false);
            inGame = true;
            leftDirection = true; rightDirection = false; upDirection = false; downDirection = false;
            initGame();
        }
    }

    public void move() {
        for (int i = DOTS - 1; i > 0; --i) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (leftDirection)
            x[0] -= DOT_SIZE;
        else if (rightDirection)
            x[0] += DOT_SIZE;
        else if (upDirection)
            y[0] -= DOT_SIZE;
        else if (downDirection)
            y[0] += DOT_SIZE;
    }

    //make snake eat food
    public void checkApple() {
        if (apple_x == x[0] && apple_y == y[0]) {
            ++DOTS;
            this.locateApple();
        }
    }

    public void checkCollision() {
        //snake's collision with itself
        if (DOTS > 4)
            for (int i = 1; i < DOTS; ++i)
                if (x[i] == x[0] && y[i] == y[0]) {
                    inGame = false;
                    return;
                }

        //collision with border
        if (x[0] <= 0 || x[0] >= B_WIDTH - 10 || y[0] <= 0 || y[0] >= B_HEIGHT - 10)
            inGame = false;
    }

    public void gameOver(Graphics g) {
        //Game Over message
        String gameOverMSG = "Game Over!";

        Font medium = new Font("Helvetica", Font.BOLD, 20);
        FontMetrics fontMetrics1 = getFontMetrics(medium);

        g.setColor(Color.RED);
        g.setFont(medium);
        g.drawString(gameOverMSG, (B_WIDTH - fontMetrics1.stringWidth(gameOverMSG)) / 2, (int) (3.0 * B_HEIGHT / 8.0) - 30);

        //Restart button
        restartButton = new JButton("Restart Game");
        this.add(restartButton);
        restartButton.setBackground(Color.WHITE);
        restartButton.setFont(new Font(restartButton.getFont().getFontName(), Font.PLAIN, restartButton.getFont().getSize() + 2));
        restartButton.setBounds(B_WIDTH / 2 - 70, B_HEIGHT / 2 - 30, 140, 24);
        restartButton.addActionListener(this);

        //Score display
        int score = (DOTS - INIT_DOTS) * 100;
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
//            try (
//                    FileWriter fw = new FileWriter("src/main/resources/HScore.txt");
//                    BufferedWriter bw = new BufferedWriter(fw)
//            ) {
//                bw.write("" + HIGHEST_SCORE);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
        }
        String scoreMSG = "Score: " + score;
        String hScoreMSG = "Highest Score : " + HIGHEST_SCORE;

        Font small = new Font("Helvetica", Font.BOLD, 16);
        FontMetrics fontMetrics2 = getFontMetrics(small);

        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(scoreMSG, (B_WIDTH - fontMetrics2.stringWidth(scoreMSG)) / 2, (int) (5.0 * B_HEIGHT / 8.0));
        g.drawString(hScoreMSG, (B_WIDTH - fontMetrics2.stringWidth(hScoreMSG)) / 2, (int) (5.0 * B_HEIGHT / 8.0) + 32);
    }

    //implements Controls
    private class TAdapter extends KeyAdapter {
        long lastEnter = 0;

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            int key = keyEvent.getKeyCode();

            if ((System.currentTimeMillis() - lastEnter) < DELAY)
                return;

            lastEnter = System.currentTimeMillis();

            if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && !rightDirection) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            else if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && !leftDirection) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            else if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && !downDirection) {
                leftDirection = false;
                rightDirection = false;
                upDirection = true;
            }
            else if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && !upDirection) {
                leftDirection = false;
                rightDirection = false;
                downDirection = true;
            }
            //pause or resume the game
            else if (key == KeyEvent.VK_SPACE) {
                if (isPaused) {
                    timer.start();
                    isPaused = false;
                }
                else {
                    timer.stop();
                    isPaused = true;
                }
            }
        }
    }
}