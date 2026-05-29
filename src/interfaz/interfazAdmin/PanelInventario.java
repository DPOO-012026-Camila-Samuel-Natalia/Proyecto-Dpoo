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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import modelo.JuegoDeMesa;
 
public class PanelInventario extends JPanel implements ActionListener {
    private static final String AGREGAR_JUEGO   = "agregarJuego";
    private static final String COPIAS_PRESTAMO = "copiasPrestamo";
    private static final String COPIAS_VENTA    = "copiasVenta";
    private static final String MOVER           = "mover";
 
    private VentanaAdministrador ventana;
 
    private DefaultTableModel modeloInventario;
    private JTable tabla;
 
    public PanelInventario(VentanaAdministrador ventana) {
        this.ventana = ventana;
 
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
        String[] columnas = {"Nombre", "Categoría", "Jugadores", "Difícil", "Copias venta", "Copias préstamo", "Precio"};
        modeloInventario = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modeloInventario);
        tabla.setDefaultEditor(Object.class, null);
        tabla.setRowHeight(24);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        add(new JScrollPane(tabla), BorderLayout.CENTER);
 
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
 
        JButton btnAgregar  = new JButton("+ Agregar juego");
        JButton btnCopPrest = new JButton("+ Copias préstamo");
        JButton btnCopVenta = new JButton("+ Copias venta");
        JButton btnMover    = new JButton("Mover venta a préstamo");
 
        btnAgregar.setBackground(new Color(70, 150, 70));   btnAgregar.setForeground(Color.WHITE);
        btnCopPrest.setBackground(new Color(70, 130, 180)); btnCopPrest.setForeground(Color.WHITE);
        btnCopVenta.setBackground(new Color(70, 130, 180)); btnCopVenta.setForeground(Color.WHITE);
        btnMover.setBackground(new Color(150, 100, 50));    btnMover.setForeground(Color.WHITE);
 
        btnAgregar.setFocusPainted(false);  btnAgregar.setActionCommand(AGREGAR_JUEGO);   btnAgregar.addActionListener(this);
        btnCopPrest.setFocusPainted(false); btnCopPrest.setActionCommand(COPIAS_PRESTAMO); btnCopPrest.addActionListener(this);
        btnCopVenta.setFocusPainted(false); btnCopVenta.setActionCommand(COPIAS_VENTA);    btnCopVenta.addActionListener(this);
        btnMover.setFocusPainted(false);    btnMover.setActionCommand(MOVER);              btnMover.addActionListener(this);
 
        panelBotones.add(btnAgregar);
        panelBotones.add(btnCopPrest);
        panelBotones.add(btnCopVenta);
        panelBotones.add(btnMover);
        add(panelBotones, BorderLayout.SOUTH);
 
