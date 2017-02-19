package guerra.aeronaves.comunicacion.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.GuerraAeronaves;
import java.awt.Point;

public abstract class EstacionGasolina extends DatosElemento {
    
    public EstacionGasolina() {
        
    }
    
    public EstacionGasolina(int idElemento, Point posicion) {
        super(idElemento, posicion, Direccion.ARRIBA, GuerraAeronaves.VIDA_ESTACION_GASOLINA);
    }
    
}
