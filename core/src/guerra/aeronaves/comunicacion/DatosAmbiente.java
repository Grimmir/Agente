package guerra.aeronaves.comunicacion;

import guerra.aeronaves.juego.elementos.Elemento;
import java.util.List;

public class DatosAmbiente {

    private final List<Elemento> elementos;

    public DatosAmbiente(List<Elemento> elementos) {
        this.elementos = elementos;
    }

    public List<Elemento> getElementos() {
        return elementos;
    }
    
}
