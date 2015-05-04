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
@Table(name = "tbClasse")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Classe.findAll", query = "SELECT c FROM Classe c"),
    @NamedQuery(name = "Classe.findByCodClas", query = "SELECT c FROM Classe c WHERE c.codClas = :codClas"),
    @NamedQuery(name = "Classe.findByCursoClas", query = "SELECT c FROM Classe c WHERE c.cursoClas = :cursoClas"),
    @NamedQuery(name = "Classe.findByModuloClas", query = "SELECT c FROM Classe c WHERE c.moduloClas = :moduloClas"),
    @NamedQuery(name = "Classe.findByPeriodoClas", query = "SELECT c FROM Classe c WHERE c.periodoClas = :periodoClas")})
public class Classe implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_clas")
    private Integer codClas;
    @Basic(optional = false)
    @Column(name = "curso_clas")
    private String cursoClas;
    @Basic(optional = false)
    @Column(name = "modulo_clas")
    private int moduloClas;
    @Basic(optional = false)
    @Column(name = "periodo_clas")
    private String periodoClas;
    @OneToMany(mappedBy = "classePes")
    private List<Pessoas> pessoasList;
    @OneToMany(mappedBy = "cursoItem")
    private List<Item> itemList;

    public Classe() {
    }

    public Classe(Integer codClas) {
        this.codClas = codClas;
    }

    public Classe(Integer codClas, String cursoClas, int moduloClas, String periodoClas) {
        this.codClas = codClas;
        this.cursoClas = cursoClas;
        this.moduloClas = moduloClas;
        this.periodoClas = periodoClas;
    }

    public Integer getCodClas() {
        return codClas;
    }

    public void setCodClas(Integer codClas) {
        this.codClas = codClas;
    }

    public String getCursoClas() {
        return cursoClas;
    }

    public void setCursoClas(String cursoClas) {
        this.cursoClas = cursoClas;
    }

    public int getModuloClas() {
        return moduloClas;
    }

    public void setModuloClas(int moduloClas) {
        this.moduloClas = moduloClas;
    }

    public String getPeriodoClas() {
        return periodoClas;
    }

    public void setPeriodoClas(String periodoClas) {
        this.periodoClas = periodoClas;
    }

    @XmlTransient
    public List<Pessoas> getPessoasList() {
        return pessoasList;
    }

    public void setPessoasList(List<Pessoas> pessoasList) {
        this.pessoasList = pessoasList;
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
        hash += (codClas != null ? codClas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Classe)) {
            return false;
        }
        Classe other = (Classe) object;
        if ((this.codClas == null && other.codClas != null) || (this.codClas != null && !this.codClas.equals(other.codClas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Classe[ codClas=" + codClas + " ]";
    }
    
}
