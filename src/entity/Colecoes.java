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
@Table(name = "tbColecoes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Colecoes.findAll", query = "SELECT c FROM Colecoes c"),
    @NamedQuery(name = "Colecoes.findByCodCol", query = "SELECT c FROM Colecoes c WHERE c.codCol = :codCol"),
    @NamedQuery(name = "Colecoes.findByNomeCol", query = "SELECT c FROM Colecoes c WHERE c.nomeCol = :nomeCol"),
    @NamedQuery(name = "Colecoes.findByVolumesCol", query = "SELECT c FROM Colecoes c WHERE c.volumesCol = :volumesCol")})
public class Colecoes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_col")
    private Integer codCol;
    @Basic(optional = false)
    @Column(name = "nome_col")
    private String nomeCol;
    @Basic(optional = false)
    @Column(name = "volumes_col")
    private int volumesCol;
    @OneToMany(mappedBy = "colecaoItem")
    private List<Item> itemList;

    public Colecoes() {
    }

    public Colecoes(Integer codCol) {
        this.codCol = codCol;
    }

    public Colecoes(Integer codCol, String nomeCol, int volumesCol) {
        this.codCol = codCol;
        this.nomeCol = nomeCol;
        this.volumesCol = volumesCol;
    }

    public Integer getCodCol() {
        return codCol;
    }

    public void setCodCol(Integer codCol) {
        this.codCol = codCol;
    }

    public String getNomeCol() {
        return nomeCol;
    }

    public void setNomeCol(String nomeCol) {
        this.nomeCol = nomeCol;
    }

    public int getVolumesCol() {
        return volumesCol;
    }

    public void setVolumesCol(int volumesCol) {
        this.volumesCol = volumesCol;
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
        hash += (codCol != null ? codCol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Colecoes)) {
            return false;
        }
        Colecoes other = (Colecoes) object;
        if ((this.codCol == null && other.codCol != null) || (this.codCol != null && !this.codCol.equals(other.codCol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Colecoes[ codCol=" + codCol + " ]";
    }
    
}
