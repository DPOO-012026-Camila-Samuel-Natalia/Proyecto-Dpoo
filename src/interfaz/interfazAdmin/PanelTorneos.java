package interfaz.interfazAdmin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import modelo.TipoTorneo;
import modelo.Torneo;
 
public class PanelTorneos extends JPanel implements ActionListener {
    private static final String CREAR     = "crear";
    private static final String INSCRIBIR = "inscribir";
    private static final String PREMIO    = "premio";
 
    private VentanaAdministrador ventana;
 
    private DefaultTableModel modeloTorneos;
 
    public PanelTorneos(VentanaAdministrador ventana) {
        this.ventana = ventana;
 
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
        String[] columnas = {"Código", "Juego", "Día", "Tipo", "Cupos ocup./máx.", "Tarifa/Bono"};
        modeloTorneos = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modeloTorneos);
        tabla.setDefaultEditor(Object.class, null);
        tabla.setRowHeight(24);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        add(new JScrollPane(tabla), BorderLayout.CENTER);
 
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
 
        JButton btnCrear     = new JButton("+ Crear torneo");
        JButton btnInscribir = new JButton("Inscribir empleado");
        JButton btnPremio    = new JButton("Entregar premio");
 
        btnCrear.setBackground(new Color(70, 150, 70));      btnCrear.setForeground(Color.WHITE);
        btnInscribir.setBackground(new Color(70, 130, 180)); btnInscribir.setForeground(Color.WHITE);
        btnPremio.setBackground(new Color(180, 140, 0));     btnPremio.setForeground(Color.WHITE);
 
        btnCrear.setFocusPainted(false);     btnCrear.setActionCommand(CREAR);         btnCrear.addActionListener(this);
        btnInscribir.setFocusPainted(false); btnInscribir.setActionCommand(INSCRIBIR); btnInscribir.addActionListener(this);
        btnPremio.setFocusPainted(false);    btnPremio.setActionCommand(PREMIO);       btnPremio.addActionListener(this);
 
        panelBotones.add(btnCrear);
        panelBotones.add(btnInscribir);
        panelBotones.add(btnPremio);
        add(panelBotones, BorderLayout.SOUTH);
 
