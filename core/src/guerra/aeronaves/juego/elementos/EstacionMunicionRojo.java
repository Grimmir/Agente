package guerra.aeronaves.juego.elementos;

import com.badlogic.gdx.math.Vector2;
import guerra.aeronaves.GuerraAeronaves;

public class EstacionMunicionRojo extends EstacionMunicion {   
    
    public EstacionMunicionRojo(Vector2 posInicial) {
        super("estacion_misiles_rojo.png", GuerraAeronaves.ID_ESTACION_MUNICION_ROJO, posInicial);
    }
    
}