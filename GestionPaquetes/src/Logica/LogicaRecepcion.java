package Logica;

import LogicaControllers.*;
import Clases.*;
import java.math.BigDecimal;
import java.util.Date;

public class LogicaRecepcion {

    //Controladores
    ClienteJpaController objCliContr;
    FacturaJpaController objFacContr;
    HistorialEstadoJpaController objHEContr;
    PersonaJpaController objPerContr;
    PaqueteJpaController objPaqContr;
    TarifaJpaController objTarContr;
    UbicacionJpaController UbiContr;
    RecepcionistaJpaController objRecContr;
    //Clases

    public LogicaRecepcion() {
        this.objCliContr = new ClienteJpaController();
        this.objFacContr = new FacturaJpaController();
        this.objHEContr = new HistorialEstadoJpaController();
        this.objPerContr = new PersonaJpaController();
        this.objPaqContr = new PaqueteJpaController();
        this.objTarContr = new TarifaJpaController();
        this.UbiContr = new UbicacionJpaController();
        this.objRecContr = new RecepcionistaJpaController();
    }

    public String generadorCDUnico() {
        Date anio = new Date();
        StringBuilder str = new StringBuilder();

        int ultimo = objPaqContr.getPaqueteCount();

        str.append("LOH-");
        str.append(anio.getYear());
        str.append("-");
        str.append(ultimo + 1);

        return str.toString();
    }

    public void registrarPersona(String ci, String nom, String app, String num, String pass, String email) throws Exception {

        if (objPerContr.findPersona(ci) != null) {
            String aviso = "Esta Persona ya se encuentra registrada";
        }
        Persona p = new Persona();
        p.setCedula(ci);
        p.setNombre(nom);
        p.setApellido(app);
        p.setNumero(num);
        p.setEmail(email);
        p.setPassword(pass);
        objPerContr.create(p);
        String aviso = "Persona ingresada exitosamente";
    }

    public void registrarCliente(String ci, String dir, String ciu, Recepcionista rec) {
        if (objPerContr.findPersona(ci) != null) {
            String aviso = "Este Cliente ya se encuentra registrada";
        }
        Persona p = objPerContr.findPersona(ci);
        Cliente c = new Cliente();
        c.setCedula(p);
        c.setDireccion(dir);
        c.setCiudad(ciu);
        c.setIdRecepcionista(rec);

    }

    public void registrarPaquete(BigDecimal peso, String tipo_envio,
            String ciudad_envio, String direccion_entrega, String ciudad_destino,
            int idRecepcionista,
            int idCliente, int idTarifa, String destinatarioNomb, String destinatarioTel, String observaciones) {
        Paquete paquete = new Paquete();
        Recepcionista recep = objRecContr.findRecepcionista(idRecepcionista);
        Cliente cliente = objCliContr.findCliente(idCliente);
        Tarifa tar = objTarContr.findTarifa(idTarifa);
        paquete.setPeso(peso);
        paquete.setTipoEnvio(tipo_envio);
        paquete.setEstado("Registrado");
        paquete.setCiudadEnvio(ciudad_envio);
        paquete.setDireccionEntrega(direccion_entrega);
        paquete.setCiudadDestino(ciudad_destino);
        paquete.setNroSeguimiento(generadorCDUnico());
        paquete.setFechaHora(new Date());
        paquete.setIdRecepcionista(recep);
        paquete.setIdCliente(cliente);
        paquete.setIdTarifa(tar);
        paquete.setDestinatarioNomb(destinatarioNomb);
        paquete.setDestinatarioTel(destinatarioTel);
        
        objPaqContr.create(paquete);
        
        //Cambiar el Historial 
        cambiarHistorial(observaciones, paquete, UbiContr.findUbicacion(recep.getLocal().getIdLocal()) );
    }

    public void cambiarHistorial(String observaciones, Paquete p, Ubicacion u) {
        HistorialEstado historial = new HistorialEstado();
        historial.setEstado(p.getEstado());
        historial.setFechaHora(new Date());
        if (observaciones.equals(" ")) { // Acordaraste de colocar un espacio si queda vacio este valor!!!
            historial.setObservaciones("Paquete " + p.getEstado() + " en: " + u.getNombre());
        } else {
            historial.setObservaciones(observaciones);
        }
        historial.setCodigoUnico(p);
        historial.setUbicacion(u);
        
        
        objHEContr.create(historial);
        
    }
    
    public void recepcion(){
        
    }

}
