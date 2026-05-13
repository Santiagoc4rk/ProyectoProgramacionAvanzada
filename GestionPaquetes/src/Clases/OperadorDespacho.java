/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.io.Serializable;
import java.util.Collection;
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

/**
 *
 * @author andre
 */
@Entity
@Table(name = "operador_despacho")
@NamedQueries({
    @NamedQuery(name = "OperadorDespacho.findAll", query = "SELECT o FROM OperadorDespacho o"),
    @NamedQuery(name = "OperadorDespacho.findByIdOperador", query = "SELECT o FROM OperadorDespacho o WHERE o.idOperador = :idOperador")})
public class OperadorDespacho implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idOperador")
    private Integer idOperador;
    @JoinColumn(name = "cedula", referencedColumnName = "cedula")
    @ManyToOne(optional = false)
    private Persona cedula;
    @JoinColumn(name = "bodega", referencedColumnName = "idLocal")
    @ManyToOne(optional = false)
    private Ubicacion bodega;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "operadorDespacho")
    private Collection<Despacha> despachaCollection;

    public OperadorDespacho() {
    }

    public OperadorDespacho(Integer idOperador) {
        this.idOperador = idOperador;
    }

    public Integer getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(Integer idOperador) {
        this.idOperador = idOperador;
    }

    public Persona getCedula() {
        return cedula;
    }

    public void setCedula(Persona cedula) {
        this.cedula = cedula;
    }

    public Ubicacion getBodega() {
        return bodega;
    }

    public void setBodega(Ubicacion bodega) {
        this.bodega = bodega;
    }

    public Collection<Despacha> getDespachaCollection() {
        return despachaCollection;
    }

    public void setDespachaCollection(Collection<Despacha> despachaCollection) {
        this.despachaCollection = despachaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOperador != null ? idOperador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OperadorDespacho)) {
            return false;
        }
        OperadorDespacho other = (OperadorDespacho) object;
        if ((this.idOperador == null && other.idOperador != null) || (this.idOperador != null && !this.idOperador.equals(other.idOperador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.OperadorDespacho[ idOperador=" + idOperador + " ]";
    }
    
}
