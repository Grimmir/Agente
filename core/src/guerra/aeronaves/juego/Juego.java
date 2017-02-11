package guerra.aeronaves.juego;

import guerra.aeronaves.juego.elementos.Elemento;
import guerra.aeronaves.juego.elementos.AvionRojo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import guerra.aeronaves.ConexionTeclas;
import guerra.aeronaves.Direccion;
import guerra.aeronaves.Ganador;
import guerra.aeronaves.GuerraAeronaves;
import guerra.aeronaves.juego.elementos.Avion;
import guerra.aeronaves.juego.elementos.AvionAzul;
import guerra.aeronaves.juego.elementos.Edificio;
import guerra.aeronaves.juego.elementos.EstacionGasolina;
import guerra.aeronaves.juego.elementos.EstacionGasolinaAzul;
import guerra.aeronaves.juego.elementos.EstacionGasolinaRojo;
import guerra.aeronaves.juego.elementos.EstacionMuniciones;
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
import guerra.aeronaves.juego.elementos.Proyectil;
import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import static java.util.stream.Collectors.toList;

public class Juego {
    
    private final Stage stage;
    private final List<Elemento> elementos;
    private final List<Elemento> elementosAQuitar;
    private final List<Vector2> centrosCasillas;
    
    private final AvionAzul avionAzul;
    private final AvionRojo avionRojo;
    
    private final Sound sonidoExplosion;
    private JuegoListener juegoListener;
    
    private final int[][] matrizMapa;
    
    private final Timer timer;
    private long ticks;
    
    private final ConexionTeclas conexionTeclas;
    
    public Juego(Stage stage, int matrizMapa[][], ConexionTeclas ct) {
        this.stage = stage;
        this.matrizMapa = matrizMapa;
        Image fondo = new Image(new SpriteDrawable(new Sprite(new Texture(
                Gdx.files.internal("cielo1.png")))));
        sonidoExplosion = Gdx.audio.newSound(Gdx.files.internal("sonidos/snd_explosion.wav"));
        
        fondo.setFillParent(true);
        stage.addActor(fondo);
        
        centrosCasillas = obtenerCentrosCasillas();
        elementos = crearElementosMapa();
        elementosAQuitar = new ArrayList<Elemento>();
        
        avionAzul = buscarAvionAzul(elementos);
        avionRojo = buscarAvionRojo(elementos);
        
        agregarElementos(stage, elementos);
        stage.addActor(fondo);
        
        timer = new Timer();
        ticks = 0;
        
        conexionTeclas = ct;
    }
    
    // Inicia el reloj del juego.
    public void iniciar() {
        timer.scheduleTask(new Task() {
            @Override
            public void run() {
                ticks = (ticks == Long.MAX_VALUE) ? 0 : ticks + 1;
                
                if (ticks % GuerraAeronaves.TICKS_DETECCION_TECLAS == 0) {
                    //detectarTeclasViejo(avionAzul, Keys.W, Keys.S, Keys.A, Keys.D, Keys.SPACE);
                    TeclasPresionadas tpAvionAzul = detectarTeclas(
                              Keys.W
                            , Keys.D
                            , Keys.S
                            , Keys.A
                            , Keys.SPACE);
                    //procesarTeclasPresionadas(avionAzul, tpAvionAzul);
                    
                    if (!conexionTeclas.isConexionActiva()) {
                        conexionTeclas.cerrarConexion();
                        conexionTeclas.iniciarConexion();
                    }
                    else {
                        conexionTeclas.enviarMensajeTeclas(tpAvionAzul);                        
                    }
                }
                
                /*if (ticks % GuerraAeronaves.TICKS_ACTUALIZACION_PROYECTILES == 0) {
                    actualizarProyectiles(buscarProyectiles(elementos));
                }
                
                if (ticks % GuerraAeronaves.TICKS_ACTUALIZACION_AVIONES == 0) {
                    actualizarAviones(buscarAviones(elementos));
                }
                
                if (ticks % GuerraAeronaves.TICKS_ACTUALIZACION_NUBES == 0) {
                    actualizarNubes(buscarNubes(elementos));
                }
                
                if (ticks % GuerraAeronaves.TICKS_DETECCION_COLISIONES == 0) {
                    detectarColisiones(elementos);
                    // Elimina todos los elementos que fueron destruidos
                    procesarElementosAQuitar();
                }*/
            }
        }, GuerraAeronaves.TIEMPO_TICK, GuerraAeronaves.TIEMPO_TICK);
    }
    

