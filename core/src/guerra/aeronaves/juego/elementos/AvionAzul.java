package guerra.aeronaves.juego.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.comunicacion.elementos.DatosElemento;
import java.awt.Point;

public class AvionAzul extends Avion {
    
    public AvionAzul(Point posicion, Direccion direccion) {
        super("avion_azul.png", posicion, direccion);
    }

    @Override
    public DatosElemento crearSerializable() {
        return new guerra.aeronaves.comunicacion.elementos.AvionAzul(posicion, direccion);
    }

}
