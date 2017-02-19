package guerra.aeronaves.comunicacion;

public class PaqueteDatosAgente extends PaqueteDatos {
    
    private TeclasPresionadas teclasPresionadas;

    public PaqueteDatosAgente() {
        
    }
    
    public PaqueteDatosAgente(TeclasPresionadas teclasPresionadas) {
        this.teclasPresionadas = teclasPresionadas;
    }

    public TeclasPresionadas getTeclasPresionadas() {
        return teclasPresionadas;
    }
    
}
