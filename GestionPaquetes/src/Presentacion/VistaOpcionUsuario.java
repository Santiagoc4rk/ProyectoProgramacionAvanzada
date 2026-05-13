package Presentacion;

import java.awt.*;
import javax.swing.*;

public class VistaOpcionUsuario extends JPanel {

    public VistaOpcionUsuario(VistaPrincipal base) {
        setLayout(new BorderLayout());
        setBackground(new Color(21, 44, 71));

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(15, 30, 50));
        header.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));
        JLabel titulo = new JLabel("Seleccione tipo de usuario");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        header.add(titulo);

        // Botones grandes centro
        JPanel centro = new JPanel(new GridLayout(1, 2, 30, 0));
        centro.setBackground(new Color(21, 44, 71));
        centro.setBorder(BorderFactory.createEmptyBorder(60, 80, 60, 80));

        JButton btnCliente = crearBotonGrande("CLIENTE", new Color(46, 134, 222));
        JButton btnPersonal = crearBotonGrande("PERSONAL", new Color(39, 174, 96));

        btnCliente.addActionListener(e -> base.cambiarVista("CLIENTE"));
        btnPersonal.addActionListener(e -> base.cambiarVista("LOGIN"));

        centro.add(btnCliente);
        centro.add(btnPersonal);

        // Footer
        JPanel footer = new JPanel();
        footer.setBackground(new Color(21, 44, 71));
        JButton btnVolver = crearBoton("Volver", new Color(100, 100, 100));
        btnVolver.addActionListener(e -> base.cambiarVista("INICIO"));
        footer.add(btnVolver);

        add(header, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    private JButton crearBotonGrande(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(120, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
