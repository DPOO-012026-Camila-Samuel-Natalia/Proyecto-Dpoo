package interfaz.interfazCliente;

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
 
import modelo.TipoTorneo;
import modelo.Torneo;
 
public class PanelTorneosCliente extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
 
    private static final String INSCRIBIRSE    = "inscribirse";
    private static final String DESINSCRIBIRSE = "desinscribirse";
 
    private VentanaCliente ventana;
 
    private DefaultTableModel modeloTorneos;
 
    public PanelTorneosCliente(VentanaCliente ventana) {
        this.ventana = ventana;
 
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
        String[] columnas = {"Código", "Juego", "Día", "Tipo", "Cupos disp.", "Tarifa/Bono"};
        modeloTorneos = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modeloTorneos);
        tabla.setDefaultEditor(Object.class, null);
        tabla.setRowHeight(24);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        add(new JScrollPane(tabla), BorderLayout.CENTER);
 
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
 
        JButton btnInscribirse    = new JButton("Inscribirse");
        JButton btnDesinscribirse = new JButton("Desinscribirse");
 
        btnInscribirse.setBackground(new Color(70, 150, 70));
        btnInscribirse.setForeground(Color.WHITE);
        btnInscribirse.setFocusPainted(false);
        btnInscribirse.setActionCommand(INSCRIBIRSE);
        btnInscribirse.addActionListener(this);
 
        btnDesinscribirse.setBackground(new Color(180, 60, 60));
        btnDesinscribirse.setForeground(Color.WHITE);
        btnDesinscribirse.setFocusPainted(false);
        btnDesinscribirse.setActionCommand(DESINSCRIBIRSE);
        btnDesinscribirse.addActionListener(this);
 
        panelBotones.add(btnInscribirse);
        panelBotones.add(btnDesinscribirse);
        add(panelBotones, BorderLayout.SOUTH);
 
        refrescar();
    }
 
    public void refrescar()
    {
        modeloTorneos.setRowCount(0);
        for (Torneo t : ventana.getTorneos())
        {
            String tarifaBono = (t.getTipo() == TipoTorneo.AMISTOSO) ? "Bono: $" + t.getBonoDescuento() : "Tarifa: $" + t.getTarifaEntrada();
            modeloTorneos.addRow(new Object[]{ t.getCodigo(), t.getJuego().getNombre(), t.getDiaSemana(), t.getTipo(), t.getCuposDisponibles(), tarifaBono
            });
        }
    }
 
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String comando = e.getActionCommand();
        if (comando.equals(INSCRIBIRSE))
        {
            inscribirse();
        }
        else if (comando.equals(DESINSCRIBIRSE))
        {
            desinscribirse();
        }
    }
 
    private void inscribirse()
    {
        String[] codigos = ventana.getCodigosTorneos();
        if (codigos.length == 0)
        {
            JOptionPane.showMessageDialog(ventana, "No hay torneos disponibles."); return;
        }
 
        String selT = (String) JOptionPane.showInputDialog(ventana, "Selecciona el torneo:", "Inscribirse", JOptionPane.PLAIN_MESSAGE, null, codigos, codigos[0]);
        if (selT == null) return;
 
        String cuposStr = JOptionPane.showInputDialog(ventana, "Número de cupos a tomar (1-3):");
        if (cuposStr == null) return;
 
        try
        {
            int cupos = Integer.parseInt(cuposStr.trim());
            String resultado = ventana.inscribirseATorneo(selT, cupos);
            JOptionPane.showMessageDialog(ventana, resultado);
            refrescar();
        }
        catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(ventana, "Número inválido.");
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(ventana, "Error: " + ex.getMessage());
        }
    }
 
    private void desinscribirse()
    {
        String[] codigos = ventana.getCodigosTorneosInscritos();
        if (codigos.length == 0)
        {
            JOptionPane.showMessageDialog(ventana, "No estás inscrito en ningún torneo."); return;
        }
 
        String selT = (String) JOptionPane.showInputDialog(ventana,"Selecciona el torneo:", "Desinscribirse",JOptionPane.PLAIN_MESSAGE, null, codigos, codigos[0]);
        if (selT == null) return;
 
        try
        {
            String resultado = ventana.desinscribirseATorneo(selT);
            JOptionPane.showMessageDialog(ventana, resultado);
            refrescar();
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(ventana, "Error: " + ex.getMessage());
        }
    }
}