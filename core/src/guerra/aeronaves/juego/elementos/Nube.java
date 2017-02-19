package guerra.aeronaves.juego.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.GuerraAeronaves;
import guerra.aeronaves.comunicacion.elementos.DatosElemento;
import java.awt.Point;

public class Nube extends Elemento {
    
    public Nube(Point posicion, Direccion direccion) {
        super("nube.png", GuerraAeronaves.ID_NUBE, posicion, direccion, GuerraAeronaves.VIDA_INFINITA);
        this.direccion = direccion;
    }

    @Override
    public DatosElemento crearSerializable() {
        return new guerra.aeronaves.comunicacion.elementos.Nube(posicion, direccion);
    }
    
}
