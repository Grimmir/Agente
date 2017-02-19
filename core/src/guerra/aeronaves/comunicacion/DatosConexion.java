package guerra.aeronaves.comunicacion;

import com.badlogic.gdx.Gdx;
import java.util.Scanner;

public class DatosConexion {
 
    private final DatosConexionCliente datosCliente;
    private final DatosConexionServidor datosServidor;

    public DatosConexion(DatosConexionCliente dc, DatosConexionServidor ds) {
        datosCliente = dc;
        datosServidor = ds;
    }
    
    public static DatosConexion crearDesdeArchivo(String rutaArchivo) {
        Scanner sc = new Scanner(Gdx.files.local(rutaArchivo).readString());
        
        String hostCliente = sc.next();
        int puertoCliente = sc.nextInt();
        sc.next();
        int puertoServidor = sc.nextInt();
        
        return new DatosConexion(new DatosConexionCliente(hostCliente, puertoCliente)
                , new DatosConexionServidor(puertoServidor));        
    }
    
    public DatosConexionCliente getDatosCliente() {
        return datosCliente;
    }

    public DatosConexionServidor getDatosServidor() {
        return datosServidor;
    }
    
}
