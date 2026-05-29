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
 
public class PanelFavoritos extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
 
    private static final String AGREGAR = "agregar";
 
    private VentanaCliente ventana;
 
    private DefaultTableModel modeloFavoritos;
 
    public PanelFavoritos(VentanaCliente ventana)
    {
        this.ventana = ventana;
 
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
        String[] columnas = {"Nombre", "Categoría", "Jugadores"};
        modeloFavoritos = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modeloFavoritos);
        tabla.setDefaultEditor(Object.class, null);
        tabla.setRowHeight(24);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        add(new JScrollPane(tabla), BorderLayout.CENTER);
 
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAgregar = new JButton("+ Agregar favorito");
        btnAgregar.setBackground(new Color(150, 100, 180));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setActionCommand(AGREGAR);
        btnAgregar.addActionListener(this);
        panelBotones.add(btnAgregar);
        add(panelBotones, BorderLayout.SOUTH);
 
        refrescar();
    }
 
    public void refrescar()
    {
        modeloFavoritos.setRowCount(0);
        for (JuegoDeMesa j : ventana.getJuegosFavoritos())
        {
            modeloFavoritos.addRow(new Object[]{
                j.getNombre(), j.getCategoria(),
                j.getMinJugadores() + "-" + j.getMaxJugadores()
            });
        }
    }
 
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals(AGREGAR))
        {
            agregarFavorito();
        }
    }
 
    private void agregarFavorito()
    {
        String[] nombres = ventana.getNombresCatalogoJuegos();
        if (nombres.length == 0)
        {
            JOptionPane.showMessageDialog(ventana, "No hay juegos en el catálogo."); return;
        }
 
        String sel = (String) JOptionPane.showInputDialog(ventana, "Selecciona el juego:", "Agregar favorito", JOptionPane.PLAIN_MESSAGE, null, nombres, nombres[0]);
        if (sel == null) return;
 
        String resultado = ventana.agregarFavorito(sel);
        JOptionPane.showMessageDialog(ventana, resultado);
        refrescar();
    }
}