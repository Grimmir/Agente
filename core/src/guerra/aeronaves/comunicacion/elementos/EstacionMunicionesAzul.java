package guerra.aeronaves.comunicacion.elementos;

import guerra.aeronaves.GuerraAeronaves;
import guerra.aeronaves.juego.elementos.Elemento;
import java.awt.Point;

public class EstacionMunicionesAzul extends EstacionMuniciones {   

    public EstacionMunicionesAzul() {
    
    }
    
    public EstacionMunicionesAzul(Point posInicial) {
        super(GuerraAeronaves.ID_ESTACION_MUNICIONES_AZUL, posInicial);
    }

    @Override
    public Elemento crearElemento() {
        guerra.aeronaves.juego.elementos.EstacionMunicionesAzul ema = 
                new guerra.aeronaves.juego.elementos.EstacionMunicionesAzul(posicion);
        
        ema.setVida(vida);
        
        return ema;
    }
    
}