package guerra.aeronaves.juego.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.GuerraAeronaves;
import guerra.aeronaves.comunicacion.elementos.DatosElemento;
import java.awt.Point;

public class Explosion extends Elemento {
    
    public Explosion(String rutaSprite, Point posicion) {
        super(rutaSprite, GuerraAeronaves.ID_EXPLOSION, posicion, Direccion.ARRIBA, GuerraAeronaves.VIDA_INFINITA);
    }

    @Override
    public DatosElemento crearSerializable() {
        return new guerra.aeronaves.comunicacion.elementos.Explosion(posicion);
    }
    
}
