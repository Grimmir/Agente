package guerra.aeronaves.juego.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.GuerraAeronaves;
import java.awt.Point;

public class Proyectil extends Elemento {
    
    private final Elemento elementoCreador;
    
    public Proyectil(Direccion direccion, Point posicion, Avion elementoCreador) {
        super("proyectil.png", GuerraAeronaves.ID_PROYECTIL, posicion, direccion, GuerraAeronaves.VIDA_PROYECTIL);
        this.elementoCreador = elementoCreador;
    }

    public Elemento getElementoCreador() {
        return elementoCreador;
    }

    @Override
    public Elemento crearAPartirDe(Elemento e) {
        return new Proyectil(e.getDireccion(), e.getPosicion(), null);
    }
    
}
