package guerra.aeronaves.juego;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import guerra.aeronaves.GuerraAeronaves;

public class HUD {
    private Stage estado;
    private Label lVidaRojo, lVidaAzul, lGasRojo, lGasAzul, lMunRojo, lMunAzul;
    private FitViewport vp;
    
    public HUD(SpriteBatch sb, Juego j) {
        vp = new FitViewport(GuerraAeronaves.NUM_COLUMNAS*GuerraAeronaves.TAMANO_CASILLA,
                GuerraAeronaves.NUM_FILAS*GuerraAeronaves.TAMANO_CASILLA,new OrthographicCamera());
        estado = new Stage(vp, sb);
        
        Table tabla = new Table();
        tabla.top();
        tabla.setFillParent(true);
        
        lVidaRojo = new Label(String.format("Vida: %2d", Math.round(GuerraAeronaves.VIDA_AVION)), new Label.LabelStyle(new BitmapFont(), Color.RED));
        lGasRojo = new Label(String.format("Gasolina: %3d", GuerraAeronaves.GASOLINA_AVION), new Label.LabelStyle(new BitmapFont(), Color.RED));
        lMunRojo = new Label(String.format("Municiones: %3d", GuerraAeronaves.MUNICIONES_AVION), new Label.LabelStyle(new BitmapFont(), Color.RED));
        
        lVidaAzul = new Label(String.format("Vida: %2d", Math.round(GuerraAeronaves.VIDA_AVION)), new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lGasAzul = new Label(String.format("Gasolina: %3d", GuerraAeronaves.GASOLINA_AVION), new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lMunAzul = new Label(String.format("Municiones: %3d", GuerraAeronaves.MUNICIONES_AVION), new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        
        tabla.add(lVidaRojo).expandX().padTop(10);
        tabla.add(lVidaAzul).expandX().padTop(10);
        tabla.row();
        tabla.add(lMunRojo).expandX();
        tabla.add(lMunAzul).expandX();
        tabla.row();
        tabla.add(lGasRojo).expandX();
        tabla.add(lGasAzul).expandX();
        
        estado.addActor(tabla);
    }
    
    public void updateRojo(int vd, int gas, int mn) {
        lGasRojo.setText(String.format("Gasolina: %3d", gas));
        lVidaRojo.setText(String.format("Vida: %2d", vd));
        lMunRojo.setText(String.format("Municiones: %3d", mn));
    }
    
    public void updateAzul(int vd, int gas, int mn) {
        lGasAzul.setText(String.format("Gasolina: %3d", gas));
        lVidaAzul.setText(String.format("Vida: %2d", vd));
        lMunAzul.setText(String.format("Municiones: %3d", mn));
    }
    
    public Stage getEstado() {
        return estado;
    }
}
