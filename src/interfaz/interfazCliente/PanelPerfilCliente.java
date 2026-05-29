package interfaz.interfazCliente;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
 
public class PanelPerfilCliente extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
 
    private static final String ACTUALIZAR = "actualizar";
 
    private VentanaCliente ventana;
 
    private JTextArea areaPerfil;
 
    public PanelPerfilCliente(VentanaCliente ventana) {
        this.ventana = ventana;
 
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
 
        areaPerfil = new JTextArea();
        areaPerfil.setEditable(false);
        areaPerfil.setFont(new Font("Arial", Font.PLAIN, 14));
        add(new JScrollPane(areaPerfil), BorderLayout.CENTER);
 
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setFocusPainted(false);
        btnActualizar.setActionCommand(ACTUALIZAR);
        btnActualizar.addActionListener(this);
 
        JPanel panelBtn = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        panelBtn.add(btnActualizar);
        add(panelBtn, BorderLayout.SOUTH);
 
        refrescar();
    }
 
    public void refrescar()
    {
        String texto = "=== MI PERFIL ===\n\n";
        texto += "Puntos de fidelidad: " + ventana.getPuntosFidelidad() + "\n\n";
 
        if (ventana.tieneBonoTorneo())
        {
            texto += "Bono de torneo activo: $" + ventana.getBonoTorneo() + "\n";
            texto += "(Se aplica automáticamente en tu próxima compra de juego)";
        }
        else
        {
            texto += "No tienes bono de torneo activo.";
        }
 
        areaPerfil.setText(texto);
    }
 
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals(ACTUALIZAR))
        {
            refrescar();
        }
    }
}