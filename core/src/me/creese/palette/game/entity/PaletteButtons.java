package me.creese.palette.game.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.SnapshotArray;

import me.creese.palette.game.entity.buttons.PaletteButton;
import me.creese.palette.game.screens.Loading;
import me.creese.palette.game.util.P;

public class PaletteButtons extends Group {


    private boolean isPan;
    private PaletteButton selectButton;

    public PaletteButtons() {
        setBounds(50,80, P.WIDTH,180);


        addListener(new ActorGestureListener(){
            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                isPan = true;
                moveBy(deltaX,0);
                checkBounds();
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isPan = false;
            }

            @Override
            public void fling(InputEvent event, float velocityX, float velocityY, int button) {
                if(Math.abs(velocityX) > 700)
                addAction(Actions.moveBy(velocityX,0,0.4f));
            }
        });
    }

    private void checkBounds() {
        if(getX() > 50) setX(50);
        if(getX() < -getWidth()+P.WIDTH) setX(-getWidth()+P.WIDTH);
    }

    public boolean isPan() {
        return isPan;
    }

    public void setSelectButton(PaletteButton selectButton) {
        this.selectButton = selectButton;
    }

    public PaletteButton getSelectButton() {
        return selectButton;
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        checkBounds();
    }
}
