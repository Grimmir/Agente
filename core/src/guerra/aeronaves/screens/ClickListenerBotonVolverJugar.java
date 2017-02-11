package guerra.aeronaves.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import guerra.aeronaves.GuerraAeronaves;

public class ClickListenerBotonVolverJugar extends ClickListenerBoton {
    
    public ClickListenerBotonVolverJugar(GuerraAeronaves guerraAeronaves) {
        super(guerraAeronaves);
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        // TO-DO: tarea eliminar clases innecesarias.
        //guerraAeronaves.setScreenJuego();
    }
    
}
