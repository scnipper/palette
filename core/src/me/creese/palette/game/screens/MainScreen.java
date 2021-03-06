package me.creese.palette.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import me.creese.palette.game.entity.SelectImageMenu;
import me.creese.palette.game.entity.buttons.StartMenuButton;
import me.creese.palette.game.util.FontUtil;
import me.creese.palette.game.util.P;
import me.creese.palette.game.util.S;
import me.creese.palette.game.util.TexturePrepare;
import me.creese.util.display.Display;
import me.creese.util.display.GameView;

public class MainScreen extends GameView {

    private final BitmapFont font;
    private final StatMenu statMenu;
    private final Group selectModeMenu;
    private Group startButtons;
    private SelectImageMenu selectImageMenu;
    private String headText;

    public MainScreen(Display root) {
        super(new ExtendViewport(P.WIDTH, P.HEIGHT), root, P.get().rootBatch);
        statMenu = new StatMenu();
        addButtons();

        font = P.get().asset.get(Loading.FONT_ROBOTO_BOLD, BitmapFont.class);
        addActor(new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                FontUtil.drawText(batch, font, headText, 0, getRootStage().getViewport().getWorldHeight() - 100, 0.9f, P.BLACK_FONT_COLOR, getRootStage().getViewport().getWorldWidth(), Align.center);
            }
        });

        selectImageMenu = new SelectImageMenu(getRoot());
        selectModeMenu = new Group();


    }

    /**
     * Меню выбора режима игры
     */
    private void showModeGameMenu() {
        startButtons.remove();
        headText = "Выберите режим игры";
        addActor(selectModeMenu);
        if(selectModeMenu.getChildren().size == 0) {
            makeButton("Обычный",800,selectModeMenu,new ActorGestureListener(){
                @Override
                public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    P.get().isSecretMode = false;

                    showSelectImage();

                }
            });
            makeButton("Завеса тайны",600,selectModeMenu,new ActorGestureListener(){
                @Override
                public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    P.get().isSecretMode = true;

                    showSelectImage();
                }
            });
        }
    }

    /**
     * Открыть меню выбора изображений
     */
    private void showSelectImage() {
        selectModeMenu.remove();

        headText = "Выберите изображение";
        addActor(selectImageMenu);

    }

    /**
     * Показать статистику
     */
    private void showStat() {
        startButtons.remove();
        headText = "Статистика";



        addActor(statMenu);

        if(statMenu.getChildren().size == 0) {
            makeButton("Назад",400,statMenu,new ActorGestureListener(){
                @Override
                public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    onBackPress();
                }
            });
        }
    }

    /**
     * Главные кнопки
     */
    private void addButtons() {
        startButtons = new Group();
        addActor(startButtons);
        makeButton("Начать игру", 1000,startButtons, new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                showModeGameMenu();


            }
        });
        makeButton("Статистика", 800,startButtons, new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                showStat();
            }
        });
        makeButton("Выход", 600,startButtons, new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
            }
        });


    }

    /**
     * Создание одной кнопки
     *
     * @param text
     * @param yPos
     * @param listener
     */
    private void makeButton(String text, float yPos, Group container,ActorGestureListener listener ) {
        StartMenuButton btn = new StartMenuButton(getRoot().getTransitObject(TexturePrepare.class), text);
        btn.setY(yPos);
        btn.addListener(listener);
        container.addActor(btn);
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

        statMenu.remove();
        selectImageMenu.remove();
        selectModeMenu.remove();
        addActor(startButtons);
        headText = "Palette Game";

    }

    class StatMenu extends Group {

        private String pixelPerLaunch;
        private String timePerPixel;
        private boolean isNoData;
        private String textPixel;
        private String wrongPixelPerLaunch;
        private String textWrongPixel;
        private float worldHeight;
        private float worldWidth;


        @Override
        protected void setParent(Group parent) {
            super.setParent(parent);
            if (parent != null) {
                long countLaunch = P.get().saves.getLong(S.COUNT_LAUNCH);
                long pixelPaint = P.get().saves.getLong(S.PIXELS_PAINT);
                long allTime = P.get().saves.getLong(S.ALL_TIME);
                long wrongPixel = P.get().saves.getLong(S.WRONG_PIXELS_PAINT);
                worldHeight = parent.getStage().getViewport().getWorldHeight();
                worldWidth = parent.getStage().getViewport().getWorldWidth();
                textPixel = String.valueOf(pixelPaint);
                textWrongPixel = String.valueOf(wrongPixel);
                if(pixelPaint == 0) {
                    isNoData = true;
                } else {
                    isNoData = false;
                    pixelPerLaunch = String.valueOf(pixelPaint / countLaunch);
                    wrongPixelPerLaunch = String.valueOf(wrongPixel / countLaunch);
                    long tP = allTime / pixelPaint;


                    if(tP > 1000) {
                        timePerPixel = String.valueOf(tP/1000)+" сек";
                    } else {
                        timePerPixel = String.valueOf(tP)+" мс";
                    }
                }


            }
        }

        /**
         * Один пункт статистики
         * @param batch
         * @param text Текст статистики
         * @param value Значение
         * @param y
         */
        private void drawItem(Batch batch,String text,String value,float y) {

            FontUtil.drawText(batch, font, text, 50, worldHeight - y, 0.5f, P.BLACK_FONT_COLOR);
            FontUtil.drawText(batch, font, value, 50, worldHeight - y, 0.5f, P.BLACK_FONT_COLOR, worldWidth - 100, Align.right);
        }
        @Override
        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch,parentAlpha);

            if(isNoData) {
                FontUtil.drawText(batch,font,"Нет данных",0,0,0.8f,P.BLACK_FONT_COLOR,worldWidth,Align.center,false,worldHeight);
            } else {

                drawItem(batch,"Пикселей за одну сессию: ",pixelPerLaunch,400);
                drawItem(batch,"Всего пикселей: ",textPixel,470);
                drawItem(batch,"Время закрашивания одного пикселя: ",timePerPixel,540);
                drawItem(batch,"Ошибок за сессию: ",wrongPixelPerLaunch,610);
                drawItem(batch,"Всего ошибок: ",textWrongPixel,680);
            }
        }
    }
}
