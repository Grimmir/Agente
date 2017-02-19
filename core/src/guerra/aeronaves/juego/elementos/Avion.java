package guerra.aeronaves.juego.elementos;

import guerra.aeronaves.Direccion;
import guerra.aeronaves.GuerraAeronaves;
import java.awt.Point;

public abstract class Avion extends Elemento {
    protected int municiones;
    protected int gasolina;
    
    public Avion(String rutaSprite, Point posicion, Direccion direccion) {
        super(rutaSprite, GuerraAeronaves.ID_AVION_AZUL, posicion, direccion, GuerraAeronaves.VIDA_AVION);
        this.direccion = direccion;
        proximaDireccion = this.direccion;
        municiones = GuerraAeronaves.MUNICIONES_AVION;
        gasolina = GuerraAeronaves.GASOLINA_AVION;
    }

    public int getMuniciones() {
        return municiones;
    }

    public void setMuniciones(int municiones) {
        this.municiones = municiones;
    }

    public int getGasolina() {
        return gasolina;
    }

    public void setGasolina(int gasolina) {
        this.gasolina = gasolina;
    }
    
}
