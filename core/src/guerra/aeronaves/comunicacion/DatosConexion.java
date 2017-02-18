package guerra.aeronaves.comunicacion;

import com.badlogic.gdx.Gdx;
import java.util.Scanner;

public class DatosConexion {
    
    private final String hostCliente, hostServidor;
    private final int puertoCliente, puertoServidor;

    public DatosConexion(String hostCliente, int puertoCliente
            , String hostServidor, int puertoServidor) {
        this.hostCliente = hostCliente;
        this.puertoCliente = puertoCliente;
        this.hostServidor = hostServidor;
        this.puertoServidor = puertoServidor;
    }

    public static DatosConexion crearDesdeArchivoConfiguracion(String rutaConf) {
        Scanner sc = new Scanner(Gdx.files.local(rutaConf).readString());     
        return new DatosConexion(sc.next(), sc.nextInt(), sc.next(), sc.nextInt());
    }
    
    public String getHostCliente() {
        return hostCliente;
    }

    public int getPuertoCliente() {
        return puertoCliente;
    }

    public String getHostServidor() {
        return hostServidor;
    }

    public int getPuertoServidor() {
        return puertoServidor;
    }

}
