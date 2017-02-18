package guerra.aeronaves.juego.elementos;

import guerra.aeronaves.GuerraAeronaves;
import java.awt.Point;

public class EstacionGasolinaAzul extends EstacionGasolina {
    
    public EstacionGasolinaAzul(Point posInicial) {
        super("estacion_gasolina_azul.png", GuerraAeronaves.ID_ESTACION_GASOLINA_AZUL, posInicial);
    }

    @Override
    public Elemento crearAPartirDe(Elemento e) {
        return new EstacionGasolinaAzul(e.getPosicion());
    }
    
}