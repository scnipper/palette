package me.creese.palette.game.entity;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;

import me.creese.palette.game.screens.GameScreen;

/**
 * Модель пикселя
 */
public class BigPixel {
    public static final int WIDTH_PIXEL = 32;
    public static final int HEIGHT_PIXEL = 32;
    // номер цвета
    private final int numColor;
    // цвет для сплошной заливки
    private final Color color;
    // позиция по x в массиве сетки
    private final int posX;
    // позиция по y в массиве сетки
    private final int posY;
    // состояние
    private State state;
    // ошибочный цвет
    private Color wrongColor;
    private SquadPixel squad;
    // если true бонусы не добавляются при нажатии
    private boolean bonusAdd;
    private boolean isVisible;




    public enum State {
        PAINT,NOT_PAINT,WRONG_PAINT;
    }
    public BigPixel(int numColor, Color color, int posX, int posY) {


        this.numColor = numColor;
        this.color = color;
        this.posX = posX;
        this.posY = posY;

        isVisible = true;
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
        ArrayList<BigPixel> history = squad.getRoot().getGameViewForName(GameScreen.class).getHistory();
        switch (state) {
            case WRONG_PAINT:
                history.remove(this);
                break;
            case PAINT:
                history.add(this);
                break;
        }
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

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
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
