package com.company.controllers;

import com.company.Display;
import com.company.ticklers.GameTicker;

public class GameController {
    private Thread thread;
    private final Display display;

    public GameController() {
        this.display = new Display(new GameTicker());
        this.thread = new Thread(display);

        this.display.setRunning(false);
    }

    public void start() {
        if (display.isRunning())
            return;

        display.setRunning(true);
        thread = new Thread(display);
        thread.start();
    }

    public void stop() {
        if (!getDisplay().isRunning())
            return;
        display.setRunning(false);

        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public Display getDisplay() {
        return display;
    }
}