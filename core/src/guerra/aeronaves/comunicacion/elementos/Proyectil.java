package guerra.aeronaves.comunicacion.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.GuerraAeronaves;
import guerra.aeronaves.juego.elementos.Elemento;
import java.awt.Point;

public class Proyectil extends DatosElemento {
    
    private DatosElemento elementoCreador;

    public Proyectil() {

    }
    
    public Proyectil(Direccion direccion, Point posicion, Avion elementoCreador) {
        super(GuerraAeronaves.ID_PROYECTIL, posicion, direccion, GuerraAeronaves.VIDA_PROYECTIL);
        this.elementoCreador = elementoCreador;
    }

    public DatosElemento getElementoCreador() {
        return elementoCreador;
    }

    @Override
    public Elemento crearElemento() {
        return new guerra.aeronaves.juego.elementos.Proyectil(direccion, posicion, null);
    }
    
}
