package guerra.aeronaves.comunicacion.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.GuerraAeronaves;
import guerra.aeronaves.juego.elementos.Elemento;
import java.awt.Point;

public class PowerupVida extends DatosElemento {

    public PowerupVida() {
    
    }
    
    public PowerupVida(Point posicion) {
        super(GuerraAeronaves.ID_POWERUP_VIDA, posicion, Direccion.ARRIBA, GuerraAeronaves.VIDA_INFINITA);
    }

    @Override
    public Elemento crearElemento() {
        return new guerra.aeronaves.juego.elementos.PowerupVida(posicion);
    }
    
}