package guerra.aeronaves.juego.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.GuerraAeronaves;
import guerra.aeronaves.comunicacion.elementos.DatosElemento;
import java.awt.Point;

public class Edificio extends Elemento {  
    
    public Edificio(Point posicion) {
        super("edificio.png", GuerraAeronaves.ID_EDIFICIO, posicion, Direccion.ARRIBA, GuerraAeronaves.VIDA_EDIFICIO);
    }

    @Override
    public DatosElemento crearSerializable() {
        guerra.aeronaves.comunicacion.elementos.Edificio e = 
                new guerra.aeronaves.comunicacion.elementos.Edificio(posicion);
        
        e.setVida(vida);
        
        return e;
    }
    
}