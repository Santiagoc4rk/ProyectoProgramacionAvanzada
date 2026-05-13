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
@Table(name = "recepcionista")
@NamedQueries({
    @NamedQuery(name = "Recepcionista.findAll", query = "SELECT r FROM Recepcionista r"),
    @NamedQuery(name = "Recepcionista.findByIdRecepcionista", query = "SELECT r FROM Recepcionista r WHERE r.idRecepcionista = :idRecepcionista"),
    @NamedQuery(name = "Recepcionista.findByTurno", query = "SELECT r FROM Recepcionista r WHERE r.turno = :turno")})
public class Recepcionista implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idRecepcionista")
    private Integer idRecepcionista;
    @Basic(optional = false)
    @Column(name = "turno")
    private String turno;
    @JoinColumn(name = "cedula", referencedColumnName = "cedula")
    @ManyToOne(optional = false)
    private Persona cedula;
    @JoinColumn(name = "local", referencedColumnName = "idLocal")
    @ManyToOne(optional = false)
    private Ubicacion local;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idRecepcionista")
    private Collection<Paquete> paqueteCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idRecepcionista")
    private Collection<Cliente> clienteCollection;

    public Recepcionista() {
    }

    public Recepcionista(Integer idRecepcionista) {
        this.idRecepcionista = idRecepcionista;
    }

    public Recepcionista(Integer idRecepcionista, String turno) {
        this.idRecepcionista = idRecepcionista;
        this.turno = turno;
    }

    public Integer getIdRecepcionista() {
        return idRecepcionista;
    }

    public void setIdRecepcionista(Integer idRecepcionista) {
        this.idRecepcionista = idRecepcionista;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public Persona getCedula() {
        return cedula;
    }

    public void setCedula(Persona cedula) {
        this.cedula = cedula;
    }

    public Ubicacion getLocal() {
        return local;
    }

    public void setLocal(Ubicacion local) {
        this.local = local;
    }

    public Collection<Paquete> getPaqueteCollection() {
        return paqueteCollection;
    }

    public void setPaqueteCollection(Collection<Paquete> paqueteCollection) {
        this.paqueteCollection = paqueteCollection;
    }

    public Collection<Cliente> getClienteCollection() {
        return clienteCollection;
    }

    public void setClienteCollection(Collection<Cliente> clienteCollection) {
        this.clienteCollection = clienteCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRecepcionista != null ? idRecepcionista.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Recepcionista)) {
            return false;
        }
        Recepcionista other = (Recepcionista) object;
        if ((this.idRecepcionista == null && other.idRecepcionista != null) || (this.idRecepcionista != null && !this.idRecepcionista.equals(other.idRecepcionista))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.Recepcionista[ idRecepcionista=" + idRecepcionista + " ]";
    }
    
}
