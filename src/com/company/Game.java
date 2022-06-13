package com.company;

import graphics.Render;

public abstract class Game {
    private final Render render;

    public Game(Render render) {
        this.render = render;
    }

}
