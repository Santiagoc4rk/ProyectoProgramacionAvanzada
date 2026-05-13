package Presentacion;

import java.awt.*;
import javax.swing.*;

public class VistaInicio extends JPanel {

    public VistaInicio(VistaPrincipal base) {
        setLayout(new BorderLayout());
        setBackground(new Color(21, 44, 71));

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(15, 30, 50));
        header.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        JLabel titulo = new JLabel("SISTEMA DE GESTIÓN DE PAQUETES");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(titulo);

        // Centro
        JPanel centro = new JPanel();
        centro.setBackground(new Color(21, 44, 71));
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));

        JLabel sub = new JLabel("Bienvenido al sistema logístico");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sub.setForeground(new Color(160, 191, 216));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(300, 2));
        sep.setForeground(new Color(46, 134, 222));

        centro.add(sub);
        centro.add(Box.createRigidArea(new Dimension(0, 20)));
        centro.add(sep);

        // Botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        botones.setBackground(new Color(21, 44, 71));

        JButton btnContinuar = crearBoton("Continuar", new Color(46, 134, 222));
        JButton btnSalir = crearBoton("Salir", new Color(231, 76, 60));

        btnContinuar.addActionListener(e -> base.cambiarVista("OPCION"));
        btnSalir.addActionListener(e -> System.exit(0));

        botones.add(btnContinuar);
        botones.add(btnSalir);

        add(header, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(150, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
