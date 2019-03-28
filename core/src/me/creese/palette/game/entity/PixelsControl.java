package me.creese.palette.game.entity;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import me.creese.palette.game.util.P;

public class PixelsControl extends Group {

    public PixelsControl(Stage stagePixel) {
        setBounds(0,0, P.WIDTH,P.HEIGHT);

        addListener(new ActorGestureListener(){
            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {

                OrthographicCamera camera = (OrthographicCamera) stagePixel.getCamera();
                camera.translate(-(deltaX*2),-(deltaY*2));

                //System.out.println(event.getStageX()+" "+event.getStageY());

            }
        });
    }
}
