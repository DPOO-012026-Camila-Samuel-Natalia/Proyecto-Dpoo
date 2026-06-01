package interfaz.interfazCliente;

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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

 
public class PanelLoginCliente extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
 
    private static final String INGRESAR    = "ingresar";
    private static final String REGISTRARSE = "registrarse";
 
    private VentanaCliente ventana;
 
    private JTextField    txtLogin;
    private JPasswordField txtPassword;
 
    public PanelLoginCliente(VentanaCliente ventana) {
        this.ventana = ventana;
 
        setBackground(new Color(30, 30, 45));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(60, 150, 60, 150));
 
        
        JLabel titulo = new JLabel("Dulces n Dados", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        JLabel subtitulo = new JLabel("Acceso Cliente", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(180, 180, 200));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        
        JPanel filaLogin = new JPanel(new FlowLayout(FlowLayout.CENTER));
        filaLogin.setBackground(new Color(30, 30, 45));
        JLabel lblLogin = new JLabel("Usuario:     ");
        lblLogin.setForeground(Color.WHITE);
        lblLogin.setFont(new Font("Arial", Font.PLAIN, 14));
        txtLogin = new JTextField(16);
        filaLogin.add(lblLogin);
        filaLogin.add(txtLogin);
 
        
        JPanel filaPass = new JPanel(new FlowLayout(FlowLayout.CENTER));
        filaPass.setBackground(new Color(30, 30, 45));
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setForeground(Color.WHITE);
        lblPass.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPassword = new JPasswordField(16);
        filaPass.add(lblPass);
        filaPass.add(txtPassword);
 
        // Botones
        JPanel filaBtn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        filaBtn.setBackground(new Color(30, 30, 45));
 
        JButton btnIngresar    = new JButton("  Ingresar  ");
        JButton btnRegistrarse = new JButton("  Registrarse  ");
 
        btnIngresar.setBackground(new Color(70, 130, 180));
        btnIngresar.setForeground(Color.BLACK);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setFont(new Font("Arial", Font.BOLD, 13));
        btnIngresar.setActionCommand(INGRESAR);
        btnIngresar.addActionListener(this);
        txtPassword.setActionCommand(INGRESAR);
        txtPassword.addActionListener(this);
 
        btnRegistrarse.setBackground(new Color(70, 150, 70));
        btnRegistrarse.setForeground(Color.BLACK);
        btnRegistrarse.setFocusPainted(false);
        btnRegistrarse.setFont(new Font("Arial", Font.BOLD, 13));
        btnRegistrarse.setActionCommand(REGISTRARSE);
        btnRegistrarse.addActionListener(this);
 
        filaBtn.add(btnIngresar);
        filaBtn.add(btnRegistrarse);
 
        // Apilar 
        add(titulo);
        add(Box.createVerticalStrut(8));
        add(subtitulo);
        add(Box.createVerticalStrut(30));
        add(filaLogin);
        add(Box.createVerticalStrut(8));
        add(filaPass);
        add(Box.createVerticalStrut(16));
        add(filaBtn);
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
 
        if (comando.equals(INGRESAR))
        {
            String login    = txtLogin.getText().trim();
            String password = new String(txtPassword.getPassword());
 
            if (login.isEmpty() || password.isEmpty())
            {
                JOptionPane.showMessageDialog(ventana, "Completa todos los campos.");
                return;
            }
            boolean autenticado = ventana.autenticar(login, password);
            if (!autenticado)
            {
                JOptionPane.showMessageDialog(ventana,"Login o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
                txtPassword.setText("");
            }
        }
        else if (comando.equals(REGISTRARSE))
        {
            mostrarDialogoRegistro();
        }
    }
 
    private void mostrarDialogoRegistro()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
 
        JTextField fNombre   = new JTextField(16);
        JTextField fLogin    = new JTextField(16);
        JPasswordField fPass = new JPasswordField(16);
 
        panel.add(crearFila("Nombre completo:", fNombre));
        panel.add(Box.createVerticalStrut(4));
        panel.add(crearFila("Login:",           fLogin));
        panel.add(Box.createVerticalStrut(4));
        panel.add(crearFila("Contraseña:",      fPass));
 
        int resultado = JOptionPane.showConfirmDialog(ventana, panel,"Registro de nuevo cliente", JOptionPane.OK_CANCEL_OPTION);
 
        if (resultado != JOptionPane.OK_OPTION) return;
 
        String nombre   = fNombre.getText().trim();
        String login    = fLogin.getText().trim();
        String password = new String(fPass.getPassword());
 
        if (nombre.isEmpty() || login.isEmpty() || password.isEmpty())
        {
            JOptionPane.showMessageDialog(ventana, "Todos los campos son obligatorios.");
            return;
        }
        if (ventana.loginExiste(login))
        {
            JOptionPane.showMessageDialog(ventana, "Ese login ya está en uso.");
            return;
        }
 
        ventana.registrar(nombre, login, password);
        JOptionPane.showMessageDialog(ventana, "¡Bienvenido, " + nombre + "!");
    }
 
    private JPanel crearFila(String etiqueta, java.awt.Component componente)
    {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila.add(new JLabel(String.format("%-20s", etiqueta)));
        fila.add(componente);
        return fila;
    }
}
