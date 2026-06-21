package vista.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import controlador.AdoptanteController;
import modelo.Adoptante;

public class AdoptantesPanel extends JPanel {
    private AdoptanteController controller = AppContext.getInstance().adoptantes;
    private DefaultTableModel modeloTabla;
    private JTable tabla;

    public AdoptantesPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRegistrar = new JButton("➕ Registrar");
        JButton btnEvaluar = new JButton("📝 Evaluar");
        JButton btnActualizar = new JButton("🔄 Actualizar");

        btnRegistrar.addActionListener(e -> registrarAdoptante());
        btnEvaluar.addActionListener(e -> evaluarAdoptante());
        btnActualizar.addActionListener(e -> cargarDatos());

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnEvaluar);
        panelBotones.add(btnActualizar);

        String[] columnas = {"DNI", "Nombre", "Edad", "Teléfono", "Email", "Estado", "Motivo Rechazo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(25);
        JScrollPane scroll = new JScrollPane(tabla);

        add(panelBotones, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        cargarDatos();
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        for (Adoptante a : controller.obtenerTodos()) {
            modeloTabla.addRow(new Object[]{
                a.getDni(),
                a.getNombre(),
                a.getEdad(),
                a.getTelefono(),
                a.getEmail(),
                a.getEstadoValidacion(),
                a.getMotivoRechazo() != null ? a.getMotivoRechazo() : ""
            });
        }
    }

    private void registrarAdoptante() {
        JTextField txtDni = new JTextField(10);
        JTextField txtNombre = new JTextField(20);
        JTextField txtEdad = new JTextField(5);
        JTextField txtTelefono = new JTextField(12);
        JTextField txtEmail = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.add(new JLabel("DNI:")); panel.add(txtDni);
        panel.add(new JLabel("Nombre:")); panel.add(txtNombre);
        panel.add(new JLabel("Edad:")); panel.add(txtEdad);
        panel.add(new JLabel("Teléfono:")); panel.add(txtTelefono);
        panel.add(new JLabel("Email:")); panel.add(txtEmail);

        int result = JOptionPane.showConfirmDialog(this, panel, "Registrar Adoptante", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String dni = txtDni.getText().trim();
                String nombre = txtNombre.getText().trim();
                int edad = Integer.parseInt(txtEdad.getText().trim());
                String telefono = txtTelefono.getText().trim();
                String email = txtEmail.getText().trim();
                controller.registrarAdoptante(dni, nombre, edad, telefono, email);
                cargarDatos();
                JOptionPane.showMessageDialog(this, "✔ Adoptante registrado.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Edad debe ser un número.");
            }
        }
    }

    private void evaluarAdoptante() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un adoptante.");
            return;
        }
        String dni = (String) modeloTabla.getValueAt(fila, 0);
        Adoptante a = controller.buscarPorDni(dni);
        if (a == null) {
            JOptionPane.showMessageDialog(this, "Adoptante no encontrado.");
            return;
        }

        String[] opciones = {"Aprobado", "Rechazado"};
        String estado = (String) JOptionPane.showInputDialog(this,
            "Estado actual: " + a.getEstadoValidacion() + "\nSeleccione nuevo estado:",
            "Evaluar Adoptante", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (estado == null) return;

        String motivo = "";
        if (estado.equals("Rechazado")) {
            motivo = JOptionPane.showInputDialog("Motivo del rechazo (obligatorio):");
            if (motivo == null || motivo.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El motivo es obligatorio.");
                return;
            }
        }
        controller.evaluarAdoptante(dni, estado, motivo);
        cargarDatos();
        JOptionPane.showMessageDialog(this, "✔ Estado actualizado.");
    }
}