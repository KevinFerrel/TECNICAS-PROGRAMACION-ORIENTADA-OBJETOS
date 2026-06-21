package vista.gui;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Desktop;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import controlador.AdopcionController;
import controlador.AdoptanteController;
import controlador.MascotaController;
import modelo.Adopcion;
import modelo.Adoptante;
import modelo.Mascota;

public class AdopcionesPanel extends JPanel {
    private AdopcionController adopcionController = AppContext.getInstance().adopciones;
    private AdoptanteController adoptanteController = AppContext.getInstance().adoptantes;
    private MascotaController mascotaController = AppContext.getInstance().mascotas;

    private DefaultTableModel modeloSolicitudes;
    private JTable tablaSolicitudes;
    private JTextArea txtDetalle;

    public AdopcionesPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnGenerar = new JButton("📝 Generar Solicitud");
        JButton btnConcretar = new JButton("✅ Concretar Adopción");
        JButton btnDevolver = new JButton("↩️ Devolución");
        JButton btnVisita = new JButton("📅 Programar Visita");
        JButton btnActualizar = new JButton("🔄 Actualizar");

        btnGenerar.addActionListener(e -> generarSolicitud());
        btnConcretar.addActionListener(e -> concretarAdopcion());
        btnDevolver.addActionListener(e -> procesarDevolucion());
        btnVisita.addActionListener(e -> programarVisita());
        btnActualizar.addActionListener(e -> cargarTabla());

        panelBotones.add(btnGenerar);
        panelBotones.add(btnConcretar);
        panelBotones.add(btnDevolver);
        panelBotones.add(btnVisita);
        panelBotones.add(btnActualizar);

        // Tabla
        String[] columnas = {"ID", "DNI Adoptante", "Adoptante", "Mascota", "Estado", "Fecha Solicitud"};
        modeloSolicitudes = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaSolicitudes = new JTable(modeloSolicitudes);
        tablaSolicitudes.setRowHeight(25);
        tablaSolicitudes.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaSolicitudes.getSelectionModel().addListSelectionListener(e -> mostrarDetalle());
        JScrollPane scrollTabla = new JScrollPane(tablaSolicitudes);

