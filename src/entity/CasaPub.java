/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author fp0520141051
 */
@Entity
@Table(name = "tbCasaPub")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CasaPub.findAll", query = "SELECT c FROM CasaPub c"),
    @NamedQuery(name = "CasaPub.findByCodCasa", query = "SELECT c FROM CasaPub c WHERE c.codCasa = :codCasa"),
    @NamedQuery(name = "CasaPub.findByDescCasa", query = "SELECT c FROM CasaPub c WHERE c.descCasa = :descCasa")})
public class CasaPub implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_casa")
    private Integer codCasa;
    @Basic(optional = false)
    @Column(name = "desc_casa")
    private String descCasa;
    @OneToMany(mappedBy = "casaPubitem")
    private List<Item> itemList;

    public CasaPub() {
    }

    public CasaPub(Integer codCasa) {
        this.codCasa = codCasa;
    }

    public CasaPub(Integer codCasa, String descCasa) {
        this.codCasa = codCasa;
        this.descCasa = descCasa;
    }

    public Integer getCodCasa() {
        return codCasa;
    }

    public void setCodCasa(Integer codCasa) {
        this.codCasa = codCasa;
    }

    public String getDescCasa() {
        return descCasa;
    }

    public void setDescCasa(String descCasa) {
        this.descCasa = descCasa;
    }

    @XmlTransient
    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codCasa != null ? codCasa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CasaPub)) {
            return false;
        }
        CasaPub other = (CasaPub) object;
        if ((this.codCasa == null && other.codCasa != null) || (this.codCasa != null && !this.codCasa.equals(other.codCasa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CasaPub[ codCasa=" + codCasa + " ]";
    }
    
}
