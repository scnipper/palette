package me.creese.palette.game.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import me.creese.palette.game.util.P;

public class PixelsControl extends Group {

    private float currZoom = 1;
    private float stageX;
    private float stageY;
    private boolean isDown;
    private BigPixel downPixel;
    private boolean isPan;
    private boolean isZoom;

    public PixelsControl(Stage stagePixel) {
        setBounds(0, 0, P.WIDTH, P.HEIGHT);


        addListener(new ActorGestureListener() {

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                isPan = true;
                OrthographicCamera camera = (OrthographicCamera) stagePixel.getCamera();
                float zoom = camera.zoom;
                camera.translate(-(deltaX * 2 * zoom), -(deltaY * 2 * zoom));


            }

            @Override
            public void zoom(InputEvent event, float initialDistance, float distance) {
                OrthographicCamera camera = (OrthographicCamera) stagePixel.getCamera();

                isZoom = true;

                camera.zoom = (initialDistance / distance) * currZoom;

                if (camera.zoom > 3) camera.zoom = 3;

                if (camera.zoom < 0.1f) camera.zoom = 0.1f;

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                OrthographicCamera camera = (OrthographicCamera) stagePixel.getCamera();
                currZoom = camera.zoom;

                if (!isPan && !isZoom && downPixel != null) {
                    downPixel.setPaint(true);
                }
                downPixel = null;
                isZoom = false;
                isPan = false;
            }
        });
    }

    public void setDownPixel(BigPixel downPixel) {
        this.downPixel = downPixel;
    }

}
