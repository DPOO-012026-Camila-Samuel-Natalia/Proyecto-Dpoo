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

import modelo.SugerenciaPlatillo;
 
public class PanelSugerencias extends JPanel implements ActionListener {
    private static final String APROBAR  = "aprobar";
    private static final String RECHAZAR = "rechazar";
 
    private VentanaAdministrador ventana;
 
    private DefaultTableModel modeloSugerencias;
    private JTable tabla;
 
    public PanelSugerencias(VentanaAdministrador ventana) {
        this.ventana = ventana;
 
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
        String[] columnas = {"Código", "Empleado", "Platillo", "Descripción", "Estado"};
        modeloSugerencias = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modeloSugerencias);
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
        modeloSugerencias.setRowCount(0);
        for (SugerenciaPlatillo s : ventana.getSugerencias())
        {
            modeloSugerencias.addRow(new Object[]{
                s.getCodigo(),
                s.getEmpleado().getNombre(),
                s.getNombrePlatillo(),
                s.getDescripcion(),
                s.getEstado()
            });
        }
    }
 
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String comando = e.getActionCommand();
        if (comando.equals(APROBAR))
        {
            aprobarSugerencia();
        }
        else if (comando.equals(RECHAZAR))
        {
            rechazarSugerencia();
        }
    }
 
    private void aprobarSugerencia()
    {
        int fila = tabla.getSelectedRow();
        if (fila < 0)
        {
            JOptionPane.showMessageDialog(ventana, "Selecciona una sugerencia."); return;
        }
 
        String codigo = (String) modeloSugerencias.getValueAt(fila, 0);
        String estado = (String) modeloSugerencias.getValueAt(fila, 4);
 
        if (!estado.equals("Pendiente"))
        {
            JOptionPane.showMessageDialog(ventana, "Sugerencia no disponible."); return;
        }
 
        String[] tipos = {"BEBIDA", "PASTELERIA"};
        String tipo = (String) JOptionPane.showInputDialog(ventana,"Tipo del producto:", "Aprobar sugerencia",JOptionPane.PLAIN_MESSAGE, null, tipos, tipos[0]);
        if (tipo == null) return;
 
        String precioStr = JOptionPane.showInputDialog(ventana, "Precio:");
        if (precioStr == null) return;
 
        try
        {
            double precio = Double.parseDouble(precioStr.trim());
            ventana.aprobarSugerencia(codigo, tipo, precio);
            refrescar();
            JOptionPane.showMessageDialog(ventana, "Sugerencia aprobada y producto agregado al menú.");
        }
        catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(ventana, "Precio inválido.");
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(ventana, "Error: " + ex.getMessage());
        }
    }
 
    private void rechazarSugerencia()
    {
        int fila = tabla.getSelectedRow();
        if (fila < 0)
        {
            JOptionPane.showMessageDialog(ventana, "Selecciona una sugerencia."); return;
        }
 
        String codigo = (String) modeloSugerencias.getValueAt(fila, 0);
        String estado = (String) modeloSugerencias.getValueAt(fila, 4);
 
        if (!estado.equals("Pendiente"))
        {
            JOptionPane.showMessageDialog(ventana, "Sugerencia no disponible."); return;
        }
 
        ventana.rechazarSugerencia(codigo);
        refrescar();
        JOptionPane.showMessageDialog(ventana, "Sugerencia rechazada.");
    }
}