        // Área de detalle
        txtDetalle = new JTextArea(10, 40);
        txtDetalle.setEditable(false);
        txtDetalle.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollDetalle = new JScrollPane(txtDetalle);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollTabla, scrollDetalle);
        splitPane.setResizeWeight(0.6);

        add(panelBotones, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        cargarTabla();
    }

    private void cargarTabla() {
        modeloSolicitudes.setRowCount(0);
        for (Adopcion a : adopcionController.obtenerTodas()) {
            Adoptante adoptante = adoptanteController.buscarPorDni(a.getDniAdoptante());
            Mascota mascota = mascotaController.buscarPorId(a.getIdMascota());
            modeloSolicitudes.addRow(new Object[]{
                a.getIdAdopcion(),
                a.getDniAdoptante(),
                adoptante != null ? adoptante.getNombre() : "N/A",
                mascota != null ? mascota.getNombre() : "N/A",
                a.getEstado(),
                a.getFechaSolicitud()
            });
        }
    }

    private void mostrarDetalle() {
        int fila = tablaSolicitudes.getSelectedRow();
        if (fila == -1) {
            txtDetalle.setText("");
            return;
        }
        String id = (String) modeloSolicitudes.getValueAt(fila, 0);
        Adopcion a = adopcionController.buscarPorId(id);
        if (a == null) return;

        Adoptante adoptante = adoptanteController.buscarPorDni(a.getDniAdoptante());
        Mascota mascota = mascotaController.buscarPorId(a.getIdMascota());

        StringBuilder sb = new StringBuilder();
        sb.append("========== DATOS DE LA ADOPCIÓN ==========\n");
        sb.append("ID: ").append(a.getIdAdopcion()).append("\n");
        sb.append("Estado: ").append(a.getEstado()).append("\n");
        sb.append("Fecha Solicitud: ").append(a.getFechaSolicitud()).append("\n");

        if (adoptante != null) {
            sb.append("\n📌 ADOPTANTE:\n");
            sb.append("   DNI: ").append(adoptante.getDni()).append("\n");
            sb.append("   Nombre: ").append(adoptante.getNombre()).append("\n");
            sb.append("   Teléfono: ").append(adoptante.getTelefono()).append("\n");
            sb.append("   Email: ").append(adoptante.getEmail()).append("\n");
            sb.append("   WhatsApp: https://wa.me/").append(adoptante.getTelefono().replaceAll("[^0-9]", "")).append("\n");
            sb.append("   Email: mailto:").append(adoptante.getEmail()).append("\n");
        }

        if (mascota != null) {
            sb.append("\n🐾 MASCOTA:\n");
            sb.append("   ID: ").append(mascota.getIdMascota()).append("\n");
            sb.append("   Nombre: ").append(mascota.getNombre()).append("\n");
            sb.append("   Especie: ").append(mascota.getEspecie()).append("\n");
            sb.append("   Raza: ").append(mascota.getRaza()).append("\n");
        }

        if (a.isEsDevuelta()) {
            sb.append("\n⚠️ DEVUELTA\n");
            sb.append("   Motivo: ").append(a.getMotivoDevolucion()).append("\n");
            sb.append("   Detalle: ").append(a.getDetalleDevolucion()).append("\n");
            sb.append("   Motivo detallado: ").append(a.getMotivoDevolucionDetallado()).append("\n");
            sb.append("   Acción tomada: ").append(a.getAccionTomada()).append("\n");
            if (a.isCuarentena()) sb.append("   ⚠️ Mascota en cuarentena\n");
        }

        if (a.getVisita1() != null) {
            sb.append("\nVisita 1: ").append(a.getVisita1().getFechaVisita()).append(" a las ").append(a.getVisita1().getHoraVisita()).append("\n");
        }
        if (a.getVisita2() != null) {
            sb.append("Visita 2: ").append(a.getVisita2().getFechaVisita()).append(" a las ").append(a.getVisita2().getHoraVisita()).append("\n");
        }

        txtDetalle.setText(sb.toString());
    }

    // ========================================================================
    //  MÉTODO: GENERAR SOLICITUD
    // ========================================================================
    private void generarSolicitud() {
        List<Adoptante> aprobados = adoptanteController.listarAdoptantesAprobados();
        if (aprobados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay adoptantes aprobados.");
            return;
        }
        Adoptante adoptante = (Adoptante) JOptionPane.showInputDialog(this,
                "Seleccione adoptante:", "Adoptante",
                JOptionPane.QUESTION_MESSAGE, null,
                aprobados.toArray(), aprobados.get(0));
        if (adoptante == null) return;

        List<Mascota> disponibles = mascotaController.listarDisponibles();
        if (disponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay mascotas disponibles.");
            return;
        }
        Mascota mascota = (Mascota) JOptionPane.showInputDialog(this,
                "Seleccione mascota:", "Mascota",
                JOptionPane.QUESTION_MESSAGE, null,
                disponibles.toArray(), disponibles.get(0));
        if (mascota == null) return;

        Adopcion solicitud = adopcionController.generarSolicitudAdopcion(adoptante.getDni(), mascota.getIdMascota());
        if (solicitud != null) {
            JOptionPane.showMessageDialog(this, "✔ Solicitud generada: " + solicitud.getIdAdopcion());
            cargarTabla();
            int resp = JOptionPane.showConfirmDialog(this, "¿Concretar adopción ahora?", "Concretar", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                concretarAdopcion(solicitud.getIdAdopcion());
            }
        }
    }

    // ========================================================================
    //  MÉTODO: CONCRETAR ADOPCIÓN (desde el botón)
    // ========================================================================
    private void concretarAdopcion() {
        List<Adopcion> enProceso = adopcionController.obtenerSolicitudesEnProceso();
        if (enProceso.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay solicitudes en proceso.");
            return;
        }
        Adopcion solicitud = (Adopcion) JOptionPane.showInputDialog(this,
                "Seleccione solicitud:", "Concretar",
                JOptionPane.QUESTION_MESSAGE, null,
                enProceso.toArray(), enProceso.get(0));
        if (solicitud == null) return;
        concretarAdopcion(solicitud.getIdAdopcion());
    }

    // ========================================================================
    //  MÉTODO: CONCRETAR ADOPCIÓN (con ID) – VERSIÓN CORREGIDA (sin superposición)
    // ========================================================================
    private void concretarAdopcion(String idSolicitud) {
        Adopcion solicitud = adopcionController.buscarPorId(idSolicitud);
        if (solicitud == null) return;

        Adoptante adoptante = adoptanteController.buscarPorDni(solicitud.getDniAdoptante());
        Mascota mascota = mascotaController.buscarPorId(solicitud.getIdMascota());

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Concretar Adopción", true);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel panelInfo = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0;

        // Título
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        JLabel titulo = new JLabel("Datos de la adopción", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelInfo.add(titulo, gbc);

        // Adoptante
        y++;
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = y;
        panelInfo.add(new JLabel("Adoptante:"), gbc);
        gbc.gridx = 1;
        JLabel lblAdoptante = new JLabel(adoptante.getNombre() + " (DNI: " + adoptante.getDni() + ")");
        lblAdoptante.setFont(new Font("Arial", Font.PLAIN, 12));
        panelInfo.add(lblAdoptante, gbc);

        // Teléfono
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panelInfo.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        JLabel lblTelefono = new JLabel(adoptante.getTelefono());
        lblTelefono.setFont(new Font("Arial", Font.PLAIN, 12));
        panelInfo.add(lblTelefono, gbc);

        // Email
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panelInfo.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        JLabel lblEmail = new JLabel(adoptante.getEmail());
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 12));
        panelInfo.add(lblEmail, gbc);

        // Botones de contacto
        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        JPanel panelContacto = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton btnWhatsApp = new JButton("📱 WhatsApp");
        JButton btnEmail = new JButton("📧 Email");
        btnWhatsApp.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://wa.me/" + adoptante.getTelefono().replaceAll("[^0-9]", "")));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error al abrir WhatsApp");
            }
        });
        btnEmail.addActionListener(e -> {
            try {
                Desktop.getDesktop().mail(new URI("mailto:" + adoptante.getEmail() + "?subject=Visita de seguimiento"));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error al abrir correo");
            }
        });
        panelContacto.add(btnWhatsApp);
        panelContacto.add(btnEmail);
        panelInfo.add(panelContacto, gbc);

        // Mascota
        y++;
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = y;
        panelInfo.add(new JLabel("Mascota:"), gbc);
        gbc.gridx = 1;
        JLabel lblMascota = new JLabel(mascota.getNombre() + " (" + mascota.getIdMascota() + ")");
        lblMascota.setFont(new Font("Arial", Font.PLAIN, 12));
        panelInfo.add(lblMascota, gbc);

        // Fecha entrega
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panelInfo.add(new JLabel("Fecha entrega:"), gbc);
        gbc.gridx = 1;
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setDate(new Date());
        dateChooser.setPreferredSize(new Dimension(150, 30));
        panelInfo.add(dateChooser, gbc);

        // Botones
        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnConcretar = new JButton("✅ Concretar");
        JButton btnCancelar = new JButton("Cancelar");
        btnConcretar.addActionListener(e -> {
            try {
                LocalDate fecha = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (adopcionController.concretarAdopcion(idSolicitud, fecha)) {
                    JOptionPane.showMessageDialog(dialog, "✔ Adopción concretada.");
                    dialog.dispose();
                    cargarTabla();

                    int resp = JOptionPane.showConfirmDialog(AdopcionesPanel.this,
                            "¿Programar visita de seguimiento (30 días)?", "Visita", JOptionPane.YES_NO_OPTION);
                    if (resp == JOptionPane.YES_OPTION) {
                        LocalDate fechaVisita = fecha.plusDays(30);
                        String horaStr = JOptionPane.showInputDialog("Hora (HH:mm):");
                        try {
                            LocalTime hora = LocalTime.parse(horaStr);
                            adopcionController.programarVisitaSeguimiento(idSolicitud, fechaVisita, hora);
                            JOptionPane.showMessageDialog(AdopcionesPanel.this,
                                    "✔ Visita programada para " + fechaVisita + " a las " + hora);
                            cargarTabla();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(AdopcionesPanel.this, "Formato de hora incorrecto.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog, "❌ No se pudo concretar la adopción.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Seleccione una fecha válida.");
            }
        });
        btnCancelar.addActionListener(e -> dialog.dispose());
        panelBotones.add(btnConcretar);
        panelBotones.add(btnCancelar);
        panelInfo.add(panelBotones, gbc);

        dialog.add(new JScrollPane(panelInfo), BorderLayout.CENTER);
        dialog.setSize(450, 480);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // ========================================================================
    //  MÉTODO: PROCESAR DEVOLUCIÓN (corregido visualmente)
    // ========================================================================
    private void procesarDevolucion() {
        List<Adopcion> concretadas = adopcionController.obtenerSolicitudesConcretadas();
        if (concretadas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay adopciones concretadas.");
            return;
        }
        Adopcion adopcion = (Adopcion) JOptionPane.showInputDialog(this,
                "Seleccione adopción:", "Devolución",
                JOptionPane.QUESTION_MESSAGE, null,
                concretadas.toArray(), concretadas.get(0));
        if (adopcion == null) return;

        Adoptante adoptante = adoptanteController.buscarPorDni(adopcion.getDniAdoptante());
        Mascota mascota = mascotaController.buscarPorId(adopcion.getIdMascota());

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Procesar Devolución", true);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0;

        // Título
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        JLabel titulo = new JLabel("REGISTRO DE DEVOLUCIÓN", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titulo, gbc);

        // Adoptante
        y++;
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Adoptante:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(adoptante.getNombre() + " (DNI: " + adoptante.getDni() + ")"), gbc);

        // Teléfono
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(adoptante.getTelefono()), gbc);

        // Email
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(adoptante.getEmail()), gbc);

        // Mascota
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Mascota:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(mascota.getNombre() + " (" + mascota.getIdMascota() + ")"), gbc);

        // Motivo
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Motivo:"), gbc);
        gbc.gridx = 1;
        String[] motivos = {"Problemas de comportamiento", "Problemas de salud", "Cambio de domicilio",
                            "Problemas económicos", "Incompatibilidad con otros animales",
                            "Incompatibilidad con niños", "Fallecimiento del adoptante", "Otro motivo"};
        JComboBox<String> cmbMotivo = new JComboBox<>(motivos);
        cmbMotivo.setPreferredSize(new Dimension(180, 25));
        panel.add(cmbMotivo, gbc);

        // Detalle adicional
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Detalle adicional:"), gbc);
        gbc.gridx = 1;
        JTextField txtDetalle = new JTextField(20);
        panel.add(txtDetalle, gbc);

        // Motivo detallado
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Motivo detallado:"), gbc);
        gbc.gridx = 1;
        JTextField txtMotivoDetallado = new JTextField(20);
        panel.add(txtMotivoDetallado, gbc);

        // Acción a tomar
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Acción a tomar:"), gbc);
        gbc.gridx = 1;
        JTextField txtAccion = new JTextField(20);
        panel.add(txtAccion, gbc);

        // Cuarentena
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Cuarentena:"), gbc);
        gbc.gridx = 1;
        JCheckBox chkCuarentena = new JCheckBox("Activar cuarentena");
        panel.add(chkCuarentena, gbc);

        // Botones
        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnDevolver = new JButton("↩️ Procesar Devolución");
        JButton btnCancelar = new JButton("Cancelar");
        btnDevolver.addActionListener(e -> {
            int motivoIdx = cmbMotivo.getSelectedIndex() + 1;
            String detalle = txtDetalle.getText();
            String motivoDetallado = txtMotivoDetallado.getText();
            String accion = txtAccion.getText();
            boolean cuarentena = chkCuarentena.isSelected();

            if (motivoDetallado.isEmpty() || accion.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Complete los campos obligatorios.");
                return;
            }

            if (adopcionController.procesarDevolucionDetallada(adopcion.getIdAdopcion(), motivoIdx, detalle,
                                                               motivoDetallado, accion, cuarentena)) {
                JOptionPane.showMessageDialog(dialog, "✔ Devolución procesada.");
                dialog.dispose();
                cargarTabla();
            }
        });
        btnCancelar.addActionListener(e -> dialog.dispose());
        panelBotones.add(btnDevolver);
        panelBotones.add(btnCancelar);
        panel.add(panelBotones, gbc);

        dialog.add(new JScrollPane(panel), BorderLayout.CENTER);
        dialog.setSize(500, 480);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // ========================================================================
    //  MÉTODO: PROGRAMAR VISITA (corregido visualmente)
    // ========================================================================
    private void programarVisita() {
        List<Adopcion> concretadas = adopcionController.obtenerSolicitudesConcretadas();
        if (concretadas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay adopciones concretadas.");
            return;
        }
        Adopcion adopcion = (Adopcion) JOptionPane.showInputDialog(this,
                "Seleccione adopción:", "Programar Visita",
                JOptionPane.QUESTION_MESSAGE, null,
                concretadas.toArray(), concretadas.get(0));
        if (adopcion == null) return;

        Adoptante adoptante = adoptanteController.buscarPorDni(adopcion.getDniAdoptante());

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Programar Visita", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Fecha
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Fecha:"), gbc);
        gbc.gridx = 1;
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setDate(new Date());
        dateChooser.setPreferredSize(new Dimension(150, 30));
        dialog.add(dateChooser, gbc);

        // Hora
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Hora (HH:mm):"), gbc);
        gbc.gridx = 1;
        JTextField txtHora = new JTextField(10);
        txtHora.setPreferredSize(new Dimension(100, 25));
        dialog.add(txtHora, gbc);

        // Botones
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnProgramar = new JButton("📅 Programar");
        JButton btnCancelar = new JButton("Cancelar");
        btnProgramar.addActionListener(e -> {
            try {
                LocalDate fecha = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalTime hora = LocalTime.parse(txtHora.getText());
                if (adopcionController.programarVisitaSeguimiento(adopcion.getIdAdopcion(), fecha, hora)) {
                    JOptionPane.showMessageDialog(dialog, "✔ Visita programada.\nSe envió notificación a " + adoptante.getEmail() + " y WhatsApp.");
                    dialog.dispose();
                    cargarTabla();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Formato de hora incorrecto. Use HH:mm (ej: 14:30)");
            }
        });
        btnCancelar.addActionListener(e -> dialog.dispose());
        panelBotones.add(btnProgramar);
        panelBotones.add(btnCancelar);
        dialog.add(panelBotones, gbc);

        dialog.setSize(400, 180);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}