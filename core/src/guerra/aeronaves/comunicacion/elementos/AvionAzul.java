package guerra.aeronaves.comunicacion.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.juego.elementos.Elemento;
import java.awt.Point;

public class AvionAzul extends Avion {
    
    public AvionAzul() {
        
    }
    
    public AvionAzul(Point posicion, Direccion direccion) {
        super(posicion, direccion);
    }

    @Override
    public Elemento crearElemento() {
        guerra.aeronaves.juego.elementos.AvionAzul aa = 
                new guerra.aeronaves.juego.elementos.AvionAzul(posicion, direccion);
        
        aa.setVida(vida);
        aa.setMuniciones(municiones);
        aa.setGasolina(gasolina);
        
        return aa;
    }

}
