package guerra.aeronaves.comunicacion.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.GuerraAeronaves;
import java.awt.Point;

public abstract class Avion extends DatosElemento {
    
    protected int municiones;
    protected int gasolina;
    
    public Avion() {
        
    }
    
    public Avion(Point posicion, Direccion direccion) {
        super(GuerraAeronaves.ID_AVION_AZUL, posicion, direccion, GuerraAeronaves.VIDA_AVION);
        municiones = GuerraAeronaves.MUNICIONES_AVION;
        gasolina = GuerraAeronaves.GASOLINA_AVION;
    }

    public int getMuniciones() {
        return municiones;
    }

    public int getGasolina() {
        return gasolina;
    }
    
}
