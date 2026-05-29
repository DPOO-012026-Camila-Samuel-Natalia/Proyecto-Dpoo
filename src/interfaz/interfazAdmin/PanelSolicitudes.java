package interfaz.interfazAdmin;

import java.awt.BorderLayout;
import java.awt.Color;
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

import modelo.SolicitudCambioTurno;
 
public class PanelSolicitudes extends JPanel implements ActionListener {
    private static final String APROBAR  = "aprobar";
    private static final String RECHAZAR = "rechazar";
 
    private VentanaAdministrador ventana;
 
    private DefaultTableModel modeloSolicitudes;
    private JTable tabla;
 
    public PanelSolicitudes(VentanaAdministrador ventana){
        this.ventana = ventana;
 
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
        String[] columnas = {"Código", "Solicitante", "Tipo", "Turno origen", "Turno deseado", "Estado"};
        modeloSolicitudes = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modeloSolicitudes);
        tabla.setDefaultEditor(Object.class, null);
        tabla.setRowHeight(24);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        add(new JScrollPane(tabla), BorderLayout.CENTER);
 
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
 
        JButton btnAprobar  = new JButton("Aprobar");
        JButton btnRechazar = new JButton("Rechazar");
 
        btnAprobar.setBackground(new Color(70, 150, 70));
        btnAprobar.setForeground(Color.WHITE);
        btnAprobar.setFocusPainted(false);
        btnAprobar.setActionCommand(APROBAR);
        btnAprobar.addActionListener(this);
 
        btnRechazar.setBackground(new Color(180, 60, 60));
        btnRechazar.setForeground(Color.WHITE);
        btnRechazar.setFocusPainted(false);
        btnRechazar.setActionCommand(RECHAZAR);
        btnRechazar.addActionListener(this);
 
        panelBotones.add(btnAprobar);
        panelBotones.add(btnRechazar);
        add(panelBotones, BorderLayout.SOUTH);
 
        refrescar();
    }
 
    public void refrescar()
    {
        modeloSolicitudes.setRowCount(0);
        for (SolicitudCambioTurno s : ventana.getSolicitudes())
        {
            modeloSolicitudes.addRow(new Object[]{
                s.getCodigo(),
                s.getEmpleadoSolicitante().getNombre(),
                s.getTipoCambio(),
                s.getTurnoQueOfrece().getDiaSemana(),
                s.getTurnoSolicitado().getDiaSemana(),
                s.getEstado()
            });
        }
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        if (comando.equals(APROBAR))
        {
            procesarSolicitud(true);
        }
        else if (comando.equals(RECHAZAR))
        {
            procesarSolicitud(false);
        }
    }
 
    private void procesarSolicitud(boolean aprobar) {
        int fila = tabla.getSelectedRow();
        if (fila < 0)
        {
            JOptionPane.showMessageDialog(ventana, "Selecciona una solicitud de la tabla."); return;
        }
 
        String codigo = (String) modeloSolicitudes.getValueAt(fila, 0);
        String estado = (String) modeloSolicitudes.getValueAt(fila, 5);
 
        if (!estado.equals("Pendiente"))
        {
            JOptionPane.showMessageDialog(ventana, "Esa solicitud ya fue procesada."); return;
        }
 
        try
        {
            if (aprobar)
            {
                ventana.aprobarSolicitud(codigo);
                JOptionPane.showMessageDialog(ventana, "Solicitud aprobada.");
            }
            else
            {
                ventana.rechazarSolicitud(codigo);
                JOptionPane.showMessageDialog(ventana, "Solicitud rechazada.");
            }
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(ventana, "Error: " + ex.getMessage());
        }
 
        refrescar();
    }
}