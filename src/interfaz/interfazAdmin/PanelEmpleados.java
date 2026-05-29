package interfaz.interfazAdmin;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import modelo.Empleado;
import modelo.Mesero;
 
public class PanelEmpleados extends JPanel implements ActionListener {
    private static final String REGISTRAR = "registrar";
 
    private VentanaAdministrador ventana;
 
    private DefaultTableModel modeloEmpleados;
 
    public PanelEmpleados(VentanaAdministrador ventana)
    {
        this.ventana = ventana;
 
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
        String[] columnas = {"ID", "Nombre", "Login", "Rol", "Código Descuento", "En turno"};
        modeloEmpleados = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modeloEmpleados);
        tabla.setDefaultEditor(Object.class, null);
        tabla.setRowHeight(24);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        add(new JScrollPane(tabla), BorderLayout.CENTER);
 
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRegistrar = new JButton("+ Registrar empleado");
        btnRegistrar.setBackground(new Color(70, 150, 70));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setActionCommand(REGISTRAR);
        btnRegistrar.addActionListener(this);
        panelBotones.add(btnRegistrar);
        add(panelBotones, BorderLayout.SOUTH);
 
        refrescar();
    }
 
    public void refrescar()
    {
        modeloEmpleados.setRowCount(0);
        for (Empleado e : ventana.getEmpleados())
        {
            String rol = (e instanceof Mesero) ? "Mesero" : "Cocinero";
            modeloEmpleados.addRow(new Object[]{
                e.getId(), e.getNombre(), e.getLogin(),
                rol, e.getCodigoDescuento(), e.isEnTurno() ? "Sí" : "No"
            });
        }
    }
 
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String comando = e.getActionCommand();
        if (comando.equals(REGISTRAR))
        {
            mostrarDialogoRegistrar();
        }
    }
 
    private void mostrarDialogoRegistrar()
    {
        JDialog dialogo = new JDialog(ventana, "Registrar Empleado", true);
        dialogo.setSize(360, 280);
        dialogo.setLocationRelativeTo(ventana);
 
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
 
        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Mesero", "Cocinero"});
        JTextField fNombre        = new JTextField(16);
        JTextField fLogin         = new JTextField(16);
        JPasswordField fPass      = new JPasswordField(16);
 
        panel.add(crearFila("Tipo:",        cmbTipo));
        panel.add(crearFila("Nombre:",      fNombre));
        panel.add(crearFila("Login:",       fLogin));
        panel.add(crearFila("Contraseña:",  fPass));
        panel.add(Box.createVerticalStrut(8));
 
        JPanel filaBtn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(70, 150, 70));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        filaBtn.add(btnGuardar);
        panel.add(filaBtn);
 
        btnGuardar.addActionListener(ev ->
        {
            String nombre   = fNombre.getText().trim();
            String login    = fLogin.getText().trim();
            String password = new String(fPass.getPassword());
 
            if (nombre.isEmpty() || login.isEmpty() || password.isEmpty())
            {
                JOptionPane.showMessageDialog(dialogo, "Todos los campos son obligatorios.");
                return;
            }
            if (ventana.loginEmpleadoExiste(login))
            {
                JOptionPane.showMessageDialog(dialogo, "Ese login ya está en uso.");
                return;
            }
 
            String tipo = (String) cmbTipo.getSelectedItem();
            String codigo = ventana.registrarEmpleado(tipo, nombre, login, password);
            refrescar();
            JOptionPane.showMessageDialog(dialogo,"Empleado registrado.\nCódigo de descuento: " + codigo);
            dialogo.dispose();
        });
 
        dialogo.setContentPane(panel);
        dialogo.setVisible(true);
    }
 
    private JPanel crearFila(String etiqueta, java.awt.Component componente)
    {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila.add(new JLabel(String.format("%-14s", etiqueta)));
        fila.add(componente);
        return fila;
    }
}