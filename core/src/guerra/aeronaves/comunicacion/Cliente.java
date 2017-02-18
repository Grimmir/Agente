package guerra.aeronaves.comunicacion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;

public class Cliente {
    
    private final String host;
    private final int puerto;
    private Socket socketCliente;

    public Cliente(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
    }
    
    public void iniciar() {
        while (socketCliente == null) {
            socketCliente = Gdx.net.newClientSocket(Net.Protocol.TCP, host, puerto, null);
        }
    }
}
