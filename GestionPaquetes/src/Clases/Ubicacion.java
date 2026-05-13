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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author andre
 */
@Entity
@Table(name = "ubicacion")
@NamedQueries({
    @NamedQuery(name = "Ubicacion.findAll", query = "SELECT u FROM Ubicacion u"),
    @NamedQuery(name = "Ubicacion.findByIdLocal", query = "SELECT u FROM Ubicacion u WHERE u.idLocal = :idLocal"),
    @NamedQuery(name = "Ubicacion.findByNombre", query = "SELECT u FROM Ubicacion u WHERE u.nombre = :nombre"),
    @NamedQuery(name = "Ubicacion.findByDireccion", query = "SELECT u FROM Ubicacion u WHERE u.direccion = :direccion"),
    @NamedQuery(name = "Ubicacion.findByCiudad", query = "SELECT u FROM Ubicacion u WHERE u.ciudad = :ciudad"),
    @NamedQuery(name = "Ubicacion.findByTipo", query = "SELECT u FROM Ubicacion u WHERE u.tipo = :tipo")})
public class Ubicacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idLocal")
    private Integer idLocal;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "direccion")
    private String direccion;
    @Basic(optional = false)
    @Column(name = "ciudad")
    private String ciudad;
    @Basic(optional = false)
    @Column(name = "tipo")
    private String tipo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "local")
    private Collection<Recepcionista> recepcionistaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bodega")
    private Collection<OperadorDespacho> operadorDespachoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ubicacion")
    private Collection<HistorialEstado> historialEstadoCollection;

    public Ubicacion() {
    }

    public Ubicacion(Integer idLocal) {
        this.idLocal = idLocal;
    }

    public Ubicacion(Integer idLocal, String nombre, String direccion, String ciudad, String tipo) {
        this.idLocal = idLocal;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.tipo = tipo;
    }

    public Integer getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(Integer idLocal) {
        this.idLocal = idLocal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Collection<Recepcionista> getRecepcionistaCollection() {
        return recepcionistaCollection;
    }

    public void setRecepcionistaCollection(Collection<Recepcionista> recepcionistaCollection) {
        this.recepcionistaCollection = recepcionistaCollection;
    }

    public Collection<OperadorDespacho> getOperadorDespachoCollection() {
        return operadorDespachoCollection;
    }

    public void setOperadorDespachoCollection(Collection<OperadorDespacho> operadorDespachoCollection) {
        this.operadorDespachoCollection = operadorDespachoCollection;
    }

    public Collection<HistorialEstado> getHistorialEstadoCollection() {
        return historialEstadoCollection;
    }

    public void setHistorialEstadoCollection(Collection<HistorialEstado> historialEstadoCollection) {
        this.historialEstadoCollection = historialEstadoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLocal != null ? idLocal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ubicacion)) {
            return false;
        }
        Ubicacion other = (Ubicacion) object;
        if ((this.idLocal == null && other.idLocal != null) || (this.idLocal != null && !this.idLocal.equals(other.idLocal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.Ubicacion[ idLocal=" + idLocal + " ]";
    }
    
}
