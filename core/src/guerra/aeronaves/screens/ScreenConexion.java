package guerra.aeronaves.screens;

import com.badlogic.gdx.Gdx;
import guerra.aeronaves.GuerraAeronaves;
import guerra.aeronaves.comunicacion.ConexionAgente;
import guerra.aeronaves.comunicacion.ConexionAgenteListener;
import guerra.aeronaves.comunicacion.DatosConexion;

public class ScreenConexion extends ScreenMenu implements ConexionAgenteListener {

    private final ConexionAgente conexionAgente;

    public ScreenConexion() {
       
        DatosConexion dc = DatosConexion.crearDesdeArchivoConfiguracion(
                GuerraAeronaves.RUTA_CONFIGURACION_CONEXION_AGENTE);
        
        conexionAgente = new ConexionAgente(dc.getHost(), dc.getPuerto());
        
        // Para el callback alEstablecerConexiones
        conexionAgente.getListeners().add(this);
        
        System.out.println("Agente conectándose a " + conexionAgente.getHost() 
                + ":" + conexionAgente.getPuerto() + "...");        
        conexionAgente.iniciar();
    }
    
    // Este método no es llamado en el hilo principal.
    @Override
    public void alEstablecerConexion() {
        System.out.println("Agente conectado a " + conexionAgente
                .getHost() + ":" + conexionAgente.getPuerto() + ".");
    }

}
