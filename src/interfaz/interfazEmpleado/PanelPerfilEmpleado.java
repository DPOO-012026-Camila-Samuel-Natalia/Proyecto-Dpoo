// =========================
// PanelPerfilEmpleado.java
// =========================

package interfaz.interfazEmpleado;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PanelPerfilEmpleado extends JPanel {

    private static final long serialVersionUID = 1L;

    private VentanaEmpleado ventana;

    private JTextArea area;

    public PanelPerfilEmpleado(VentanaEmpleado ventana) {

        this.ventana = ventana;

        setLayout(new BorderLayout());

        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        area = new JTextArea();

        area.setEditable(false);

        area.setFont(new Font("Arial", Font.PLAIN, 14));

        add(new JScrollPane(area), BorderLayout.CENTER);

        refrescar();
    }

    public void refrescar() {

        String texto = "";

        texto += "=== PERFIL EMPLEADO ===\n\n";

        texto += "Nombre: "
                + ventana.getNombreEmpleado()
                + "\n";

        texto += "Tipo: "
                + ventana.getTipoEmpleado()
                + "\n";

        texto += "Estado: ";

        if (ventana.estaEnTurno()) {

            texto += "En turno\n";
        }

        else {

            texto += "Fuera de turno\n";
        }

        texto += "\nCódigo descuento:\n";

        texto += ventana.getCodigoDescuento();

        area.setText(texto);
    }
}