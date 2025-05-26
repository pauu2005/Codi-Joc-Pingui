package modelo;

import java.util.ArrayList;

public class Trineo extends Casilla {
    private ArrayList<Integer> posicionesTrineos;

    public Trineo(int posicion, ArrayList<Jugador> jugadoresActuales, ArrayList<Integer> posicionesTrineos) {
        super(posicion, jugadoresActuales);
        this.posicionesTrineos = posicionesTrineos;
    }

    @Override
    public void realizarAccion() {
        int idx = posicionesTrineos.indexOf(this.posicion);
        if (idx < posicionesTrineos.size() - 1) {
            int posSiguiente = posicionesTrineos.get(idx + 1);
            for (Jugador j : jugadoresActuales) {
                j.setPosicion(posSiguiente);
                // Aquí podrías llamar a la acción de la casilla destino si quieres
            }
        }
        // Si es el último trineo, no hace nada
    }
}