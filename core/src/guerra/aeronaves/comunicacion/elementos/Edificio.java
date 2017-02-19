package guerra.aeronaves.comunicacion.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.GuerraAeronaves;
import guerra.aeronaves.juego.elementos.Elemento;
import java.awt.Point;

public class Edificio extends DatosElemento {  
    
    public Edificio() {
        
    }
    
    public Edificio(Point posicion) {
        super(GuerraAeronaves.ID_EDIFICIO, posicion, Direccion.ARRIBA, GuerraAeronaves.VIDA_EDIFICIO);
    }

    @Override
    public Elemento crearElemento() {
        return new guerra.aeronaves.juego.elementos.Edificio(posicion);
    }
    
}