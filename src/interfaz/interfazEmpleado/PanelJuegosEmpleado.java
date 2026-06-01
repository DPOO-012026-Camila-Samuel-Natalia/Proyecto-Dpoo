// =========================
// PanelJuegosEmpleado.java
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

import modelo.JuegoDeMesa;

public class PanelJuegosEmpleado extends JPanel
implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final String COMPRAR = "comprar";

    private VentanaEmpleado ventana;

    private DefaultTableModel modelo;

    public PanelJuegosEmpleado(VentanaEmpleado ventana) {

        this.ventana = ventana;

        setLayout(new BorderLayout());

        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        modelo = new DefaultTableModel(
                new String[]{
                        "Nombre",
                        "Categoría",
                        "Precio",
                        "Copias"
                },
                0
        );

        JTable tabla = new JTable(modelo);

        tabla.setRowHeight(24);

        tabla.getTableHeader()
                .setFont(new Font("Arial", Font.BOLD, 12));

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JButton btnComprar =
                new JButton("Comprar");

        btnComprar.setActionCommand(COMPRAR);

        btnComprar.addActionListener(this);

        JPanel panelBtn =
                new JPanel(new FlowLayout(FlowLayout.LEFT));

        panelBtn.add(btnComprar);

        add(panelBtn, BorderLayout.SOUTH);

        refrescar();
    }

    public void refrescar() {

        modelo.setRowCount(0);

        for (JuegoDeMesa j :
                ventana.getJuegosDisponiblesVenta()) {

            modelo.addRow(new Object[]{
                    j.getNombre(),
                    j.getCategoria(),
                    "$" + j.getPrecioUnitario(),
                    j.getCopiasVenta()
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String juego =
                (String) JOptionPane.showInputDialog(
                        ventana,
                        "Juego:",
                        "Comprar",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        ventana.getNombresJuegosVenta(),
                        null
                );

        if (juego == null) return;

        String cantidad =
                JOptionPane.showInputDialog(
                        ventana,
                        "Cantidad:"
                );

        if (cantidad == null) return;

        try {

            int cant =
                    Integer.parseInt(cantidad);

            String resultado =
                    ventana.comprarJuego(
                            juego,
                            cant
                    );

            JOptionPane.showMessageDialog(
                    ventana,
                    resultado
            );

            refrescar();
        }

        catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    ventana,
                    "Número inválido."
            );
        }
    }
}