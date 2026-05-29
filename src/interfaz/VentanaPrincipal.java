package interfaz;

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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
 
import interfaz.interfazAdmin.VentanaAdministrador;
import interfaz.interfazCliente.VentanaCliente; 

public class VentanaPrincipal extends JFrame implements ActionListener{
    
 
    // Comandos de los botones
    private static final String ADMIN    = "admin";
    private static final String EMPLEADO = "empleado";
    private static final String CLIENTE  = "cliente";
 
    public VentanaPrincipal()
    {
        setTitle("Dulces n Dados");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 400);
        setLocationRelativeTo(null);
        setResizable(false);
 
        // Panel principal con fondo oscuro
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(30, 30, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80));
 
        // Titulo
        JLabel titulo = new JLabel("Dulces n Dados", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        JLabel subtitulo = new JLabel("¿Cómo deseas ingresar?", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(180, 180, 200));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        // Botones
        JButton btnAdmin    = crearBoton("Administrador", ADMIN,    new Color(70, 130, 180));
        JButton btnEmpleado = crearBoton("Empleado",      EMPLEADO, new Color(70, 150, 70));
        JButton btnCliente  = crearBoton("Cliente",       CLIENTE,  new Color(150, 100, 180));
 
        // Apilar 
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(8));
        panel.add(subtitulo);
        panel.add(Box.createVerticalStrut(40));
        panel.add(crearFilaBoton(btnAdmin));
        panel.add(Box.createVerticalStrut(12));
        panel.add(crearFilaBoton(btnEmpleado));
        panel.add(Box.createVerticalStrut(12));
        panel.add(crearFilaBoton(btnCliente));
 
        setContentPane(panel);
        setVisible(true);
    }
 
    // Crea un boton
    private JButton crearBoton(String texto, String comando, Color color)
    {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setActionCommand(comando);
        btn.addActionListener(this);
        return btn;
    }
 
    //centrar boton 
    private JPanel crearFilaBoton(JButton btn)
    {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.CENTER));
        fila.setBackground(new Color(30, 30, 45));
        fila.add(btn);
        return fila;
    }
 
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String comando = e.getActionCommand();
        if (comando.equals(ADMIN))
        {
            new VentanaAdministrador();
        }
        else if (comando.equals(EMPLEADO))
        {
            //new VentanaEmpleado(); 
        }
        else if (comando.equals(CLIENTE))
        {
            new VentanaCliente();  
        }
    }
 
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(VentanaPrincipal::new);
    }
}
