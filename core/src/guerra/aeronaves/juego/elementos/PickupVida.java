package guerra.aeronaves.juego.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.GuerraAeronaves;
import java.awt.Point;

public class PickupVida extends Elemento {

    public PickupVida(Point posicion) {
        super("reparacion.png", GuerraAeronaves.ID_PICKUP_VIDA, posicion, Direccion.ARRIBA, GuerraAeronaves.VIDA_INFINITA);
    }

    @Override
    public Elemento crearAPartirDe(Elemento e) {
        return new PickupVida(e.getPosicion());
    }
    
}