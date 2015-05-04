/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author fp0520141051
 */
@Entity
@Table(name = "tbPessoas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pessoas.findAll", query = "SELECT p FROM Pessoas p"),
    @NamedQuery(name = "Pessoas.findByCodPes", query = "SELECT p FROM Pessoas p WHERE p.codPes = :codPes"),
    @NamedQuery(name = "Pessoas.findByCpfPes", query = "SELECT p FROM Pessoas p WHERE p.cpfPes = :cpfPes"),
    @NamedQuery(name = "Pessoas.findByDataExpirapes", query = "SELECT p FROM Pessoas p WHERE p.dataExpirapes = :dataExpirapes"),
    @NamedQuery(name = "Pessoas.findByDataIngressopes", query = "SELECT p FROM Pessoas p WHERE p.dataIngressopes = :dataIngressopes"),
    @NamedQuery(name = "Pessoas.findByIdPes", query = "SELECT p FROM Pessoas p WHERE p.idPes = :idPes"),
    @NamedQuery(name = "Pessoas.findByNascPes", query = "SELECT p FROM Pessoas p WHERE p.nascPes = :nascPes"),
    @NamedQuery(name = "Pessoas.findByNomePes", query = "SELECT p FROM Pessoas p WHERE p.nomePes = :nomePes"),
    @NamedQuery(name = "Pessoas.findByRgPes", query = "SELECT p FROM Pessoas p WHERE p.rgPes = :rgPes"),
    @NamedQuery(name = "Pessoas.findBySexoPes", query = "SELECT p FROM Pessoas p WHERE p.sexoPes = :sexoPes"),
    @NamedQuery(name = "Pessoas.findByTipoPes", query = "SELECT p FROM Pessoas p WHERE p.tipoPes = :tipoPes")})
public class Pessoas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_pes")
    private Integer codPes;
    @Column(name = "cpf_pes")
    private BigInteger cpfPes;
    @Column(name = "dataExpira_pes")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataExpirapes;
    @Column(name = "dataIngresso_pes")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataIngressopes;
    @Basic(optional = false)
    @Column(name = "id_pes")
    private String idPes;
    @Column(name = "nasc_pes")
    @Temporal(TemporalType.TIMESTAMP)
    private Date nascPes;
    @Basic(optional = false)
    @Column(name = "nome_pes")
    private String nomePes;
    @Column(name = "rg_pes")
    private BigInteger rgPes;
    @Basic(optional = false)
    @Column(name = "sexo_pes")
    private Character sexoPes;
    @Basic(optional = false)
    @Column(name = "tipo_pes")
    private Character tipoPes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pesEmp")
    private List<Emprestimo> emprestimoList;
    @JoinColumn(name = "classe_pes", referencedColumnName = "cod_clas")
    @ManyToOne
    private Classe classePes;
    @JoinColumn(name = "emp_pes", referencedColumnName = "cod_emp")
    @ManyToOne
    private Emprestimo empPes;

    public Pessoas() {
    }

    public Pessoas(Integer codPes) {
        this.codPes = codPes;
    }

    public Pessoas(Integer codPes, String idPes, String nomePes, Character sexoPes, Character tipoPes) {
        this.codPes = codPes;
        this.idPes = idPes;
        this.nomePes = nomePes;
        this.sexoPes = sexoPes;
        this.tipoPes = tipoPes;
    }

    public Integer getCodPes() {
        return codPes;
    }

    public void setCodPes(Integer codPes) {
        this.codPes = codPes;
    }

    public BigInteger getCpfPes() {
        return cpfPes;
    }

    public void setCpfPes(BigInteger cpfPes) {
        this.cpfPes = cpfPes;
    }

    public Date getDataExpirapes() {
        return dataExpirapes;
    }

    public void setDataExpirapes(Date dataExpirapes) {
        this.dataExpirapes = dataExpirapes;
    }

    public Date getDataIngressopes() {
        return dataIngressopes;
    }

    public void setDataIngressopes(Date dataIngressopes) {
        this.dataIngressopes = dataIngressopes;
    }

    public String getIdPes() {
        return idPes;
    }

    public void setIdPes(String idPes) {
        this.idPes = idPes;
    }

    public Date getNascPes() {
        return nascPes;
    }

    public void setNascPes(Date nascPes) {
        this.nascPes = nascPes;
    }

    public String getNomePes() {
        return nomePes;
    }

    public void setNomePes(String nomePes) {
        this.nomePes = nomePes;
    }

    public BigInteger getRgPes() {
        return rgPes;
    }

    public void setRgPes(BigInteger rgPes) {
        this.rgPes = rgPes;
    }

    public Character getSexoPes() {
        return sexoPes;
    }

    public void setSexoPes(Character sexoPes) {
        this.sexoPes = sexoPes;
    }

    public Character getTipoPes() {
        return tipoPes;
    }

    public void setTipoPes(Character tipoPes) {
        this.tipoPes = tipoPes;
    }

    @XmlTransient
    public List<Emprestimo> getEmprestimoList() {
        return emprestimoList;
    }

    public void setEmprestimoList(List<Emprestimo> emprestimoList) {
        this.emprestimoList = emprestimoList;
    }

    public Classe getClassePes() {
        return classePes;
    }

    public void setClassePes(Classe classePes) {
        this.classePes = classePes;
    }

    public Emprestimo getEmpPes() {
        return empPes;
    }

    public void setEmpPes(Emprestimo empPes) {
        this.empPes = empPes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codPes != null ? codPes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pessoas)) {
            return false;
        }
        Pessoas other = (Pessoas) object;
        if ((this.codPes == null && other.codPes != null) || (this.codPes != null && !this.codPes.equals(other.codPes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Pessoas[ codPes=" + codPes + " ]";
    }
    
}
