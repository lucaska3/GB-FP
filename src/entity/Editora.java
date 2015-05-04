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
@Table(name = "tbEditora")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Editora.findAll", query = "SELECT e FROM Editora e"),
    @NamedQuery(name = "Editora.findByCodEditora", query = "SELECT e FROM Editora e WHERE e.codEditora = :codEditora"),
    @NamedQuery(name = "Editora.findByNomeEditora", query = "SELECT e FROM Editora e WHERE e.nomeEditora = :nomeEditora")})
public class Editora implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_editora")
    private Integer codEditora;
    @Basic(optional = false)
    @Column(name = "nome_editora")
    private String nomeEditora;
    @OneToMany(mappedBy = "editoraItem")
    private List<Item> itemList;

    public Editora() {
    }

    public Editora(Integer codEditora) {
        this.codEditora = codEditora;
    }

    public Editora(Integer codEditora, String nomeEditora) {
        this.codEditora = codEditora;
        this.nomeEditora = nomeEditora;
    }

    public Integer getCodEditora() {
        return codEditora;
    }

    public void setCodEditora(Integer codEditora) {
        this.codEditora = codEditora;
    }

    public String getNomeEditora() {
        return nomeEditora;
    }

    public void setNomeEditora(String nomeEditora) {
        this.nomeEditora = nomeEditora;
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
        hash += (codEditora != null ? codEditora.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Editora)) {
            return false;
        }
        Editora other = (Editora) object;
        if ((this.codEditora == null && other.codEditora != null) || (this.codEditora != null && !this.codEditora.equals(other.codEditora))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Editora[ codEditora=" + codEditora + " ]";
    }
    
}
