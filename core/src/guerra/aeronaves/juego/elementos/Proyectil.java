package guerra.aeronaves.juego.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.GuerraAeronaves;
import guerra.aeronaves.comunicacion.elementos.DatosElemento;
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
    public DatosElemento crearSerializable() {
        guerra.aeronaves.comunicacion.elementos.Proyectil p = 
                new guerra.aeronaves.comunicacion.elementos.Proyectil(direccion, posicion, null);
        
        p.setVida(vida);
        
        return p;
    }
    
}
