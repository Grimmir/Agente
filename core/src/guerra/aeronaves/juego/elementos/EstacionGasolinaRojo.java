package guerra.aeronaves.juego.elementos;

import guerra.aeronaves.GuerraAeronaves;
import java.awt.Point;

public class EstacionGasolinaRojo extends EstacionGasolina {
    
    public EstacionGasolinaRojo(Point posInicial) {
        super("estacion_gasolina_rojo.png", GuerraAeronaves.ID_ESTACION_GASOLINA_ROJO, posInicial);
    }

    @Override
    public Elemento crearAPartirDe(Elemento e) {
        return new EstacionGasolinaRojo(e.getPosicion());
    }
    
}