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
@Table(name = "tbAutor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Autor.findAll", query = "SELECT a FROM Autor a"),
    @NamedQuery(name = "Autor.findByCodAutor", query = "SELECT a FROM Autor a WHERE a.codAutor = :codAutor"),
    @NamedQuery(name = "Autor.findByNascAutor", query = "SELECT a FROM Autor a WHERE a.nascAutor = :nascAutor"),
    @NamedQuery(name = "Autor.findByNomeAutor", query = "SELECT a FROM Autor a WHERE a.nomeAutor = :nomeAutor")})
public class Autor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_autor")
    private Integer codAutor;
    @Column(name = "nasc_autor")
    private Integer nascAutor;
    @Basic(optional = false)
    @Column(name = "nome_autor")
    private String nomeAutor;
    @OneToMany(mappedBy = "autorItem")
    private List<Item> itemList;

    public Autor() {
    }

    public Autor(Integer codAutor) {
        this.codAutor = codAutor;
    }

    public Autor(Integer codAutor, String nomeAutor) {
        this.codAutor = codAutor;
        this.nomeAutor = nomeAutor;
    }

    public Integer getCodAutor() {
        return codAutor;
    }

    public void setCodAutor(Integer codAutor) {
        this.codAutor = codAutor;
    }

    public Integer getNascAutor() {
        return nascAutor;
    }

    public void setNascAutor(Integer nascAutor) {
        this.nascAutor = nascAutor;
    }

    public String getNomeAutor() {
        return nomeAutor;
    }

    public void setNomeAutor(String nomeAutor) {
        this.nomeAutor = nomeAutor;
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
        hash += (codAutor != null ? codAutor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Autor)) {
            return false;
        }
        Autor other = (Autor) object;
        if ((this.codAutor == null && other.codAutor != null) || (this.codAutor != null && !this.codAutor.equals(other.codAutor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Autor[ codAutor=" + codAutor + " ]";
    }
    
}
