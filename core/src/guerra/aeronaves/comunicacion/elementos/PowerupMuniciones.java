package guerra.aeronaves.comunicacion.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.GuerraAeronaves;
import guerra.aeronaves.juego.elementos.Elemento;
import java.awt.Point;

public class PowerupMuniciones extends DatosElemento {

    public PowerupMuniciones() {

    }
    
    public PowerupMuniciones(Point posicion) {
        super(GuerraAeronaves.ID_POWERUP_MUNICIONES, posicion, Direccion.ARRIBA, GuerraAeronaves.VIDA_INFINITA);
    }

    @Override
    public Elemento crearElemento() {
        return new guerra.aeronaves.juego.elementos.PowerupMuniciones(posicion);
    }

}