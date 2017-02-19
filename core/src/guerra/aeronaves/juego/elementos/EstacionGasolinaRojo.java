package guerra.aeronaves.juego.elementos;

import guerra.aeronaves.GuerraAeronaves;
import guerra.aeronaves.comunicacion.elementos.DatosElemento;
import java.awt.Point;

public class EstacionGasolinaRojo extends EstacionGasolina {
    
    public EstacionGasolinaRojo(Point posInicial) {
        super("estacion_gasolina_rojo.png", GuerraAeronaves.ID_ESTACION_GASOLINA_ROJO, posInicial);
    }

    @Override
    public DatosElemento crearSerializable() {
        return new guerra.aeronaves.comunicacion.elementos.EstacionGasolinaRojo(posicion);
    }
    
}