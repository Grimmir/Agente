package guerra.aeronaves.comunicacion.elementos;

import guerra.aeronaves.GuerraAeronaves;
import guerra.aeronaves.juego.elementos.Elemento;
import java.awt.Point;

public class EstacionGasolinaRojo extends EstacionGasolina {

    public EstacionGasolinaRojo() {
        
    }
    
    public EstacionGasolinaRojo(Point posInicial) {
        super(GuerraAeronaves.ID_ESTACION_GASOLINA_ROJO, posInicial);
    }

    @Override
    public Elemento crearElemento() {
        return new guerra.aeronaves.juego.elementos.EstacionGasolinaRojo(posicion);
    }
    
}