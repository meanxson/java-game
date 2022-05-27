package com.company;

import javax.swing.*;
import java.awt.*;

public class Display extends Canvas implements Runnable {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private Thread thread;
    private boolean isRunning;

    //todo: make in other class
    private void start() {
        if (isRunning)
            return;

        isRunning = true;
        thread = new Thread(this);
        thread.start();

    }

    private void stop() {
        if (!isRunning)
            return;
        isRunning = false;

        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public void run() {
        while (isRunning) {
        }
    }

    public static void main(String[] args) {
        Display game = new Display();
        JFrame frame = new JFrame();
        frame.add(game);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setVisible(true);

        System.out.println("Running...");
        game.start();
    }


}
