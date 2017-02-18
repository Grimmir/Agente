package guerra.aeronaves.juego.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.GuerraAeronaves;
import java.awt.Point;

public class PowerupVida extends Elemento {
    
    public PowerupVida(Point posicion) {
        super("powerup_vida.png", GuerraAeronaves.ID_POWERUP_VIDA, posicion, Direccion.ARRIBA, GuerraAeronaves.VIDA_INFINITA);
    }

    @Override
    public Elemento crearAPartirDe(Elemento e) {
        return new PowerupVida(e.getPosicion());
    }
    
}