package vista.gui;

import javax.swing.*;
import java.awt.*;
import controlador.SeguridadController;
import controlador.PersistenciaManager;  // ← Importación correcta

public class LoginFrame extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginFrame() {
        setTitle("🐾 Sistema de Refugio - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTitulo = new JLabel("SISTEMA DE GESTIÓN DE REFUGIO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(lblTitulo, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        txtUsuario = new JTextField(15);
        panel.add(txtUsuario, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        txtPassword = new JPasswordField(15);
        panel.add(txtPassword, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        btnLogin = new JButton("Ingresar");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(e -> login());
        panel.add(btnLogin, gbc);

        add(panel);

        getRootPane().setDefaultButton(btnLogin);
        txtPassword.addActionListener(e -> login());

        setVisible(true);
    }

    private void login() {
        String usuario = txtUsuario.getText();
        String pass = new String(txtPassword.getPassword());

        AppContext ctx = AppContext.getInstance();
        // Cargar datos antes de loguear
        PersistenciaManager.cargarDatos(ctx.mascotas, ctx.adoptantes, ctx.adopciones, ctx.historial);

        if (ctx.seguridad.login(usuario, pass)) {
            new MenuPrincipalFrame();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            txtUsuario.setText("");
            txtPassword.setText("");
            txtUsuario.requestFocus();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}