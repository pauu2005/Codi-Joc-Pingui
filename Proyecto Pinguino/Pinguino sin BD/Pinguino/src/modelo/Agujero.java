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
        int posActual = jugador.getPosicion();
        int agujeroAnterior = 0; // Por defecto, vuelve al inicio si no hay anterior

        // Busca el agujero anterior en la lista (debe estar ordenada)
        for (int posHoyo : posicionesHoyos) {
            if (posHoyo < posActual) {
                agujeroAnterior = posHoyo;
            } else {
                break;
            }
        }

        jugador.setPosicion(agujeroAnterior);
    }

	@Override
	public void realizarAccion() {
		// TODO Auto-generated method stub
		
	}
}