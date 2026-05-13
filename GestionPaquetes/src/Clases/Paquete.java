/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author andre
 */
@Entity
@Table(name = "paquete")
@NamedQueries({
    @NamedQuery(name = "Paquete.findAll", query = "SELECT p FROM Paquete p"),
    @NamedQuery(name = "Paquete.findByCodigoUnico", query = "SELECT p FROM Paquete p WHERE p.codigoUnico = :codigoUnico"),
    @NamedQuery(name = "Paquete.findByPeso", query = "SELECT p FROM Paquete p WHERE p.peso = :peso"),
    @NamedQuery(name = "Paquete.findByTipoEnvio", query = "SELECT p FROM Paquete p WHERE p.tipoEnvio = :tipoEnvio"),
    @NamedQuery(name = "Paquete.findByEstado", query = "SELECT p FROM Paquete p WHERE p.estado = :estado"),
    @NamedQuery(name = "Paquete.findByCiudadEnvio", query = "SELECT p FROM Paquete p WHERE p.ciudadEnvio = :ciudadEnvio"),
    @NamedQuery(name = "Paquete.findByDireccionEntrega", query = "SELECT p FROM Paquete p WHERE p.direccionEntrega = :direccionEntrega"),
    @NamedQuery(name = "Paquete.findByCiudadDestino", query = "SELECT p FROM Paquete p WHERE p.ciudadDestino = :ciudadDestino"),
    @NamedQuery(name = "Paquete.findByNroSeguimiento", query = "SELECT p FROM Paquete p WHERE p.nroSeguimiento = :nroSeguimiento"),
    @NamedQuery(name = "Paquete.findByFechaHora", query = "SELECT p FROM Paquete p WHERE p.fechaHora = :fechaHora"),
    @NamedQuery(name = "Paquete.findByDestinatarioNomb", query = "SELECT p FROM Paquete p WHERE p.destinatarioNomb = :destinatarioNomb"),
    @NamedQuery(name = "Paquete.findByDestinatarioTel", query = "SELECT p FROM Paquete p WHERE p.destinatarioTel = :destinatarioTel")})
public class Paquete implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo_unico")
    private Integer codigoUnico;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "peso")
    private BigDecimal peso;
    @Column(name = "tipo_envio")
    private String tipoEnvio;
    @Basic(optional = false)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @Column(name = "ciudad_envio")
    private String ciudadEnvio;
    @Basic(optional = false)
    @Column(name = "direccion_entrega")
    private String direccionEntrega;
    @Basic(optional = false)
    @Column(name = "ciudad_destino")
    private String ciudadDestino;
    @Basic(optional = false)
    @Column(name = "nro_seguimiento")
    private String nroSeguimiento;
    @Basic(optional = false)
    @Column(name = "fecha_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @Basic(optional = false)
    @Column(name = "destinatarioNomb")
    private String destinatarioNomb;
    @Basic(optional = false)
    @Column(name = "destinatarioTel")
    private String destinatarioTel;
    @JoinColumn(name = "idCliente", referencedColumnName = "idCliente")
    @ManyToOne(optional = false)
    private Cliente idCliente;
    @JoinColumn(name = "idRecepcionista", referencedColumnName = "idRecepcionista")
    @ManyToOne(optional = false)
    private Recepcionista idRecepcionista;
    @JoinColumn(name = "idTarifa", referencedColumnName = "idTarifa")
    @ManyToOne(optional = false)
    private Tarifa idTarifa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paquetecodigounico")
    private Collection<Factura> facturaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoUnico")
    private Collection<HistorialEstado> historialEstadoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paquete")
    private Collection<Despacha> despachaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paquete")
    private Collection<Entrega> entregaCollection;

    public Paquete() {
    }

    public Paquete(Integer codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

    public Paquete(Integer codigoUnico, String estado, String ciudadEnvio, String direccionEntrega, String ciudadDestino, String nroSeguimiento, Date fechaHora, String destinatarioNomb, String destinatarioTel) {
        this.codigoUnico = codigoUnico;
        this.estado = estado;
        this.ciudadEnvio = ciudadEnvio;
        this.direccionEntrega = direccionEntrega;
        this.ciudadDestino = ciudadDestino;
        this.nroSeguimiento = nroSeguimiento;
        this.fechaHora = fechaHora;
        this.destinatarioNomb = destinatarioNomb;
        this.destinatarioTel = destinatarioTel;
    }

    public Integer getCodigoUnico() {
        return codigoUnico;
    }

    public void setCodigoUnico(Integer codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public String getTipoEnvio() {
        return tipoEnvio;
    }

    public void setTipoEnvio(String tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCiudadEnvio() {
        return ciudadEnvio;
    }

    public void setCiudadEnvio(String ciudadEnvio) {
        this.ciudadEnvio = ciudadEnvio;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getCiudadDestino() {
        return ciudadDestino;
    }

    public void setCiudadDestino(String ciudadDestino) {
        this.ciudadDestino = ciudadDestino;
    }

    public String getNroSeguimiento() {
        return nroSeguimiento;
    }

    public void setNroSeguimiento(String nroSeguimiento) {
        this.nroSeguimiento = nroSeguimiento;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getDestinatarioNomb() {
        return destinatarioNomb;
    }

    public void setDestinatarioNomb(String destinatarioNomb) {
        this.destinatarioNomb = destinatarioNomb;
    }

    public String getDestinatarioTel() {
        return destinatarioTel;
    }

    public void setDestinatarioTel(String destinatarioTel) {
        this.destinatarioTel = destinatarioTel;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    public Recepcionista getIdRecepcionista() {
        return idRecepcionista;
    }

    public void setIdRecepcionista(Recepcionista idRecepcionista) {
        this.idRecepcionista = idRecepcionista;
    }

    public Tarifa getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(Tarifa idTarifa) {
        this.idTarifa = idTarifa;
    }

    public Collection<Factura> getFacturaCollection() {
        return facturaCollection;
    }

    public void setFacturaCollection(Collection<Factura> facturaCollection) {
        this.facturaCollection = facturaCollection;
    }

    public Collection<HistorialEstado> getHistorialEstadoCollection() {
        return historialEstadoCollection;
    }

    public void setHistorialEstadoCollection(Collection<HistorialEstado> historialEstadoCollection) {
        this.historialEstadoCollection = historialEstadoCollection;
    }

    public Collection<Despacha> getDespachaCollection() {
        return despachaCollection;
    }

    public void setDespachaCollection(Collection<Despacha> despachaCollection) {
        this.despachaCollection = despachaCollection;
    }

    public Collection<Entrega> getEntregaCollection() {
        return entregaCollection;
    }

    public void setEntregaCollection(Collection<Entrega> entregaCollection) {
        this.entregaCollection = entregaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoUnico != null ? codigoUnico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Paquete)) {
            return false;
        }
        Paquete other = (Paquete) object;
        if ((this.codigoUnico == null && other.codigoUnico != null) || (this.codigoUnico != null && !this.codigoUnico.equals(other.codigoUnico))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.Paquete[ codigoUnico=" + codigoUnico + " ]";
    }
    
}
