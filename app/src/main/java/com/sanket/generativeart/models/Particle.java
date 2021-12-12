package com.sanket.generativeart.models;

import java.util.Random;

public class Particle {
    public int distFromOrigin = 0;
    private final double direction;
    private final double directionCosine;
    private final double directionSine;
    public int color;
    public int x;
    public int y;
    private int initX;
    private int initY;

    public Particle(int x, int y, int color) {
        init(x, y,color);
        this.direction = 2*Math.PI * new Random().nextInt(NO_OF_DIRECTION)/NO_OF_DIRECTION;
        this.directionCosine = Math.cos(direction);
        this.directionSine = Math.sin(direction);
        //this.color = new Random().nextInt(3);
        this.color = color;
    }

    public void init(int x, int y, int color) {
        distFromOrigin = 0;
        this.initX = this.x = x;
        this.initY = this.y = y;
        this.color = color;

    }

    public synchronized void move(){
        distFromOrigin +=2;
        x = (int) (initX+distFromOrigin*directionCosine);
        y = (int) (initY+distFromOrigin*directionSine);
    }
    private final static int NO_OF_DIRECTION = 400;

}