package guerra.aeronaves.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import guerra.aeronaves.GuerraAeronaves;
import guerra.aeronaves.juego.HUD;
import guerra.aeronaves.juego.Juego;

public class ScreenJuego extends ScreenAdapter {
    
    private final Stage stage;
    private final Juego juego;
    private final GuerraAeronaves guerraAeronaves;
    private final HUD h;

    public ScreenJuego(GuerraAeronaves guerraAeronaves) {
        stage = new Stage(new FitViewport(GuerraAeronaves.calcularTamañoCasilla(Gdx
                .graphics.getWidth(), Gdx.graphics.getHeight()) * GuerraAeronaves.NUM_COLUMNAS
                , GuerraAeronaves.calcularTamañoCasilla(Gdx.graphics.getWidth()
                        , Gdx.graphics.getHeight()) * GuerraAeronaves.NUM_FILAS));
        
        this.guerraAeronaves = guerraAeronaves;
        
        juego = new Juego(stage, guerraAeronaves);
        juego.iniciar();
        
        h = new HUD(guerraAeronaves.batch, juego);
        
        Gdx.input.setInputProcessor(stage);
    }
    
    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act(delta);
        stage.draw();
        
        guerraAeronaves.batch.setProjectionMatrix(h.getEstado().getCamera().combined);
        
        h.updateRojo(Math.round(juego.getVidaAvionRojo()),juego.getGasAvionRojo(), juego.getMunicionAvionRojo());
        //Quitar este comentario para habilitar el HUD del avión Azul
        h.updateAzul(Math.round(juego.getVidaAvionAzul()),juego.getGasAvionAzul(), juego.getMunicionAvionAzul());
        
        h.getEstado().draw();        
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
