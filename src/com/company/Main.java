package com.company;

import com.company.controllers.GameController;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        GameController controller = new GameController();
        JFrame frame = new JFrame();
        frame.add(controller.getDisplay());
        frame.pack();
        frame.setTitle(Display.TITLE);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        controller.start();

        Thread.sleep(5000);
        controller.stop();
    }
}
