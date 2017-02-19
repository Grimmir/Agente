package guerra.aeronaves;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import guerra.aeronaves.comunicacion.Conexion;
import guerra.aeronaves.comunicacion.DatosConexion;
import guerra.aeronaves.screens.ScreenJuego;
import java.util.Arrays;
import java.util.List;

public class GuerraAeronaves extends Game {
   
    public SpriteBatch batch;
    
    private Music music_menu, music_edicion, music_juego;
    
    public static final float 
              TIEMPO_TICK = 0.010f
            , VIDA_AVION = 3
            , VIDA_ESTACION_MUNICION = 6
            , VIDA_ESTACION_GASOLINA = 6
            , VIDA_EDIFICIO = 9
            , VIDA_INFINITA = Float.POSITIVE_INFINITY
            , VIDA_PROYECTIL = 1
            , CANTIDAD_PICKUP_VIDA = VIDA_AVION / 3
            , TIEMPO_EXPLOSION = 0.5f;
    
    public static final int
              ALTURA_BOTON = 82
            , ANCHO_BOTON = 284
            , NUM_COLUMNAS = 19
            , NUM_FILAS = 14
            , NUM_COLUMNAS_PALETA = 2
            , NUM_FILAS_PALETA = NUM_FILAS
            , TIEMPO_FINALIZACION = 2
            , ID_CIELO = 0
            , ID_AVION_ROJO = 4 
            , ID_AVION_AZUL = 9 
            , ID_PROYECTIL = 14
            , ID_NUBE = 19
            , ID_MONTANA = 24
            , ID_EDIFICIO = 29
            , ID_PICKUP_VIDA = 34
            , ID_PICKUP_GASOLINA = 39
            , ID_POWERUP_VIDA = 49
            , ID_POWERUP_MUNICIONES = 54
            , ID_PICKUP_MUNICIONES = 44 
            , ID_ESTACION_GASOLINA_ROJO = 64
            , ID_ESTACION_GASOLINA_AZUL = 65
            , ID_ESTACION_MUNICIONES_ROJO = 69
            , ID_ESTACION_MUNICIONES_AZUL = 70
            , ID_EXPLOSION = 71
            , MUNICIONES_AVION = 64
            , CANTIDAD_PICKUP_MUNICIONES = MUNICIONES_AVION / 3
            , TICKS_DETECCION_TECLAS = 5
            , TICKS_DETECCION_COLISIONES = 10
            , TICKS_ACTUALIZACION_AVIONES = 40
            , TICKS_ACTUALIZACION_PROYECTILES = 10
            , TICKS_ACTUALIZACION_NUBES = 60
            , TICKS_ENVIO_PAQUETE_DATOS = 5
            , GASOLINA_AVION = (int)Math.round(20 / (TICKS_ACTUALIZACION_AVIONES * TIEMPO_TICK))
            , CANTIDAD_PICKUP_GASOLINA = GASOLINA_AVION / 3
            ;            
    
    public static final List<String> RUTA_EXPLOSIONES = Arrays.asList(
              "explosion1.png"
            , "explosion2.png"
            , "explosion3.png"
            , "explosion4.png"
            , "explosion5.png"
            , "explosion6.png");
    
    public static final String RUTA_CONFIGURACION_CONEXION_AGENTE = "config/ConexionTeclasAgente.txt";
    
    private Conexion conexion;
    
    @Override
    public void create() {
        batch = new SpriteBatch();

        music_menu = Gdx.audio.newMusic(Gdx.files.internal("sonidos/musica_menu.mp3"));
        music_edicion = Gdx.audio.newMusic(Gdx.files.internal("sonidos/musica_edicion.mp3"));
        music_juego = Gdx.audio.newMusic(Gdx.files.internal("sonidos/musica_juego.mp3"));
        
        conexion = new Conexion(DatosConexion.crearDesdeArchivo(RUTA_CONFIGURACION_CONEXION_AGENTE));
        conexion.abrir();
        
        setScreen(new ScreenJuego(this));
        setMusica(music_menu);
    }

    @Override
    public void render () {
        super.render();
    }

    @Override
    public void dispose () {
        super.dispose();
        //conexion.cerrar();
    }

    public void setMusica(Music m) {
        m.setVolume(0.1f);
        m.setLooping(true);
        if(m.equals(music_menu)) {
            music_edicion.stop();
            music_juego.stop();
        } else if (m.equals(music_edicion)) {
            music_menu.stop();
            music_juego.stop();
        } else {
            music_menu.stop();
            music_edicion.stop();
        }
        if(!(m.equals(music_menu) && music_menu.isPlaying())) {
            m.play();
        }
    }
    
    // Devuelve el tamaño que debe tener una casilla del juego
    public static final float calcularTamañoCasilla(float ancho, float alto) {
        float tamañoSegunColumnas = (float)Math.floor(ancho / NUM_COLUMNAS);
        float tamañoSegunFilas = (float)Math.floor(alto / NUM_FILAS);
        
        return (tamañoSegunColumnas < tamañoSegunFilas) 
                ? tamañoSegunColumnas 
                : tamañoSegunFilas;
    }
    
    // Devuelve el tamaño que debe tener una casilla del editor de mapas
    public static final float calcularTamañoCasillaEditor(float ancho, float alto) {       
        float tamañoSegunColumnas = ancho / getNumColumnasEditor();
        float tamañoSegunFilas = alto / getNumFilasEditor();
        
        return (tamañoSegunColumnas < tamañoSegunFilas) 
                ? tamañoSegunColumnas 
                : tamañoSegunFilas;
    }
    
    public static final int getNumColumnasEditor() {
        return NUM_COLUMNAS + NUM_COLUMNAS_PALETA;
    }
    
    public static final int getNumFilasEditor() {
        return NUM_FILAS;
    }  

    public Conexion getConexion() {
        return conexion;
    }
    
}
