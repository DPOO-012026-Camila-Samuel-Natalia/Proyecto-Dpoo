// =========================
// PanelBarraEmpleado.java
// =========================

package interfaz.interfazEmpleado;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelBarraEmpleado extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final String CERRAR = "cerrar";

    private VentanaEmpleado ventana;

    public PanelBarraEmpleado(VentanaEmpleado ventana) {

        this.ventana = ventana;

        setLayout(new BorderLayout());

        setBackground(new Color(30, 30, 45));

        setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

        JLabel lblEmpleado = new JLabel(
                "Empleado: "
                        + ventana.getNombreEmpleado()
                        + " ("
                        + ventana.getTipoEmpleado()
                        + ")"
        );

        lblEmpleado.setForeground(Color.WHITE);

        lblEmpleado.setFont(new Font("Arial", Font.BOLD, 13));

        add(lblEmpleado, BorderLayout.WEST);

        JButton btnCerrar = new JButton("Cerrar sesión");

        btnCerrar.setBackground(new Color(180, 60, 60));

        btnCerrar.setForeground(Color.WHITE);

        btnCerrar.setFocusPainted(false);

        btnCerrar.setActionCommand(CERRAR);

        btnCerrar.addActionListener(this);

        add(btnCerrar, BorderLayout.EAST);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals(CERRAR)) {

            ventana.cerrarSesion();
        }
    }
}