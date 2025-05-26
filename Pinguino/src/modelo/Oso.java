package modelo;

import java.util.ArrayList;

public class Oso extends Casilla {

	public Oso(int posicion, ArrayList<Jugador> jugadoresActuales) {
		super(posicion, jugadoresActuales);
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

}
