package vista;

import java.util.Random;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import java.util.Arrays;

public class pantallaJuegoController {

    // Menu items
    @FXML private MenuItem newGame;
    @FXML private MenuItem saveGame;
    @FXML private MenuItem loadGame;
    @FXML private MenuItem quitGame;

    // Buttons
    @FXML private Button dado;
    @FXML private Button rapido;
    @FXML private Button lento;
    @FXML private Button peces;
    @FXML private Button nieve;

    // Texts
    @FXML private Text dadoResultText;
    @FXML private Text rapido_t;
    @FXML private Text lento_t;
    @FXML private Text peces_t;
    @FXML private Text nieve_t;
    @FXML private Text eventos;
    @FXML private Text interrogante; // <-- Añade esto en tu FXML

    // Game board and player pieces
    @FXML private GridPane tablero;
    @FXML private Circle P1;
    @FXML private Circle P2;
    @FXML private Circle P3;
    @FXML private Circle P4;

    // Variables de juego
    private final int COLUMNS = 5;
    private final int NUM_JUGADORES = 4;
    private int[] posiciones = {0, 0, 0, 0}; // P1, P2, P3, P4
    private int jugadorActual = 0; // 0 = P1, 1 = P2, 2 = P3, 3 = P4

    // Posiciones de hoyos y trineos (ajusta según tu tablero)
    private final int[] posicionesHoyos = {11, 28, 35}; // ejemplo
    private final int[] posicionesTrineos = {8, 20, 42}; // ejemplo

    // --- Casilla de evento especial ---
    private int casillaEventoEspecial = -1;

    // --- Inventario de cada jugador ---
    private ArrayList<String>[] inventarios = new ArrayList[NUM_JUGADORES];

    @FXML
    private void initialize() {
        eventos.setText("¡El juego ha comenzado! Turno de: " + getColorJugador(jugadorActual));
        for (int i = 0; i < NUM_JUGADORES; i++) {
            moverFichaVisual(i, 0);
            posiciones[i] = 0;
        }
        for (int i = 0; i < NUM_JUGADORES; i++) {
            inventarios[i] = new ArrayList<>();
        }
        // Elegir una casilla aleatoria que no sea hoyo ni trineo
        Random rand = new Random();
        while (true) {
            int posible = rand.nextInt(50); // 0 a 49
            if (!esHoyo(posible) && !esTrineo(posible)) {
                casillaEventoEspecial = posible;
                break;
            }
        }
        mostrarInterroganteEnCasillaEvento();
        System.out.println("Casilla de evento especial: " + casillaEventoEspecial);
    }

    @FXML
    private void handleNewGame() {
        for (int i = 0; i < NUM_JUGADORES; i++) {
            posiciones[i] = 0;
            moverFichaVisual(i, 0);
            inventarios[i].clear();
        }
        jugadorActual = 0;
        eventos.setText("¡Nuevo juego! Turno de: " + getColorJugador(jugadorActual));
        // Elegir nueva casilla de evento especial
        Random rand = new Random();
        while (true) {
            int posible = rand.nextInt(50);
            if (!esHoyo(posible) && !esTrineo(posible)) {
                casillaEventoEspecial = posible;
                break;
            }
        }
        mostrarInterroganteEnCasillaEvento();
        System.out.println("Casilla de evento especial: " + casillaEventoEspecial);
    }

    @FXML
    private void handleSaveGame() {  
        System.out.println("Saved game.");
        // TODO
    }

    @FXML
    private void handleLoadGame() {
        System.out.println("Loaded game.");
        // TODO
    }

    @FXML
    private void handleQuitGame() {
        System.out.println("Exit...");
        // TODO
    }

    @FXML
    private void handleDado(ActionEvent event) {
        Random rand = new Random();
        int diceResult = rand.nextInt(6) + 1;
        dadoResultText.setText("Ha salido: " + diceResult);
        moveP1(diceResult);
    }

    private void moveP1(int steps) {
        posiciones[0] += steps;
        if (posiciones[0] >= 50) posiciones[0] = 49;
        moverFichaVisual(0, posiciones[0]);
        // Hoyos
        if (esHoyo(posiciones[0])) {
            posiciones[0] = 0;
            moverFichaVisual(0, 0);
            eventos.setText("¡El jugador " + getColorJugador(0) + " ha caído en un hoyo y vuelve a la salida!");
        }
        // Trineos
        else if (esTrineo(posiciones[0])) {
            int destino = siguienteTrineo(posiciones[0]);
            posiciones[0] = destino;
            moverFichaVisual(0, destino);
            eventos.setText("¡El jugador " + getColorJugador(0) + " ha cogido un trineo hasta la casilla " + destino + "!");
        }
        // Evento especial
        else if (posiciones[0] == casillaEventoEspecial) {
            String objeto = obtenerObjetoAleatorio();
            inventarios[0].add(objeto);
            eventos.setText("¡El jugador " + getColorJugador(0) + " ha encontrado un objeto: " + objeto + "!\nInventario: " + inventarios[0]);
        }
    }

