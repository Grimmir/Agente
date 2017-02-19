package guerra.aeronaves.juego.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.comunicacion.elementos.DatosElemento;
import java.awt.Point;

public class AvionRojo extends Avion {
    
    public AvionRojo(Point posicion, Direccion dir) {
        super("avion_rojo.png", posicion, dir);
    }

    @Override
    public DatosElemento crearSerializable() {
        guerra.aeronaves.comunicacion.elementos.AvionRojo ar = 
                new guerra.aeronaves.comunicacion.elementos.AvionRojo(posicion, direccion);
        
        ar.setVida(vida);
        ar.setMuniciones(municiones);
        ar.setGasolina(gasolina);
        
        return ar;
    }
    
}