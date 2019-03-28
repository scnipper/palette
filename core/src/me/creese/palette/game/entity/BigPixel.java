package me.creese.palette.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BigPixel extends Actor {
    public static final int WIDTH_PIXEL = 128;
    public static final int HEIGHT_PIXEL = 128;
    private final int numColor;
    private final Color color;
    private final int posX;
    private final int posY;

    public BigPixel(int numColor, Color color, int posX, int posY) {
        this.numColor = numColor;
        this.color = color;
        this.posX = posX;
        this.posY = posY;
    }

    @Override
    public String toString() {
        return "BigPixel{" + "numColor=" + numColor + ", color=" + color + ", posX=" + posX + ", posY=" + posY + '}';
    }
}
