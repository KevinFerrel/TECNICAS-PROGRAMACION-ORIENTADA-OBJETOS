package vista.gui;

import javax.swing.*;
import java.awt.*;
import controlador.HistorialMedicoController;

public class HistorialPanel extends JPanel {
    private HistorialMedicoController controller = AppContext.getInstance().historial;

    public HistorialPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitulo = new JLabel("🏥 HISTORIAL MÉDICO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        JTextArea txtResumen = new JTextArea();
        txtResumen.setEditable(false);
        txtResumen.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JButton btnBuscar = new JButton("🔍 Buscar Resumen Clínico");
        btnBuscar.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("ID de la mascota:");
            if (id != null && !id.trim().isEmpty()) {
                txtResumen.setText(controller.generarResumenClinicoConsolidado(id.trim()));
            }
        });

        JPanel panelSuperior = new JPanel(new FlowLayout());
        panelSuperior.add(btnBuscar);
        add(panelSuperior, BorderLayout.CENTER);
        add(new JScrollPane(txtResumen), BorderLayout.SOUTH);
    }
}