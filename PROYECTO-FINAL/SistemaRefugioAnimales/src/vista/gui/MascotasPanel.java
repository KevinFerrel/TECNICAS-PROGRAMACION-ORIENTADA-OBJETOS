package vista.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import controlador.MascotaController;
import modelo.Mascota;

public class MascotasPanel extends JPanel {
    private MascotaController controller = AppContext.getInstance().mascotas;
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public MascotasPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRegistrar = new JButton("➕ Registrar");
        JButton btnEditar = new JButton("✏️ Editar");
        JButton btnEliminar = new JButton("🗑️ Dar de Baja");
        JButton btnActualizar = new JButton("🔄 Actualizar");
        JButton btnDisponibles = new JButton("🐾 Solo Disponibles");

        btnRegistrar.addActionListener(e -> abrirFormularioMascota(null));
        btnEditar.addActionListener(e -> editarMascota());
        btnEliminar.addActionListener(e -> eliminarMascota());
        btnActualizar.addActionListener(e -> cargarDatos());
        btnDisponibles.addActionListener(e -> cargarDisponibles());

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnDisponibles);

        String[] columnas = {"ID", "Nombre", "Especie", "Raza", "Estado", "Fecha Rescate"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(25);
        tabla.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tabla);

        add(panelBotones, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        cargarDatos();
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        for (Mascota m : controller.listarTodas()) {
            agregarFila(m);
        }
    }

    private void cargarDisponibles() {
        modeloTabla.setRowCount(0);
        for (Mascota m : controller.listarDisponibles()) {
            agregarFila(m);
        }
    }

    private void agregarFila(Mascota m) {
        modeloTabla.addRow(new Object[]{
            m.getIdMascota(),
            m.getNombre(),
            m.getEspecie(),
            m.getRaza(),
            m.getEstado(),
            m.getFechaRescate() != null ? m.getFechaRescate().format(formatter) : "N/A"
        });
    }

    private void abrirFormularioMascota(Mascota mascota) {
        MascotaDialog dialog = new MascotaDialog((Frame) SwingUtilities.getWindowAncestor(this), mascota);
        dialog.setVisible(true);
        if (dialog.isConfirmado()) {
            cargarDatos();
        }
    }

    private void editarMascota() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una mascota.");
            return;
        }
        String id = (String) modeloTabla.getValueAt(fila, 0);
        Mascota m = controller.buscarPorId(id);
        if (m != null) {
            abrirFormularioMascota(m);
        }
    }

    private void eliminarMascota() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una mascota.");
            return;
        }
        String id = (String) modeloTabla.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Dar de baja por fallecimiento?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.darDeBajaPorFallecimiento(id);
            cargarDatos();
            JOptionPane.showMessageDialog(this, "Mascota dada de baja.");
        }
    }
}