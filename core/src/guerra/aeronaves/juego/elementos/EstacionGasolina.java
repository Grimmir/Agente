package guerra.aeronaves.juego.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.GuerraAeronaves;
import java.awt.Point;

public abstract class EstacionGasolina extends Elemento {
    
    public EstacionGasolina(String rutaSprite, int idElemento, Point posicion) {
        super(rutaSprite, idElemento, posicion, Direccion.ARRIBA, GuerraAeronaves.VIDA_ESTACION_GASOLINA);
    }
    
}
