package guerra.aeronaves.comunicacion;

public class DatosConexionServidor {

    private final int puerto;

    public DatosConexionServidor(int puerto) {
        this.puerto = puerto;
    }
    
    public int getPuerto() {
        return puerto;
    }
    
}
