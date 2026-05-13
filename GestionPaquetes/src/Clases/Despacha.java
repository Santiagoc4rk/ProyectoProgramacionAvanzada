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
@Table(name = "despacha")
@NamedQueries({
    @NamedQuery(name = "Despacha.findAll", query = "SELECT d FROM Despacha d"),
    @NamedQuery(name = "Despacha.findByFechaHora", query = "SELECT d FROM Despacha d WHERE d.fechaHora = :fechaHora"),
    @NamedQuery(name = "Despacha.findByIdOperador", query = "SELECT d FROM Despacha d WHERE d.despachaPK.idOperador = :idOperador"),
    @NamedQuery(name = "Despacha.findByCodigoUnico", query = "SELECT d FROM Despacha d WHERE d.despachaPK.codigoUnico = :codigoUnico")})
public class Despacha implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DespachaPK despachaPK;
    @Basic(optional = false)
    @Column(name = "fecha_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @JoinColumn(name = "idOperador", referencedColumnName = "idOperador", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private OperadorDespacho operadorDespacho;
    @JoinColumn(name = "codigo_unico", referencedColumnName = "codigo_unico", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Paquete paquete;

    public Despacha() {
    }

    public Despacha(DespachaPK despachaPK) {
        this.despachaPK = despachaPK;
    }

    public Despacha(DespachaPK despachaPK, Date fechaHora) {
        this.despachaPK = despachaPK;
        this.fechaHora = fechaHora;
    }

    public Despacha(int idOperador, int codigoUnico) {
        this.despachaPK = new DespachaPK(idOperador, codigoUnico);
    }

    public DespachaPK getDespachaPK() {
        return despachaPK;
    }

    public void setDespachaPK(DespachaPK despachaPK) {
        this.despachaPK = despachaPK;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public OperadorDespacho getOperadorDespacho() {
        return operadorDespacho;
    }

    public void setOperadorDespacho(OperadorDespacho operadorDespacho) {
        this.operadorDespacho = operadorDespacho;
    }

    public Paquete getPaquete() {
        return paquete;
    }

    public void setPaquete(Paquete paquete) {
        this.paquete = paquete;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (despachaPK != null ? despachaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Despacha)) {
            return false;
        }
        Despacha other = (Despacha) object;
        if ((this.despachaPK == null && other.despachaPK != null) || (this.despachaPK != null && !this.despachaPK.equals(other.despachaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.Despacha[ despachaPK=" + despachaPK + " ]";
    }
    
}