        refrescar();
    }
 
    public void refrescar()
    {
        modeloTorneos.setRowCount(0);
        for (Torneo t : ventana.getTorneos())
        {
            String tarifaBono = (t.getTipo() == TipoTorneo.AMISTOSO) ? "Bono: $" + t.getBonoDescuento() : "Tarifa: $" + t.getTarifaEntrada();
            modeloTorneos.addRow(new Object[]{ t.getCodigo(), t.getJuego().getNombre(), t.getDiaSemana(),
            		t.getTipo(), t.getCuposOcupados() + "/" + t.getMaxParticipantes(),tarifaBono
            });
        }
    }
 
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String comando = e.getActionCommand();
        if (comando.equals(CREAR))
        {
            mostrarDialogoCrearTorneo();
        }
        else if (comando.equals(INSCRIBIR))
        {
            inscribirEmpleado();
        }
        else if (comando.equals(PREMIO))
        {
            entregarPremio();
        }
    }
 
    private void mostrarDialogoCrearTorneo()
    {
        if (ventana.getCatalogoJuegos().isEmpty())
        {
            JOptionPane.showMessageDialog(ventana, "No hay juegos en el catálogo."); return;
        }
 
        JDialog d = new JDialog(ventana, "Crear Torneo", true);
        d.setSize(360, 310);
        d.setLocationRelativeTo(ventana);
 
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
 
        JComboBox<String> cmbJuego = new JComboBox<>(ventana.getNombresJuegos());
        JTextField fDia   = new JTextField(12);
        JTextField fMaxP  = new JTextField("8", 5);
        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"AMISTOSO", "COMPETITIVO"});
        JTextField fMonto = new JTextField("0", 8);
        JLabel lblMonto   = new JLabel("Bono al ganador ($):");
 
        cmbTipo.addActionListener(ev ->lblMonto.setText(cmbTipo.getSelectedItem().equals("COMPETITIVO") ? "Tarifa de entrada ($):" : "Bono al ganador ($):")
        );
 
        panel.add(crearFila("Juego:",              cmbJuego));
        panel.add(crearFila("Día de la semana:",   fDia));
        panel.add(crearFila("Máx. participantes:", fMaxP));
        panel.add(crearFila("Tipo:",               cmbTipo));
 
        JPanel filaMonto = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaMonto.add(lblMonto);
        filaMonto.add(fMonto);
        panel.add(filaMonto);
        panel.add(Box.createVerticalStrut(6));
 
        JPanel filaBtn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnCrear = new JButton("Crear");
        btnCrear.setBackground(new Color(70, 150, 70));
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFocusPainted(false);
        filaBtn.add(btnCrear);
        panel.add(filaBtn);
 
        btnCrear.addActionListener(ev ->
        {
            try
            {
                String nombreJuego = (String) cmbJuego.getSelectedItem();
                String dia  = fDia.getText().trim();
                int maxP    = Integer.parseInt(fMaxP.getText().trim());
                TipoTorneo tipo = cmbTipo.getSelectedItem().equals("AMISTOSO")
                        ? TipoTorneo.AMISTOSO : TipoTorneo.COMPETITIVO;
                double monto  = Double.parseDouble(fMonto.getText().trim());
                double tarifa = tipo == TipoTorneo.COMPETITIVO ? monto : 0;
                double bono   = tipo == TipoTorneo.AMISTOSO    ? monto : 0;
 
                String codigo = ventana.crearTorneo(nombreJuego, dia, maxP, tipo, tarifa, bono);
                refrescar();
                JOptionPane.showMessageDialog(d, "Torneo creado: " + codigo);
                d.dispose();
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(d, "Error: " + ex.getMessage());
            }
        });
 
        d.setContentPane(panel);
        d.setVisible(true);
    }
 
    private void inscribirEmpleado()
    {
        if (ventana.getEmpleados().isEmpty() || ventana.getTorneos().isEmpty())
        {
            JOptionPane.showMessageDialog(ventana, "Se necesitan empleados y torneos registrados."); return;
        }
 
        String[] opcionesEmp = ventana.getOpcionesEmpleados();
        String selEmp = (String) JOptionPane.showInputDialog(ventana, "Empleado:", "Inscribir a torneo", JOptionPane.PLAIN_MESSAGE, null, opcionesEmp, opcionesEmp[0]);
        if (selEmp == null) return;
        String loginEmp = selEmp.split(" – ")[0];
 
        String[] codigos = ventana.getCodigosTorneos();
        String selT = (String) JOptionPane.showInputDialog(ventana,"Torneo:", "Inscribir a torneo",JOptionPane.PLAIN_MESSAGE, null, codigos, codigos[0]);
        if (selT == null) return;
 
        String cuposStr = JOptionPane.showInputDialog(ventana, "Número de cupos (1-3):");
        if (cuposStr == null) return;
        try
        {
            int cupos = Integer.parseInt(cuposStr.trim());
            ventana.inscribirEmpleadoATorneo(loginEmp, selT, cupos);
            refrescar();
            JOptionPane.showMessageDialog(ventana, "Empleado inscrito correctamente.");
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(ventana, "Error: " + ex.getMessage());
        }
    }
 
    private void entregarPremio()
    {
        String[] codigos = ventana.getCodigosTorneosAmistosos();
        if (codigos.length == 0)
        {
            JOptionPane.showMessageDialog(ventana, "No hay torneos amistosos."); return;
        }
 
        String selT = (String) JOptionPane.showInputDialog(ventana, "Torneo amistoso:", "Entregar premio",JOptionPane.PLAIN_MESSAGE, null, codigos, codigos[0]);
        if (selT == null) return;
 
        String loginG = JOptionPane.showInputDialog(ventana, "Login del cliente ganador:");
        if (loginG == null) return;
 
        try
        {
            String nombreGanador = ventana.entregarPremio(selT, loginG.trim());
            if (nombreGanador == null)
            {
                JOptionPane.showMessageDialog(ventana, "Cliente no encontrado."); return;
            }
            JOptionPane.showMessageDialog(ventana, "Premio entregado a " + nombreGanador + ".");
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(ventana, "Error: " + ex.getMessage());
        }
    }
 
    private JPanel crearFila(String etiqueta, Component componente)
    {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila.add(new JLabel(String.format("%-22s", etiqueta)));
        fila.add(componente);
        return fila;
    }
}
