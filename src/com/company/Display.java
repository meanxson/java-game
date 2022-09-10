package com.company;

import com.company.ticklers.Tickle;
import graphics.Render;
import graphics.Screen;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Display extends Canvas implements Runnable {
    private static final long serialVersion = 1L;

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final String TITLE = "Java Game PRE-ALPHA 0.01";
    private boolean isRunning;
    private Render render;
    private final Screen screen;
    private final BufferedImage image;
    private final int[] pixels;
    private final Tickle tickle;

    public Display(){
        Dimension size = new Dimension(WIDTH, HEIGHT);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        screen = new Screen(WIDTH, HEIGHT);
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        tickle = new Tickle() {
            int time = 0;
            @Override
            public void tick() {
                time += 4;
            }

            @Override
            public int getTick() {
                return time;
            }
        };
    }

    public Display(Tickle tickle){
        Dimension size = new Dimension(WIDTH, HEIGHT);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        screen = new Screen(WIDTH, HEIGHT);
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        this.tickle = tickle;
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
                tickle.tick();
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

    private void render(){
        BufferStrategy bs = getBufferStrategy();

        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        screen.render(tickle);

        System.arraycopy(screen.pixels, 0, pixels, 0, WIDTH * HEIGHT);

        Graphics graphics = bs.getDrawGraphics();
        graphics.drawImage(image,0,0,WIDTH,HEIGHT, null);
        graphics.dispose();
        bs.show();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
