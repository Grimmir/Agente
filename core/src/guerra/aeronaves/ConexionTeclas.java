package guerra.aeronaves;

import guerra.aeronaves.juego.TeclasPresionadas;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionTeclas {
    
    final String host;
    final int puerto;
    Socket so;
    
    public ConexionTeclas(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
    }
    
    public void iniciarConexion() {
        try {
            so = new Socket(host, puerto);
        }
        catch (IOException ex) {
            Logger.getLogger(ConexionTeclas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cerrarConexion() {
       if (so != null) {
           try {
               so.close();
           } catch (IOException ex) {
               Logger.getLogger(ConexionTeclas.class.getName()).log(Level.SEVERE, null, ex);
           }
       } 
    }    
    
    public void enviarMensajeTeclas(TeclasPresionadas tp) {
        if (so != null) {
            try {
                ObjectOutputStream mensaje = new ObjectOutputStream(so.getOutputStream());
                mensaje.writeObject(tp);
            } catch (IOException ex) {
                Logger.getLogger(ConexionTeclas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}