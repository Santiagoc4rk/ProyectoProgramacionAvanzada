/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "historial_estado")
@NamedQueries({
    @NamedQuery(name = "HistorialEstado.findAll", query = "SELECT h FROM HistorialEstado h"),
    @NamedQuery(name = "HistorialEstado.findByIdHistorial", query = "SELECT h FROM HistorialEstado h WHERE h.idHistorial = :idHistorial"),
    @NamedQuery(name = "HistorialEstado.findByObservaciones", query = "SELECT h FROM HistorialEstado h WHERE h.observaciones = :observaciones"),
    @NamedQuery(name = "HistorialEstado.findByEstado", query = "SELECT h FROM HistorialEstado h WHERE h.estado = :estado"),
    @NamedQuery(name = "HistorialEstado.findByFechaHora", query = "SELECT h FROM HistorialEstado h WHERE h.fechaHora = :fechaHora")})
public class HistorialEstado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idHistorial")
    private Integer idHistorial;
    @Column(name = "observaciones")
    private String observaciones;
    @Basic(optional = false)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @Column(name = "fecha_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @JoinColumn(name = "codigo_unico", referencedColumnName = "codigo_unico")
    @ManyToOne(optional = false)
    private Paquete codigoUnico;
    @JoinColumn(name = "ubicacion", referencedColumnName = "idLocal")
    @ManyToOne(optional = false)
    private Ubicacion ubicacion;

    public HistorialEstado() {
    }

    public HistorialEstado(Integer idHistorial) {
        this.idHistorial = idHistorial;
    }

    public HistorialEstado(Integer idHistorial, String estado, Date fechaHora) {
        this.idHistorial = idHistorial;
        this.estado = estado;
        this.fechaHora = fechaHora;
    }

    public Integer getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(Integer idHistorial) {
        this.idHistorial = idHistorial;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Paquete getCodigoUnico() {
        return codigoUnico;
    }

    public void setCodigoUnico(Paquete codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHistorial != null ? idHistorial.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistorialEstado)) {
            return false;
        }
        HistorialEstado other = (HistorialEstado) object;
        if ((this.idHistorial == null && other.idHistorial != null) || (this.idHistorial != null && !this.idHistorial.equals(other.idHistorial))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.HistorialEstado[ idHistorial=" + idHistorial + " ]";
    }
    
}
