/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author fp0520141051
 */
@Entity
@Table(name = "tbEmprestimo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Emprestimo.findAll", query = "SELECT e FROM Emprestimo e"),
    @NamedQuery(name = "Emprestimo.findByCodEmp", query = "SELECT e FROM Emprestimo e WHERE e.codEmp = :codEmp"),
    @NamedQuery(name = "Emprestimo.findByDataDevemp", query = "SELECT e FROM Emprestimo e WHERE e.dataDevemp = :dataDevemp"),
    @NamedQuery(name = "Emprestimo.findByDataEmpemp", query = "SELECT e FROM Emprestimo e WHERE e.dataEmpemp = :dataEmpemp"),
    @NamedQuery(name = "Emprestimo.findByMultaEmp", query = "SELECT e FROM Emprestimo e WHERE e.multaEmp = :multaEmp"),
    @NamedQuery(name = "Emprestimo.findByTipoItememp", query = "SELECT e FROM Emprestimo e WHERE e.tipoItememp = :tipoItememp"),
    @NamedQuery(name = "Emprestimo.findByTipoPesemp", query = "SELECT e FROM Emprestimo e WHERE e.tipoPesemp = :tipoPesemp")})
public class Emprestimo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_emp")
    private Integer codEmp;
    @Basic(optional = false)
    @Column(name = "dataDev_emp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataDevemp;
    @Basic(optional = false)
    @Column(name = "dataEmp_emp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEmpemp;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "multa_emp")
    private Double multaEmp;
    @Basic(optional = false)
    @Column(name = "tipoItem_emp")
    private Character tipoItememp;
    @Basic(optional = false)
    @Column(name = "tipoPes_emp")
    private Character tipoPesemp;
    @JoinColumn(name = "item_emp", referencedColumnName = "cod_item")
    @ManyToOne(optional = false)
    private Item itemEmp;
    @JoinColumn(name = "pes_emp", referencedColumnName = "cod_pes")
    @ManyToOne(optional = false)
    private Pessoas pesEmp;
    @OneToMany(mappedBy = "empPes")
    private List<Pessoas> pessoasList;

    public Emprestimo() {
    }

    public Emprestimo(Integer codEmp) {
        this.codEmp = codEmp;
    }

    public Emprestimo(Integer codEmp, Date dataDevemp, Date dataEmpemp, Character tipoItememp, Character tipoPesemp) {
        this.codEmp = codEmp;
        this.dataDevemp = dataDevemp;
        this.dataEmpemp = dataEmpemp;
        this.tipoItememp = tipoItememp;
        this.tipoPesemp = tipoPesemp;
    }

    public Integer getCodEmp() {
        return codEmp;
    }

    public void setCodEmp(Integer codEmp) {
        this.codEmp = codEmp;
    }

    public Date getDataDevemp() {
        return dataDevemp;
    }

    public void setDataDevemp(Date dataDevemp) {
        this.dataDevemp = dataDevemp;
    }

    public Date getDataEmpemp() {
        return dataEmpemp;
    }

    public void setDataEmpemp(Date dataEmpemp) {
        this.dataEmpemp = dataEmpemp;
    }

    public Double getMultaEmp() {
        return multaEmp;
    }

    public void setMultaEmp(Double multaEmp) {
        this.multaEmp = multaEmp;
    }

    public Character getTipoItememp() {
        return tipoItememp;
    }

    public void setTipoItememp(Character tipoItememp) {
        this.tipoItememp = tipoItememp;
    }

    public Character getTipoPesemp() {
        return tipoPesemp;
    }

    public void setTipoPesemp(Character tipoPesemp) {
        this.tipoPesemp = tipoPesemp;
    }

    public Item getItemEmp() {
        return itemEmp;
    }

    public void setItemEmp(Item itemEmp) {
        this.itemEmp = itemEmp;
    }

    public Pessoas getPesEmp() {
        return pesEmp;
    }

    public void setPesEmp(Pessoas pesEmp) {
        this.pesEmp = pesEmp;
    }

    @XmlTransient
    public List<Pessoas> getPessoasList() {
        return pessoasList;
    }

    public void setPessoasList(List<Pessoas> pessoasList) {
        this.pessoasList = pessoasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codEmp != null ? codEmp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Emprestimo)) {
            return false;
        }
        Emprestimo other = (Emprestimo) object;
        if ((this.codEmp == null && other.codEmp != null) || (this.codEmp != null && !this.codEmp.equals(other.codEmp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Emprestimo[ codEmp=" + codEmp + " ]";
    }
    
}
