package interfaz.interfazAdmin;
 
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
 
public class PanelLogin extends JPanel implements ActionListener {
    // Comando del boton ingresar
    private static final String INGRESAR = "ingresar";
 
    private VentanaAdministrador ventana;
 
    // Campos del formulario
    private JTextField txtLogin;
    private JPasswordField txtPassword;
 
    public PanelLogin(VentanaAdministrador ventana)
    {
        this.ventana = ventana;
 
        setBackground(new Color(30, 30, 45));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(80, 150, 80, 150));
 
        // Titulo
        JLabel titulo = new JLabel("Dulces n Dados", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 30));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        JLabel subtitulo = new JLabel("Acceso Administrador", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 15));
        subtitulo.setForeground(new Color(180, 180, 200));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        // Fila usuario
        JPanel filaLogin = new JPanel(new FlowLayout(FlowLayout.CENTER));
        filaLogin.setBackground(new Color(30, 30, 45));
        JLabel labelLogin = new JLabel("Usuario:     ");
        labelLogin.setForeground(Color.WHITE);
        labelLogin.setFont(new Font("Arial", Font.PLAIN, 14));
        txtLogin = new JTextField(16);
        filaLogin.add(labelLogin);
        filaLogin.add(txtLogin);
 
        // Fila contraseña
        JPanel filaPass = new JPanel(new FlowLayout(FlowLayout.CENTER));
        filaPass.setBackground(new Color(30, 30, 45));
        JLabel labelPass = new JLabel("Contraseña:");
        labelPass.setForeground(Color.WHITE);
        labelPass.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPassword = new JPasswordField(16);
        filaPass.add(labelPass);
        filaPass.add(txtPassword);
 
        // Boton ingresar
        JPanel filaBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        filaBoton.setBackground(new Color(30, 30, 45));
        JButton botonIngresar = new JButton("  Ingresar  ");
        botonIngresar.setBackground(new Color(70, 130, 180));
        botonIngresar.setForeground(Color.BLACK);
        botonIngresar.setFocusPainted(false);
        botonIngresar.setFont(new Font("Arial", Font.BOLD, 14));
        botonIngresar.setActionCommand(INGRESAR);
        botonIngresar.addActionListener(this);
        // Ingresar al presionar Enter en contraseña
        txtPassword.setActionCommand(INGRESAR);
        txtPassword.addActionListener(this);
        filaBoton.add(botonIngresar);
 
        
        add(titulo);
        add(Box.createVerticalStrut(8));
        add(subtitulo);
        add(Box.createVerticalStrut(30));
        add(filaLogin);
        add(Box.createVerticalStrut(8));
        add(filaPass);
        add(Box.createVerticalStrut(16));
        add(filaBoton);
    }
 
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String comando = e.getActionCommand();
        if (comando.equals(INGRESAR))
        {
            String login    = txtLogin.getText().trim();
            String password = new String(txtPassword.getPassword());
 
            boolean autenticado = ventana.autenticar(login, password);
            if (!autenticado)
            {
                JOptionPane.showMessageDialog(ventana,"Login o contraseña incorrectos.","Error", JOptionPane.ERROR_MESSAGE);
                txtPassword.setText("");
            }
        }
    }
}
