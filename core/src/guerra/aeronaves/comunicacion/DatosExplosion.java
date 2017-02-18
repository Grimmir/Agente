package guerra.aeronaves.comunicacion;

import java.awt.Point;
import java.io.Serializable;

public class DatosExplosion implements Serializable {
    
    private final Point posicion;
    private final float tiempo;

    public DatosExplosion(Point posicion, float tiempo) {
        this.posicion = posicion;
        this.tiempo = tiempo;
    }

    public Point getPosicion() {
        return posicion;
    }

    public float getTiempo() {
        return tiempo;
    }

}
