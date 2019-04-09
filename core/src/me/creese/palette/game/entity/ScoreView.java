package me.creese.palette.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;

import me.creese.palette.game.screens.GameScreen;
import me.creese.palette.game.screens.Loading;
import me.creese.palette.game.util.FTextures;
import me.creese.palette.game.util.FontUtil;
import me.creese.palette.game.util.P;
import me.creese.palette.game.util.TexturePrepare;
import me.creese.util.display.Display;

public class ScoreView extends Actor {

    private static final float PERCENT_TO_ADD_MORE_SECRET_PIXELS = 0.8f;
    private final Sprite sprite;
    private final Sprite shadow;
    private final BitmapFont font;
    private final Display root;
    private int totalPixels;
    private int currPixels;
    private String drawText = "";
    private int countSecretPixels;
    private int wrongPixels;

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

    /**
     * Увеличение закрашенных пикселей
     * @return Возвращает true если пиксели все закрашены
     */
    public boolean iteratePixel() {
        currPixels++;
        updateDrawText();
        GameScreen gameScreen = root.getGameViewForName(GameScreen.class);

        if(P.get().isSecretMode && countSecretPixels < totalPixels) {
            if(currPixels>= countSecretPixels*PERCENT_TO_ADD_MORE_SECRET_PIXELS) {
                countSecretPixels += gameScreen.getGroupPixels().openMoreSecretPixels();
            }
        }



        if(currPixels >= totalPixels ) {

            gameScreen.gameOver();
            return true;
        }
        return false;
    }
    /**
     * Уменьшение количества закрашенных пикселей
     * @param delta
     */
    public void decrementScore(int delta) {
        currPixels-=delta;
        updateDrawText();
    }

    public void iterateWrongPixel() {
        wrongPixels++;
    }

    public int getWrongPixels() {
        return wrongPixels;
    }

    public void setWrongPixels(int wrongPixels) {
        this.wrongPixels = wrongPixels;
    }

    public void setCountSecretPixels(int countSecretPixels) {
        this.countSecretPixels = countSecretPixels;
    }

    public int getCurrPixels() {
        return currPixels;
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
