package guerra.aeronaves.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import guerra.aeronaves.ConexionTeclas;
import guerra.aeronaves.GuerraAeronaves;
import java.util.Scanner;

public class ScreenConexion extends ScreenMenu {
    
    public ScreenConexion(GuerraAeronaves guerraAeronaves) {
        FileHandle archivoConexion = Gdx.files.local("config/conexion.txt");
        String datosConexion = archivoConexion.readString();
        Scanner sc = new Scanner(datosConexion);
        String ip = sc.next();
        String port = sc.next();
        
        ConexionTeclas ct = new ConexionTeclas(ip, Integer.parseInt(port));
        ct.iniciarConexion();
        guerraAeronaves.setScreenJuego(ct);
    }
    
}