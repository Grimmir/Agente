package guerra.aeronaves.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import guerra.aeronaves.GuerraAeronaves;
import guerra.aeronaves.comunicacion.ClienteListener;
import guerra.aeronaves.comunicacion.Conexion;
import guerra.aeronaves.comunicacion.DatosAgente;
import guerra.aeronaves.comunicacion.DatosAmbiente;
import guerra.aeronaves.comunicacion.DatosExplosion;
import guerra.aeronaves.comunicacion.TeclasPresionadas;
import guerra.aeronaves.juego.elementos.AvionAzul;
import guerra.aeronaves.juego.elementos.AvionRojo;
import guerra.aeronaves.juego.elementos.Edificio;
import guerra.aeronaves.juego.elementos.Elemento;
import guerra.aeronaves.juego.elementos.EstacionGasolinaAzul;
import guerra.aeronaves.juego.elementos.EstacionGasolinaRojo;
import guerra.aeronaves.juego.elementos.EstacionMunicionesAzul;
import guerra.aeronaves.juego.elementos.EstacionMunicionesRojo;
import guerra.aeronaves.juego.elementos.Explosion;
import guerra.aeronaves.juego.elementos.Montana;
import guerra.aeronaves.juego.elementos.Nube;
import guerra.aeronaves.juego.elementos.PickupGasolina;
import guerra.aeronaves.juego.elementos.PickupMuniciones;
import guerra.aeronaves.juego.elementos.PickupVida;
import guerra.aeronaves.juego.elementos.PowerupMuniciones;
import guerra.aeronaves.juego.elementos.PowerupVida;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Juego implements ClienteListener {  

    private final Stage stage;
    private final Conexion conexion;
    private final Timer timer;
    private int ticks;
    private TeclasPresionadas teclasPresionadas;
    private final List<Vector2> centrosCasillas;
    private final Sound sonidoExplosion;
    
    public Juego(Stage stage, Conexion conexion) {
        this.stage = stage;
        this.conexion = conexion;
        
        conexion.getCliente().getListeners().add(this);
        
        sonidoExplosion = Gdx.audio.newSound(Gdx.files.internal("sonidos/snd_explosion.wav"));
        
        centrosCasillas = obtenerCentrosCasillas();
        
        Image fondo = new Image(new SpriteDrawable(new Sprite(new Texture(
                Gdx.files.internal("cielo1.png")))));        
        fondo.setFillParent(true);
        stage.addActor(fondo);        
        
        timer = new Timer();
        ticks = 0;
        
        // Valor inicial de las teclas presionadas
        teclasPresionadas = new TeclasPresionadas(false, false, false, false, false);
    }

    public void iniciar() {
        timer.clear();
        timer.scheduleTask(new Task() {
            @Override
            public void run() {
                ticks = (ticks == Long.MAX_VALUE) ? 0 : ticks + 1;
                
                if (ticks % GuerraAeronaves.TICKS_SOLICITUD_DATOS_AMBIENTE == 0) {
                    conexion.getCliente().solicitarDatosAmbiente();
                }
                
                if (ticks % GuerraAeronaves.TICKS_DETECCION_TECLAS == 0) {
                    teclasPresionadas = detectarTeclas(  
                              Keys.W
                            , Keys.D
                            , Keys.S
                            , Keys.A
                            , Keys.SPACE);
                }
                
                if (ticks % GuerraAeronaves.TICKS_ENVIO_DATOS_AGENTE == 0) {
                    conexion.getServidor().enviarDatosAlAmbiente(new DatosAgente(
                            teclasPresionadas));
                }
            }
        }, GuerraAeronaves.TIEMPO_TICK, GuerraAeronaves.TIEMPO_TICK);
    }

    @Override
    public void alRecibirDatosServidor(Object datosServidor) {
        DatosAmbiente da = (DatosAmbiente)datosServidor;
        List<Elemento> copiaElementos = new ArrayList<Elemento>();
        
        for (Elemento e : da.getElementosVisibles()) {
            Elemento copiaElemento = e.crearAPartirDe(e);
            copiaElementos.add(copiaElemento);
            posicionarElementoMapa(copiaElemento);           
        }
        
        stage.getActors().clear();
        agregarElementos(stage, copiaElementos);
        
        for (DatosExplosion de : da.getExplosiones()) {
            crearExplosion(de);
        }
    }
    
    // Detecta las teclas que presionó el usuario.
    private TeclasPresionadas detectarTeclas(int teclaArriba, int teclaDerecha, int teclaAbajo
            , int teclaIzquierda, int teclaDisparar) {
        
        return new TeclasPresionadas(
                  Gdx.input.isKeyPressed(teclaArriba)
                , Gdx.input.isKeyPressed(teclaDerecha)
                , Gdx.input.isKeyPressed(teclaAbajo)
                , Gdx.input.isKeyPressed(teclaIzquierda)
                , Gdx.input.isKeyPressed(teclaDisparar));
    }
    
    // Agrega todos los elemento de un arreglo de elementos a un stage, tomando 
    // en cuenta que un elemento es un actor.
    private void agregarElementos(Stage stage, List<Elemento> elementos) {
        for (int i=0;i<elementos.size();i++) {
            if(elementos.get(i) instanceof Edificio || elementos.get(i) instanceof Montana 
                    || elementos.get(i) instanceof EstacionGasolinaAzul || elementos.get(i) instanceof EstacionGasolinaRojo
                    || elementos.get(i) instanceof EstacionMunicionesAzul || elementos.get(i) instanceof EstacionMunicionesRojo
                    || elementos.get(i) instanceof PickupMuniciones || elementos.get(i) instanceof PickupVida
                    || elementos.get(i) instanceof PickupGasolina || elementos.get(i) instanceof PowerupVida
                    || elementos.get(i) instanceof PowerupMuniciones) {
                stage.addActor(elementos.get(i));
            }
        }
        for (int i=0;i<elementos.size();i++) {
            if(elementos.get(i) instanceof AvionAzul || elementos.get(i) instanceof AvionRojo) {
                stage.addActor(elementos.get(i));
            }
        }
        for (int i=0;i<elementos.size();i++) {
            if(elementos.get(i) instanceof Nube) {
                stage.addActor(elementos.get(i));
            }
        }
    }
    
    // Remplaza el sprite del elemento por sprites de explosión durante un 
    // tiempo en segundos.
    private void crearExplosion(DatosExplosion de) {
        final float tiempoSprite = de.getTiempo() / 6;
        final ArrayDeque<String> rutaExplosiones = new ArrayDeque<String>(
                GuerraAeronaves.RUTA_EXPLOSIONES);
        Vector2 posicionEnMapa = calcularPosicionMapa(GuerraAeronaves.NUM_FILAS, GuerraAeronaves.NUM_COLUMNAS
                , centrosCasillas, de.getPosicion().x, de.getPosicion().y);
        final Explosion explosion = new Explosion(rutaExplosiones.pop(), de.getPosicion());
        
        stage.addActor(explosion);
        explosion.setPosition(posicionEnMapa.x, posicionEnMapa.y);
        sonidoExplosion.play(0.2f);
        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if (!rutaExplosiones.isEmpty()) {
                    explosion.setDrawable(new SpriteDrawable(new Sprite(new Texture(
                            Gdx.files.internal(rutaExplosiones.pop())))));
                }
                else {
                    stage.getActors().removeValue(explosion, true);
                }
            }
        }, 0, tiempoSprite, GuerraAeronaves.RUTA_EXPLOSIONES.size() + 1);
    }    
    
    // Devuelve un arreglo con todos los centros de las casillas del mapa
    private ArrayList<Vector2> obtenerCentrosCasillas() {
        ArrayList<Vector2> centros = new ArrayList<Vector2>();
        
        for (int i = 0; i < GuerraAeronaves.NUM_FILAS; i++) {
            for (int j = 0; j < GuerraAeronaves.NUM_COLUMNAS; j++) {
                centros.add(new Vector2(j * GuerraAeronaves.calcularTamañoCasilla(stage.getWidth(), stage.getHeight()),
                i * GuerraAeronaves.calcularTamañoCasilla(stage.getWidth(), stage.getHeight())));
            }
        }
        return centros;
    }

    private Vector2 calcularPosicionMapa(int numFilasMapa, int numColumnasMapa
            , List<Vector2> centrosCasillas, int columna, int fila) {
        
        columna = Math.max(0, columna);
        columna = Math.min(numColumnasMapa - 1, columna);
        
        fila = Math.max(0, fila);
        fila = Math.min(numFilasMapa - 1, fila);
        
        int idxCentro = (numFilasMapa - 1 - fila) * numColumnasMapa + columna;
        return centrosCasillas.get(idxCentro);
    }
    
    private void posicionarElementoMapa(Elemento e) {
        Vector2 posicionMapa = calcularPosicionMapa(GuerraAeronaves.NUM_FILAS, GuerraAeronaves.NUM_COLUMNAS
                , centrosCasillas, e.getPosicion().x, e.getPosicion().y);
        e.setPosition(posicionMapa.x, posicionMapa.y);
    }    
    
}