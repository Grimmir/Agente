package guerra.aeronaves.comunicacion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.util.ArrayList;
import java.util.List;

public class ConexionAgente extends Conexion {

    private final List<ConexionAgenteListener> listeners;
    private Socket socket;
    
    public ConexionAgente(String host, int puerto) {
        super(host, puerto);       
        listeners = new ArrayList<ConexionAgenteListener>();
    }

    @Override
    public void iniciar() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket == null) {
                    try {
                        socket = Gdx.net.newClientSocket(Net.Protocol.TCP, host, puerto, null);                    
                    }
                    catch (GdxRuntimeException e) {  }
                }
                
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        for (ConexionAgenteListener cal : listeners) {
                            cal.alEstablecerConexion();
                        }                  
                    }
                });
            }
        }).start();
    }

    public List<ConexionAgenteListener> getListeners() {
        return listeners;
    }
    
}
