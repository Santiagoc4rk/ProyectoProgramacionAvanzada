package Presentacion;

import java.awt.*;
import javax.swing.*;

public class VistaRepartidor extends JPanel {

    public VistaRepartidor(VistaPrincipal base) {
        setLayout(new BorderLayout());
        setBackground(new Color(21, 44, 71));

        JPanel header = new JPanel();
        header.setBackground(new Color(15, 30, 50));
        header.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));
        JLabel titulo = new JLabel("Panel Repartidor");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        header.add(titulo);

        JPanel centro = new JPanel(new GridLayout(2, 1, 15, 15));
        centro.setBackground(new Color(21, 44, 71));
        centro.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        JButton btnAsignados = crearBoton("Paquetes Asignados", new Color(46, 134, 222));
        JButton btnEntrega = crearBoton("Registrar Entrega", new Color(39, 174, 96));

        btnAsignados.addActionListener(e -> JOptionPane.showMessageDialog(this, "Próximamente: Paquetes Asignados"));
        btnEntrega.addActionListener(e -> JOptionPane.showMessageDialog(this, "Próximamente: Registrar Entrega"));

        centro.add(btnAsignados);
        centro.add(btnEntrega);

        JPanel footer = new JPanel();
        footer.setBackground(new Color(21, 44, 71));
        JButton btnVolver = crearBotonPequeno("Cerrar Sesión", new Color(100, 100, 100));
        btnVolver.addActionListener(e -> base.cambiarVista("LOGIN"));
        footer.add(btnVolver);

        add(header, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton crearBotonPequeno(String texto, Color color) {
        JButton btn = crearBoton(texto, color);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(140, 35));
        return btn;
    }
}
