package modelo;

import java.util.ArrayList;

public class Inventario {
    private ArrayList<Item> lista;

    public Inventario(ArrayList<Item> lista) {
        this.lista = lista;
    }

    public ArrayList<Item> getLista() {
        return lista;
    }

    public void setLista(ArrayList<Item> lista) {
        this.lista = lista;
    }

    // Añadir objeto con límite
    public boolean añadirItem(String nombre, int max) {
        for (Item i : lista) {
            if (i.getNombre().equals(nombre)) {
                if (i.getCantidad() < max) {
                    i.setCantidad(i.getCantidad() + 1);
                    return true;
                } else {
                    return false; // Límite alcanzado
                }
            }
        }
        // Si no existe, lo añade
        if (max > 0) {
            lista.add(new Item(nombre, 1));
            return true;
        }
        return false;
    }

    // Quitar objeto
    public boolean quitarItem(String nombre) {
        for (Item i : lista) {
            if (i.getNombre().equals(nombre) && i.getCantidad() > 0) {
                i.setCantidad(i.getCantidad() - 1);
                return true;
            }
        }
        return false;
    }

    // Obtener cantidad de un objeto
    public int getCantidad(String nombre) {
        for (Item i : lista) {
            if (i.getNombre().equals(nombre)) return i.getCantidad();
        }
        return 0;
    }
}



