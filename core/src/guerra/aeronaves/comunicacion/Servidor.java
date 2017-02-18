package guerra.aeronaves.comunicacion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {
    
    private final String host;
    private final int puerto;
    private final ServerSocket socketServidor;
    private Socket socket;
    
    private boolean 
              solicitudPendienteDatosAgente
            , solicitudPendienteDatosAmbiente;
    
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public Servidor(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
        socketServidor = Gdx.net.newServerSocket(Net.Protocol.TCP, puerto, null);
        
        solicitudPendienteDatosAgente = false;
        solicitudPendienteDatosAmbiente = false;
    }

    public void iniciar() {
        while (socket == null) {
            try {
                socket = socketServidor.accept(null);
            }
            catch (RuntimeException ex) {  }
        }
        
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } 
        catch (IOException ex) {
            //Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (socket != null && socket.isConnected()) {                        
                        try {
                            if (objectInputStream != null && objectOutputStream != null) {
                                Mensaje m = (Mensaje)objectInputStream.readObject();
                            
                                if (m == Mensaje.AMBIENTE_LECTURA_DATOS_AGENTE) {
                                    setSolicitudPendienteDatosAgente(true);
                                }

                                else if (m == Mensaje.AGENTE_LECTURA_DATOS_AMBIENTE) {
                                    setSolicitudPendienteDatosAmbiente(true);
                                }
                            }
                        } 
                        catch (IOException ex) {
                            //Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                        } 
                        catch (ClassNotFoundException ex) {
                            //Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }.start();      
    }

    private void setSolicitudPendienteDatosAgente(final boolean spda) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                solicitudPendienteDatosAgente = spda;
            }
        });
    }

    private void setSolicitudPendienteDatosAmbiente(final boolean spda) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                solicitudPendienteDatosAmbiente = spda;
            }
        });
    }
    
    public void enviarDatosAlAgente(DatosAmbiente da) {
        if (solicitudPendienteDatosAmbiente) {
            enviarDatosAlCliente(da);
        }
    }
    
    public void enviarDatosAlAmbiente(DatosAgente da) {
        if (solicitudPendienteDatosAgente) {
            enviarDatosAlCliente(da);
        }
    }
    
    public void enviarDatosAlCliente(final Object o) {
        if (socket != null && socket.isConnected()) {
            try {
                objectOutputStream.writeObject(o);
            } 
            catch (IOException ex) {
                //Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public String getHost() {
        return host;
    }

    public int getPuerto() {
        return puerto;
    }
    
}
