package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

import static com.mygdx.game.Constantes.PPM;


public class PantallaJuego extends Pantalla{
    private final float SCALE = 2.0f;

    private World world;
    private SpriteBatch batch;
    private MyCamera camara;

    private Board board;
    private Piso piso;

    private BotonPausa botonPausa;
    private Control control;
    private Stage stage;

    private boolean renovarJuego;


    public PantallaJuego(final MyGdxGame game){
        super (game);

        camara = new MyCamera( game.getWidth(), game.getHeight() , game.getUnidad() );
        camara.setToOrtho(false, game.getWidth()/ SCALE, game.getHeight()/SCALE);

        world = new World(new Vector2(0, 10f) , false);

        batch = new SpriteBatch();

        board = new Board();
        piso = new Piso(world, game.getUnidad(), game.getWidth(), game.getHeight());

        botonPausa = new BotonPausa(game);

        stage = new Stage();
        stage.addActor(botonPausa.getButton());
        control = new Control(game, stage);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        cls();

        update(Gdx.graphics.getDeltaTime());

		camara.revisarSensor ( board.hayBloqueArriba(2*game.getHeight()/3 - camara.getDesfaceY() ) );
		if ( getRenovarJuego() ) {
            camara.position.add(0, -camara.getDesfaceY(), 0);
            setRenovarJuego(false);
        }

        batch.begin();
        board.draw(batch);
        piso.draw(batch);
        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height){
        camara.setToOrtho(false, width, height);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    public void reiniciar(){
        camara.position.set(game.getWidth()/2 , game.getWidth()/2,0);
        board.dispose(world);
        camara.setDesfaceY(0);
    }

    public void update(float delta){
        world.step(1/60f, 6, 2);
        board.update();
        camara.update();
        batch.setProjectionMatrix(camara.combined);
        piso.update();
        control.update( board, world, game.getUnidad(), camara, Gdx.input.isTouched() );
    }


    @Override
    public void dispose() {
        batch.dispose();
        board.dispose(world);
        piso.dispose(world);
        world.dispose();
    }

    public static void cls() {
        Gdx.gl.glClearColor(0, 0, 0, 1.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void setRenovarJuego(boolean x){
        renovarJuego = x;
    }

    public boolean getRenovarJuego(){
        return renovarJuego;
    }

}
