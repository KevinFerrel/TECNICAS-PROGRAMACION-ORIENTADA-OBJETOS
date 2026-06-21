package vista.gui;

import javax.swing.*;
import java.awt.*;
import persistencia.PersistenciaManager;
import com.toedter.calendar.JDateChooser; // Importación correcta

public class MenuPrincipalFrame extends JFrame {
    private JTabbedPane tabbedPane;

    public MenuPrincipalFrame() {
        setTitle("🐾 Sistema de Gestión de Refugio de Mascotas");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        // Si alguna clase interna usa JDateChooser, se cargará en el momento de crear la pestaña.
        tabbedPane.addTab("🐾 Mascotas", new MascotasPanel());
        tabbedPane.addTab("👤 Adoptantes", new AdoptantesPanel());
        tabbedPane.addTab("📋 Adopciones", new AdopcionesPanel());
        tabbedPane.addTab("🏥 Historial Médico", new HistorialPanel());

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("💾 Guardar Datos");
        JButton btnSalir = new JButton("🚪 Salir");

        btnGuardar.addActionListener(e -> guardarDatos());
        btnSalir.addActionListener(e -> {
            guardarDatos();
            System.exit(0);
        });

        panelInferior.add(btnGuardar);
        panelInferior.add(btnSalir);

        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        mostrarRecordatorios();
        setVisible(true);
    }

    private void guardarDatos() {
        AppContext ctx = AppContext.getInstance();
        PersistenciaManager.guardarDatos(ctx.mascotas, ctx.adoptantes, ctx.adopciones, ctx.historial);
        JOptionPane.showMessageDialog(this, "Datos guardados correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarRecordatorios() {
        // Implementación opcional
    }
}