package me.creese.palette.game.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.SnapshotArray;

import me.creese.palette.game.util.P;
import me.creese.palette.game.util.S;
import me.creese.util.display.Display;

public class SelectImageMenu extends Group {

    private final Display root;
    private final Actor moveActor;
    private Rectangle rectangle;
    private Rectangle scissors;
    private boolean isPan;

    public SelectImageMenu(Display root) {
        this.root = root;
        rectangle = new Rectangle(0,0,P.WIDTH,P.HEIGHT-200);


        moveActor = new Actor();
        moveActor.setWidth(P.WIDTH);
        moveActor.setHeight(P.HEIGHT-200);
        addActor(moveActor);
        moveActor.addListener(new ActorGestureListener(){
            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                SelectImageMenu.this.moveBy(0,deltaY);
                isPan = true;
                boundPos();
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {


                if(!isPan) {
                    moveActor.setTouchable(Touchable.disabled);
                    Actor hit = SelectImageMenu.this.hit(event.getStageX(), event.getStageY()-getY(), true);

                    if (hit != null) {
                        ((SelectImage) hit).selectImage();
                    }
                    moveActor.setTouchable(Touchable.enabled);
                }

                isPan = false;
            }

            @Override
            public void fling(InputEvent event, float velocityX, float velocityY, int button) {
                if(Math.abs(velocityY) > 500)
                addAction(Actions.moveBy(0,velocityY,0.5f));
            }
        });
    }

    private void boundPos() {
        if(getY() < 0) setY(0);
        if(getY() > moveActor.getHeight()-(P.HEIGHT-200)) setY(moveActor.getHeight()-(P.HEIGHT-200));
    }

    private void addImages() {

        int openImages = P.get().saves.getInteger(S.COUNT_OPEN_IMAGES, -1);
        int unlockImages = P.get().saves.getInteger(S.COUNT_UNLOCK_IMAGES, 1);

        float x = 120, y = 1300;
        for (int i = 0; i < P.COUNT_IMAGES; i++) {
            SelectImage selectImage = new SelectImage(root, i);
            selectImage.setPosition(x, y);

            if (i < openImages) {
                selectImage.setState(SelectImage.State.OPEN);
            } else if (i < unlockImages) {
                selectImage.setState(SelectImage.State.UNLOCK);
            }

            x += 120 + selectImage.getWidth();

            if (x + selectImage.getWidth() > P.WIDTH) {
                x = 120;
                y -= 400;
                if(y <=0) {
                    moveActor.setHeight(moveActor.getHeight() + 400);
                    moveActor.setY(moveActor.getY() - 400);
                }
            }
            addActor(selectImage);
        }
    }

    public void freeTexturesExcept(int numExcept) {
        SnapshotArray<Actor> children = getChildren();

        for (Actor child : children) {
            if(child instanceof SelectImage) {
                SelectImage selectImage = (SelectImage) child;
                if (numExcept != selectImage.getNumImage()) {
                    selectImage.getTexture().getTexture().dispose();
                }
            }
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

            boundPos();

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        batch.begin();
        ScissorStack.pushScissors(scissors);
        super.draw(batch, parentAlpha);
        ScissorStack.popScissors();
    }

    @Override
    protected void setParent(Group parent) {
        super.setParent(parent);
        if (parent != null) {

            if (scissors == null) {
                scissors = new Rectangle();

                ScissorStack.calculateScissors(parent.getStage().getCamera(),
                        parent.getStage().getBatch().getTransformMatrix(),rectangle,scissors);
            }

            SnapshotArray<Actor> children = getChildren();
            if(children.size > 1) {
                for (Actor child : children) {
                    if(child instanceof SelectImage) {
                        SelectImage selectImage = (SelectImage) child;
                        selectImage.loadTexture();
                    }
                }
            } else addImages();

            moveActor.setZIndex(999999);
        } else {
            freeTexturesExcept(-1);
        }
    }
}
