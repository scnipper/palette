package me.creese.palette.game.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Array;

import me.creese.palette.game.util.P;

public class PixelsControl extends Group {

    private static final float MARGIN = 150;
    private final Actor moveActor;
    private final OrthographicCamera camera;
    private float currZoom = 1;
    private boolean isPan;
    private boolean isZoom;
    private SquadPixel downPixelSquad;
    private int realWidth;
    private int realHeight;
    private boolean isMaxZoom;
    private boolean isMoveToMaxZoom;

    public PixelsControl(Stage stagePixel) {
        setBounds(0, 0, P.WIDTH, P.HEIGHT);
        camera = (OrthographicCamera) stagePixel.getCamera();

        moveActor = new Actor();
        addActor(moveActor);
        addListener(new ActorGestureListener() {

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                isPan = true;


                float zoom = camera.zoom;
                camera.translate(-(deltaX * 2 * zoom), -(deltaY * 2 * zoom));

                if(moveActor.getActions().size == 0)
                boundPos(true);


            }

            @Override
            public void zoom(InputEvent event, float initialDistance, float distance) {
                OrthographicCamera camera = (OrthographicCamera) stagePixel.getCamera();

                isZoom = true;
                camera.zoom = (initialDistance / distance) * currZoom;

                float zoomMax = (realWidth + MARGIN * 2) / (P.WIDTH);
                if (camera.zoom > zoomMax) {


                    isMaxZoom = true;
                    camera.zoom = zoomMax;
                } else {
                    isMaxZoom = false;
                    isMoveToMaxZoom = false;

                }


                if (camera.zoom < 0.2f) camera.zoom = 0.2f;

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                currZoom = camera.zoom;

                if (!isPan && !isZoom && downPixelSquad != null) {
                    downPixelSquad.touchDown();
                }
                downPixelSquad = null;

                if(isZoom && !isMoveToMaxZoom) {
                    boundPos(false);
                }

         /*       if (isMaxZoom && isZoom) {
                    float heightScreen = P.HEIGHT - ((P.HEIGHT / 2) * (camera.zoom));
                    moveActor.setPosition(camera.position.x,camera.position.y);
                    moveActor.addAction(Actions.moveTo((P.WIDTH*camera.zoom)/2-150,
                            (P.HEIGHT * camera.zoom)/2 - realHeight/2.f,2));
                }*/

                isZoom = false;
                isPan = false;


            }

            @Override
            public void fling(InputEvent event, float velocityX, float velocityY, int button) {
                if ((Math.abs(velocityX) > 1000 || Math.abs(velocityY) > 1000) && moveActor.getActions().size == 0 && !isMaxZoom) {
                    moveActor.setPosition(camera.position.x, camera.position.y);
                    moveActor.setUserObject(true);
                    moveActor.addAction(Actions.moveBy(-velocityX, -velocityY, 0.7f));
                }
            }
        });
    }

    private void boundPos(boolean isPan) {



        float wScreen = P.WIDTH * camera.zoom;
        float hScreen = P.HEIGHT * camera.zoom;
        float setX = camera.position.x;
        float setY = camera.position.y;

        if (camera.position.x < wScreen / 2 - MARGIN) {
            setX = wScreen / 2 - MARGIN;
        }

        if (camera.position.x > realWidth - wScreen / 2 + MARGIN) {
            setX = realWidth - wScreen / 2 + MARGIN;
        }



        if (!isMaxZoom) {
            if (camera.position.y > (Math.abs(hScreen / 2 - P.HEIGHT) + MARGIN)) {
                setY = (Math.abs(hScreen / 2 - P.HEIGHT) + MARGIN);
            }
            if (camera.position.y < ((realHeight - Math.abs(hScreen / 2 + P.HEIGHT)) + MARGIN) * -1) {
                setY = ((realHeight - Math.abs(hScreen / 2 + P.HEIGHT)) + MARGIN) * -1;
            }
        } else {
            if (camera.position.y < -(hScreen/2-P.HEIGHT-MARGIN)) {
                setY = -(hScreen/2-P.HEIGHT-MARGIN);
            }
            if (camera.position.y > hScreen/2-realHeight+P.HEIGHT-MARGIN) {
                setY = hScreen/2-realHeight+P.HEIGHT-MARGIN;
            }

        }

        if(isPan) {
            camera.position.x = setX;
            camera.position.y = setY;
        } else {
            moveActor.setPosition(camera.position.x,camera.position.y);
            moveActor.setUserObject(false);
            moveActor.addAction(Actions.sequence(Actions.moveTo(setX,setY,0.2f),Actions.run(new Runnable() {
                @Override
                public void run() {
                    isMoveToMaxZoom = true;
                }
            })));
        }

    }


    public void setDownPixelSquad(SquadPixel downPixelSquad) {
        this.downPixelSquad = downPixelSquad;
    }

    public void setRealSize(int realWidth, int realHeight) {
        this.realWidth = realWidth;
        this.realHeight = realHeight;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        Array<Action> actions = moveActor.getActions();
        if (actions.size > 0) {

            camera.position.x = moveActor.getX();
            camera.position.y = moveActor.getY();
            if (((Boolean) moveActor.getUserObject())) {
                boundPos(true);
            }


        }
    }
}
