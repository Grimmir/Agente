package guerra.aeronaves.comunicacion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import guerra.aeronaves.GuerraAeronaves;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
    
    private final String host;
    private final int puerto;
    private Socket socketCliente;
    private final List<ClienteListener> listeners;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    
    private boolean 
              solicitudPendienteDatosAgente
            , solicitudPendienteDatosAmbiente;    

    public Cliente(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
        listeners = new ArrayList<ClienteListener>();
        solicitudPendienteDatosAgente = false;
        solicitudPendienteDatosAmbiente = false;
    }
    
    public void iniciar() {
        while (socketCliente == null) {
            try {
                socketCliente = Gdx.net.newClientSocket(Net.Protocol.TCP, host, puerto, null);
            }
            catch (RuntimeException ex) {  }
        }
        
        try {
            objectOutputStream = new ObjectOutputStream(socketCliente.getOutputStream());           
            objectInputStream = new ObjectInputStream(socketCliente.getInputStream());
        } 
        catch (IOException ex) {
            //Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        sleep((int)Math.floor(GuerraAeronaves.TIEMPO_TICK));
                    } 
                    catch (InterruptedException ex) {
                        //Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (socketCliente != null && socketCliente.isConnected() 
                            && objectInputStream != null && objectOutputStream != null) {
                        try {
                            if (solicitudPendienteDatosAgente) {
                                objectOutputStream.writeObject(Mensaje.AMBIENTE_LECTURA_DATOS_AGENTE);
                                System.out.println("ESPERANDO MENSAJE DE SERVIDOR");
                                informarRecepcionDatos((DatosAgente)objectInputStream.readObject());
                                System.out.println("FIN DE ESPERA MENSAJE SERVIDOR");
                                solicitudPendienteDatosAgente = false;
                            }

                            if (solicitudPendienteDatosAmbiente) {
                                objectOutputStream.writeObject(Mensaje.AGENTE_LECTURA_DATOS_AMBIENTE);
                                System.out.println("ESPERANDO MENSAJE DE SERVIDOR");
                                informarRecepcionDatos((DatosAmbiente)objectInputStream.readObject());
                                System.out.println("FIN DE ESPERA MENSAJE SERVIDOR");
                                solicitudPendienteDatosAmbiente = false;
                            }                            
                        }
                        catch (IOException ex) {
                            //Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                        } 
                        catch (ClassNotFoundException ex) {
                            //Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                        }                     
                    }
                }
            }
        }.start();
    }
        
    public void solicitarDatosAgente() {
        solicitudPendienteDatosAgente = true;
    }
    
    public void solicitarDatosAmbiente() {
        solicitudPendienteDatosAmbiente = true;   
    }
    
    public void informarRecepcionDatos(final Object datos) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                for (ClienteListener l : listeners) {
                    l.alRecibirDatosServidor(datos);
                }                                
            }
        });        
    }
    
    public List<ClienteListener> getListeners() {
        return listeners;
    }
    
}
