package Presentacion;

import java.awt.*;
import javax.swing.*;

public class VistaPrincipal extends JFrame {

    private CardLayout layout;
    private JPanel contenedor;

    public VistaPrincipal() {
        setTitle("Sistema de Gestión de Paquetes");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        layout = new CardLayout();
        contenedor = new JPanel(layout);

        contenedor.add(new VistaInicio(this), "INICIO");
        contenedor.add(new VistaOpcionUsuario(this), "OPCION");
        contenedor.add(new VistaCliente(this), "CLIENTE");
        contenedor.add(new VistaLogin(this), "LOGIN");
        contenedor.add(new VistaRecepcionista(this), "RECEPCIONISTA");
        contenedor.add(new VistaOperador(this), "OPERADOR");
        contenedor.add(new VistaRepartidor(this), "REPARTIDOR");
        contenedor.add(new VistaSupervisor(this), "SUPERVISOR");

        cambiarVista("INICIO");
        add(contenedor);
        setVisible(true);
    }

    public void cambiarVista(String clave) {
        layout.show(contenedor, clave);
    }
}
