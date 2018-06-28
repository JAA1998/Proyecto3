package com.mygdx.game;

/**
 * Created by Gustavo on 18-Jun-18.
 */

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;

import static com.mygdx.game.Constantes.PPM;

public class BloqueCuadrado extends Bloque {
    private Texture imagen;
    private Sprite sprite;
    private Body body;

    public BloqueCuadrado(World world, float unidad, float x, float y, Color c){
        imagen = new Texture("Bloque2.png");
        sprite = new Sprite(imagen);
        sprite.setSize(unidad, unidad);
        sprite.setPosition(x, y);
        sprite.setColor(c.red , c.green , c.blue , c.transparency);

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x /PPM , y /PPM);
        def.fixedRotation = false;
        body = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(unidad/2 / PPM, unidad/2 / PPM);

        body.createFixture(shape, 1.0f);
        shape.dispose();
    }
    

    public void update(){
        sprite.setPosition( body.getPosition().x*PPM - sprite.getWidth()/2 , body.getPosition().y*PPM - sprite.getHeight()/2);
        sprite.setRotation( (float) Math.toDegrees( body.getAngle() ) );
    }

    public void draw(SpriteBatch batch){
        sprite.draw(batch);
    }
    
    public void dispose(World world){
        imagen.dispose();
        world.destroyBody(body);
    }

    public float getGraphicY(){
        return sprite.getY();
    }

    public float getGraphicX(){
        return sprite.getX();
    }

    public float getPhisicY(){
        return body.getPosition().y;
    }

    public float getPhisicX(){
        return body.getPosition().x;
    }

    public void conectar(Bloque b){
        DistanceJointDef dDef = new DistanceJointDef();

        dDef.bodyA = this.body;
        dDef.bodyB = b.getBody();
        dDef.length = sprite.getWidth() / PPM;
        dDef.collideConnected = true;
        dDef.frequencyHz = 0f;
        dDef.dampingRatio = 1f;

        World world = body.getWorld();
        world.createJoint(dDef);
    }

    public Body getBody(){
        return this.body;
    }

}