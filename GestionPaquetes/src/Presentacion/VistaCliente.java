package Presentacion;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VistaCliente extends JPanel {

    private JTextField txtSeguimiento;
    private DefaultTableModel modeloTabla;

    public VistaCliente(VistaPrincipal base) {
        setLayout(new BorderLayout());
        setBackground(new Color(21, 44, 71));

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(15, 30, 50));
        header.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));
        JLabel titulo = new JLabel("Seguimiento de Paquete");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        header.add(titulo);

        // Búsqueda
        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        busqueda.setBackground(new Color(21, 44, 71));

        JLabel lbl = new JLabel("Nro. Seguimiento:");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(Color.WHITE);

        txtSeguimiento = new JTextField(20);
        txtSeguimiento.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSeguimiento.setBackground(new Color(40, 70, 110));
        txtSeguimiento.setForeground(Color.WHITE);
        txtSeguimiento.setCaretColor(Color.WHITE);
        txtSeguimiento.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(46, 134, 222)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBuscar.setBackground(new Color(46, 134, 222));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        busqueda.add(lbl);
        busqueda.add(txtSeguimiento);
        busqueda.add(btnBuscar);

        // Tabla historial
        String[] columnas = {"Fecha/Hora", "Estado", "Ubicación", "Observaciones"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modeloTabla);
        tabla.setBackground(new Color(30, 58, 95));
        tabla.setForeground(Color.WHITE);
        tabla.setGridColor(new Color(46, 134, 222));
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setBackground(new Color(15, 30, 50));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setRowHeight(28);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btnBuscar.addActionListener(e -> {
            String nro = txtSeguimiento.getText().trim();
            if (nro.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese un número de seguimiento", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            modeloTabla.setRowCount(0);
            // Datos de prueba
            modeloTabla.addRow(new Object[]{"2025-05-13 08:00", "Receptado", "Local Norte", "Paquete ingresado"});
            modeloTabla.addRow(new Object[]{"2025-05-13 10:30", "En Tránsito", "Bodega Central", "En camino"});
        });

        // Footer
        JPanel footer = new JPanel();
        footer.setBackground(new Color(21, 44, 71));
        JButton btnVolver = new JButton("Volver");
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVolver.setBackground(new Color(100, 100, 100));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setPreferredSize(new Dimension(120, 35));
        btnVolver.addActionListener(e -> base.cambiarVista("OPCION"));
        footer.add(btnVolver);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(new Color(21, 44, 71));
        centro.add(busqueda, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }
}
