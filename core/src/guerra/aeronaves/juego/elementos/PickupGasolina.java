package guerra.aeronaves.juego.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.GuerraAeronaves;
import guerra.aeronaves.comunicacion.elementos.DatosElemento;
import java.awt.Point;

public class PickupGasolina extends Elemento {
    
    public PickupGasolina(Point posicion) {
        super("gasolina.png", GuerraAeronaves.ID_PICKUP_GASOLINA, posicion, Direccion.ARRIBA, GuerraAeronaves.VIDA_INFINITA);
    }

    @Override
    public DatosElemento crearSerializable() {
        return new guerra.aeronaves.comunicacion.elementos.PickupGasolina(posicion);
    }
    
}