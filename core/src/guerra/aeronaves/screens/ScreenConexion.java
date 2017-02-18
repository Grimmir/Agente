package guerra.aeronaves.screens;

import guerra.aeronaves.GuerraAeronaves;
import guerra.aeronaves.comunicacion.Conexion;
import guerra.aeronaves.comunicacion.DatosConexion;
import guerra.aeronaves.comunicacion.ConexionListener;

public class ScreenConexion extends ScreenMenu implements ConexionListener {

    private final GuerraAeronaves guerraAeronaves;
    private final Conexion conexion;

    public ScreenConexion(GuerraAeronaves guerraAeronaves) {
        this.guerraAeronaves = guerraAeronaves;
        conexion = new Conexion(DatosConexion.crearDesdeArchivoConfiguracion(
                GuerraAeronaves.RUTA_CONFIGURACION_CONEXION_AGENTE));
        
        // Para el callback alEstablecerConexiones
        conexion.getListeners().add(this);
        
        System.out.println("Agente conectándose a " + conexion.getDatosConexion().getHostCliente()
                + ":" + conexion.getDatosConexion().getPuertoCliente() + "...");        
        conexion.iniciar();
    }
    
    // Este método no es llamado en el hilo principal.
    @Override
    public void alEstablecerConexion() {
        System.out.println("Agente conectado a " + conexion.getDatosConexion()
                .getHostCliente() + ":" + conexion.getDatosConexion().getPuertoCliente() + ".");
        ScreenJuego sj = new ScreenJuego(conexion);
        guerraAeronaves.setScreen(sj);
    }

}
