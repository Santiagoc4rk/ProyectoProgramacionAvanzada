/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author andre
 */
@Embeddable
public class EntregaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "idRepartidor")
    private int idRepartidor;
    @Basic(optional = false)
    @Column(name = "codigo_unico")
    private int codigoUnico;

    public EntregaPK() {
    }

    public EntregaPK(int idRepartidor, int codigoUnico) {
        this.idRepartidor = idRepartidor;
        this.codigoUnico = codigoUnico;
    }

    public int getIdRepartidor() {
        return idRepartidor;
    }

    public void setIdRepartidor(int idRepartidor) {
        this.idRepartidor = idRepartidor;
    }

    public int getCodigoUnico() {
        return codigoUnico;
    }

    public void setCodigoUnico(int codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idRepartidor;
        hash += (int) codigoUnico;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EntregaPK)) {
            return false;
        }
        EntregaPK other = (EntregaPK) object;
        if (this.idRepartidor != other.idRepartidor) {
            return false;
        }
        if (this.codigoUnico != other.codigoUnico) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.EntregaPK[ idRepartidor=" + idRepartidor + ", codigoUnico=" + codigoUnico + " ]";
    }
    
}
