package me.creese.palette.game.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;

import me.creese.palette.game.util.FTextures;
import me.creese.palette.game.util.TexturePrepare;

public class GroupPixels extends Group {

    private final PixelsControl pixelsControl;

    private final Sprite sprite;


    public GroupPixels(PixelsControl pixelsControl, TexturePrepare prepare) {
        this.pixelsControl = pixelsControl;


        this.sprite = prepare.getByName(FTextures.PIXEL_SQUARE);


    }

    public PixelsControl getPixelsControl() {
        return pixelsControl;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setGridPixels(int[][] gridPixels) {



        /*Pixmap pixmap = new Pixmap(4,4, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();

        Sprite spritePix = new Sprite(new Texture(pixmap));
        spritePix.setScale(0.7f);


        OrthographicCamera camera = new OrthographicCamera(texture.getWidth()*4,texture.getHeight()*4);
        camera.translate((texture.getWidth()*4)/2.f,(texture.getHeight()*4)/2.f);
        camera.update();


        fbo = new FrameBuffer(Pixmap.Format.RGBA8888,texture.getWidth()*4,texture.getHeight()*4,false);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        fbo.begin();
        P.rootBatch.setProjectionMatrix(camera.combined);
        P.rootBatch.begin();
        for (int i = 0; i < gridPixels.length; i++) {
            for (int j = 0; j < gridPixels[i].length; j++) {
                //spritePix.setColor(new Color(gridPixels[i][j]));
                spritePix.setPosition(j*4,i*4);
                spritePix.draw(P.rootBatch);
            }
        }



        P.rootBatch.end();
        fbo.end();
        colorBufferTexture = fbo.getColorBufferTexture();*/

    }

}
