package modelo;
import java.util.ArrayList;

// Esta clase representa un producto de pastelería.
// Hereda de ProductoMenu.
public class Pasteleria extends ProductoMenu {
    private ArrayList<String> alergenos;

    public Pasteleria(String nombre, double precio) {
        super(nombre, precio);
        this.alergenos = new ArrayList<>();
    }

    public void agregarAlergeno(String alergeno) { alergenos.add(alergeno); }
    public ArrayList<String> getAlergenos()      { return alergenos; }

    public boolean tieneAlergeno(String alergeno) {
        return alergenos.contains(alergeno);
    }

    @Override
    public String toString() {
        if (alergenos.isEmpty()) {
            return getNombre() + " - $" + getPrecio();
        } else {
            return getNombre() + " - $" + getPrecio() + " | Alérgenos: " + alergenos;
        }
    }
}