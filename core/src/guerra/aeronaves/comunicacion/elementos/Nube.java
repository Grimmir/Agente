package guerra.aeronaves.comunicacion.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.GuerraAeronaves;
import guerra.aeronaves.juego.elementos.Elemento;
import java.awt.Point;

public class Nube extends DatosElemento {

    public Nube() {
    }
    
    public Nube(Point posicion, Direccion direccion) {
        super(GuerraAeronaves.ID_NUBE, posicion, direccion, GuerraAeronaves.VIDA_INFINITA);
    }

    @Override
    public Elemento crearElemento() {
        return new guerra.aeronaves.juego.elementos.Nube(posicion, direccion);
    }
    
}
