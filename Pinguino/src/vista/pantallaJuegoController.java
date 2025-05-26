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

    private final int TOTAL_CASILLAS = 50;
    private final int MAX_ESPECIALES = 4;
    private final int MAX_OSOS = 4;
    private final int MAX_SUELOS = 4;
    private int[] posicionesHoyos;
    private int[] posicionesTrineos;
    private int[] posicionesOsos;
    private int[] posicionesSuelos;
    private int casillaEventoEspecial = -1;

    // --- Inventario de cada jugador ---
    private ArrayList<String>[] inventarios = new ArrayList[NUM_JUGADORES];

    @FXML
    private void initialize() {
        generarCasillasEspeciales();
        eventos.setText("¡El juego ha comenzado! Turno de: " + getColorJugador(jugadorActual));
        for (int i = 0; i < NUM_JUGADORES; i++) {
            moverFichaVisual(i, 0);
            posiciones[i] = 0;
        }
        for (int i = 0; i < NUM_JUGADORES; i++) {
            inventarios[i] = new ArrayList<>();
        }
        mostrarInterroganteEnCasillaEvento();
        dibujarCasillasEspeciales();
        System.out.println("Casilla de evento especial: " + casillaEventoEspecial);
    }

    // Genera todas las posiciones especiales sin solaparse
    private void generarCasillasEspeciales() {
        ArrayList<Integer> disponibles = new ArrayList<>();
        for (int i = 1; i < TOTAL_CASILLAS - 1; i++) { // evita inicio y fin
            disponibles.add(i);
        }
        java.util.Collections.shuffle(disponibles);

        posicionesHoyos = new int[MAX_ESPECIALES];
        posicionesTrineos = new int[MAX_ESPECIALES];
        posicionesOsos = new int[MAX_OSOS];
        posicionesSuelos = new int[MAX_SUELOS];

        for (int i = 0; i < MAX_ESPECIALES; i++) posicionesHoyos[i] = disponibles.remove(0);
        for (int i = 0; i < MAX_ESPECIALES; i++) posicionesTrineos[i] = disponibles.remove(0);
        for (int i = 0; i < MAX_OSOS; i++) posicionesOsos[i] = disponibles.remove(0);
        for (int i = 0; i < MAX_SUELOS; i++) posicionesSuelos[i] = disponibles.remove(0);

        casillaEventoEspecial = disponibles.remove(0);
    }

    // Dibuja todas las casillas especiales
    private void dibujarCasillasEspeciales() {
        tablero.getChildren().removeIf(node -> node instanceof Circle && node != P1 && node != P2 && node != P3 && node != P4);

        // Hoyos
        for (int hoyo : posicionesHoyos) {
            int row = hoyo / COLUMNS;
            int col = hoyo % COLUMNS;
            Circle c = new Circle(12);
            c.setFill(javafx.scene.paint.Color.BLACK);
            c.setStroke(javafx.scene.paint.Color.GRAY);
            tablero.add(c, col, row);
        }
        // Dibuja trineos
        for (int trineo : posicionesTrineos) {
            int row = trineo / COLUMNS;
            int col = trineo % COLUMNS;
            Circle c = new Circle(12);
            c.setFill(javafx.scene.paint.Color.CYAN);
            c.setStroke(javafx.scene.paint.Color.BLUE);
            tablero.add(c, col, row);
        }
        // Osos
        for (int oso : posicionesOsos) {
            int row = oso / COLUMNS;
            int col = oso % COLUMNS;
            Circle c = new Circle(12);
            c.setFill(javafx.scene.paint.Color.BROWN);
            c.setStroke(javafx.scene.paint.Color.DARKRED);
            tablero.add(c, col, row);
        }
        // Suelo Quebradizo
        for (int suelo : posicionesSuelos) {
            int row = suelo / COLUMNS;
            int col = suelo % COLUMNS;
            Circle c = new Circle(12);
            c.setFill(javafx.scene.paint.Color.LIGHTGRAY);
            c.setStroke(javafx.scene.paint.Color.DARKGRAY);
            tablero.add(c, col, row);
        }
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
            posiciones[0] = buscarAgujeroAnterior(posiciones[0]);
            moverFichaVisual(0, posiciones[0]);
            eventos.setText("¡El jugador " + getColorJugador(0) + " ha caído en un hoyo y vuelve al anterior!");
        }
        // Trineos
        else if (esTrineo(posiciones[0])) {
            int destino = siguienteTrineo(posiciones[0]);
            posiciones[0] = destino;
            moverFichaVisual(0, destino);
            eventos.setText("¡El jugador " + getColorJugador(0) + " ha cogido un trineo hasta la casilla " + destino + "!");
        }
        // Oso
        else if (esOso(posiciones[0])) {
            posiciones[0] = 0;
            moverFichaVisual(0, 0);
            eventos.setText("¡El jugador " + getColorJugador(0) + " ha sido atacado por un oso y vuelve al inicio!");
        }
        // Suelo Quebradizo
        else if (esSueloQuebradizo(posiciones[0])) {
            // Aquí puedes poner el efecto que quieras, por ejemplo perder turno o retroceder 2 casillas
            posiciones[0] = Math.max(0, posiciones[0] - 2);
            moverFichaVisual(0, posiciones[0]);
            eventos.setText("¡El jugador " + getColorJugador(0) + " ha pisado suelo quebradizo y retrocede 2 casillas!");
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
            posiciones[jugadorActual] = buscarAgujeroAnterior(posiciones[jugadorActual]);
            moverFichaVisual(jugadorActual, posiciones[jugadorActual]);
            eventos.setText("¡El jugador " + getColorJugador(jugadorActual) + " ha caído en un hoyo y vuelve al anterior!");
        }
        // Trineos
        else if (esTrineo(posiciones[jugadorActual])) {
            int destino = siguienteTrineo(posiciones[jugadorActual]);
            posiciones[jugadorActual] = destino;
            moverFichaVisual(jugadorActual, destino);
            eventos.setText("¡El jugador " + getColorJugador(jugadorActual) + " ha cogido un trineo hasta la casilla " + destino + "!");
        }
        // Oso
        else if (esOso(posiciones[jugadorActual])) {
            posiciones[jugadorActual] = 0;
            moverFichaVisual(jugadorActual, 0);
            eventos.setText("¡El jugador " + getColorJugador(jugadorActual) + " ha sido atacado por un oso y vuelve al inicio!");
        }
        // Suelo Quebradizo
        else if (esSueloQuebradizo(posiciones[jugadorActual])) {
            // Aquí puedes poner el efecto que quieras, por ejemplo perder turno o retroceder 2 casillas
            posiciones[jugadorActual] = Math.max(0, posiciones[jugadorActual] - 2);
            moverFichaVisual(jugadorActual, posiciones[jugadorActual]);
            eventos.setText("¡El jugador " + getColorJugador(jugadorActual) + " ha pisado suelo quebradizo y retrocede 2 casillas!");
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

    // --- OSOS ---
    private boolean esOso(int posicion) {
        for (int oso : posicionesOsos) {
            if (posicion == oso) return true;
        }
        return false;
    }

    // --- SUELO QUEBRADIZO ---
    private boolean esSueloQuebradizo(int posicion) {
        for (int suelo : posicionesSuelos) {
            if (posicion == suelo) return true;
        }
        return false;
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
    
    // Busca el agujero anterior
    private int buscarAgujeroAnterior(int posActual) {
        int agujeroAnterior = 0;
        Arrays.sort(posicionesHoyos);
        for (int posHoyo : posicionesHoyos) {
            if (posHoyo < posActual) {
                agujeroAnterior = posHoyo;
            } else {
                break;
            }
        }
        return agujeroAnterior;
    }
}