package guerra.aeronaves.juego.elementos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import guerra.aeronaves.Direccion;
import java.awt.Point;
import java.io.Serializable;

public abstract class Elemento extends Image implements Serializable {
    
    protected final int id;
    protected Point posicion;
    protected Direccion direccion;
    protected Direccion proximaDireccion;
    protected float vida;
    
    public Elemento(String rutaSprite, int id, Point posicion, Direccion direccion, float vida) {
        super(new SpriteDrawable(new Sprite(new Texture(Gdx.files
                .internal(rutaSprite)))));
        this.id = id;
        this.posicion = posicion;
        this.direccion = direccion;
        cambiarRotacion(this.direccion);
        proximaDireccion = this.direccion;
        this.vida = vida;
    }

    public abstract Elemento crearAPartirDe(Elemento e);
    
    public int getId() {
        return id;
    }
    
    public float getVida() {
        return vida;
    }

    public void setVida(float vida) {
        this.vida = vida;
    }

    public Point getPosicion() {
        return posicion;
    }

    public void setPosicion(Point posicion) {
        this.posicion = posicion;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public final void setDireccion(Direccion direccion) {
        if (this.direccion != direccion) {        
            float rotacion;
            
            setOrigin(getWidth() / 2, getHeight() / 2);
            switch (direccion) {
                case ARRIBA:
                    if (this.direccion == Direccion.DERECHA) {
                        rotacion = 90;
                    }
                    else {
                        rotacion = -90;
                    }
                    break;
                case ABAJO:
                    if (this.direccion == Direccion.DERECHA) {
                        rotacion = -90;
                    }
                    else {
                        rotacion = 90;
                    }
                    break;
                case DERECHA:
                    if (this.direccion == Direccion.ARRIBA) {
                        rotacion = -90;
                    }
                    else {
                        rotacion = 90;
                    }
                    break;
                default:
                    if (this.direccion == Direccion.ARRIBA) {
                        rotacion = 90;
                    }
                    else {
                        rotacion = -90;
                    }
            }
            addAction(Actions.rotateBy(rotacion, 0.1f));
            this.direccion = direccion;
        }
    }

    public Point getProximaPosicion() {        
        switch (direccion) {
            case ARRIBA:
                return new Point(posicion.x, posicion.y - 1);
            case DERECHA:
                return new Point(posicion.x + 1, posicion.y);
            case ABAJO:
                return new Point(posicion.x, posicion.y + 1);
            default:
                return new Point(posicion.x - 1, posicion.y);
        }
    }

    public Direccion getProximaDireccion() {
        return proximaDireccion;
    }

    public void setProximaDireccion(Direccion proximaDireccion) {
        this.proximaDireccion = proximaDireccion;
    }

    private void cambiarRotacion(Direccion direccion) {
        setOrigin(getWidth() / 2, getHeight() / 2);
        
        switch (direccion) {
            case ARRIBA:
                setRotation(0);
                break;
            case ABAJO:
                setRotation(180);
                break;
            case DERECHA:
                setRotation(270);
                break;
            default:
                setRotation(90);
        }         
    }
    
}
