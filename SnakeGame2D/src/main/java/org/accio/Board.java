package org.accio;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;


public class Board extends JPanel implements ActionListener {
    CardLayout cardLayout;
    JPanel mainPanel;
    GameOver gameOver;
    int B_WIDTH = SnakeGame.B_WIDTH;
    int B_HEIGHT = SnakeGame.B_HEIGHT;
    int DOT_SIZE = 10;
    int MAX_DOTS;
    static final int INIT_DOTS = 4;
    int DOTS;
    int[] x, y;
    int apple_x, apple_y;
    Image body, head, apple;
    Timer timer;
    int DELAY;
    boolean leftDirection = true, rightDirection = false, upDirection = false, downDirection = false;
    boolean inGame = true;
    boolean isPaused = false;

    Board(CardLayout cardLayout, JPanel mainPanel, GameOver gameOver) {
        TAdapter tAdapter = new TAdapter();
        this.addKeyListener(tAdapter);

        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setLayout(null);

        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.gameOver = gameOver;

        MAX_DOTS = (B_WIDTH * B_HEIGHT) / (DOT_SIZE * DOT_SIZE);

        x = new int[MAX_DOTS];
        y = new int[MAX_DOTS];
    }

    //set delay
    void setDELAY(int DELAY) {
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
            gameOver.init(DOTS, this);
            cardLayout.show(mainPanel, "gameOver");
            gameOver.screen();
            timer.stop();
        }
    }

    public void locateApple() {
        int range_x = B_WIDTH / 10 - 1;
        int range_y = B_HEIGHT / 10 - 1;

        apple_x = (int) (Math.random() * range_x) * DOT_SIZE;
        apple_y = (int) (Math.random() * range_y) * DOT_SIZE;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (inGame) {
            this.checkCollision();
            this.checkApple();
            this.move();
            repaint();
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
        if (x[0] < 0 || x[0] > B_WIDTH - 10 || y[0] < 0 || y[0] > B_HEIGHT - 10)
            inGame = false;
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

            if (!isPaused) {
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
            }
            //pause or resume the game
            if (key == KeyEvent.VK_SPACE) {
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