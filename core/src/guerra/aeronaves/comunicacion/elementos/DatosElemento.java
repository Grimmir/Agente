package guerra.aeronaves.comunicacion.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.juego.elementos.Elemento;
import java.awt.Point;

public abstract class DatosElemento {
    
    protected int id;
    protected Point posicion;
    protected Direccion direccion;
    protected Direccion proximaDireccion;
    protected float vida;
    
    public DatosElemento() {
        
    }
    
    public DatosElemento(int id, Point posicion, Direccion direccion, float vida) {
        this.id = id;
        this.posicion = posicion;
        this.direccion = direccion;
        proximaDireccion = this.direccion;
        this.vida = vida;
    }

    public int getId() {
        return id;
    }

    public Point getPosicion() {
        return posicion;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public Direccion getProximaDireccion() {
        return proximaDireccion;
    }

    public float getVida() {
        return vida;
    }
    
    public abstract Elemento crearElemento();
    
}
