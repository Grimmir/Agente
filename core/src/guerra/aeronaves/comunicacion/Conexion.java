package guerra.aeronaves.comunicacion;

public class Conexion {
    
    private final Cliente cliente;
    private final Servidor servidor;
    
    public Conexion(DatosConexion datosConexion) {
        cliente = new Cliente(datosConexion.getDatosCliente());
        servidor = new Servidor(datosConexion.getDatosServidor());
    }
    
    public void abrir() {
        servidor.encender();
        cliente.encender();
    }
    
    public Cliente getCliente() {
        return cliente;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void cerrar() {
        servidor.cerrar();
        cliente.cerrar();
    }
    
}
