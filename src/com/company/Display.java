package com.company;

import graphics.Render;
import graphics.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Display extends Canvas implements Runnable {
    private static final long serialVersion = 1L;

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final String TITLE = "Java Game PRE-ALPHA 0.01";

    private Thread thread;
    private boolean isRunning;
    private Render render;
    private Screen screen;
    private BufferedImage image;
    private int[] pixels;
    private Game game;


    public Display(){
        Dimension size = new Dimension(WIDTH, HEIGHT);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        screen = new Screen(WIDTH, HEIGHT);
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        game = new Game();
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
        int frames = 0;
        double unprocessedSeconds = 0;
        long previousTime = System.nanoTime();
        double secondPerTick = 1 / 60.0;
        int tickCount = 0;
        boolean ticked = false;

        while (isRunning) {
            long currentTime = System.nanoTime();
            long passedTime = currentTime - previousTime;
            previousTime = currentTime;
            unprocessedSeconds += passedTime / 1000000000.0;

            while (unprocessedSeconds > secondPerTick){
                tick();
                unprocessedSeconds -= secondPerTick;
                ticked = true;
                tickCount++;
                if (tickCount % 60 == 0){
                    System.out.println(frames + "fps");
                    previousTime += 1000;
                    frames = 0;
                }
            }

            if (ticked){
                render();
                frames++;
            }

            render();
            frames++;
        }
    }

    private void tick(){
        game.tick();
    }

    private void render(){
        BufferStrategy bs = getBufferStrategy();

        if (bs == null){
            createBufferStrategy(3);
            return;
        }

        screen.render(game);

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
        frame.pack();
        frame.setTitle(TITLE);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        game.start();
    }


}
