package guerra.aeronaves.comunicacion;

import java.io.Serializable;

public class TeclasPresionadas implements Serializable {
    
    private final boolean
              presionadaTeclaArriba
            , presionadaTeclaDerecha
            , presionadaTeclaAbajo
            , presionadaTeclaIzquierda
            , presionadaTeclaDisparar;
    
    public TeclasPresionadas(boolean tArr, boolean tDer, boolean tAba, boolean tIzq, boolean tDis) {
        presionadaTeclaArriba = tArr;
        presionadaTeclaDerecha = tDer;
        presionadaTeclaAbajo = tAba;
        presionadaTeclaIzquierda = tIzq;
        presionadaTeclaDisparar = tDis;
    }

    public boolean isPresionadaTeclaArriba() {
        return presionadaTeclaArriba;
    }

    public boolean isPresionadaTeclaDerecha() {
        return presionadaTeclaDerecha;
    }

    public boolean isPresionadaTeclaAbajo() {
        return presionadaTeclaAbajo;
    }

    public boolean isPresionadaTeclaIzquierda() {
        return presionadaTeclaIzquierda;
    }

    public boolean isPresionadaTeclaDisparar() {
        return presionadaTeclaDisparar;
    }
    
}
