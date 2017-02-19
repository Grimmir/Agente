package guerra.aeronaves.comunicacion;

import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.esotericsoftware.kryonet.Server;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

    private final DatosConexionServidor datosConexion;
    private final Server server;
    
    public Servidor(DatosConexionServidor datosConexion) {
        this.datosConexion = datosConexion;
        server = new Server();

        Red.registrar(server);
        
        try {
            server.bind(datosConexion.getPuerto());
        } 
        catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }

    public DatosConexionServidor getDatosConexion() {
        return datosConexion;
    }
    
    public void enviarPaqueteDatos(PaqueteDatos paqueteDatos) {
        server.sendToAllTCP(paqueteDatos);            
    }

    public void encender() {
        server.start();            
    }
    
    public void cerrar() {
        server.stop();
    }
    
}