    @FXML
    private void handleDadoTurnos(ActionEvent event) {
        Random rand = new Random();
        int diceResult = rand.nextInt(6) + 1;
        dadoResultText.setText("Ha salido: " + diceResult);
        moverJugadorActual(diceResult);
        jugadorActual = (jugadorActual + 1) % NUM_JUGADORES;
        eventos.setText("Turno de: " + getColorJugador(jugadorActual));
    }

    private void moverJugadorActual(int steps) {
        posiciones[jugadorActual] += steps;
        if (posiciones[jugadorActual] >= 50) posiciones[jugadorActual] = 49;
        moverFichaVisual(jugadorActual, posiciones[jugadorActual]);
        // Hoyos
        if (esHoyo(posiciones[jugadorActual])) {
            posiciones[jugadorActual] = 0;
            moverFichaVisual(jugadorActual, 0);
            eventos.setText("¡El jugador " + getColorJugador(jugadorActual) + " ha caído en un hoyo y vuelve a la salida!");
        }
        // Trineos
        else if (esTrineo(posiciones[jugadorActual])) {
            int destino = siguienteTrineo(posiciones[jugadorActual]);
            posiciones[jugadorActual] = destino;
            moverFichaVisual(jugadorActual, destino);
            eventos.setText("¡El jugador " + getColorJugador(jugadorActual) + " ha cogido un trineo hasta la casilla " + destino + "!");
        }
        // Evento especial
        else if (posiciones[jugadorActual] == casillaEventoEspecial) {
            String objeto = obtenerObjetoAleatorio();
            inventarios[jugadorActual].add(objeto);
            eventos.setText("¡El jugador " + getColorJugador(jugadorActual) + " ha encontrado un objeto: " + objeto + "!\nInventario: " + inventarios[jugadorActual]);
        }
    }

    private void moverFichaVisual(int idx, int posicion) {
        int row = posicion / COLUMNS;
        int col = posicion % COLUMNS;
        Circle ficha = getCircleByJugador(idx);
        GridPane.setRowIndex(ficha, row);
        GridPane.setColumnIndex(ficha, col);
    }

    private void mostrarInterroganteEnCasillaEvento() {
        if (interrogante != null) {
            interrogante.setText("?");
            // Asegúrate de que el interrogante está en el GridPane
            if (!tablero.getChildren().contains(interrogante)) {
                tablero.getChildren().add(interrogante);
            }
            int row = casillaEventoEspecial / COLUMNS;
            int col = casillaEventoEspecial % COLUMNS;
            GridPane.setRowIndex(interrogante, row);
            GridPane.setColumnIndex(interrogante, col);
        }
    }

    private Circle getCircleByJugador(int idx) {
        switch (idx) {
            case 0: return P1;
            case 1: return P2;
            case 2: return P3;
            case 3: return P4;
            default: return null;
        }
    }

    // --- HOYOS ---
    private boolean esHoyo(int posicion) {
        for (int hoyo : posicionesHoyos) {
            if (posicion == hoyo) return true;
        }
        return false;
    }

    // --- TRINEOS ---
    private boolean esTrineo(int posicion) {
        for (int trineo : posicionesTrineos) {
            if (posicion == trineo) return true;
        }
        return false;
    }

    private int siguienteTrineo(int posicionActual) {
        Arrays.sort(posicionesTrineos);
        for (int trineo : posicionesTrineos) {
            if (trineo > posicionActual) return trineo;
        }
        // Si está en el último trineo, vuelve al primero
        return posicionesTrineos[0];
    }

    // --- COLORES DE JUGADOR ---
    private String getColorJugador(int idx) {
        switch (idx) {
            case 0: return "rojo";
            case 1: return "azul";
            case 2: return "verde";
            case 3: return "amarillo";
            default: return "desconocido";
        }
    }

    // --- OBJETO ALEATORIO DE EVENTO ---
    private String obtenerObjetoAleatorio() {
        String[] objetos = { "pez", "bolas", "rapido", "lento", "pierdeTurno", "pierdeItem", "motos" };
        Random rand = new Random();
        return objetos[rand.nextInt(objetos.length)];
    }

    @FXML
    private void handleRapido() {
        System.out.println("Fast.");
        // TODO
    }

    @FXML
    private void handleLento() {
        System.out.println("Slow.");
        // TODO
    }

    @FXML
    private void handlePeces() {
        System.out.println("Fish.");
        // TODO
    }

    @FXML
    private void handleNieve() {
        System.out.println("Snow.");
        // TODO
    }
}