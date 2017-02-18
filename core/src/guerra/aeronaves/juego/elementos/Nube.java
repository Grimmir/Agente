package guerra.aeronaves.juego.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.GuerraAeronaves;
import java.awt.Point;

public class Nube extends Elemento {
    
    public Nube(Point posicion, Direccion direccion) {
        super("nube.png", GuerraAeronaves.ID_NUBE, posicion, direccion, GuerraAeronaves.VIDA_INFINITA);
        this.direccion = direccion;
    }

    @Override
    public Elemento crearAPartirDe(Elemento e) {
        return new Nube(e.getPosicion(), e.getDireccion());
    }
    
}
