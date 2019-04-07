package me.creese.palette.game.entity;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import me.creese.palette.game.entity.buttons.PaletteButton;
import me.creese.palette.game.util.P;

public class PaletteButtons extends Group {


    private boolean isPan;
    private PaletteButton selectButton;

    public PaletteButtons() {



        addListener(new ActorGestureListener() {
            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                isPan = true;
                moveBy(deltaX, 0);
                checkBounds();
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isPan = false;
            }

            @Override
            public void fling(InputEvent event, float velocityX, float velocityY, int button) {
                if (Math.abs(velocityX) > 700) addAction(Actions.moveBy(velocityX, 0, 0.4f));
            }
        });
    }

    private void checkBounds() {
        float worldWidth = getStage().getViewport().getWorldWidth();


        if (getX() > 50) setX(50);
        if (getX() < -getWidth() + worldWidth) setX(-getWidth() + worldWidth);
    }

    public boolean isPan() {
        return isPan;
    }

    public PaletteButton getSelectButton() {
        return selectButton;
    }

    public void setSelectButton(PaletteButton selectButton) {
        this.selectButton = selectButton;
    }

    @Override
    protected void setParent(Group parent) {
        super.setParent(parent);
        if (parent != null) {
            setBounds(50, 80, parent.getStage().getViewport().getWorldWidth(), 180);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        checkBounds();
    }
}
