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
 
import modelo.JuegoDeMesa;
 
public class PanelJuegosCliente extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
 
    private static final String COMPRAR = "comprar";
 
    private VentanaCliente ventana;
 
    private DefaultTableModel modeloJuegos;
 
    public PanelJuegosCliente(VentanaCliente ventana){
        this.ventana = ventana;
 
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
        String[] columnas = {"Nombre", "Categoría", "Jugadores", "Precio", "Copias disponibles"};
        modeloJuegos = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modeloJuegos);
        tabla.setDefaultEditor(Object.class, null);
        tabla.setRowHeight(24);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        add(new JScrollPane(tabla), BorderLayout.CENTER);
 
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnComprar = new JButton("Comprar juego");
        btnComprar.setBackground(new Color(70, 130, 180));
        btnComprar.setForeground(Color.WHITE);
        btnComprar.setFocusPainted(false);
        btnComprar.setActionCommand(COMPRAR);
        btnComprar.addActionListener(this);
        panelBotones.add(btnComprar);
        add(panelBotones, BorderLayout.SOUTH);
 
        refrescar();
    }
 
    public void refrescar() {
        modeloJuegos.setRowCount(0);
        for (JuegoDeMesa j : ventana.getJuegosDisponiblesVenta())
        {
            modeloJuegos.addRow(new Object[]{j.getNombre(), j.getCategoria(),j.getMinJugadores() + "-" + j.getMaxJugadores(),
                "$" + j.getPrecioUnitario(),j.getCopiasVenta()
            });
        }
    }
 
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getActionCommand().equals(COMPRAR))
        {
            comprarJuego();
        }
    }
 
    private void comprarJuego() {
        String[] nombres = ventana.getNombresJuegosVenta();
        if (nombres.length == 0)
        {
            JOptionPane.showMessageDialog(ventana, "No hay juegos disponibles para comprar.");
            return;
        }
 
        // Seleccionar juego
        String selJuego = (String) JOptionPane.showInputDialog(ventana,"Selecciona el juego:", "Comprar juego", JOptionPane.PLAIN_MESSAGE, null, nombres, nombres[0]);
        if (selJuego == null) return;
 
        // Ingresar cantidad
        String cantStr = JOptionPane.showInputDialog(ventana, "Cantidad:");
        if (cantStr == null) return;
 
        try
        {
            int cantidad = Integer.parseInt(cantStr.trim());
            if (cantidad <= 0) { JOptionPane.showMessageDialog(ventana, "Debe ser mayor a 0."); return; }
 
            String resultado = ventana.comprarJuego(selJuego, cantidad);
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
}