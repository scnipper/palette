package me.creese.palette.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import me.creese.palette.game.entity.SelectImageMenu;
import me.creese.palette.game.entity.buttons.StartMenuButton;
import me.creese.palette.game.util.FontUtil;
import me.creese.palette.game.util.P;
import me.creese.palette.game.util.TexturePrepare;
import me.creese.util.display.Display;
import me.creese.util.display.GameView;

public class MainScreen extends GameView {

    private Group startButtons;
    private SelectImageMenu selectImageMenu;
    private String headText;

    public MainScreen(Display root) {
        super(new FitViewport(P.WIDTH, P.HEIGHT), root, P.rootBatch);

        addButtons();

        BitmapFont font = P.get().asset.get(Loading.FONT_ROBOTO_BOLD, BitmapFont.class);
        addActor(new Actor(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                FontUtil.drawText(batch,font,headText,0,P.HEIGHT-100,0.9f,P.BLACK_FONT_COLOR,P.WIDTH, Align.center);
            }
        });



    }

    private void addButtons() {
        startButtons = new Group();
        addActor(startButtons);
        makeButton("Начать игру",1000,new ActorGestureListener(){
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                startButtons.remove();

                if (selectImageMenu == null) {
                    selectImageMenu = new SelectImageMenu(getRoot());
                }

                headText = "Выберите изображение";
                addActor(selectImageMenu);


            }
        });
        makeButton("Статистика",800,new ActorGestureListener(){
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {

            }
        });
        makeButton("Выход",600,new ActorGestureListener(){
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
            }
        });
    }

    private void makeButton(String text, float yPos, ActorGestureListener listener) {
        StartMenuButton btn = new StartMenuButton(getRoot().getTransitObject(TexturePrepare.class), text);
        btn.setY(yPos);
        btn.addListener(listener);
        startButtons.addActor(btn);
    }

    @Override
    public void addRoot(Display display) {
        super.addRoot(display);
        if (display != null) {
            headText = "Palette Game";
        }
    }

    @Override
    public void onBackPress() {
        if (selectImageMenu != null) {
            if (selectImageMenu.getParent() != null) {
                selectImageMenu.remove();
                addActor(startButtons);
                headText = "Palette Game";
            }
        }
    }
}
