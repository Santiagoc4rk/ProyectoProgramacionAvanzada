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
public class DespachaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "idOperador")
    private int idOperador;
    @Basic(optional = false)
    @Column(name = "codigo_unico")
    private int codigoUnico;

    public DespachaPK() {
    }

    public DespachaPK(int idOperador, int codigoUnico) {
        this.idOperador = idOperador;
        this.codigoUnico = codigoUnico;
    }

    public int getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(int idOperador) {
        this.idOperador = idOperador;
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
        hash += (int) idOperador;
        hash += (int) codigoUnico;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DespachaPK)) {
            return false;
        }
        DespachaPK other = (DespachaPK) object;
        if (this.idOperador != other.idOperador) {
            return false;
        }
        if (this.codigoUnico != other.codigoUnico) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.DespachaPK[ idOperador=" + idOperador + ", codigoUnico=" + codigoUnico + " ]";
    }
    
}
