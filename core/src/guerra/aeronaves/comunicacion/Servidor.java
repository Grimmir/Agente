package guerra.aeronaves.comunicacion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;

public class Servidor {
    
    private final String host;
    private final int puerto;
    private final ServerSocket socketServidor;

    public Servidor(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
        socketServidor = Gdx.net.newServerSocket(Net.Protocol.TCP, puerto, null);
    }

    public void iniciar() {
        System.out.println("Iniciado socketServidor (" + socketServidor.getProtocol() + ").");
    }
    
    public String getHost() {
        return host;
    }

    public int getPuerto() {
        return puerto;
    }
    
}
