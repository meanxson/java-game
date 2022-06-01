package com.company;

import graphics.Render;
import graphics.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Display extends Canvas implements Runnable {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private Thread thread;
    private boolean isRunning;
    private Render render;
    private Screen screen;
    private BufferedImage image;
    private int[] pixels;


    public Display(){
        screen = new Screen(WIDTH, HEIGHT);
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

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
            tick();
            render();
        }
    }

    private void tick(){

    }

    private void render(){
        BufferStrategy bs = getBufferStrategy();

        if (bs == null){
            createBufferStrategy(3);
            return;
        }

        screen.render();

        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            pixels[i] = screen.pixels[i];
        }

        Graphics graphics = bs.getDrawGraphics();
        graphics.drawImage(image,0,0,WIDTH,HEIGHT, null);
        graphics.dispose();
        bs.show();
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
