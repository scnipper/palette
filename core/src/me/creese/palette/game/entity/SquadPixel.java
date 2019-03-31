package me.creese.palette.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.GLOnlyTextureData;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;

import java.lang.reflect.Field;

import me.creese.palette.game.screens.Loading;
import me.creese.palette.game.util.FontUtil;
import me.creese.palette.game.util.P;

public class SquadPixel extends Actor {

    public static final int WIDTH_SQUAD = 32;
    public static final int HEIGHT_SQUAD = 32;
    private final FrameBuffer frameBuffer;
    private final OrthographicCamera camera;
    private final BigPixel[][] gridPixels;
    private final int arrX;
    private final int arrY;
    private final BitmapFont font;
    private Sprite sprite;
    private TextureRegion bufferTexture;
    private float downX;
    private float downY;
    private OrthographicCamera stageCamera;

    public SquadPixel(BigPixel[][] gridPixels, int arrX, int arrY) {
        this.gridPixels = gridPixels;
        this.arrX = arrX;
        this.arrY = arrY;
        setBounds(arrX * BigPixel.WIDTH_PIXEL, P.HEIGHT - arrY * BigPixel.HEIGHT_PIXEL, BigPixel.WIDTH_PIXEL * WIDTH_SQUAD, BigPixel.HEIGHT_PIXEL * HEIGHT_SQUAD);

        moveBy(0, -getHeight());
        setDebug(true);
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, (int) getWidth(), (int) getHeight(), false) {
            @Override
            protected Texture createTexture(FrameBufferTextureAttachmentSpec attachmentSpec) {
                int internalFormat = 0;
                int format = 0;
                int type = 0;
                try {
                    Field f1 = attachmentSpec.getClass().getDeclaredField("internalFormat");
                    f1.setAccessible(true);
                    internalFormat = f1.getInt(attachmentSpec);
                    Field f2 = attachmentSpec.getClass().getDeclaredField("format");
                    f2.setAccessible(true);
                    format = f2.getInt(attachmentSpec);

                    Field f3 = attachmentSpec.getClass().getDeclaredField("type");
                    f3.setAccessible(true);
                    type = f3.getInt(attachmentSpec);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }

                GLOnlyTextureData data = new GLOnlyTextureData(getWidth(), getHeight(), 0, internalFormat, format, type);

                return new Texture(data);
            }
        };
        camera = new OrthographicCamera(getWidth(), getHeight());
        camera.translate(getWidth() / 2, getHeight() / 2);
        camera.update();

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ((GroupPixels) getParent()).getPixelsControl().setDownPixelSquad(SquadPixel.this);
                downX = x;
                downY = y;
                return false;
            }


        });

        font = P.get().asset.get(Loading.FONT_ROBOTO_BOLD, BitmapFont.class);
    }

    public void redrawSquad() {
        //System.out.println("redraw");
        SpriteBatch rootBatch = P.rootBatch;
        frameBuffer.begin();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        rootBatch.setProjectionMatrix(camera.combined);
        boolean beginAfter = false;
        if (rootBatch.isDrawing()) {
            rootBatch.end();
            beginAfter = true;
        }
        rootBatch.begin();
        for (int i = 0; i < HEIGHT_SQUAD; i++) {
            if (i + arrY >= gridPixels.length) break;
            for (int j = 0; j < WIDTH_SQUAD; j++) {
                if (j + arrX >= gridPixels[i].length) break;

                sprite.setPosition(j * BigPixel.WIDTH_PIXEL, ((HEIGHT_SQUAD * BigPixel.HEIGHT_PIXEL) - i * BigPixel.HEIGHT_PIXEL) - BigPixel.HEIGHT_PIXEL);
                BigPixel bigPixel = gridPixels[i + arrY][j + arrX];

                switch (bigPixel.getState()) {
                    case PAINT:
                        sprite.setScale(1);
                        sprite.setColor(bigPixel.getColor());
                        sprite.draw(rootBatch);
                        break;
                    case NOT_PAINT:
                        sprite.setColor(Color.WHITE);
                        sprite.setScale(0.95f);
                        sprite.draw(rootBatch);

                        FontUtil.drawText(rootBatch, font, String.valueOf(bigPixel.getNumColor()), sprite.getX(), sprite.getY(), 0.2f, P.GRAY_FONT_COLOR, sprite.getWidth(), Align.center, false, sprite.getHeight());

                        break;
                }


            }
        }
        rootBatch.end();

        Texture colorBufferTexture = frameBuffer.getColorBufferTexture();
        bufferTexture = new TextureRegion(colorBufferTexture);
        bufferTexture.flip(false, true);
        frameBuffer.end();

        if (beginAfter) rootBatch.begin();
    }

    public void touchDown() {
        //System.out.println("touch down squad  "+downX+" "+downY);

        int indX = (int) (downX / BigPixel.WIDTH_PIXEL) + arrX;
        int indY = (int) (((HEIGHT_SQUAD * BigPixel.HEIGHT_PIXEL) - downY) / BigPixel.HEIGHT_PIXEL) + arrY;

        if (indY < gridPixels.length) {
            if (indX < gridPixels[indY].length) {
                gridPixels[indY][indX].setState(BigPixel.State.PAINT);
                redrawSquad();
            }
        }


    }

    private boolean boundsScreen() {


        float zoom = stageCamera.zoom;
        float xToCamera = ((stageCamera.position.x - (P.WIDTH * zoom) / 2) - getX()) * -1;
        float yToCamera = (stageCamera.position.y - (P.HEIGHT * zoom) / 2) - (getY() - (P.HEIGHT * zoom));

        return xToCamera > -getWidth() * 2 * zoom && xToCamera < P.WIDTH * zoom && yToCamera > -getHeight() * 2 * zoom && yToCamera < (P.HEIGHT + getHeight()) * zoom;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (bufferTexture != null) {
            //if(boundsScreen())
            batch.draw(bufferTexture, getX(), getY());
        }
    }

    @Override
    protected void setParent(Group parent) {
        super.setParent(parent);
        if (parent != null) {
            stageCamera = (OrthographicCamera) parent.getStage().getCamera();
            sprite = ((GroupPixels) parent).getSprite();
            //if(boundsScreen())
            redrawSquad();
        }
    }
}
