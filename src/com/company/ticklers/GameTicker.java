package com.company.ticklers;

public class GameTicker implements Tickle {
    private int time = 0;
    @Override
    public void tick() {
        this.time += 4;
    }

    @Override
    public int getTick() {
        return time;
    }
}