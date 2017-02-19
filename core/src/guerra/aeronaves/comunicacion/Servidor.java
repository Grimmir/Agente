package guerra.aeronaves.comunicacion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.Socket;
import guerra.aeronaves.GuerraAeronaves;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Thread.sleep;
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
    
    private Object datoSolicitado;

    public Servidor(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
        socketServidor = Gdx.net.newServerSocket(Net.Protocol.TCP, puerto, null);
        
        solicitudPendienteDatosAgente = false;
        solicitudPendienteDatosAmbiente = false;
        
        datoSolicitado = null;
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
                try {
                    sleep((int)Math.floor(GuerraAeronaves.TIEMPO_TICK));
                } 
                catch (InterruptedException ex) {
                    //Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (socket != null && socket.isConnected() && objectOutputStream != null 
                        && getDatoSolicitado() != null) {
                    System.out.println("INTENTANDO ENVIAR DATOS AL AMBIENTE");
                    try {
                        objectOutputStream.writeObject(getDatoSolicitado());
                        setDatoSolicitado(null);                        
                    } 
                    catch (IOException ex) {
                        //Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
        
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
                    if (socket != null && socket.isConnected() && objectInputStream != null) {
                        try {
                            System.out.println("ESPERANDO MENSAJES DEL CLIENTE");
                            Mensaje m = (Mensaje)objectInputStream.readObject();

                            if (m == Mensaje.AMBIENTE_LECTURA_DATOS_AGENTE) {
                                setSolicitudPendienteDatosAgente(true);
                            }

                            else if (m == Mensaje.AGENTE_LECTURA_DATOS_AMBIENTE) {
                                setSolicitudPendienteDatosAmbiente(true);
                            }
                            System.out.println("FIN DE ESPERA MENSAJE CLIENTE");
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
        solicitudPendienteDatosAmbiente = false;
    }
    
    public void enviarDatosAlAmbiente(DatosAgente da) {
        if (solicitudPendienteDatosAgente) {
            enviarDatosAlCliente(da);
        }
        solicitudPendienteDatosAgente = false;
    }
    
    public void enviarDatosAlCliente(Object o) {
        setDatoSolicitado(o);
    }
    
    private synchronized void setDatoSolicitado(Object o) {
        datoSolicitado = o;
    }
    
    private synchronized Object getDatoSolicitado() {
        return datoSolicitado;
    }
    
    public String getHost() {
        return host;
    }

    public int getPuerto() {
        return puerto;
    }
    
}
