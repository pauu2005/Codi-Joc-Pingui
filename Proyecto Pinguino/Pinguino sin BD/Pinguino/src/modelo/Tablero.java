package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;

public class Tablero {
    private ArrayList<Casilla> casillas;
    private ArrayList<Jugador> jugadores;
    private int turnos;
    private Jugador jugadorActual;

    // Listas para hoyos y trineos
    private ArrayList<Integer> posicionesHoyos = new ArrayList<>();
    private ArrayList<Integer> posicionesTrineos = new ArrayList<>();

    public Tablero(ArrayList<Casilla> casillas, ArrayList<Jugador> jugadores, int turnos, Jugador jugadorActual) {
        super();
        // Inicializa casillas si es null
        if (casillas == null) {
            this.casillas = new ArrayList<>();
        } else {
            this.casillas = casillas;
        }
        this.jugadores = jugadores;
        this.turnos = turnos;
        this.jugadorActual = jugadorActual;
        inicializarCasillasEspeciales();
    }

    // Inicialización de hoyos y trineos
    private void inicializarCasillasEspeciales() {
        int totalCasillas = 50; // Por ejemplo, tablero de 50 casillas
        Random rand = new Random();

        // Limpia las posiciones antes de rellenar
        posicionesHoyos.clear();
        posicionesTrineos.clear();

        // Hoyos
        int numHoyos = rand.nextInt(5) + 2; // entre 2 y 6
        Set<Integer> hoyos = new HashSet<>();
        while (hoyos.size() < numHoyos) {
            int pos = rand.nextInt(totalCasillas - 2) + 1; // evita 0 y última
            hoyos.add(pos);
        }
        posicionesHoyos.addAll(hoyos);
        Collections.sort(posicionesHoyos);

        // Trineos
        int numTrineos = rand.nextInt(5) + 2; // entre 2 y 6
        Set<Integer> trineos = new HashSet<>();
        while (trineos.size() < numTrineos) {
            int pos = rand.nextInt(totalCasillas - 2) + 1;
            if (!hoyos.contains(pos)) trineos.add(pos); // no solapar
        }
        posicionesTrineos.addAll(trineos);
        Collections.sort(posicionesTrineos);

        // Crear casillas
        casillas.clear();
        for (int i = 0; i < totalCasillas; i++) {
            ArrayList<Jugador> vacio = new ArrayList<>();
            if (posicionesHoyos.contains(i)) {
                casillas.add(new Agujero(i, vacio, posicionesHoyos));
            } else if (posicionesTrineos.contains(i)) {
                casillas.add(new Trineo(i, vacio, posicionesTrineos));
            } else {
                casillas.add(new CasillaNormal(i, vacio));
            }
        }
    }

    // Permite acceder a las casillas desde el controlador
    public ArrayList<Casilla> getCasillas() {
        return casillas;
    }

    // Puedes añadir aquí más métodos get/set si los necesitas
}