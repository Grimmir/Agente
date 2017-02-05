package guerra.aeronaves;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import guerra.aeronaves.screens.*;
import java.util.Arrays;
import java.util.List;

public class GuerraAeronaves extends Game {
   
    public SpriteBatch batch;
    
    private ScreenEditorNuevo sc_editor;
    
    private Music music_menu, music_edicion, music_juego;
    
    public static final float TIEMPO_ACTUALIZACION_JUEGO = 0.003f;
    public static final int ALTURA_BOTON = 82;
    public static final int ANCHO_BOTON = 284;
    public static final int NUM_COLUMNAS = 19;
    public static final int NUM_FILAS = 14;
    public static final int NUM_COLUMNAS_PALETA = 2;
    public static final int NUM_FILAS_PALETA = NUM_FILAS;
    
    public static final int INDICE_FONDO = 0;
    public static final int INDICE_MEDIO = 1;
    public static final int INDICE_INTERMEDIO = 2;
    public static final int INDICE_ALTO = 3;
    
    // Tiempo que hay entre la explosión de una aeronave y la restauración de 
    // la misma.
    public static final int TIEMPO_FINALIZACION = 2;
    
    // Tiempo durante el cual se muestran los sprites de explosión
    public static final float TIEMPO_EXPLOSION = 0.5f;
    
    public static final int
              ID_CIELO = 0
            , ID_AVION_ROJO = 4 
            , ID_AVION_AZUL = 9 
            , ID_PROYECTIL = 14
            , ID_NUBE = 19
            , ID_MONTANA = 24
            , ID_EDIFICIO = 29
            , ID_PICKUP_VIDA = 34
            , ID_PICKUP_GASOLINA = 39
            , ID_POWERUP_VIDA = 49
            , ID_POWERUP_MUNICION = 54
            , ID_PICKUP_MUNICION = 44 
            , ID_ESTACION_GASOLINA_ROJO = 64
            , ID_ESTACION_GASOLINA_AZUL = 65
            , ID_ESTACION_MUNICION_ROJO = 69
            , ID_ESTACION_MUNICION_AZUL = 70;
    
    public static final List<String> RUTA_EXPLOSIONES = Arrays.asList(
              "explosion1.png"
            , "explosion2.png"
            , "explosion3.png"
            , "explosion4.png"
            , "explosion5.png"
            , "explosion6.png");
    
    public static final float 
              VIDA_AVION                = 3
            , VIDA_ESTACION_MUNICION    = 6
            , VIDA_ESTACION_GASOLINA    = 6
            , VIDA_EDIFICIO             = 9
            , VIDA_INFINITA             = Float.POSITIVE_INFINITY
            , VIDA_PROYECTIL            = 1;
    
    public static final int 
              MUNICION_INICIAL_AVION = 64;
    
    public static final int 
              GASOLINA_INICIAL_AVION = 100;
    
    @Override
    public void create () {
        batch = new SpriteBatch();

        music_menu = Gdx.audio.newMusic(Gdx.files.internal("sonidos/musica_menu.mp3"));
        music_edicion = Gdx.audio.newMusic(Gdx.files.internal("sonidos/musica_edicion.mp3"));
        music_juego = Gdx.audio.newMusic(Gdx.files.internal("sonidos/musica_juego.mp3"));

        setScreenMenuPrincipal();
    }

    @Override
    public void render () {
        super.render();
    }

    @Override
    public void dispose () {
        super.dispose();
    }

    public void setScreenMenuPrincipal() {
        setScreen(new ScreenMenuPrincipal(this));
        setMusica(music_menu);
    }
    public void setScreenJuego() {
        setScreen(new ScreenJuego(this));
        setMusica(music_juego);
    }
    public void setScreenMenuEditar() {
        setScreen(new ScreenMenuEditar(this));
        setMusica(music_menu);
    }
    public void setScreenEditor() {
        setScreen(new ScreenExploradorArchivos(this));
        setMusica(music_edicion);
    }
    public void setScreenEditorNuevo() {
        sc_editor = new ScreenEditorNuevo(this);
        setScreen(sc_editor);
        setMusica(music_edicion);
    }
    
    public void setScreenFinalizacionJuego(Ganador ganador) {
        setScreen(new ScreenFinalizacionJuego(this, ganador));
        setMusica(music_edicion);
    }
    
    public ScreenEditorNuevo getScreenEditorNuevo() {
        return sc_editor;
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
    
}
