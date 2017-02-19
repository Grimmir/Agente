package guerra.aeronaves.juego.elementos;

import guerra.aeronaves.GuerraAeronaves;
import guerra.aeronaves.comunicacion.elementos.DatosElemento;
import java.awt.Point;

public class EstacionGasolinaAzul extends EstacionGasolina {
    
    public EstacionGasolinaAzul(Point posInicial) {
        super("estacion_gasolina_azul.png", GuerraAeronaves.ID_ESTACION_GASOLINA_AZUL, posInicial);
    }

    @Override
    public DatosElemento crearSerializable() {
        return new guerra.aeronaves.comunicacion.elementos.EstacionGasolinaAzul(posicion);
    }
    
}