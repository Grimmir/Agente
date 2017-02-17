package guerra.aeronaves.comunicacion;

import com.badlogic.gdx.Gdx;
import java.util.Scanner;

public class DatosConexion {
    
    private final String host;
    private final int puerto;

    public DatosConexion(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
    }

    public static DatosConexion crearDesdeArchivoConfiguracion(String rutaConf) {
        Scanner sc = new Scanner(Gdx.files.local(rutaConf).readString());     
        return new DatosConexion(sc.next(), sc.nextInt());
    }
    
    public String getHost() {
        return host;
    }

    public int getPuerto() {
        return puerto;
    }
    
}
