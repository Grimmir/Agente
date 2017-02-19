package guerra.aeronaves.comunicacion.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.juego.elementos.Elemento;
import java.awt.Point;

public class AvionRojo extends Avion {
    
    public AvionRojo() {
        
    }
    
    public AvionRojo(Point posicion, Direccion dir) {
        super(posicion, dir);
    }

    @Override
    public Elemento crearElemento() {
        guerra.aeronaves.juego.elementos.AvionRojo ar = 
                new guerra.aeronaves.juego.elementos.AvionRojo(posicion, direccion);
        
        ar.setVida(vida);
        ar.setMuniciones(municiones);
        ar.setGasolina(gasolina);
        
        return ar;
    }
    
}