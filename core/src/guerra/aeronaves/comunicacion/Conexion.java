package guerra.aeronaves.comunicacion;

public abstract class Conexion {
    
    protected  final String host;
    protected final int puerto;
    
    public Conexion(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
    }
    
    public abstract void iniciar();

    public String getHost() {
        return host;
    }
    
    public int getPuerto() {
        return puerto;
    }
    
}
