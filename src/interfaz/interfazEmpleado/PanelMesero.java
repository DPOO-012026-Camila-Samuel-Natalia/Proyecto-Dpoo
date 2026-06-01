// =========================
// PanelMesero.java
// =========================

package interfaz.interfazEmpleado;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PanelMesero extends JPanel
implements ActionListener {

    private static final long serialVersionUID = 1L;

    private VentanaEmpleado ventana;

    public PanelMesero(VentanaEmpleado ventana) {

        this.ventana = ventana;

        setLayout(new GridLayout(3,2,10,10));

        JButton btnDescuento =
                new JButton("Aplicar descuento");

        JButton btnPrestamoMesa =
                new JButton("Préstamo mesa");

        btnDescuento.addActionListener(this);

        btnPrestamoMesa.addActionListener(this);

        add(btnDescuento);

        add(btnPrestamoMesa);
    }

    public void refrescar() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String texto =
                ((JButton)e.getSource()).getText();

        if (texto.equals("Aplicar descuento")) {

            String login =
                    JOptionPane.showInputDialog(
                            ventana,
                            "Login cliente:"
                    );

            if (login == null) return;

            String resultado =
                    ventana.aplicarDescuento(login);

            JOptionPane.showMessageDialog(
                    ventana,
                    resultado
            );
        }

        else if (texto.equals("Préstamo mesa")) {

            String mesa =
                    JOptionPane.showInputDialog(
                            ventana,
                            "Número mesa:"
                    );

            if (mesa == null) return;

            String juego =
                    (String) JOptionPane.showInputDialog(
                            ventana,
                            "Juego:",
                            "Préstamo",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            ventana.getNombresJuegosPrestamo(),
                            null
                    );

            if (juego == null) return;

            try {

                String resultado =
                        ventana.prestamoParaMesa(
                                Integer.parseInt(mesa),
                                juego
                        );

                JOptionPane.showMessageDialog(
                        ventana,
                        resultado
                );
            }

            catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        ventana,
                        "Número inválido."
                );
            }
        }
    }
}