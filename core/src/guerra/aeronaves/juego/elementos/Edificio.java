package guerra.aeronaves.juego.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.GuerraAeronaves;
import java.awt.Point;

public class Edificio extends Elemento {  
    
    public Edificio(Point posicion) {
        super("edificio.png", GuerraAeronaves.ID_EDIFICIO, posicion, Direccion.ARRIBA, GuerraAeronaves.VIDA_EDIFICIO);
    }

    @Override
    public Elemento crearAPartirDe(Elemento e) {
        return new Edificio(e.getPosicion());
    }
    
}