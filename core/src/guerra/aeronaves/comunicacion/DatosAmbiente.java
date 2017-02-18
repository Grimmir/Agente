package guerra.aeronaves.comunicacion;

import guerra.aeronaves.juego.elementos.Elemento;
import java.io.Serializable;
import java.util.List;

public class DatosAmbiente implements Serializable {
    
    private final List<Elemento> elementosVisibles;
    private final List<DatosExplosion> explosiones;

    public DatosAmbiente(List<Elemento> elementosVisibles, List<DatosExplosion> explosiones) {
        this.elementosVisibles = elementosVisibles;
        this.explosiones = explosiones;
    }

    public List<Elemento> getElementosVisibles() {
        return elementosVisibles;
    }

    public List<DatosExplosion> getExplosiones() {
        return explosiones;
    }

}
