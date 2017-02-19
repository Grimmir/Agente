package guerra.aeronaves.comunicacion;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente extends Listener {

    private final DatosConexionCliente datosConexion;
    private final Client client;
    private final List<ClienteListener> listeners;
    private boolean mantenerConexionViva;
    
    public Cliente(DatosConexionCliente datosConexion) {
        this.datosConexion = datosConexion;
        listeners = new ArrayList<ClienteListener>();
        client = new Client();
        
        Red.registrar(client);
        
        mantenerConexionViva = false;
    }

    public void encender() {
        mantenerConexionViva = true;
        client.addListener(this);
        client.start();            
        new Thread() {
            @Override
            public void run() {
                mantenerConexionViva();
            }
        }.start();
    }
    
    public void cerrar() {
        mantenerConexionViva = false;
        client.removeListener(this);
        client.stop();
    }

    @Override
    public void received(Connection cnctn, Object o) {
        super.received(cnctn, o);
        
        if (o instanceof PaqueteDatos) {
            notificarRecepcionPaqueteDatos((PaqueteDatos)o);
        }
    }
    
    public void notificarRecepcionPaqueteDatos(final PaqueteDatos paqueteDatos) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                for (ClienteListener l : listeners) {
                    l.alRecibirPaqueteDatos(paqueteDatos);
                }
            }
        });
    }
    
    public DatosConexionCliente getDatosConexion() {
        return datosConexion;
    }
    
    public List<ClienteListener> getListeners() {
        return listeners;
    }
    
    public void mantenerConexionViva() {
        while (mantenerConexionViva) {           
            if (!client.isConnected()) {
                try {
                    System.out.println("Conectándose a " + datosConexion.getHost() 
                            + ":" + datosConexion.getPuerto() + "...");
                    client.connect(5000, datosConexion.getHost(), datosConexion.getPuerto());
                    System.out.println("Conectado a " + datosConexion.getHost() 
                            + ":" + datosConexion.getPuerto() + ".");
                } 
                catch (IOException ex) {
                    //Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            try {
                Thread.sleep(1000);
            } 
            catch (InterruptedException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }
    }
    
}