    // Realiza las tareas antes de terminar el juego y dispara el evento 
    // del listener correspondiente.
    private void terminar(final Avion a) {
        timer.stop();
        
        new Timer().scheduleTask(new Task() {
            @Override
            public void run() {
                if (juegoListener != null) {
                    if (a instanceof AvionAzul) {
                        juegoListener.alTerminar(Ganador.ROJO);
                    }
                    else {
                        juegoListener.alTerminar(Ganador.AZUL);
                    }
                }                
            }
        }, GuerraAeronaves.TIEMPO_FINALIZACION, 0, 0);
    }    
   
    // Método que convierte los ID de la matriz de elementos leidos del 
    // archivo de texto en objetos de tipo Elemento
    private ArrayList<Elemento> crearElementosMapa() {
        ArrayList<Elemento> elementosMapa = new ArrayList<Elemento>();
        
        // Recorremos cada elemento de la matriz
        for (int i = 0; i < matrizMapa.length; ++i) {
            for (int j = 0; j < matrizMapa[0].length; ++j) {
                Elemento e = crearElemento(matrizMapa[i][j], new Point(j, i));
                               
                if (e != null) {
                    Vector2 posicionMapa = calcularPosicionMapa(matrizMapa, centrosCasillas, j, i);
                    e.setPosition(posicionMapa.x, posicionMapa.y);
                    if (e instanceof Avion) {
                        Avion a = (Avion)e;
                        moverElemento(a, GuerraAeronaves.TICKS_ACTUALIZACION_AVIONES);
                    }
                    
                    elementosMapa.add(e);                  
                }
            }
        }
        return elementosMapa;
    }

