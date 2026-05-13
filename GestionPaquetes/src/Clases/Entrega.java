/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author andre
 */
@Entity
@Table(name = "entrega")
@NamedQueries({
    @NamedQuery(name = "Entrega.findAll", query = "SELECT e FROM Entrega e"),
    @NamedQuery(name = "Entrega.findByNombreRec", query = "SELECT e FROM Entrega e WHERE e.nombreRec = :nombreRec"),
    @NamedQuery(name = "Entrega.findByObservaciones", query = "SELECT e FROM Entrega e WHERE e.observaciones = :observaciones"),
    @NamedQuery(name = "Entrega.findByFechaHora", query = "SELECT e FROM Entrega e WHERE e.fechaHora = :fechaHora"),
    @NamedQuery(name = "Entrega.findByIdRepartidor", query = "SELECT e FROM Entrega e WHERE e.entregaPK.idRepartidor = :idRepartidor"),
    @NamedQuery(name = "Entrega.findByCodigoUnico", query = "SELECT e FROM Entrega e WHERE e.entregaPK.codigoUnico = :codigoUnico")})
public class Entrega implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EntregaPK entregaPK;
    @Basic(optional = false)
    @Column(name = "nombre_rec")
    private String nombreRec;
    @Column(name = "observaciones")
    private String observaciones;
    @Basic(optional = false)
    @Column(name = "fecha_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @JoinColumn(name = "codigo_unico", referencedColumnName = "codigo_unico", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Paquete paquete;
    @JoinColumn(name = "idRepartidor", referencedColumnName = "idRepartidor", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Repartidor repartidor;

    public Entrega() {
    }

    public Entrega(EntregaPK entregaPK) {
        this.entregaPK = entregaPK;
    }

    public Entrega(EntregaPK entregaPK, String nombreRec, Date fechaHora) {
        this.entregaPK = entregaPK;
        this.nombreRec = nombreRec;
        this.fechaHora = fechaHora;
    }

    public Entrega(int idRepartidor, int codigoUnico) {
        this.entregaPK = new EntregaPK(idRepartidor, codigoUnico);
    }

    public EntregaPK getEntregaPK() {
        return entregaPK;
    }

    public void setEntregaPK(EntregaPK entregaPK) {
        this.entregaPK = entregaPK;
    }

    public String getNombreRec() {
        return nombreRec;
    }

    public void setNombreRec(String nombreRec) {
        this.nombreRec = nombreRec;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Paquete getPaquete() {
        return paquete;
    }

    public void setPaquete(Paquete paquete) {
        this.paquete = paquete;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(Repartidor repartidor) {
        this.repartidor = repartidor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (entregaPK != null ? entregaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Entrega)) {
            return false;
        }
        Entrega other = (Entrega) object;
        if ((this.entregaPK == null && other.entregaPK != null) || (this.entregaPK != null && !this.entregaPK.equals(other.entregaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.Entrega[ entregaPK=" + entregaPK + " ]";
    }
    
}