        refrescar();
    }
 
    public void refrescar()
    {
        modeloInventario.setRowCount(0);
        for (JuegoDeMesa j : ventana.getCatalogoJuegos())
        {
            modeloInventario.addRow(new Object[]{j.getNombre(), j.getCategoria(),j.getMinJugadores() + "-" + j.getMaxJugadores(),
                j.isDificil() ? "Sí" : "No", j.getCopiasVenta(),j.getTotalCopiasPrestamo(),"$" + j.getPrecioUnitario()
            });
        }
    }
 
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String comando = e.getActionCommand();
        if (comando.equals(AGREGAR_JUEGO))
        {
            mostrarDialogoAgregarJuego();
        }
        else if (comando.equals(COPIAS_PRESTAMO))
        {
            agregarCopias(true);
        }
        else if (comando.equals(COPIAS_VENTA))
        {
            agregarCopias(false);
        }
        else if (comando.equals(MOVER))
        {
            moverVentaAPrestamo();
        }
    }
 
    private void mostrarDialogoAgregarJuego()
    {
        JDialog d = new JDialog(ventana, "Agregar juego", true);
        d.setSize(380, 400);
        d.setLocationRelativeTo(ventana);
 
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
 
        JTextField fNombre   = new JTextField(14);
        JComboBox<String> cmbCat = new JComboBox<>(new String[]{"ACCION", "CARTAS", "TABLERO"});
        JTextField fMinJ     = new JTextField("2", 5);
        JTextField fMaxJ     = new JTextField("4", 5);
        JTextField fEdad     = new JTextField("0", 5);
        JCheckBox chkDificil = new JCheckBox("¿Es difícil?");
        JTextField fCopPrest = new JTextField("1", 5);
        JTextField fCopVenta = new JTextField("1", 5);
        JTextField fPrecio   = new JTextField("0", 8);
 
        panel.add(crearFila("Nombre:",          fNombre));
        panel.add(crearFila("Categoría:",        cmbCat));
        panel.add(crearFila("Min. jugadores:",   fMinJ));
        panel.add(crearFila("Max. jugadores:",   fMaxJ));
        panel.add(crearFila("Edad mínima:",      fEdad));
        JPanel filaDificil = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaDificil.add(chkDificil);
        panel.add(filaDificil);
        panel.add(crearFila("Copias préstamo:",  fCopPrest));
        panel.add(crearFila("Copias venta:",     fCopVenta));
        panel.add(crearFila("Precio unitario:",  fPrecio));
        panel.add(Box.createVerticalStrut(6));
 
        JPanel filaBtn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(70, 150, 70));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        filaBtn.add(btnGuardar);
        panel.add(filaBtn);
 
        btnGuardar.addActionListener(ev ->
        {
            try
            {
                String nombre = fNombre.getText().trim();
                if (nombre.isEmpty()) { JOptionPane.showMessageDialog(d, "El nombre es obligatorio."); return; }
                String cat    = (String) cmbCat.getSelectedItem();
                int minJ      = Integer.parseInt(fMinJ.getText().trim());
                int maxJ      = Integer.parseInt(fMaxJ.getText().trim());
                int edad      = Integer.parseInt(fEdad.getText().trim());
                boolean dific = chkDificil.isSelected();
                int copP      = Integer.parseInt(fCopPrest.getText().trim());
                int copV      = Integer.parseInt(fCopVenta.getText().trim());
                double precio = Double.parseDouble(fPrecio.getText().trim());
 
                ventana.agregarJuego(nombre, cat, minJ, maxJ, edad, dific, copP, copV, precio);
                refrescar();
                JOptionPane.showMessageDialog(d, "Juego agregado correctamente.");
                d.dispose();
            }
            catch (NumberFormatException ex)
            {
                JOptionPane.showMessageDialog(d, "Revisa los campos numéricos.");
            }
        });
 
        d.setContentPane(panel);
        d.setVisible(true);
    }
 
    private void agregarCopias(boolean esPrestamo)
    {
        if (ventana.getCatalogoJuegos().isEmpty())
        {
            JOptionPane.showMessageDialog(ventana, "No hay juegos en el catálogo."); return;
        }
 
        String[] nombres = ventana.getNombresJuegos();
        String sel = (String) JOptionPane.showInputDialog(ventana,"Selecciona el juego:", "Agregar copias",JOptionPane.PLAIN_MESSAGE, null, nombres, nombres[0]);
        if (sel == null) return;
 
        String cantStr = JOptionPane.showInputDialog(ventana, "Cantidad a agregar:");
        if (cantStr == null) return;
        try
        {
            int cantidad = Integer.parseInt(cantStr.trim());
            if (cantidad <= 0) { JOptionPane.showMessageDialog(ventana, "Debe ser mayor a 0."); return; }
 
            String resultado = esPrestamo ? ventana.agregarCopiasPrestamo(sel, cantidad): ventana.agregarCopiasVenta(sel, cantidad);
 
            JOptionPane.showMessageDialog(ventana, resultado);
            refrescar();
        }
        catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(ventana, "Número inválido.");
        }
    }
 
    private void moverVentaAPrestamo()
    {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(ventana, "Selecciona un juego de la tabla."); return; }
 
        String nombre = (String) modeloInventario.getValueAt(fila, 0);
        try
        {
            String resultado = ventana.moverVentaAPrestamo(nombre);
            refrescar();
            JOptionPane.showMessageDialog(ventana, resultado);
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(ventana, "Error: " + ex.getMessage());
        }
    }
 
    private JPanel crearFila(String etiqueta, Component componente)
    {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila.add(new JLabel(String.format("%-18s", etiqueta)));
        fila.add(componente);
        return fila;
    }
}
