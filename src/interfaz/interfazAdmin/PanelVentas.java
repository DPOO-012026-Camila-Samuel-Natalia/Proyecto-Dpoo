package interfaz.interfazAdmin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
 
public class PanelVentas extends JPanel implements ActionListener {
    private static final String ACTUALIZAR = "actualizar";
 
    private VentanaAdministrador ventana;
 
    private JTextArea areaInforme;
 
    public PanelVentas(VentanaAdministrador ventana) {
        this.ventana = ventana;
 
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
 
        areaInforme = new JTextArea();
        areaInforme.setEditable(false);
        areaInforme.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(areaInforme), BorderLayout.CENTER);
 
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnActualizar = new JButton("Actualizar informe");
        btnActualizar.setFocusPainted(false);
        btnActualizar.setActionCommand(ACTUALIZAR);
        btnActualizar.addActionListener(this);
        panelBotones.add(btnActualizar);
        add(panelBotones, BorderLayout.SOUTH);
 
        refrescar();
    }
 
    public void refrescar()
    {
        areaInforme.setText(ventana.getInformeVentas());
    }
 
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String comando = e.getActionCommand();
        if (comando.equals(ACTUALIZAR))
        {
            refrescar();
        }
    }
}
