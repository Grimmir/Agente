package guerra.aeronaves.comunicacion;

import java.io.Serializable;

public class DatosAgente implements Serializable {
    
    private final TeclasPresionadas teclasPresionadas;

    public DatosAgente(TeclasPresionadas teclasPresionadas) {
        this.teclasPresionadas = teclasPresionadas;
    }

    public TeclasPresionadas getTeclasPresionadas() {
        return teclasPresionadas;
    }
    
}
