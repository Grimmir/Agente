package guerra.aeronaves.juego.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.GuerraAeronaves;
import java.awt.Point;

public class PickupMuniciones extends Elemento {
    
    public PickupMuniciones(Point posicion) {
        super("municion", GuerraAeronaves.ID_PICKUP_MUNICIONES, posicion, Direccion.ARRIBA, GuerraAeronaves.VIDA_INFINITA);
    }
        
    @Override
    public Elemento crearAPartirDe(Elemento e) {
        return new PickupMuniciones(e.getPosicion());
    }
}