package guerra.aeronaves.comunicacion.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.GuerraAeronaves;
import guerra.aeronaves.juego.elementos.Elemento;
import java.awt.Point;

public class Explosion extends DatosElemento {

    public Explosion() {
    
    }
    
    public Explosion(Point posicion) {
        super(GuerraAeronaves.ID_EXPLOSION, posicion, Direccion.ARRIBA, GuerraAeronaves.VIDA_INFINITA);
    }

    @Override
    public Elemento crearElemento() {
        return new guerra.aeronaves.juego.elementos.Explosion(GuerraAeronaves.RUTA_EXPLOSIONES.get(0), posicion);
    }
    
}
