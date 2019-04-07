package me.creese.palette.game.entity;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.SnapshotArray;

import java.io.File;

import me.creese.palette.game.entity.buttons.LoadImageBtn;
import me.creese.palette.game.entity.buttons.SelectImageBtn;
import me.creese.palette.game.entity.buttons.SelectImpl;
import me.creese.palette.game.screens.Loading;
import me.creese.palette.game.util.FontUtil;
import me.creese.palette.game.util.P;
import me.creese.palette.game.util.S;
import me.creese.util.display.Display;

import static me.creese.palette.game.entity.buttons.SelectImageBtn.OPEN;

public class SelectImageMenu extends Group {

    private final Display root;
    private final Actor moveActor;
    private final BitmapFont font;
    private Rectangle rectangle;
    private Rectangle scissors;
    private boolean isPan;
    private float xOffset;
    private float yOffset;

    public SelectImageMenu(Display root) {
        this.root = root;
        rectangle = new Rectangle(0,0,P.WIDTH,P.HEIGHT-200);

        font = P.get().asset.get(Loading.FONT_ROBOTO_BOLD, BitmapFont.class);

        moveActor = new Actor();
        moveActor.setWidth(P.WIDTH);


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
                        ((SelectImpl) hit).selectImage();
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
        System.out.println("add images");
        addActor(moveActor);


        xOffset = 120;
        yOffset = 1300;
        moveActor.setPosition(0,0);
        moveActor.setHeight(P.HEIGHT-200);
        for (int i = 0; i < P.COUNT_IMAGES; i++) {
            SelectImageBtn selectImage = new SelectImageBtn(root, i,null);
            selectImage.setPosition(xOffset, yOffset);
           /* if (i < openImages) {
                selectImage.setState(SelectImageBtn.State.OPEN);
            } else if (i < unlockImages) {
                selectImage.setState(SelectImageBtn.State.UNLOCK);
            }*/

            xOffset += 120 + selectImage.getWidth();

            boundOffset(selectImage.getWidth());
            addActor(selectImage);
        }

        yOffset-=100;
        inflateMoveActor(600);


        float finalY = yOffset;
        addActor(new Actor(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                FontUtil.drawText(batch,font,"Свое изображение",0, finalY,0.9f,P.BLACK_FONT_COLOR,P.WIDTH, Align.center);
            }
        });

        yOffset-=500;
        LoadImageBtn loadImageBtn = new LoadImageBtn(root);
        loadImageBtn.setPosition(120,yOffset);
        addActor(loadImageBtn);
    }

    private void boundOffset(float width) {
        if (xOffset + width > P.WIDTH) {
            xOffset = 120;
            yOffset -= 400;
            if(yOffset <=0) {
                inflateMoveActor(400);
            }
        }
    }

    private void inflateMoveActor(float delta) {
        moveActor.setHeight(moveActor.getHeight() + delta);
        moveActor.setY(moveActor.getY() - delta);
    }

    public void freeTexturesExcept(int numExcept) {
        SnapshotArray<Actor> children = getChildren();

        for (Actor child : children) {
            if(child instanceof SelectImageBtn) {
                SelectImageBtn selectImage = (SelectImageBtn) child;
                if (numExcept != selectImage.getNumImage()) {
                    selectImage.getTexture().getTexture().dispose();
                }
            }
        }
    }

    private void addLoadImages() {

        if(Gdx.app.getType().equals(Application.ApplicationType.Desktop)) return;

        File images = new File(Gdx.files.getLocalStoragePath()+"/images");


        String[] list = images.list();
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                SelectImageBtn selectImageBtn = new SelectImageBtn(root, P.COUNT_IMAGES + i, images.getAbsolutePath()+"/"+list[i]);

                selectImageBtn.setState(OPEN);
                selectImageBtn.setPosition(xOffset,yOffset);
                addActor(selectImageBtn);
                xOffset += 120 + selectImageBtn.getWidth();
                boundOffset(selectImageBtn.getWidth());


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

           /* SnapshotArray<Actor> children = getChildren();
            if(children.size > 0) {
                for (Actor child : children) {
                    if(child instanceof SelectImageBtn) {
                        SelectImageBtn selectImage = (SelectImageBtn) child;
                        selectImage.loadTexture();
                    }
                }
            } else {*/

                addImages();
                addLoadImages();
            //}

            moveActor.setZIndex(999999);
        } else {
            freeTexturesExcept(-1);
            clearChildren();
        }
    }
}
