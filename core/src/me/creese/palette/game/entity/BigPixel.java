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
    private Color wrongColor;
    private SquadPixel squad;
    private boolean bonusAdd;




    public enum State {
        PAINT,NOT_PAINT,WRONG_PAINT;
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

    public void setWrongColor(Color wrongColor) {
        this.wrongColor = wrongColor;
    }

    public Color getWrongColor() {
        return wrongColor;
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

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setSquad(SquadPixel squad) {

        this.squad = squad;
    }
    public void setBonusAdd(boolean bonusAdd) {

        this.bonusAdd = bonusAdd;
    }

    public boolean getBonusAdd() {
        return bonusAdd;
    }
    public SquadPixel getSquad() {
        return squad;
    }

    @Override
    public String toString() {
        return "BigPixel{" + "numColor=" + numColor + ", color=" + color + ", posX=" + posX + ", posY=" + posY + '}';
    }
}
