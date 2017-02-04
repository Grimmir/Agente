package guerra.aeronaves.juego.elementos;

import com.badlogic.gdx.math.Vector2;
import guerra.aeronaves.GuerraAeronaves;

public class Montana extends Elemento {
   
    public Montana(Vector2 posInicial) {
        super("montana.png", GuerraAeronaves.ID_MONTANA, posInicial, GuerraAeronaves.VIDA_INFINITA);
    }
    
    @Override
    public final void colocarEnPosicionInicial() {
        super.colocarEnPosicionInicial();
    } 
    
}
