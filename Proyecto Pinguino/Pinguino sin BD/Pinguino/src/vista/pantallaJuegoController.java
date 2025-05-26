package vista;

import java.util.Random;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import modelo.Inventario;
import modelo.Item;

import java.util.Arrays;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    @FXML private ImageView P1;
    @FXML private ImageView P2;
    @FXML private ImageView P3;
    @FXML private ImageView P4;
    @FXML private ImageView Auto;

    // Variables de juego
    private final int COLUMNS = 5;
    private final int NUM_JUGADORES = 5; // 4 humanos + 1 automático
    private int[] posiciones = {0, 0, 0, 0, 0}; // Añade espacio para el automático
    private int jugadorActual = 0;

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
    private modelo.Inventario[] inventarios = new modelo.Inventario[5]; // 4 humanos + 1 automático

    // Límites máximos
    private static final int MAX_DADOS = 3;
    private static final int MAX_PECES = 2;
    private static final int MAX_BOLAS = 6;

    private int focaBloqueada = 0; // 0 = no bloqueada, >0 = turnos bloqueada

    @FXML
    private void initialize() {
        generarCasillasEspeciales();
        eventos.setText("¡El juego ha comenzado! Turno de: " + getColorJugador(jugadorActual));
        for (int i = 0; i < NUM_JUGADORES; i++) {
            moverFichaVisual(i, 0);
            posiciones[i] = 0;
        }
        // ...en initialize() y donde reinicies inventario...
        for (int i = 0; i < NUM_JUGADORES; i++) {
            if (i < 4) { // Solo los 4 humanos tienen inventario
                ArrayList<Item> items = new ArrayList<>();
                items.add(new Item("dado_rapido", 1));
                items.add(new Item("dado_lento", 1));
                items.add(new Item("pez", 1));
                items.add(new Item("bola", 1));
                inventarios[i] = new Inventario(items);
            }
        // inventarios[4] (el automático) quedará null, ¡y no pasa nada!
        }
        mostrarInterroganteEnCasillaEvento();
        dibujarCasillasEspeciales();
        actualizarInventarioVista(jugadorActual); // <-- Añade esta línea
        System.out.println("Casilla de evento especial: " + casillaEventoEspecial);

        P1.setImage(new Image(getClass().getResourceAsStream("/Pinguino.png")));
        P2.setImage(new Image(getClass().getResourceAsStream("/Pinguino.png")));
        P3.setImage(new Image(getClass().getResourceAsStream("/Pinguino.png")));
        P4.setImage(new Image(getClass().getResourceAsStream("/Pinguino.png")));
        Auto.setImage(new Image(getClass().getResourceAsStream("/Foca.png")));
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


    for (int i = 0; i < NUM_JUGADORES; i++) {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("dado", 1)); // Empieza con 1 dado
        items.add(new Item("pez", 1));  // Empieza con 1 pez
        items.add(new Item("bola", 1)); // Empieza con 1 bola de nieve
        inventarios[i] = new Inventario(items);
    }

    }

    // Dibuja todas las casillas especiales
    private void dibujarCasillasEspeciales() {
        // Elimina imágenes anteriores si es necesario
        tablero.getChildren().removeIf(node -> node instanceof ImageView && node != P1 && node != P2 && node != P3 && node != P4 && node != Auto);

        // Hoyos
        for (int hoyo : posicionesHoyos) {
            int row = hoyo / COLUMNS;
            int col = hoyo % COLUMNS;
            ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/Hoyo.png")));
            img.setFitWidth(30);
            img.setFitHeight(30);
            tablero.add(img, col, row);
        }
        // Trineos
        for (int trineo : posicionesTrineos) {
            int row = trineo / COLUMNS;
            int col = trineo % COLUMNS;
            ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/Trineo.png")));
            img.setFitWidth(30);
            img.setFitHeight(30);
            tablero.add(img, col, row);
        }
        // Osos
        for (int oso : posicionesOsos) {
            int row = oso / COLUMNS;
            int col = oso % COLUMNS;
            ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/Oso.png")));
            img.setFitWidth(30);
            img.setFitHeight(30);
            tablero.add(img, col, row);
        }
        // Suelo Quebradizo
        for (int suelo : posicionesSuelos) {
            int row = suelo / COLUMNS;
            int col = suelo % COLUMNS;
            ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/Suelo Quebradizo.png")));
            img.setFitWidth(30);
            img.setFitHeight(30);
            tablero.add(img, col, row);
        }
    }

    @FXML
    private void handleNewGame() {
        for (int i = 0; i < NUM_JUGADORES; i++) {
            posiciones[i] = 0;
            moverFichaVisual(i, 0);
            ArrayList<Item> items = new ArrayList<>();
            items.add(new Item("dado_rapido", 1));
            items.add(new Item("dado_lento", 1));
            items.add(new Item("pez", 1));
            items.add(new Item("bola", 1));
            inventarios[i] = new Inventario(items);
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
            boolean añadido = false;
            int max = 1;
            if (objeto.equals("dado_rapido")) max = MAX_DADOS;
            else if (objeto.equals("dado_lento")) max = MAX_DADOS;
            else if (objeto.equals("pez")) max = MAX_PECES;
            else if (objeto.equals("bola")) max = MAX_BOLAS;
            añadido = inventarios[0].añadirItem(objeto, max);

            if (añadido) {
                eventos.setText("¡El jugador " + getColorJugador(0) + " ha encontrado un " + objeto + "!");
            } else {
                eventos.setText("¡Inventario lleno para " + objeto + "!");
            }
            actualizarInventarioVista(0); // Para un solo jugador

            // En multijugador:
            actualizarInventarioVista(jugadorActual); // o actualizarInventarioVista(0) para un solo jugador
        }
    }

    @FXML
    private void handleDadoTurnos(ActionEvent event) {
        Random rand = new Random();
        int diceResult = rand.nextInt(6) + 1;
        dadoResultText.setText("Ha salido: " + diceResult);

        if (jugadorActual < 4) {
            moverJugadorActual(diceResult);
            jugadorActual++;
            if (jugadorActual == 4) {
                jugarAutomatico();
                jugadorActual = 0;
                eventos.setText(eventos.getText() + "\nTurno de: " + getColorJugador(jugadorActual));
            } else {
                eventos.setText("Turno de: " + getColorJugador(jugadorActual));
            }
        }
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
            boolean añadido = false;
            int max = 1;
            if (objeto.equals("dado_rapido")) max = MAX_DADOS;
            else if (objeto.equals("dado_lento")) max = MAX_DADOS;
            else if (objeto.equals("pez")) max = MAX_PECES;
            else if (objeto.equals("bola")) max = MAX_BOLAS;
            añadido = inventarios[jugadorActual].añadirItem(objeto, max);

            if (añadido) {
                eventos.setText("¡El jugador " + getColorJugador(jugadorActual) + " ha encontrado un " + objeto + "!");
            } else {
                eventos.setText("¡Inventario lleno para " + objeto + "!");
            }
            actualizarInventarioVista(jugadorActual);
        }
    }

    private void moverFichaVisual(int idx, int posicion) {
        int row = posicion / COLUMNS;
        int col = posicion % COLUMNS;
        javafx.scene.Node ficha = getFichaByJugador(idx);

        // Desplazamiento visual para que no se tapen entre sí ni con la casilla especial
        double offsetX = 0;
        double offsetY = 0;
        switch (idx) {
            case 0: offsetX = -10; offsetY = -10; break; // P1 arriba izquierda
            case 1: offsetX = 10;  offsetY = -10; break; // P2 arriba derecha
            case 2: offsetX = -10; offsetY = 10;  break; // P3 abajo izquierda
            case 3: offsetX = 10;  offsetY = 10;  break; // P4 abajo derecha
            case 4: offsetX = 0;   offsetY = 0;   break; // Auto (foca) centro
        }
        ficha.setTranslateX(offsetX);
        ficha.setTranslateY(offsetY);

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
        String[] objetos = { "dado_rapido", "dado_lento", "pez", "bola" };
        Random rand = new Random();
        return objetos[rand.nextInt(objetos.length)];
    }

    // --- FUNCIONES DE USO DE OBJETOS ---

    private void usarDadoRapido() {
        if (inventarios[jugadorActual].quitarItem("dado_rapido")) {
            Random rand = new Random();
            int avance = rand.nextInt(6) + 5; // 5 a 10
            posiciones[jugadorActual] += avance;
            if (posiciones[jugadorActual] >= 50) posiciones[jugadorActual] = 49;
            moverFichaVisual(jugadorActual, posiciones[jugadorActual]);
            eventos.setText("¡Has usado un dado rápido! (+ " + avance + " casillas)");
            actualizarInventarioVista(jugadorActual);
            // Pasar turno al siguiente jugador
            jugadorActual = (jugadorActual + 1) % NUM_JUGADORES;
            eventos.setText(eventos.getText() + "\nTurno de: " + getColorJugador(jugadorActual));
        } else {
            eventos.setText("No tienes dados rápidos para usar.");
            actualizarInventarioVista(jugadorActual);
        }
    }

    private void usarDadoLento() {
        if (inventarios[jugadorActual].quitarItem("dado_lento")) {
            Random rand = new Random();
            int avance = rand.nextInt(3) + 1; // 1 a 3
            posiciones[jugadorActual] += avance;
            if (posiciones[jugadorActual] >= 50) posiciones[jugadorActual] = 49;
            moverFichaVisual(jugadorActual, posiciones[jugadorActual]);
            eventos.setText("¡Has usado un dado lento! (+ " + avance + " casillas)");
            actualizarInventarioVista(jugadorActual);
            // Pasar turno al siguiente jugador
            jugadorActual = (jugadorActual + 1) % NUM_JUGADORES;
            eventos.setText(eventos.getText() + "\nTurno de: " + getColorJugador(jugadorActual));
        } else {
            eventos.setText("No tienes dados lentos para usar.");
            actualizarInventarioVista(jugadorActual);
        }
    }

    private void usarPez() {
        if (inventarios[jugadorActual].quitarItem("pez")) {
            // Efecto: activar un "escudo" para el oso (ejemplo: variable booleana)
            // Puedes crear una variable: boolean escudoOso[] = new boolean[NUM_JUGADORES];
            // escudoOso[jugadorActual] = true;
            eventos.setText("¡Has usado un pez! Si caes en un oso, no volverás al inicio este turno.");
            // Implementa la lógica en el método donde gestionas el oso
        } else {
            eventos.setText("No tienes peces para usar.");
        }
        actualizarInventarioVista(jugadorActual);
    }

    private void usarBolaNieve() {
        if (inventarios[jugadorActual].quitarItem("bola")) {
            // Efecto: hacer retroceder a otro jugador (ejemplo: al siguiente jugador)
            int objetivo = (jugadorActual + 1) % NUM_JUGADORES;
            posiciones[objetivo] = Math.max(0, posiciones[objetivo] - 3);
            moverFichaVisual(objetivo, posiciones[objetivo]);
            eventos.setText("¡Has lanzado una bola de nieve! El jugador " + getColorJugador(objetivo) + " retrocede 3 casillas.");
        } else {
            eventos.setText("No tienes bolas de nieve para usar.");
        }
        actualizarInventarioVista(jugadorActual);
    }

    // --- HANDLERS DE BOTONES ---

    @FXML
    private void handleRapido() {
        usarDadoRapido();
    }

    @FXML
    private void handleLento() {
        usarDadoLento();
    }

    @FXML
    private void handlePeces() {
        usarPez();
    }

    @FXML
    private void handleNieve() {
        usarBolaNieve();
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

    private void actualizarInventarioVista(int idx) {
        Inventario inv = inventarios[idx];
        rapido_t.setText("Dado rápido: " + inv.getCantidad("dado_rapido"));
        lento_t.setText("Dado lento: " + inv.getCantidad("dado_lento"));
        peces_t.setText("Peces: " + inv.getCantidad("pez"));
        nieve_t.setText("Bolas de nieve: " + inv.getCantidad("bola"));
    }
    
    private void comprobarPeleaNieve(int jugadorQueSeMueve) {
        int pos = posiciones[jugadorQueSeMueve];
        for (int i = 0; i < NUM_JUGADORES; i++) {
            if (i != jugadorQueSeMueve && posiciones[i] == pos) {
                // Hay dos jugadores en la misma casilla
                int bolas1 = inventarios[jugadorQueSeMueve].getCantidad("bola");
                int bolas2 = inventarios[i].getCantidad("bola");
                if (bolas1 == 0 && bolas2 == 0) {
                    eventos.setText("¡Empate en la pelea de nieve! Nadie avanza.");
                } else if (bolas1 > bolas2) {
                    posiciones[jugadorQueSeMueve] += bolas1;
                    if (posiciones[jugadorQueSeMueve] >= 50) posiciones[jugadorQueSeMueve] = 49;
                    moverFichaVisual(jugadorQueSeMueve, posiciones[jugadorQueSeMueve]);
                    eventos.setText("¡" + getColorJugador(jugadorQueSeMueve) + " gana la pelea de nieve y avanza " + bolas1 + " casillas!");
                } else if (bolas2 > bolas1) {
                    posiciones[i] += bolas2;
                    if (posiciones[i] >= 50) posiciones[i] = 49;
                    moverFichaVisual(i, posiciones[i]);
                    eventos.setText("¡" + getColorJugador(i) + " gana la pelea de nieve y avanza " + bolas2 + " casillas!");
                } else {
                    eventos.setText("¡Empate en la pelea de nieve! Nadie avanza.");
                }
                // Ambos pierden todas sus bolas de nieve
                inventarios[jugadorQueSeMueve].añadirItem("bola", 0); // Por si no existe el item
                inventarios[i].añadirItem("bola", 0);
                for (Item it : inventarios[jugadorQueSeMueve].getLista()) {
                    if (it.getNombre().equals("bola")) it.setCantidad(0);
                }
                for (Item it : inventarios[i].getLista()) {
                    if (it.getNombre().equals("bola")) it.setCantidad(0);
                }
                actualizarInventarioVista(jugadorQueSeMueve);
                actualizarInventarioVista(i);
                break; // Solo una pelea por turno
            }
        }
    }

    private void jugarFoca() {
        Random rand = new Random();
        int avance = rand.nextInt(6) + 1; // 1 a 6
        posiciones[4] += avance;
        if (posiciones[4] >= 50) posiciones[4] = 49;
        moverFichaVisual(4, posiciones[4]);
        eventos.setText(eventos.getText() + "\nLa foca avanza " + avance + " casillas.");

        // Puedes añadir aquí efectos especiales si la foca cae en hoyo, trineo, etc.
        if (esHoyo(posiciones[4])) {
            posiciones[4] = buscarAgujeroAnterior(posiciones[4]);
            moverFichaVisual(4, posiciones[4]);
            eventos.setText(eventos.getText() + "\n¡La foca ha caído en un hoyo!");
        }
        // ...igual para trineo, oso, suelo quebradizo, evento especial...
    }

    private void jugarAutomatico() {
        Random rand = new Random();
        int avance = rand.nextInt(6) + 1; // 1 a 6
        posiciones[4] += avance;
        if (posiciones[4] >= 50) posiciones[4] = 49;
        moverFichaVisual(4, posiciones[4]);
        eventos.setText(eventos.getText() + "\nEl jugador automático avanza " + avance + " casillas.");
        // Puedes añadir aquí efectos de hoyos, trineos, etc. si quieres
    }

    private javafx.scene.Node getFichaByJugador(int idx) {
        switch (idx) {
            case 0: return P1;
            case 1: return P2;
            case 2: return P3;
            case 3: return P4;
            case 4: return Auto; // El jugador automático
            default: return null;
        }
    }
}