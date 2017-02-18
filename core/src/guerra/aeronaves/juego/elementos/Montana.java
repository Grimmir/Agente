package guerra.aeronaves.juego.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.GuerraAeronaves;
import java.awt.Point;

public class Montana extends Elemento {
   
    public Montana(Point posicion) {
        super("montana.png", GuerraAeronaves.ID_MONTANA, posicion, Direccion.ARRIBA, GuerraAeronaves.VIDA_INFINITA);
    }

    @Override
    public Elemento crearAPartirDe(Elemento e) {
        return new Montana(e.getPosicion());
    }
    
}