    // Método que recibe el ID de un tipo de elemento y una posición 
    // inicial y construye un elemento en base a ello.
    private Elemento crearElemento(int idElemento, Point posicion) {        
        switch (idElemento) {            
            case GuerraAeronaves.ID_AVION_AZUL:
                return new AvionAzul(posicion, Direccion.DERECHA);
                
            case GuerraAeronaves.ID_AVION_ROJO:
                return new AvionRojo(posicion, Direccion.IZQUIERDA); 
                
            case GuerraAeronaves.ID_EDIFICIO:
                return new Edificio(posicion);
                
            case GuerraAeronaves.ID_ESTACION_GASOLINA_AZUL:
                return new EstacionGasolinaAzul(posicion);
                
            case GuerraAeronaves.ID_ESTACION_GASOLINA_ROJO:
                return new EstacionGasolinaRojo(posicion);
                
            case GuerraAeronaves.ID_ESTACION_MUNICIONES_AZUL:
                return new EstacionMunicionesAzul(posicion);
                
            case GuerraAeronaves.ID_ESTACION_MUNICIONES_ROJO:
                return new EstacionMunicionesRojo(posicion);
                
            case GuerraAeronaves.ID_MONTANA:
                return new Montana(posicion);
                
            case GuerraAeronaves.ID_NUBE:
                return new Nube(posicion, Direccion.DERECHA);
                
            case GuerraAeronaves.ID_PICKUP_GASOLINA:
                return new PickupGasolina(posicion);
                
            case GuerraAeronaves.ID_PICKUP_MUNICIONES:
                return new PickupMuniciones(posicion);
                
            case GuerraAeronaves.ID_PICKUP_VIDA:
                return new PickupVida(posicion);
                
            case GuerraAeronaves.ID_POWERUP_MUNICIONES:
                return new PowerupMuniciones(posicion);
                
            case GuerraAeronaves.ID_POWERUP_VIDA:
                return new PowerupVida(posicion);  
        }
        
        return null;
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
    
    // Aplica diferentes acciones al avión en función de las teclas que 
    // fueron presionadas.
    private void procesarTeclasPresionadas(Avion a, TeclasPresionadas tp) {
        if (a != null) {
            if(tp.isPresionadaTeclaArriba()) {
                actualizarDireccionAvion(a, Direccion.ARRIBA);
            }
            
            else if(tp.isPresionadaTeclaAbajo()) {
                actualizarDireccionAvion(a, Direccion.ABAJO);
            }
            
            else if(tp.isPresionadaTeclaIzquierda()) {
                actualizarDireccionAvion(a, Direccion.IZQUIERDA);
            }
            
            else if(tp.isPresionadaTeclaDerecha()) {
                actualizarDireccionAvion(a, Direccion.DERECHA);
            }
            
            if (tp.isPresionadaTeclaDisparar()) {
                if (a.getMuniciones() > 0) {
                    Proyectil p = new Proyectil(a.getDireccion(), a.getProximaPosicion(), a);
                    a.setMuniciones(a.getMuniciones() - 1);
                    Vector2 posicionEnMapa = calcularPosicionMapa(matrizMapa, centrosCasillas
                            , p.getPosicion().x, p.getPosicion().y);
                    p.setPosition(posicionEnMapa.x, posicionEnMapa.y);
                    moverElemento(p, GuerraAeronaves.TICKS_ACTUALIZACION_PROYECTILES);
                    elementos.add(p);
                    stage.addActor(p);
                }               
            }
        }        
    }    
    
    // Remplaza el sprite del elemento por sprites de explosión durante un 
    // tiempo en segundos.
    private void crearExplosion(Point posicion, float tiempo) {
        final float tiempoSprite = tiempo / 6;
        final ArrayDeque<String> rutaExplosiones = new ArrayDeque<String>(
                GuerraAeronaves.RUTA_EXPLOSIONES);
        Vector2 posicionEnMapa = calcularPosicionMapa(matrizMapa
                , centrosCasillas, posicion.x, posicion.y);
        final Explosion explosion = new Explosion(rutaExplosiones.pop(), posicion);
        
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

    public void setJuegoListener(JuegoListener jl) {
        juegoListener = jl;
    }

    // Recorre cada elemento, buscando si ha colisionado con otro elemento o una 
    // pared.
    private void detectarColisiones(List<Elemento> elementos) {
        for (Elemento e : elementos) {
            if (e.getVida() > 0) {
                if (colisionoConPared(e)) {
                    alColisionarConPared(e);
                }
                else {
                    Elemento elementoColisionado = buscarElementoColisionado(e);
                    
                    if (elementoColisionado != null && elementoColisionado.getVida() > 0) {
                        alColisionarElementos(e, elementoColisionado);
                    }
                }
            }
        }
    }

    private boolean colisionoConPared(Elemento e) {
        return (e instanceof Avion || e instanceof Nube || e instanceof Proyectil) 
                && ((e.getPosicion().x <= 0 && e.getDireccion() == Direccion.IZQUIERDA)
                || e.getPosicion().x >= matrizMapa[0].length - 1 && e.getDireccion() == Direccion.DERECHA
                || e.getPosicion().y <= 0 && e.getDireccion() == Direccion.ARRIBA
                || e.getPosicion().y >= matrizMapa.length - 1 && e.getDireccion() == Direccion.ABAJO)
                || e.getPosicion().x < 0 || e.getPosicion().x > matrizMapa[0].length - 1
                || e.getPosicion().y < 0 || e.getPosicion().y > matrizMapa.length - 1;
    }    
    
    // Llamado cuando un elemento choca con una pared.
    private void alColisionarConPared(Elemento e) {
        if (e instanceof Nube) {

        }
        else {
            intentarDestruir(e, e.getVida());
        }
    }

    // Llamado cuando un elemento choca con otro elemento.
    private void alColisionarElementos(Elemento e1, Elemento e2) {
        // Cuando un proyectil choca con un elemento o cuando dos elementos 
        // sólidos chocan.
        if (e1 instanceof Proyectil && recibeDañoProyectil(e2) 
                || recibeDañoProyectil(e1) && e2 instanceof Proyectil
                || esElementoSolido(e1) && esElementoSolido(e2)) {
            float daño = Math.min(e1.getVida(), e2.getVida());
            intentarDestruir(e1, daño);
            intentarDestruir(e2, daño);
        }
        
        else if (e1 instanceof Avion) {
            colisionAvionElementoNoSolido((Avion)e1, e2);
        }
        else if (e2 instanceof Avion) {
            colisionAvionElementoNoSolido((Avion)e2, e1);
        }
    }

    // Determina si un elemento puede recibir daño de un proyectil.
    private boolean recibeDañoProyectil(Elemento e) {
        return e instanceof Avion || e instanceof Edificio 
                || e instanceof EstacionGasolina || e instanceof EstacionMuniciones 
                || e instanceof Montana || e instanceof Proyectil;
    }
    
    // Quita vida al elemento y crea una explosión si la vida llega a cero.
    private void intentarDestruir(Elemento e, float daño) {
        e.setVida(e.getVida() - daño);
        if (e.getVida() <= 0) {
            crearExplosion(e.getPosicion(), GuerraAeronaves.TIEMPO_EXPLOSION);
            
            // Si es un avión, significa que uno de los jugadores perdió.
            if (e instanceof Avion) {
                terminar((Avion)e);  
            }
            elementosAQuitar.add(e);
        }
    }
    
    // Determina si un elemento es destruido al colisionar con otro elemento.
    private boolean esElementoSolido(Elemento e) {
        return e instanceof Avion || e instanceof Edificio || e instanceof Montana 
                || e instanceof Proyectil;
    }

    private void procesarElementosAQuitar() {
        elementos.removeAll(elementosAQuitar);
        stage.getActors().removeAll(new Array(elementosAQuitar.toArray()), true);
        elementosAQuitar.clear();
    }

    // Llamado cada vez que un avión entra en contacto con un elemento que el 
    // avión pueda traspasar.
    private void colisionAvionElementoNoSolido(Avion a, Elemento e) {
        if (e instanceof PickupGasolina) {
            a.setGasolina(a.getGasolina() + GuerraAeronaves.CANTIDAD_PICKUP_GASOLINA);
            elementosAQuitar.add(e);
        }
        else if (e instanceof PickupMuniciones) {
            a.setMuniciones(a.getMuniciones() + GuerraAeronaves.CANTIDAD_PICKUP_MUNICIONES);
            elementosAQuitar.add(e);
        }
        else if (e instanceof PickupVida) {
            a.setVida(a.getVida() + GuerraAeronaves.CANTIDAD_PICKUP_VIDA);
            elementosAQuitar.add(e);
        }
        else if (e instanceof PowerupMuniciones) {
            elementosAQuitar.add(e);
        }
        else if (e instanceof PowerupVida) {
            elementosAQuitar.add(e);
        }
        else if (a instanceof AvionAzul) {
            colisionAvionElementoNoSolido((AvionAzul)a, e);
        }
        else {
            colisionAvionElementoNoSolido((AvionRojo)a, e);
        }
    }
    
    // Verifica si hubo colisión con alguna de las estaciones del avión azul.
    private void colisionAvionElementoNoSolido(AvionAzul a, Elemento e) { 
        if (e instanceof EstacionGasolinaAzul) {
            a.setGasolina(a.getGasolina() + GuerraAeronaves.GASOLINA_AVION);
        }
        else if (e instanceof EstacionMunicionesAzul) {
            a.setMuniciones(a.getMuniciones() + GuerraAeronaves.MUNICIONES_AVION);
        }
    }    
    
    // Verifica si hubo colisión con alguna de las estaciones del avión rojo.
    private void colisionAvionElementoNoSolido(AvionRojo a, Elemento e) { 
        if (e instanceof EstacionGasolinaRojo) {
            a.setGasolina(a.getGasolina() + GuerraAeronaves.GASOLINA_AVION);
        }
        else if (e instanceof EstacionMunicionesRojo) {
            a.setMuniciones(a.getMuniciones() + GuerraAeronaves.MUNICIONES_AVION);
        }        
    }
    
    private void actualizarAviones(List<Avion> aviones) {
        for (Avion a : aviones) {
            if (a.getVida() > 0) {
                if (a.getGasolina() <= 0) {
                    intentarDestruir(a, a.getVida());
                }
                else {
                    a.setGasolina(a.getGasolina() - 1);
                    actualizarPosicionAvion(a);                 
                }
            }
        }
    }
    
    // Verifica si la dirección es válida. En caso de serlo, es fijada como
    // próxima dirección del avión.
    private void actualizarDireccionAvion(Avion a, Direccion d) {
        if(d == Direccion.ARRIBA && a.getDireccion() != Direccion.ABAJO 
                || d == Direccion.DERECHA && a.getDireccion() != Direccion.IZQUIERDA
                || d == Direccion.ABAJO && a.getDireccion() != Direccion.ARRIBA 
                || d == Direccion.IZQUIERDA && a.getDireccion() != Direccion.DERECHA) {
            a.setProximaDireccion(d);
        }        
    }
    
    private void actualizarPosicionAvion(Avion a) {
        a.setPosicion(a.getProximaPosicion());
        Vector2 posicionActualMapa = calcularPosicionMapa(matrizMapa, centrosCasillas, a.getPosicion().x, a.getPosicion().y);
        a.setPosition(posicionActualMapa.x, posicionActualMapa.y);
        a.setDireccion(a.getProximaDireccion());
        moverElemento(a, GuerraAeronaves.TICKS_ACTUALIZACION_AVIONES);        
    }      

    private void actualizarProyectiles(List<Proyectil> proyectiles) {
        for (Elemento p : proyectiles) {
            if (p instanceof Proyectil && p.getVida() > 0) {
                actualizarPosicionProyectiles((Proyectil)p);
            }
        }
    }

    private void actualizarPosicionProyectiles(Proyectil p) {
        p.setPosicion(p.getProximaPosicion());
        moverElemento(p, GuerraAeronaves.TICKS_ACTUALIZACION_PROYECTILES);
    }
    
    private void actualizarNubes(List<Nube> nubes) {
        // TO-DO: movimiento de nubes
    }
    
    private void moverElemento(Elemento e, int ticksVelocidad) {
        Vector2 proximaPosicionMapa = calcularPosicionMapa(matrizMapa, centrosCasillas
                , e.getProximaPosicion().x, e.getProximaPosicion().y);
        e.addAction(Actions.moveTo(proximaPosicionMapa.x, proximaPosicionMapa.y
                , ticksVelocidad * GuerraAeronaves.TIEMPO_TICK));        
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
    
    private List<Avion> buscarAviones(List<Elemento> es) {
        return (List<Avion>)es.stream().filter(new Predicate() {
                @Override
                public boolean test(Object t) {
                    return t instanceof Avion;
                }
            }).collect(toList());        
    }
    
    private List<Proyectil> buscarProyectiles(List<Elemento> es) {
        return (List<Proyectil>)es.stream().filter(new Predicate() {
                @Override
                public boolean test(Object t) {
                    return t instanceof Proyectil;
                }
            }).collect(toList());
    }
    
    private List<Nube> buscarNubes(List<Elemento> es) {
        return (List<Nube>)es.stream().filter(new Predicate() {
                @Override
                public boolean test(Object t) {
                    return t instanceof Nube;
                }
            }).collect(toList());
    }
    
    // Determinar si el elemento colisión con otro elemento o con el borde del 
    // mapa
    private Elemento buscarElementoColisionado(Elemento e) {
        for (Elemento ec : elementos) {
            if (e instanceof Proyectil && ec instanceof Proyectil) {                
                if (((Proyectil)e).getElementoCreador() != ((Proyectil)ec).getElementoCreador()) {
                    return ec;
                }
                else {
                    return null;
                }
            }
            else if (e != ec && e.getPosicion().x == ec.getPosicion().x 
                    && e.getPosicion().y == ec.getPosicion().y) {
                return ec;
            }
        }
        return null;
    }
    
    private Vector2 calcularPosicionMapa(int[][] matrizMapa
            , List<Vector2> centrosCasillas, int columna, int fila) {
        
        columna = Math.max(0, columna);
        columna = Math.min(matrizMapa[0].length - 1, columna);
        
        fila = Math.max(0, fila);
        fila = Math.min(matrizMapa.length - 1, fila);
        
        int idxCentro = (matrizMapa.length - 1 - fila) * matrizMapa[0].length + columna;
        return centrosCasillas.get(idxCentro);
    }
    
}
