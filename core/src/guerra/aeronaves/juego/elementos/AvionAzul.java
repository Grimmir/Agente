package guerra.aeronaves.juego.elementos;

import guerra.aeronaves.Direccion;
import java.awt.Point;

public class AvionAzul extends Avion {
    
    public AvionAzul(Point posicion, Direccion direccion) {
        super("avion_azul.png", posicion, direccion);
    }

    @Override
    public Elemento crearAPartirDe(Elemento e) {
        return new AvionAzul(e.getPosicion(), e.getDireccion());
    }

}
