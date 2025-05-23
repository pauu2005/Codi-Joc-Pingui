package modelo;

import java.util.ArrayList;

public class Agujero extends Casilla {
    private ArrayList<Integer> posicionesHoyos;

    public Agujero(int posicion, ArrayList<Jugador> jugadoresActuales, ArrayList<Integer> posicionesHoyos) {
        super(posicion, jugadoresActuales);
        this.posicionesHoyos = posicionesHoyos;
    }

    @Override
    public void realizarAccion(Jugador jugador) {
        jugador.setPosicion(0); // Vuelve a la casilla de salida
    }

	@Override
	public void realizarAccion() {
		// TODO Auto-generated method stub
		
	}
}