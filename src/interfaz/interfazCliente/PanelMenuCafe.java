package interfaz.interfazCliente;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
 
import modelo.ProductoMenu;
 
public class PanelMenuCafe extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
 
    private VentanaCliente ventana;
 
    private DefaultTableModel modeloMenu;
 
    public PanelMenuCafe(VentanaCliente ventana)
    {
        this.ventana = ventana;
 
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
        String[] columnas = {"Nombre", "Precio", "Tipo"};
        modeloMenu = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modeloMenu);
        tabla.setDefaultEditor(Object.class, null);
        tabla.setRowHeight(24);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        add(new JScrollPane(tabla), BorderLayout.CENTER);
 
        refrescar();
    }
 
    public void refrescar()
    {
        modeloMenu.setRowCount(0);
        for (ProductoMenu p : ventana.getProductosMenu())
        {
            String tipo = p.getClass().getSimpleName();
            modeloMenu.addRow(new Object[]{p.getNombre(), "$" + p.getPrecio(), tipo
            });
        }
    }
 
    //No necesita el action performed pero como todos tienen por consistencia lo dejamos
    @Override
    public void actionPerformed(ActionEvent e) {}
}
