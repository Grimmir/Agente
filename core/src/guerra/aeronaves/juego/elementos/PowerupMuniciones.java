package guerra.aeronaves.juego.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.GuerraAeronaves;
import java.awt.Point;

public class PowerupMuniciones extends Elemento {
    
    public PowerupMuniciones(Point posicion) {
        super("powerup_municion.png", GuerraAeronaves.ID_POWERUP_MUNICIONES, posicion, Direccion.ARRIBA, GuerraAeronaves.VIDA_INFINITA);
    }

    @Override
    public Elemento crearAPartirDe(Elemento e) {
        return new PowerupMuniciones(e.getPosicion());
    }

}