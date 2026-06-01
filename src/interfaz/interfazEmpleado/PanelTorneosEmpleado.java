// =========================
// PanelTorneosEmpleado.java
// =========================

package interfaz.interfazEmpleado;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import modelo.Torneo;

public class PanelTorneosEmpleado extends JPanel {

    private static final long serialVersionUID = 1L;

    private VentanaEmpleado ventana;

    private DefaultTableModel modelo;

    public PanelTorneosEmpleado(VentanaEmpleado ventana) {

        this.ventana = ventana;

        setLayout(new BorderLayout());

        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        modelo = new DefaultTableModel(
                new String[]{
                        "Código",
                        "Juego",
                        "Día",
                        "Cupos"
                },
                0
        );

        JTable tabla = new JTable(modelo);

        tabla.setRowHeight(24);

        tabla.getTableHeader()
                .setFont(new Font("Arial", Font.BOLD, 12));

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        refrescar();
    }

    public void refrescar() {

        modelo.setRowCount(0);

        for (Torneo t : ventana.getTorneos()) {

            modelo.addRow(new Object[]{
                    t.getCodigo(),
                    t.getJuego().getNombre(),
                    t.getDiaSemana(),
                    t.getCuposDisponibles()
            });
        }
    }
}