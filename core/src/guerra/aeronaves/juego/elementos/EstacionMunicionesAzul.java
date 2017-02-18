package guerra.aeronaves.juego.elementos;

import guerra.aeronaves.GuerraAeronaves;
import java.awt.Point;

public class EstacionMunicionesAzul extends EstacionMuniciones {   
    
    public EstacionMunicionesAzul(Point posInicial) {
        super("estacion_misiles_azul.png", GuerraAeronaves.ID_ESTACION_MUNICIONES_AZUL, posInicial);
    }

    @Override
    public Elemento crearAPartirDe(Elemento e) {
        return new EstacionGasolinaAzul(e.getPosicion());
    }
    
}