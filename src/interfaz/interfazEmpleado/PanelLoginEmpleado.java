// =========================
// PanelLoginEmpleado.java
// =========================

package interfaz.interfazEmpleado;

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

public class PanelLoginEmpleado extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final String INGRESAR = "ingresar";

    private VentanaEmpleado ventana;

    private JTextField txtLogin;

    private JPasswordField txtPassword;

    public PanelLoginEmpleado(VentanaEmpleado ventana) {

        this.ventana = ventana;

        setBackground(new Color(30, 30, 45));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        setBorder(BorderFactory.createEmptyBorder(60, 150, 60, 150));

        JLabel titulo =
                new JLabel("Dulces n Dados", SwingConstants.CENTER);

        titulo.setFont(new Font("Arial", Font.BOLD, 28));

        titulo.setForeground(Color.WHITE);

        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo =
                new JLabel("Acceso Empleado", SwingConstants.CENTER);

        subtitulo.setForeground(new Color(180, 180, 200));

        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel filaLogin =
                new JPanel(new FlowLayout(FlowLayout.CENTER));

        filaLogin.setBackground(new Color(30, 30, 45));

        JLabel lblLogin = new JLabel("Usuario:");

        lblLogin.setForeground(Color.WHITE);

        txtLogin = new JTextField(16);

        filaLogin.add(lblLogin);

        filaLogin.add(txtLogin);

        JPanel filaPass =
                new JPanel(new FlowLayout(FlowLayout.CENTER));

        filaPass.setBackground(new Color(30, 30, 45));

        JLabel lblPass = new JLabel("Contraseña:");

        lblPass.setForeground(Color.WHITE);

        txtPassword = new JPasswordField(16);

        filaPass.add(lblPass);

        filaPass.add(txtPassword);

        JButton btnIngresar = new JButton("Ingresar");

        btnIngresar.setBackground(new Color(70, 130, 180));

        btnIngresar.setForeground(Color.WHITE);

        btnIngresar.setFocusPainted(false);

        btnIngresar.setActionCommand(INGRESAR);

        btnIngresar.addActionListener(this);

        add(titulo);

        add(Box.createVerticalStrut(10));

        add(subtitulo);

        add(Box.createVerticalStrut(30));

        add(filaLogin);

        add(Box.createVerticalStrut(10));

        add(filaPass);

        add(Box.createVerticalStrut(20));

        JPanel panelBtn =
                new JPanel(new FlowLayout(FlowLayout.CENTER));

        panelBtn.setBackground(new Color(30, 30, 45));

        panelBtn.add(btnIngresar);

        add(panelBtn);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String login = txtLogin.getText().trim();

        String password =
                new String(txtPassword.getPassword());

        boolean autenticado =
                ventana.autenticar(login, password);

        if (!autenticado) {

            JOptionPane.showMessageDialog(
                    ventana,
                    "Login o contraseña incorrectos."
            );

            txtPassword.setText("");
        }
    }
}