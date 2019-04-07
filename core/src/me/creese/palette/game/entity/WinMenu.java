package me.creese.palette.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;

import me.creese.palette.game.screens.GameScreen;
import me.creese.palette.game.screens.Loading;
import me.creese.palette.game.util.FontUtil;
import me.creese.palette.game.util.P;
import me.creese.palette.game.util.Shapes;
import me.creese.util.display.Display;

public class WinMenu extends Group {

    private final Display root;
    private final Shapes shapes;
    private final BitmapFont font;
    private final Actor hideBtn;
    private final Actor backBtn;
    private String timeText;
    private long endTime;

    public WinMenu(Display root) {
        this.root = root;
        shapes = new Shapes();
        setBounds(P.WIDTH/2-300,700,600,300);
        font = P.get().asset.get(Loading.FONT_ROBOTO_BOLD, BitmapFont.class);

        hideBtn = new Actor();
        backBtn = new Actor();
        hideBtn.setBounds(50,30,150,50);
        backBtn.setBounds(getWidth()-200,30,150,50);

        hideBtn.addListener(new ActorGestureListener(){
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                remove();
            }
        });

        backBtn.addListener(new ActorGestureListener(){
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                root.getGameViewForName(GameScreen.class).onBackPress();
                remove();
            }
        });

        addActor(hideBtn);
        addActor(backBtn);
    }

    private String formatTime(long time) {
        long tmp = time;
        int hour = (int) (time / 3600);
        tmp -= hour * 3600;
        int min = (int) (tmp / 60);
        int sec = (int) (tmp - min * 60);

        String hourText = hour < 10 ? "0" + hour : "" + hour;
        String minText = min < 10 ? "0" + min : "" + min;
        String secText = sec < 10 ? "0" + sec : "" + sec;


        return hourText + ":" + minText + ":" + secText;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    protected void setParent(Group parent) {
        super.setParent(parent);
        if (parent != null) {
            timeText = "Ваше время: "+formatTime(endTime/1000);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);


        batch.end();
        shapes.setProjMatrix(batch.getProjectionMatrix());

        shapes.setColor(P.BLACK_FONT_COLOR);
        shapes.rectRound(getX(),getY(),getWidth(),getHeight(),20);
        shapes.setColor(Color.WHITE);
        shapes.rectRound(getX()+5,getY()+5,getWidth()-10,getHeight()-10,20);

        shapes.setColor(P.BLACK_FONT_COLOR);
        shapes.rectRound(backBtn.getX()+getX(),getY()+backBtn.getY(),backBtn.getWidth(),backBtn.getHeight(),10);
        shapes.rectRound(hideBtn.getX()+getX(),getY()+hideBtn.getY(),hideBtn.getWidth(),hideBtn.getHeight(),10);

        shapes.flush();

        batch.begin();
        FontUtil.drawText(batch,font,"Поздравляем!",getX(),getY()+getHeight()-30,0.7f,P.ACTIVATE_BONUS_COLOR,getWidth(), Align.center);

        FontUtil.drawText(batch,font,timeText,getX(),getY()+getHeight()-120,0.4f,P.BLACK_FONT_COLOR,getWidth(),Align.center);
        FontUtil.drawText(batch,font,"В меню",backBtn.getX()+getX(),getY()+backBtn.getY(),0.3f,Color.WHITE,backBtn.getWidth(),Align.center,false,backBtn.getHeight());
        FontUtil.drawText(batch,font,"Скрыть",hideBtn.getX()+getX(),getY()+hideBtn.getY(),0.3f,Color.WHITE,hideBtn.getWidth(),Align.center,false,hideBtn.getHeight());



    }
}
