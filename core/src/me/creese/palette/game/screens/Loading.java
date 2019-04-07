package me.creese.palette.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.FitViewport;

import me.creese.palette.game.PaletteStart;
import me.creese.palette.game.entity.BigPixel;
import me.creese.palette.game.util.FTextures;
import me.creese.palette.game.util.P;
import me.creese.palette.game.util.Shapes;
import me.creese.palette.game.util.TexturePrepare;
import me.creese.util.display.GameView;


public class Loading extends GameView {

    public static final String FONT_ROBOTO_BOLD = "fonts/font_bold.fnt";
    public static final String FONT_PIXEL_NUM = "fonts/pixel_font.fnt";
    private TexturePrepare prep;


    public Loading(PaletteStart root) {
        super(new FitViewport(P.WIDTH, P.HEIGHT), root,P.rootBatch);
        addActor(new LogoDraw(root));
        load();
    }

    private void load() {
        P.get().asset = new AssetManager();


        TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
        param.genMipMaps = true; // enabling mipmaps


        BitmapFontLoader.BitmapFontParameter paramFont = new BitmapFontLoader.BitmapFontParameter();
        paramFont.genMipMaps = true;
        paramFont.magFilter = Texture.TextureFilter.Linear;
        paramFont.minFilter = Texture.TextureFilter.Linear;


        BitmapFontLoader.BitmapFontParameter paramFont2 = new BitmapFontLoader.BitmapFontParameter();
        paramFont2.genMipMaps = true;


        P.get().asset.load(FONT_ROBOTO_BOLD, BitmapFont.class, paramFont);
        P.get().asset.load(FONT_PIXEL_NUM, BitmapFont.class,paramFont2);
        loadFrameTextures();


    }

