package vista.gui;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import controlador.MascotaController;
import modelo.Mascota;

public class MascotaDialog extends JDialog {
    private JTextField txtId, txtNombre, txtEspecie, txtRaza;
    private JDateChooser dateChooser;
    private JComboBox<String> cmbEstado;
    private boolean confirmado = false;
    private Mascota mascotaEdit;
    private MascotaController controller = AppContext.getInstance().mascotas;

    public MascotaDialog(Frame parent, Mascota mascota) {
        super(parent, mascota == null ? "Registrar Mascota" : "Editar Mascota", true);
        this.mascotaEdit = mascota;
        setSize(450, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(20);
        if (mascota != null) {
            txtId.setText(mascota.getIdMascota());
            txtId.setEnabled(false);
        }
        panel.add(txtId, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        if (mascota != null) txtNombre.setText(mascota.getNombre());
        panel.add(txtNombre, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Especie:"), gbc);
        gbc.gridx = 1;
        txtEspecie = new JTextField(20);
        if (mascota != null) txtEspecie.setText(mascota.getEspecie());
        panel.add(txtEspecie, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Raza:"), gbc);
        gbc.gridx = 1;
        txtRaza = new JTextField(20);
        if (mascota != null) txtRaza.setText(mascota.getRaza());
        panel.add(txtRaza, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Fecha Rescate:"), gbc);
        gbc.gridx = 1;
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setPreferredSize(new Dimension(180, 25));
        if (mascota != null && mascota.getFechaRescate() != null) {
            dateChooser.setDate(Date.from(mascota.getFechaRescate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        panel.add(dateChooser, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        cmbEstado = new JComboBox<>(new String[]{"Disponible", "En cuarentena", "En tratamiento", "Adoptada"});
        if (mascota != null) cmbEstado.setSelectedItem(mascota.getEstado());
        panel.add(cmbEstado, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        panel.add(panelBotones, gbc);

        add(panel, BorderLayout.CENTER);
    }

    private void guardar() {
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String especie = txtEspecie.getText().trim();
        String raza = txtRaza.getText().trim();
        Date fechaDate = dateChooser.getDate();

        if (id.isEmpty() || nombre.isEmpty() || especie.isEmpty() || fechaDate == null) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios.");
            return;
        }

        LocalDate fecha = fechaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (mascotaEdit == null) {
            if (controller.registrarMascota(id, nombre, especie, raza, fecha)) {
                confirmado = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "ID ya registrado.");
            }
        } else {
            controller.modificarMascota(id, nombre, especie, raza);
            String estado = (String) cmbEstado.getSelectedItem();
            controller.actualizarEstado(id, estado);
            confirmado = true;
            dispose();
        }
    }

    public boolean isConfirmado() {
        return confirmado;
    }
}