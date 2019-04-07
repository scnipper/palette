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

import me.creese.palette.game.entity.bonus.Bonus;
import me.creese.palette.game.entity.bonus.BonusGroup;
import me.creese.palette.game.util.P;

public class PixelsControl extends Group {

    private static final float MARGIN = 150;
    private final Actor moveActor;
    private final OrthographicCamera camera;
    private final Actor zoomActor;
    private float currZoom = P.START_ZOOM;
    private boolean isPan;
    private boolean isZoom;
    private SquadPixel downPixelSquad;
    private int realWidth;
    private int realHeight;
    private boolean isMaxZoom;
    private boolean isMoveToMaxZoom;
    private float zoomMax;
    private boolean isDoubleTap;
    private BigPixel lastBigPixel;
    private boolean isOneTap;

    public PixelsControl(Stage stagePixel, BonusGroup bonusGroup) {
        setBounds(0, 0, P.WIDTH, P.HEIGHT);
        camera = (OrthographicCamera) stagePixel.getCamera();

        moveActor = new Actor();
        zoomActor = new Actor();
        addActor(moveActor);
        addActor(zoomActor);
        addListener(new ActorGestureListener() {

            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {



                if (count == 1) isOneTap = true;
                if (count >= 2) isDoubleTap = true;
            }

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                isPan = true;

/*                boolean continuePan = true;
                Bonus activateBonus = bonusGroup.getActivateBonus();
                if (activateBonus != null && downPixelSquad != null) {
                    System.out.println(x+" "+y);
                    System.out.println(downPixelSquad.getDownX()+" "+downPixelSquad.getDownY());
                    downPixelSquad.addDownX(deltaX);
                    downPixelSquad.addDownY(deltaY);
                    lastBigPixel = downPixelSquad.touchDown(true);
                    GroupPixels groupPixel = (GroupPixels) stagePixel.getActors().get(0);
                    continuePan = activateBonus.panFinger(groupPixel,PixelsControl.this,lastBigPixel);
                }*/

                    float zoom = camera.zoom;
                    camera.translate(-(deltaX * 2 * zoom), -(deltaY * 2 * zoom));

                    if (moveActor.getActions().size == 0) boundPos(true);




            }

            @Override
            public void zoom(InputEvent event, float initialDistance, float distance) {
                OrthographicCamera camera = (OrthographicCamera) stagePixel.getCamera();

                camera.zoom = (initialDistance / distance) * currZoom;

                isZoom = true;


                checkCameraZoom();


                if (camera.zoom < 0.2f) camera.zoom = 0.2f;

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                currZoom = camera.zoom;

                Bonus activateBonus = bonusGroup.getActivateBonus();
                if (isDoubleTap) {

                    if (activateBonus != null) {

                        GroupPixels groupPixel = (GroupPixels) stagePixel.getActors().get(0);
                        activateBonus.doubleTapFinger(groupPixel, PixelsControl.this, lastBigPixel);


                    }
                }

                if (!isPan && !isZoom && downPixelSquad != null) {
                    lastBigPixel = downPixelSquad.touchDown(false);
                    if (lastBigPixel != null) {
                        if (lastBigPixel.getState().equals(BigPixel.State.PAINT) && !lastBigPixel.getBonusAdd()) {
                            lastBigPixel.setBonusAdd(true);
                            if (bonusGroup.getActivateBonus() == null) {
                                bonusGroup.addRandomBonus(event.getStageX(), event.getStageY());
                            }
                        }

                        if (activateBonus != null ) {
                            GroupPixels groupPixel = (GroupPixels) stagePixel.getActors().get(0);

                            activateBonus.upFinger(groupPixel,PixelsControl.this,lastBigPixel);
                        }
                    }


                }


                downPixelSquad = null;

                if (isZoom && !isMoveToMaxZoom) {
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
                isDoubleTap = false;


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

    public void animateZoomToMax() {
        zoomActor.setScale(camera.zoom);

        zoomActor.addAction(Actions.sequence(Actions.scaleTo(zoomMax,1,0.5f),Actions.run(new Runnable() {
            @Override
            public void run() {
                isMaxZoom = true;
                boundPos(false);
            }
        })));
    }
    private void checkCameraZoom() {
        if (camera.zoom > zoomMax) {
            isMaxZoom = true;
            camera.zoom = zoomMax;
        } else {
            isMaxZoom = false;
            isMoveToMaxZoom = false;

        }
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
            if (camera.position.y < -(hScreen / 2 - P.HEIGHT - MARGIN)) {
                setY = -(hScreen / 2 - P.HEIGHT - MARGIN);
            }
            if (camera.position.y > hScreen / 2 - realHeight + P.HEIGHT - MARGIN) {
                setY = hScreen / 2 - realHeight + P.HEIGHT - MARGIN;
            }

        }

        if (isPan) {
            camera.position.x = setX;
            camera.position.y = setY;
        } else {
            moveActor.setPosition(camera.position.x, camera.position.y);
            moveActor.setUserObject(false);
            moveActor.addAction(Actions.sequence(Actions.moveTo(setX, setY, 0.2f), Actions.run(new Runnable() {
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

    public boolean isMaxZoom() {
        return isMaxZoom;
    }

    public void setRealSize(int realWidth, int realHeight) {
        isMaxZoom = false;
        this.realWidth = realWidth;
        this.realHeight = realHeight;

        zoomMax = (realWidth + MARGIN * 2) / (P.WIDTH);
        checkCameraZoom();
        if (isMaxZoom) animateZoomToMax();
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

        Array<Action> zoomActions = zoomActor.getActions();

        if(zoomActions.size > 0) {
            camera.zoom = zoomActor.getScaleX();
        }
    }
}
