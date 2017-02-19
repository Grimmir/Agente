package guerra.aeronaves.comunicacion;

import guerra.aeronaves.comunicacion.elementos.DatosElemento;
import guerra.aeronaves.juego.elementos.Elemento;
import java.util.List;
import java.util.function.Function;
import static java.util.stream.Collectors.toList;

public class PaqueteDatosAmbiente extends PaqueteDatos {
    
    private List<DatosElemento> elementosVisibles;
    private List<DatosExplosion> explosiones;
    
    public PaqueteDatosAmbiente() {
        
    }
    
    public PaqueteDatosAmbiente(List<Elemento> elementosVisibles, List<DatosExplosion> explosiones) {
        this.elementosVisibles = prepararParaSerializacion(elementosVisibles);
        this.explosiones = explosiones;
    }
    
    public List<DatosElemento> getElementosVisibles() {
        return elementosVisibles;
    }

    public List<DatosExplosion> getExplosiones() {
        return explosiones;
    } 

    public static List<DatosElemento> prepararParaSerializacion(List<Elemento> elementosVisibles) {
        return (List<DatosElemento>)elementosVisibles.stream().map(new Function() {
            @Override
            public Object apply(Object t) {
                return ((Elemento)t).crearSerializable();
            }
        }).collect(toList());
    }
    
}
