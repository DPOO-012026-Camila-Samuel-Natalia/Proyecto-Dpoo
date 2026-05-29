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

import modelo.Empleado;
import modelo.Turno;
 
public class PanelTurnos extends JPanel implements ActionListener {
    private static final String CREAR_TURNO      = "crearTurno";
    private static final String ASIGNAR_EMPLEADO = "asignarEmpleado";
 
    private VentanaAdministrador ventana;
 
    private DefaultTableModel modeloTurnos;
 
    public PanelTurnos(VentanaAdministrador ventana)
    {
        this.ventana = ventana;
 
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
        String[] columnas = {"Día de la semana", "Empleados asignados"};
        modeloTurnos = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modeloTurnos);
        tabla.setDefaultEditor(Object.class, null);
        tabla.setRowHeight(24);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        add(new JScrollPane(tabla), BorderLayout.CENTER);
 
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
 
        JButton btnCrear   = new JButton("+ Crear turno");
        JButton btnAsignar = new JButton("Asignar empleado");
 
        btnCrear.setBackground(new Color(70, 150, 70));
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFocusPainted(false);
        btnCrear.setActionCommand(CREAR_TURNO);
        btnCrear.addActionListener(this);
 
        btnAsignar.setBackground(new Color(70, 130, 180));
        btnAsignar.setForeground(Color.WHITE);
        btnAsignar.setFocusPainted(false);
        btnAsignar.setActionCommand(ASIGNAR_EMPLEADO);
        btnAsignar.addActionListener(this);
 
        panelBotones.add(btnCrear);
        panelBotones.add(btnAsignar);
        add(panelBotones, BorderLayout.SOUTH);
 
        refrescar();
    }
 
    public void refrescar(){
        modeloTurnos.setRowCount(0);
        for (Turno t : ventana.getTurnos())
        {
            String empleados = "";
            for (Empleado e : t.getEmpleados())
            {
                if (!empleados.isEmpty()) empleados += ", ";
                empleados += e.getNombre();
            }
            modeloTurnos.addRow(new Object[]{t.getDiaSemana(),empleados.isEmpty() ? "(sin empleados)" : empleados
            });
        }
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        if (comando.equals(CREAR_TURNO))
        {
            crearTurno();
        }
        else if (comando.equals(ASIGNAR_EMPLEADO))
        {
            asignarEmpleado();
        }
    }
 
    private void crearTurno()
    {
        String dia = JOptionPane.showInputDialog(ventana, "Día de la semana:");
        if (dia == null || dia.trim().isEmpty()) return;
        ventana.crearTurno(dia.trim());
        refrescar();
        JOptionPane.showMessageDialog(ventana, "Turno del " + dia + " creado.");
    }
 
    private void asignarEmpleado()
    {
        if (ventana.getEmpleados().isEmpty())
        {
            JOptionPane.showMessageDialog(ventana, "No hay empleados registrados."); return;
        }
        if (ventana.getTurnos().isEmpty())
        {
            JOptionPane.showMessageDialog(ventana, "No hay turnos creados."); return;
        }
 
        // Seleccionar empleado
        String[] opcionesEmp = ventana.getOpcionesEmpleados();
        String selEmp = (String) JOptionPane.showInputDialog(ventana,"Selecciona empleado:", "Asignar turno",JOptionPane.PLAIN_MESSAGE, null, opcionesEmp, opcionesEmp[0]);
        if (selEmp == null) return;
        String loginSeleccionado = selEmp.split(" – ")[0];
 
        // Seleccionar turno
        String[] dias = ventana.getDiasTurnos();
        String dia = (String) JOptionPane.showInputDialog(ventana,"Selecciona turno:", "Asignar turno",JOptionPane.PLAIN_MESSAGE, null, dias, dias[0]);
        if (dia == null) return;
 
        ventana.asignarEmpleadoATurno(loginSeleccionado, dia);
        refrescar();
        JOptionPane.showMessageDialog(ventana, "Empleado asignado al turno del " + dia + ".");
    }
}