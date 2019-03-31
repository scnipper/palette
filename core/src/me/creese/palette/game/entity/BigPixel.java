package me.creese.palette.game.entity;

import com.badlogic.gdx.graphics.Color;

public class BigPixel {
    public static final int WIDTH_PIXEL = 32;
    public static final int HEIGHT_PIXEL = 32;
    private final int numColor;
    private final Color color;
    private final int posX;
    private final int posY;
    private State state;

    public enum State {
        PAINT,NOT_PAINT,WRONG_PAINT
    }

    public BigPixel(int numColor, Color color, int posX, int posY) {

        this.numColor = numColor;
        this.color = color;
        this.posX = posX;
        this.posY = posY;

        state = State.NOT_PAINT;

    }


    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Color getColor() {
        return color;
    }

    public int getNumColor() {
        return numColor;
    }

    @Override
    public String toString() {
        return "BigPixel{" + "numColor=" + numColor + ", color=" + color + ", posX=" + posX + ", posY=" + posY + '}';
    }
}
