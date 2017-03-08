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
import guerra.aeronaves.Direccion;
import guerra.aeronaves.GuerraAeronaves;
import guerra.aeronaves.comunicacion.ClienteListener;
import guerra.aeronaves.comunicacion.DatosExplosion;
import guerra.aeronaves.comunicacion.PaqueteDatos;
import guerra.aeronaves.comunicacion.PaqueteDatosAgente;
import guerra.aeronaves.comunicacion.PaqueteDatosAmbiente;
import guerra.aeronaves.comunicacion.TeclasPresionadas;
import guerra.aeronaves.comunicacion.elementos.DatosElemento;
import guerra.aeronaves.juego.elementos.Avion;
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
import guerra.aeronaves.juego.estrella.Astra1;
import guerra.aeronaves.juego.estrella.NodoAstar;
import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Juego implements ClienteListener {  

    private final Stage stage;
    private final Timer timer;
    private int ticks;
    private TeclasPresionadas teclasPresionadas;
    private final List<Vector2> centrosCasillas;
    private final Sound sonidoExplosion;
    private final GuerraAeronaves guerraAeronaves;
    private AvionAzul avionAzul;
    private AvionRojo avionRojo;
    
    public Juego(Stage stage, GuerraAeronaves guerraAeronaves) {
        this.stage = stage;
        this.guerraAeronaves = guerraAeronaves;
        
        sonidoExplosion = Gdx.audio.newSound(Gdx.files.internal("sonidos/snd_explosion.wav"));
        
        centrosCasillas = obtenerCentrosCasillas();
        
        Image fondo = new Image(new SpriteDrawable(new Sprite(new Texture(
                Gdx.files.internal("cielo1.png")))));        
        fondo.setFillParent(true);
        stage.addActor(fondo);        
        
        avionAzul = new AvionAzul(new Point(0, 0), Direccion.ARRIBA);
        avionRojo = new AvionRojo(new Point(0, 0), Direccion.ARRIBA);
        
        timer = new Timer();
        ticks = 0;
        
        // Valor inicial de las teclas presionadas
        teclasPresionadas = new TeclasPresionadas(false, false, false, false, false);        
    }

    public void iniciar() {
        guerraAeronaves.getConexion().getCliente().getListeners().clear();
        guerraAeronaves.getConexion().getCliente().getListeners().add(this);
        timer.clear();
        timer.scheduleTask(new Task() {
            @Override
            public void run() {
                ticks = (ticks == Long.MAX_VALUE) ? 0 : ticks + 1;
                                
                if (ticks % GuerraAeronaves.TICKS_ENVIO_PAQUETE_DATOS == 0) {
                    guerraAeronaves.getConexion().getServidor().enviarPaqueteDatos(
                            new PaqueteDatosAgente(teclasPresionadas));
                }                
            }
        }, GuerraAeronaves.TIEMPO_TICK, GuerraAeronaves.TIEMPO_TICK);
    }
    
    // La idea es recorrer la lista de elementos hasta encontrar uno cuya 
    // posición, obtenida con getPosicion, sea igual a la suministrada por 
    // el método. De no encontrar ninguno, devolver null.
    private Elemento buscarElementoEnPosicion(List<Elemento> es, final int f, final int c) {
        return (Elemento)es.stream().filter(new Predicate<Elemento>() {
            @Override
            public boolean test(Elemento e) {
                return e.getPosicion().x == c && e.getPosicion().y == f;
            }
        }).findFirst().orElse(null);
    }
    
    private void pseudoInteligencia(List<Elemento> elementos) {
        // Llenar la matriz con todos los elementos que se encuentran 
        // actualmente en el mapa.
        
        // Si no se encuentra un elemento en alguna posición de la matriz, se 
        // rellenará con los datos de un elemento "cielo" que será transitable.
        
        final NodoAstar[][] matrizAstar = new NodoAstar[GuerraAeronaves.NUM_FILAS][GuerraAeronaves.NUM_COLUMNAS];
        NodoAstar nodoAgente = null;
        NodoAstar nodoJugador = null;
        NodoAstar nodoObjetivo = null;
        NodoAstar nodoEstacionGasolina;
        NodoAstar nodoEstacionMuniciones;
        // Se recorren todas las posibles filas.
        for (int f = 0; f < GuerraAeronaves.NUM_FILAS; ++f) {
            
            // Por cada iteración, se instancia una fila en la matriz. 
            // Inicialmente no tiene fila alguna.
            
            // Se recorren las columnas de la fila recientemente creada.
            for (int c = 0; c < GuerraAeronaves.NUM_COLUMNAS; ++c) {
                
                // Buscar el elemento que corresponde al par (fila, columna).
                final Elemento e = buscarElementoEnPosicion(elementos, f, c);
                
                NodoAstar nodoActual = new NodoAstar();
                
                nodoActual.setX(c);
                nodoActual.setY(f);
                
                if (e == null) {
                    // En caso de que no haya encontrado un elemento en la posición 
                    // suministrada, deberá meter las características de un "cielo" 
                    // en la matriz.
                    nodoActual.setTransitable(true);
                }
                
                else {
                    // Se almacena una referencia del nodo en donde se encuentra el agente, que 
                    // representa el nodo inicial en el algoritmo de estrella.
                    if (e == avionAzul) {
                        nodoAgente = nodoActual;
                    }
                    
                    // Se almacena una referencia del nodo en donde se encuentra el jugador, que 
                    // representará el nodo objetivo.
                    else if (e == avionRojo) {
                        nodoJugador = nodoActual;
                    }
                    
                    // En caso de haber encontrado un elemento, deberá asignar propiedades 
                    // en función de la naturaleza del elemento. Por ejemplo, los edificios 
                    // no deben ser transitables.
                    List<Class<? extends Elemento>> clasesNoTransitables = 
                            Arrays.asList(Avion.class, Edificio.class, Montana.class);
                    
                    boolean transitable = !clasesNoTransitables.stream().anyMatch(new Predicate<Class<? extends Elemento>>() {
                        @Override
                        public boolean test(Class<? extends Elemento> t) {
                            return t.isInstance(e);
                        }
                    });
                    
                    nodoActual.setTransitable(transitable);
                }
                
                // Por razones de tiempo, se decidió que todos los nodos tengan 
                // el mismo coste.
                //nodoActual.setCoste(1);
                
                matrizAstar[f][c] = nodoActual;
                System.out.print((nodoActual.getTransitable()) ? "1 " : "0 ");
            }
            System.out.println();
        }
        System.out.println();
        
        nodoEstacionGasolina = elementos.stream().filter(new Predicate<Elemento>() {
            @Override
            public boolean test(Elemento e) {
                return e instanceof EstacionGasolinaAzul;
            }
        }).sorted(new Comparator<Elemento>() {
            @Override
            public int compare(Elemento o1, Elemento o2) {
                double distancia1, distancia2;
                
                distancia1 = Math.sqrt(Math.pow(avionAzul.getPosicion().getX() - o1.getPosicion().x, 2) + Math.pow(avionAzul.getPosicion().getY() - o1.getPosicion().y, 2));
                distancia2 = Math.sqrt(Math.pow(avionAzul.getPosicion().getX() - o2.getPosicion().x, 2) + Math.pow(avionAzul.getPosicion().getY() - o2.getPosicion().y, 2));
            
                return (distancia1 < distancia2) ? -1 : (distancia1 > distancia2) ? 1 : 0;
            }
        })
        .map(new Function<Elemento, NodoAstar>() {
            @Override
            public NodoAstar apply(Elemento t) {
                for (int f = 0; f < GuerraAeronaves.NUM_FILAS; ++f) {
                    for (int c = 0; c < GuerraAeronaves.NUM_COLUMNAS; ++c) {
                        if (matrizAstar[f][c].getX() == t.getPosicion().x && matrizAstar[f][c].getY() == t.getPosicion().y) {
                            return matrizAstar[f][c];
                        }
                    }
                }
                return null;
            }
        }).findFirst().orElse(null);
        
        nodoEstacionMuniciones = elementos.stream().filter(new Predicate<Elemento>() {
            @Override
            public boolean test(Elemento e) {
                return e instanceof EstacionGasolinaAzul;
            }
        }).sorted(new Comparator<Elemento>() {
            @Override
            public int compare(Elemento o1, Elemento o2) {
                double distancia1, distancia2;
                
                distancia1 = Math.sqrt(Math.pow(avionAzul.getPosicion().getX() - o1.getPosicion().x, 2) + Math.pow(avionAzul.getPosicion().getY() - o1.getPosicion().y, 2));
                distancia2 = Math.sqrt(Math.pow(avionAzul.getPosicion().getX() - o2.getPosicion().x, 2) + Math.pow(avionAzul.getPosicion().getY() - o2.getPosicion().y, 2));
            
                return (distancia1 < distancia2) ? -1 : (distancia1 > distancia2) ? 1 : 0;
            }
        }).map(new Function<Elemento, NodoAstar>() {
            @Override
            public NodoAstar apply(Elemento t) {
                for (int f = 0; f < GuerraAeronaves.NUM_FILAS; ++f) {
                    for (int c = 0; c < GuerraAeronaves.NUM_COLUMNAS; ++c) {
                        if (matrizAstar[f][c].getX() == t.getPosicion().x && matrizAstar[f][c].getY() == t.getPosicion().y) {
                            return matrizAstar[f][c];
                        }
                    }
                }
                return null;
            }
        }).findFirst().orElse(null);
        
        // Se determina cuál es el objetivo
        int segundosGasolina = (int) (avionAzul.getGasolina() * GuerraAeronaves.TICKS_ACTUALIZACION_AVIONES 
                * GuerraAeronaves.TIEMPO_TICK);
        
        if (segundosGasolina <= 10 && nodoEstacionGasolina != null) {
            nodoObjetivo = nodoEstacionGasolina;
        }
        else if (avionAzul.getMuniciones() == 0 && nodoEstacionMuniciones != null) {
            nodoObjetivo = nodoEstacionMuniciones;
        }
        else if (nodoJugador != null) {
            nodoObjetivo = nodoJugador;
        }
        
        // No se puede buscar un camino si no existe un origen y un destino.
        if (nodoAgente != null && nodoObjetivo != null) {
            // Si el nodo final no es transitable, el cálculo del camino devuelve null.
            nodoObjetivo.setTransitable(true);
            
            // Marcar el bloque que está destrás del jugador como no transitable, ya que 
            // los aviones no se pueden devolver.
            int x = nodoAgente.getX() + ((avionAzul.getDireccion() == Direccion.DERECHA) 
                    ? -1
                    : (avionAzul.getDireccion() == Direccion.IZQUIERDA)
                        ? 1
                        : 0);
            
            int y = nodoAgente.getY() + ((avionAzul.getDireccion() == Direccion.ABAJO) 
                    ? -1
                    : (avionAzul.getDireccion() == Direccion.ARRIBA)
                        ? 1
                        : 0);
            
            for (int f = 0; f < GuerraAeronaves.NUM_FILAS; ++f) {
                for (int c = 0; c < GuerraAeronaves.NUM_COLUMNAS; ++c) {
                    if (matrizAstar[f][c].getX() == x && matrizAstar[f][c].getY() == y) {
                        matrizAstar[f][c].setTransitable(false);
                    }
                }
            }
            
            Astra1 busquedaEstrella = new Astra1(matrizAstar, nodoAgente, nodoObjetivo, false);
            
            List<NodoAstar> camino = busquedaEstrella.calcularCamino();
            
            // Se activará una tecla si se detecta cierta condición.
            boolean 
                      tArriba = false
                    , tAbajo = false
                    , tIzquierda = false
                    , tDerecha = false
                    , tDisparar = false;
            
            if (camino != null && camino.size() >= 2) {
                NodoAstar proximoPaso = camino.get(1);
                
                switch (avionAzul.getDireccion()) {
                    
                    // Si está apuntando hacia arriba o abajo, solamente puede moverse hacia los lados.
                    case ARRIBA: case ABAJO:
                        if (proximoPaso.getX() > nodoAgente.getX()) {
                            tDerecha = true;
                        }
                        if (proximoPaso.getX() < nodoAgente.getX()) {
                            tIzquierda = true;
                        }
                        break;
                        
                    // Si está apuntando hacia un lado, solamente puede moverse hacia arriba y abajo.
                    case DERECHA: case IZQUIERDA:
                        // Recordar que la primera van desde arriba hacia abajo, como en una matriz, 
                        // por lo que moverse a una fila superior implica moverse hacia abajo.
                        if (proximoPaso.getY() > nodoAgente.getY()) {
                            tAbajo = true;
                        }
                        if (proximoPaso.getY() < nodoAgente.getY()) {
                            tArriba = true;
                        }
                        break;
                }
            }          
            
            for (Elemento e : elementos) {
                if ((e instanceof AvionRojo || e instanceof EstacionGasolinaRojo || e instanceof EstacionMunicionesRojo) 
                        && (avionAzul.getPosicion().x == e.getPosicion().x && (avionAzul.getPosicion().y < e.getPosicion().y && avionAzul.getDireccion() == Direccion.ABAJO || avionAzul.getPosicion().y > e.getPosicion().y && avionAzul.getDireccion() == Direccion.ARRIBA)
                            || avionAzul.getPosicion().y == e.getPosicion().y && (avionAzul.getPosicion().x < e.getPosicion().x && avionAzul.getDireccion() == Direccion.DERECHA || avionAzul.getPosicion().x > e.getPosicion().x && avionAzul.getDireccion() == Direccion.IZQUIERDA))) {
                    tDisparar = true;
                }
            }
            teclasPresionadas = new TeclasPresionadas(tArriba, tDerecha, tAbajo, tIzquierda, tDisparar);
        }
    }    

    // Se encarga de plasmar el estado del juego enviado por el ambiente. Crear 
    // y configura todos los elementos del juego que contiene el paquete.
    private void recrearMapa(PaqueteDatos paqueteDatos)
    {
        List<Elemento> nuevosElementos = new ArrayList<Elemento>();
        
        for (DatosElemento de : ((PaqueteDatosAmbiente)paqueteDatos).getElementosVisibles()) {
            Elemento nuevoElemento = de.crearElemento();
            nuevosElementos.add(nuevoElemento);
            posicionarElementoMapa(nuevoElemento);
        }
        
        AvionAzul aa = buscarAvionAzul(nuevosElementos);
        AvionRojo ar = buscarAvionRojo(nuevosElementos);
        
        if (aa != null) {
            avionAzul = aa;
        }
        
        if (ar != null) {
            avionRojo = ar;
        }
        
        stage.getActors().clear();
        
        Image fondo = new Image(new SpriteDrawable(new Sprite(new Texture(
                Gdx.files.internal("cielo1.png")))));        
        fondo.setFillParent(true);
        stage.addActor(fondo);        
        agregarElementos(stage, nuevosElementos);
        
        for (DatosExplosion de : ((PaqueteDatosAmbiente)paqueteDatos).getExplosiones()) {
            crearExplosion(de);
        }

        pseudoInteligencia(nuevosElementos);
    }
    
    @Override
    public void alRecibirPaqueteDatos(PaqueteDatos paqueteDatos) {
        recrearMapa(paqueteDatos);
    }
    
    // Detecta las teclas que presionó el usuario.
    /*private TeclasPresionadas detectarTeclas(int teclaArriba, int teclaDerecha, int teclaAbajo
            , int teclaIzquierda, int teclaDisparar) {
        
        return new TeclasPresionadas(
                  Gdx.input.isKeyPressed(teclaArriba)
                , Gdx.input.isKeyPressed(teclaDerecha)
                , Gdx.input.isKeyPressed(teclaAbajo)
                , Gdx.input.isKeyPressed(teclaIzquierda)
                , Gdx.input.isKeyPressed(teclaDisparar));
    }*/
    
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
    
    public int getGasAvionRojo() {
        return avionRojo.getGasolina();
    }
    
    public float getVidaAvionRojo() {
        return avionRojo.getVida();
    }
    
    public int getMunicionAvionRojo() {
        return avionRojo.getMuniciones();
    }
    
    public int getGasAvionAzul() {
        return avionAzul.getGasolina();
    }
    
    public float getVidaAvionAzul() {
        return avionAzul.getVida();
    }
    
    public int getMunicionAvionAzul() {
        return avionAzul.getMuniciones();
    }
    
    // Retorna el primer avión azul que consiga en un arreglo de elementos. 
    // Si no encuentra ninguno, devuelve null.
    private AvionAzul buscarAvionAzul(List<Elemento> es) {
        return (AvionAzul)es.stream().filter(new Predicate<Elemento>() {
            @Override
            public boolean test(Elemento e) {
                return e instanceof AvionAzul;
            }
        })
        .findFirst().orElseGet(new Supplier<Elemento>() {
            @Override
            public Elemento get() {
                return null;
            }
        });
    }

    // Retorna el primer avión rojo que consiga en un arreglo de elementos. 
    // Si no encuentra ninguno, devuelve null.    
    private AvionRojo buscarAvionRojo(List<Elemento> es) {
        return (AvionRojo)es.stream()
                .filter(new Predicate<Elemento>() {
                    @Override
                    public boolean test(Elemento e) {
                        return e instanceof AvionRojo;
                    }
                })
                .findFirst().orElseGet(new Supplier<Elemento>() {
                    @Override
                    public Elemento get() {
                        return null;
                    }
                });       
    }    
}