    private void loadFrameTextures() {
        prep = new TexturePrepare();
        getRoot().addTransitObject(prep);
        final Shapes shape = new Shapes();
        prep.setPaddingX(2);
        prep.setPaddingY(2);
        //prep.setDebugImage(true);
        prep.setPreAndPostDraw(new TexturePrepare.PreAndPostDraw() {
            @Override
            public void drawPre() {
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            }

            @Override
            public void drawPost() {
                shape.flush();
            }
        });
        shape.setProjMatrix(prep.getCamera().combined);

        prep.addDraw(FTextures.PIXEL_SQUARE, BigPixel.WIDTH_PIXEL, BigPixel.HEIGHT_PIXEL, (bX, bY) -> {
            shape.setSmooth(0);
            shape.rect(bX, bY, BigPixel.WIDTH_PIXEL, BigPixel.HEIGHT_PIXEL);
            shape.setSmooth(1.5f);
        });
        prep.addDraw(FTextures.ARC_PALETTE_BUTTON, 180, 180, (bX, bY) -> {
            shape.arcLine(bX,bY,90,80,300,12);
            shape.arcLine(bX,bY,90,40,20,12);
        });

        prep.addDraw(FTextures.SMALL_ARC_PALETTE, 120, 120, (bX, bY) -> {
            shape.arcLine(bX,bY,60,120,80,12);
            shape.arcLine(bX,bY,60,220,30,12);
        });

        prep.addDraw(FTextures.CIRCLE_PALETTE, 180, 180, new TexturePrepare.Draw() {
            @Override
            public void draw(float bX, float bY) {
                shape.circle(bX,bY,90);
            }
        });
        prep.addDraw(FTextures.SCORE_BACK, 400, 120, new TexturePrepare.Draw() {
            @Override
            public void draw(float bX, float bY) {
                shape.rectRound(bX,bY,400,120,50);
            }
        });

        prep.addDraw(FTextures.SCORE_BACK_SHADOW, 400, 120, new TexturePrepare.Draw() {
            @Override
            public void draw(float bX, float bY) {
                shape.setSmooth(40);
                shape.rectRound(bX,bY,400,120,50);
                shape.setSmooth(1.5f);
            }
        });
        prep.addDraw(FTextures.BLOB, 170, 158, new TexturePrepare.Draw() {
            @Override
            public void draw(float bX, float bY) {
                short vert1 = shape.vertexAdd(bX,bY+158,shape.getColor());
                short vert2 = shape.vertexAdd(bX+170,bY+158,shape.getColor());
                short vert3 = shape.vertexAdd(bX+85,bY,shape.getColor());
                shape.indicesAdd(vert1,vert2,vert3);
            }
        });
        prep.addDraw(FTextures.SMALL_BLOB, 34, 68, new TexturePrepare.Draw() {
            @Override
            public void draw(float bX, float bY) {
                short vert1 = shape.vertexAdd(bX,bY+32,shape.getColor());
                short vert2 = shape.vertexAdd(bX+34,bY+32,shape.getColor());
                short vert3 = shape.vertexAdd(bX+17,bY,shape.getColor());
                shape.indicesAdd(vert1,vert2,vert3);
                shape.setSmooth(0);
                shape.circle(bX,bY+20.2f,17);
                shape.setSmooth(1.5f);
            }
        });
        prep.addDraw(FTextures.BLOB_STROKE, 170, 158, new TexturePrepare.Draw() {
            @Override
            public void draw(float bX, float bY) {
                short vert1 = shape.vertexAdd(bX,bY+158,shape.getColor());
                short vert2 = shape.vertexAdd(bX+12,bY+158,shape.getColor());
                short vert3 = shape.vertexAdd(bX+85,bY+24,shape.getColor());
                short vert4 = shape.vertexAdd(bX+85,bY,shape.getColor());


                short vert5 = shape.vertexAdd(bX+170,bY+158,shape.getColor());
                short vert6 = shape.vertexAdd(bX+158,bY+158,shape.getColor());


                shape.indicesAdd(vert1,vert2,vert3,
                        vert1,vert3,vert4,
                        vert3,vert6,vert5,
                        vert4,vert3,vert5);
            }
        });
        prep.addDraw(FTextures.CLOSED_ICON, 128, 128, (bX, bY) -> {
            shape.circleLine(bX,bY, 64,20);
            shape.setSmooth(0);
            shape.line(bX+28,bY+100,bX+100,bY+28,20);
            shape.setSmooth(1.5f);
        });
        prep.addDraw(FTextures.SELECT_IMAGE, 200, 300, (bX, bY) -> {
            shape.rectRound(bX,bY,200,300,20);
        });

        prep.addDraw(FTextures.PLUS_ICON, 128, 128, (bX, bY) -> {
            shape.setSmooth(0);
            shape.rect(bX+54,bY,20,128);
            shape.rect(bX,bY+54,128,20);
            shape.setSmooth(1.5f);
        });

        prep.addDraw(FTextures.DOUBLE_TAP_ICON, 128, 140, (bX, bY) -> {
            shape.circleLine(bX+32,bY+32,32,13);
            shape.arcLine(bX+19,bY+32,45,30,120,8);
            shape.arcLine(bX+9,bY+32,55,30,120,8);
        });

        prep.addDraw(FTextures.HOLD_ICON, 64, 64, (bX, bY) -> {
            shape.circle(bX+12,bY+12,20);
            shape.circleLine(bX,bY,32,7);
        });

        prep.addDraw(FTextures.BOMB_ICON, 40, 64, (bX, bY) -> {
            shape.circle(bX,bY,20);
            shape.setSmooth(0);
            shape.rectRound(bX+13,bY+30,14,20,5);
            shape.setSmooth(1.5f);

            shape.arcLine(bX+17,bY+25,20,90,70,5);
        });

        prep.addDraw(FTextures.CIRCLE_BONUS, 140, 140, (bX, bY) -> {
            shape.circle(bX,bY,70);
        });
        prep.addDraw(FTextures.CIRCLE_BONUS_SHADOW, 140, 140, (bX, bY) -> {
            shape.setSmooth(30);
            shape.circle(bX,bY,70);
            shape.setSmooth(1.5f);
        });
        prep.start();
    }

    class LogoDraw extends Group {


        private final Sprite splash;
        private final PaletteStart root;
        private final Color clearColor;


        private boolean drawBar;

        LogoDraw(PaletteStart root) {
            this.root = root;
            Texture texture = new Texture("splash/splash.png");
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            splash = new Sprite(texture);

            SequenceAction sequence = new SequenceAction();
            sequence.addAction(Actions.delay(0.3f));
            clearColor = Color.BLACK.cpy();
            clearColor.a = 0;
            sequence.addAction(Actions.color(Color.BLACK, 0.5f));

            addAction(sequence);

            setColor(clearColor);
            splash.setPosition(P.WIDTH / 2 - (splash.getWidth() / 2), P.HEIGHT / 2 - (splash.getHeight() / 2));

        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            splash.setColor(getColor());
            splash.draw(batch);
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            try {
                if (P.get().asset != null) {
                    if (P.get().asset.update() && prep.isLoad()) {

                        if (getActions().size == 0 && !drawBar) {
                            addAction(Actions.color(clearColor, 1));
                            drawBar = true;

                        }

                    }
                }

            } catch (GdxRuntimeException e) {
                e.printStackTrace();
            }

            if (drawBar && getActions().size == 0) {
                root.loadOk();
                remove();
                splash.getTexture().dispose();
            }
        }

    }

}
