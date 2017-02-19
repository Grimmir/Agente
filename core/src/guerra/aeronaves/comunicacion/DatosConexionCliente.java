package guerra.aeronaves.comunicacion;

public class DatosConexionCliente {
    
    private final String host;
    private final int puerto;

    public DatosConexionCliente(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
    }

    public String getHost() {
        return host;
    }

    public int getPuerto() {
        return puerto;
    }
    
}
