package guerra.aeronaves.comunicacion.elementos;

import guerra.aeronaves.GuerraAeronaves;
import guerra.aeronaves.juego.elementos.Elemento;
import java.awt.Point;

public class EstacionGasolinaAzul extends EstacionGasolina {

    public EstacionGasolinaAzul() {
        
    }
    
    public EstacionGasolinaAzul(Point posInicial) {
        super(GuerraAeronaves.ID_ESTACION_GASOLINA_AZUL, posInicial);
    }

    @Override
    public Elemento crearElemento() {
        guerra.aeronaves.juego.elementos.EstacionGasolinaAzul ega = 
                new guerra.aeronaves.juego.elementos.EstacionGasolinaAzul(posicion);
        
        ega.setVida(vida);
        
        return ega;
    }
    
}