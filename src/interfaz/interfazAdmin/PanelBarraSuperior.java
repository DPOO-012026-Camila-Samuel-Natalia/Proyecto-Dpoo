package interfaz.interfazAdmin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
 
public class PanelBarraSuperior extends JPanel implements ActionListener {
    private static final String CERRAR_SESION = "cerrarSesion";
 
    private VentanaAdministrador ventana;
 
    public PanelBarraSuperior(VentanaAdministrador ventana)
    {
        this.ventana = ventana;
 
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 45));
        setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
 
        JLabel lblAdmin = new JLabel("Admin: " + ventana.getNombreAdmin());
        lblAdmin.setForeground(Color.WHITE);
        lblAdmin.setFont(new Font("Arial", Font.BOLD, 13));
        add(lblAdmin, BorderLayout.WEST);
 
        JButton btnCerrar = new JButton("Cerrar sesión");
        btnCerrar.setBackground(new Color(180, 60, 60));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setActionCommand(CERRAR_SESION);
        btnCerrar.addActionListener(this);
        add(btnCerrar, BorderLayout.EAST);
    }
 
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String comando = e.getActionCommand();
        if (comando.equals(CERRAR_SESION))
        {
            ventana.cerrarSesion();
        }
    }
}