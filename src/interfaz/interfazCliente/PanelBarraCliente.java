package interfaz.interfazCliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
 
public class PanelBarraCliente extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
 
    private static final String CERRAR_SESION = "cerrarSesion";
 
    private VentanaCliente ventana;
 
    public PanelBarraCliente(VentanaCliente ventana) {
        this.ventana = ventana;
 
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 45));
        setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
 
        JLabel lblCliente = new JLabel("Cliente: " + ventana.getNombreCliente());
        lblCliente.setForeground(Color.WHITE);
        lblCliente.setFont(new Font("Arial", Font.BOLD, 13));
        add(lblCliente, BorderLayout.WEST);
 
        JButton btnCerrar = new JButton("Cerrar sesión");
        btnCerrar.setBackground(new Color(180, 60, 60));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setActionCommand(CERRAR_SESION);
        btnCerrar.addActionListener(this);
        add(btnCerrar, BorderLayout.EAST);
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(CERRAR_SESION))
        {
            ventana.cerrarSesion();
        }
    }
}
