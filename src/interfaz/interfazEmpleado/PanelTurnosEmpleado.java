// =========================
// PanelTurnosEmpleado.java
// =========================

package interfaz.interfazEmpleado;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import modelo.Turno;

public class PanelTurnosEmpleado extends JPanel
implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final String CAMBIO = "cambio";

    private VentanaEmpleado ventana;

    private DefaultTableModel modelo;

    public PanelTurnosEmpleado(VentanaEmpleado ventana) {

        this.ventana = ventana;

        setLayout(new BorderLayout());

        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        modelo = new DefaultTableModel(
                new String[]{"Día"},
                0
        );

        JTable tabla = new JTable(modelo);

        tabla.setRowHeight(24);

        tabla.getTableHeader()
                .setFont(new Font("Arial", Font.BOLD, 12));

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JButton btnCambio =
                new JButton("Solicitar cambio");

        btnCambio.setActionCommand(CAMBIO);

        btnCambio.addActionListener(this);

        JPanel panelBtn =
                new JPanel(new FlowLayout(FlowLayout.LEFT));

        panelBtn.add(btnCambio);

        add(panelBtn, BorderLayout.SOUTH);

        refrescar();
    }

    public void refrescar() {

        modelo.setRowCount(0);

        for (Turno t : ventana.getTurnosEmpleado()) {

            modelo.addRow(new Object[]{
                    t.getDiaSemana()
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String diaOfrece =
                (String) JOptionPane.showInputDialog(
                        ventana,
                        "Turno que ofreces:",
                        "Cambio turno",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        ventana.getDiasTurnosEmpleado(),
                        null
                );

        if (diaOfrece == null) return;

        String diaDesea =
                (String) JOptionPane.showInputDialog(
                        ventana,
                        "Turno que deseas:",
                        "Cambio turno",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        ventana.getDiasTurnos(),
                        null
                );

        if (diaDesea == null) return;

        String motivo =
                JOptionPane.showInputDialog(
                        ventana,
                        "Motivo:"
                );

        if (motivo == null) return;

        String resultado =
                ventana.solicitarCambioGeneral(
                        diaOfrece,
                        diaDesea,
                        motivo
                );

        JOptionPane.showMessageDialog(
                ventana,
                resultado
        );
    }
}