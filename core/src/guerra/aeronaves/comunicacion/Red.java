package guerra.aeronaves.comunicacion;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import guerra.aeronaves.Direccion;
import guerra.aeronaves.comunicacion.elementos.AvionAzul;
import guerra.aeronaves.comunicacion.elementos.AvionRojo;
import guerra.aeronaves.comunicacion.elementos.Edificio;
import guerra.aeronaves.comunicacion.elementos.EstacionGasolinaAzul;
import guerra.aeronaves.comunicacion.elementos.EstacionGasolinaRojo;
import guerra.aeronaves.comunicacion.elementos.EstacionMunicionesAzul;
import guerra.aeronaves.comunicacion.elementos.EstacionMunicionesRojo;
import guerra.aeronaves.comunicacion.elementos.Explosion;
import guerra.aeronaves.comunicacion.elementos.Montana;
import guerra.aeronaves.comunicacion.elementos.Nube;
import guerra.aeronaves.comunicacion.elementos.PickupGasolina;
import guerra.aeronaves.comunicacion.elementos.PickupMuniciones;
import guerra.aeronaves.comunicacion.elementos.PickupVida;
import guerra.aeronaves.comunicacion.elementos.PowerupMuniciones;
import guerra.aeronaves.comunicacion.elementos.PowerupVida;
import guerra.aeronaves.comunicacion.elementos.Proyectil;
import java.awt.Point;
import java.util.ArrayList;

public class Red {
    
    static public void registrar (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        
        kryo.register(ArrayList.class);
        kryo.register(Point.class);
        
        kryo.register(Direccion.class);
        kryo.register(TeclasPresionadas.class);
        
        kryo.register(AvionAzul.class);
        kryo.register(AvionRojo.class);
        kryo.register(Edificio.class);
        kryo.register(EstacionGasolinaAzul.class);
        kryo.register(EstacionGasolinaRojo.class);
        kryo.register(EstacionMunicionesAzul.class);
        kryo.register(EstacionMunicionesRojo.class);
        kryo.register(Explosion.class);
        kryo.register(Montana.class);
        kryo.register(Nube.class);
        kryo.register(PickupGasolina.class);
        kryo.register(PickupMuniciones.class);
        kryo.register(PickupVida.class);
        kryo.register(PowerupMuniciones.class);
        kryo.register(PowerupVida.class);
        kryo.register(Proyectil.class);
        
        kryo.register(PaqueteDatosAgente.class);
        kryo.register(PaqueteDatosAmbiente.class);
    }
    
}
