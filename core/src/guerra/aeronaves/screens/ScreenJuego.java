package guerra.aeronaves.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import guerra.aeronaves.GuerraAeronaves;
import guerra.aeronaves.comunicacion.Conexion;
import guerra.aeronaves.juego.Juego;

public class ScreenJuego extends ScreenAdapter {
    
    private final Stage stage;
    private final Juego juego;

    ScreenJuego(Conexion conexion) {
        stage = new Stage(new FitViewport(GuerraAeronaves.calcularTamañoCasilla(Gdx
                .graphics.getWidth(), Gdx.graphics.getHeight()) * GuerraAeronaves.NUM_COLUMNAS
                , GuerraAeronaves.calcularTamañoCasilla(Gdx.graphics.getWidth()
                        , Gdx.graphics.getHeight()) * GuerraAeronaves.NUM_FILAS));
        
        juego = new Juego(stage, conexion);
        juego.iniciar();
        
        conexion.getCliente().solicitarDatosAmbiente();
        Gdx.input.setInputProcessor(stage);
    }
    
    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act(delta);
        stage.draw();
    }
    
    @Override
    public void resume() {
        super.resume();
        juego.iniciar();
    }    
    
    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
    }
    
}
