package me.creese.palette.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;

import javax.xml.soap.Text;

import me.creese.palette.game.screens.GameScreen;
import me.creese.palette.game.screens.Loading;
import me.creese.palette.game.util.FTextures;
import me.creese.palette.game.util.FontUtil;
import me.creese.palette.game.util.P;
import me.creese.palette.game.util.TexturePrepare;
import me.creese.util.display.Display;

public class ScoreView extends Actor {

    private final Sprite sprite;
    private final Sprite shadow;
    private final BitmapFont font;
    private final Display root;
    private int totalPixels;
    private int currPixels;
    private String drawText = "";

    public ScoreView(Display root) {
        this.root = root;
        TexturePrepare prepare = root.getTransitObject(TexturePrepare.class);
        sprite = prepare.getByName(FTextures.SCORE_BACK);
        Color shadowColor = Color.BLACK.cpy();
        shadowColor.a = 0.15f;
        shadow = prepare.getByName(FTextures.SCORE_BACK_SHADOW);
        shadow.setColor(shadowColor);



        font = P.get().asset.get(Loading.FONT_ROBOTO_BOLD, BitmapFont.class);
    }

    private void updateDrawText() {
        drawText = currPixels+"/"+totalPixels+" px";
    }

    public void setTotalPixels(int totalPixels) {
        this.totalPixels = totalPixels;
        updateDrawText();
    }

    public void iteratePixel() {
        currPixels++;
        updateDrawText();

        if(currPixels >= totalPixels ) {
            root.getGameViewForName(GameScreen.class).gameOver();
        }
    }
    public void decrementScore(int delta) {
        currPixels-=delta;
        updateDrawText();
    }
    public void setCurrPixels(int currPixels) {
        this.currPixels = currPixels;
        updateDrawText();
    }

    @Override
    protected void setParent(Group parent) {
        super.setParent(parent);
        if (parent != null) {
            setBounds(parent.getStage().getViewport().getWorldWidth()/2-sprite.getWidth()/2,
                    parent.getStage().getViewport().getWorldHeight()-sprite.getHeight()-80,sprite.getWidth(),sprite.getHeight());

            sprite.setPosition(getX(),getY());
            shadow.setPosition(getX()+20,getY()-25);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        shadow.draw(batch);
        sprite.draw(batch);
        FontUtil.drawText(batch,font,drawText,getX(),getY(),0.56f,P.BLACK_FONT_COLOR,getWidth(), Align.center,false,getHeight());
    }
}
