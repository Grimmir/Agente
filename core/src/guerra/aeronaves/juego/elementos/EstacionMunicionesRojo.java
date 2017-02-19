package guerra.aeronaves.juego.elementos;

import guerra.aeronaves.GuerraAeronaves;
import guerra.aeronaves.comunicacion.elementos.DatosElemento;
import java.awt.Point;

public class EstacionMunicionesRojo extends EstacionMuniciones {   
    
    public EstacionMunicionesRojo(Point posInicial) {
        super("estacion_misiles_rojo.png", GuerraAeronaves.ID_ESTACION_MUNICIONES_ROJO, posInicial);
    }

    @Override
    public DatosElemento crearSerializable() {
        return new guerra.aeronaves.comunicacion.elementos.EstacionMunicionesRojo(posicion);
    }
}