package guerra.aeronaves.comunicacion;

import com.badlogic.gdx.Gdx;
import java.util.ArrayList;
import java.util.List;

public class Conexion {
    
    private final DatosConexion datosConexion;
    private final List<ConexionListener> listeners;
    private final Servidor servidor;
    private final Cliente cliente;
    
    public Conexion(DatosConexion datosConexion) {
        this.datosConexion = datosConexion;
        listeners = new ArrayList<ConexionListener>();
        servidor = new Servidor(datosConexion.getHostServidor(), datosConexion.getPuertoServidor());
        cliente = new Cliente(datosConexion.getHostCliente(), datosConexion.getPuertoCliente());
    }

    public void iniciar() {
        final Thread thServidor = new Thread() {
            @Override
            public void run() {
                servidor.iniciar();
            }
        };
        
        final Thread thCliente = new Thread() {
            @Override
            public void run() {
                cliente.iniciar();
            }
        };        
        
        new Thread() {
            @Override
            public void run() {
               thServidor.start();
               thCliente.start();
               
                try {
                    thCliente.join();
                    thServidor.join();
                } 
                catch (InterruptedException ex) {  }
                
                Gdx.app.postRunnable(new Runnable() {
                   @Override
                   public void run() {
                       for (ConexionListener l : listeners) {
                           l.alEstablecerConexion();
                       }
                   }
                });
            }
        }.start();
    }
    
    public List<ConexionListener> getListeners() {
        return listeners;
    }

    public DatosConexion getDatosConexion() {
        return datosConexion;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public Cliente getCliente() {
        return cliente;
    }
    
}