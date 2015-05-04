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
@Table(name = "tbItem")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Item.findAll", query = "SELECT i FROM Item i"),
    @NamedQuery(name = "Item.findByCodItem", query = "SELECT i FROM Item i WHERE i.codItem = :codItem"),
    @NamedQuery(name = "Item.findByAnoItem", query = "SELECT i FROM Item i WHERE i.anoItem = :anoItem"),
    @NamedQuery(name = "Item.findByCondicaoItem", query = "SELECT i FROM Item i WHERE i.condicaoItem = :condicaoItem"),
    @NamedQuery(name = "Item.findByDataAquisicaoitem", query = "SELECT i FROM Item i WHERE i.dataAquisicaoitem = :dataAquisicaoitem"),
    @NamedQuery(name = "Item.findByEdicaoItem", query = "SELECT i FROM Item i WHERE i.edicaoItem = :edicaoItem"),
    @NamedQuery(name = "Item.findByIdiomaItem", query = "SELECT i FROM Item i WHERE i.idiomaItem = :idiomaItem"),
    @NamedQuery(name = "Item.findByLegendaItem", query = "SELECT i FROM Item i WHERE i.legendaItem = :legendaItem"),
    @NamedQuery(name = "Item.findByLocalizacaoItem", query = "SELECT i FROM Item i WHERE i.localizacaoItem = :localizacaoItem"),
    @NamedQuery(name = "Item.findByObsItem", query = "SELECT i FROM Item i WHERE i.obsItem = :obsItem"),
    @NamedQuery(name = "Item.findByPaginasItem", query = "SELECT i FROM Item i WHERE i.paginasItem = :paginasItem"),
    @NamedQuery(name = "Item.findByTipoItem", query = "SELECT i FROM Item i WHERE i.tipoItem = :tipoItem"),
    @NamedQuery(name = "Item.findByTituloItem", query = "SELECT i FROM Item i WHERE i.tituloItem = :tituloItem"),
    @NamedQuery(name = "Item.findByVolumeAtualitem", query = "SELECT i FROM Item i WHERE i.volumeAtualitem = :volumeAtualitem")})
public class Item implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_item")
    private Integer codItem;
    @Column(name = "ano_item")
    private Integer anoItem;
    @Basic(optional = false)
    @Column(name = "condicao_item")
    private int condicaoItem;
    @Column(name = "dataAquisicao_item")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAquisicaoitem;
    @Column(name = "edicao_item")
    private Integer edicaoItem;
    @Basic(optional = false)
    @Column(name = "idioma_item")
    private String idiomaItem;
    @Column(name = "legenda_item")
    private String legendaItem;
    @Basic(optional = false)
    @Column(name = "localizacao_item")
    private String localizacaoItem;
    @Column(name = "obs_item")
    private String obsItem;
    @Column(name = "paginas_item")
    private Integer paginasItem;
    @Basic(optional = false)
    @Column(name = "tipo_item")
    private Character tipoItem;
    @Basic(optional = false)
    @Column(name = "titulo_item")
    private String tituloItem;
    @Column(name = "volumeAtual_item")
    private Integer volumeAtualitem;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itemEmp")
    private List<Emprestimo> emprestimoList;
    @JoinColumn(name = "area_item", referencedColumnName = "cod_area")
    @ManyToOne
    private Area areaItem;
    @JoinColumn(name = "autor_item", referencedColumnName = "cod_autor")
    @ManyToOne
    private Autor autorItem;
    @JoinColumn(name = "casaPub_item", referencedColumnName = "cod_casa")
    @ManyToOne
    private CasaPub casaPubitem;
    @JoinColumn(name = "curso_item", referencedColumnName = "cod_clas")
    @ManyToOne
    private Classe cursoItem;
    @JoinColumn(name = "colecao_item", referencedColumnName = "cod_col")
    @ManyToOne
    private Colecoes colecaoItem;
    @JoinColumn(name = "editora_item", referencedColumnName = "cod_editora")
    @ManyToOne
    private Editora editoraItem;
    @JoinColumn(name = "genero_item", referencedColumnName = "cod_genero")
    @ManyToOne
    private Genero generoItem;

    public Item() {
    }

    public Item(Integer codItem) {
        this.codItem = codItem;
    }

    public Item(Integer codItem, int condicaoItem, String idiomaItem, String localizacaoItem, Character tipoItem, String tituloItem) {
        this.codItem = codItem;
        this.condicaoItem = condicaoItem;
        this.idiomaItem = idiomaItem;
        this.localizacaoItem = localizacaoItem;
        this.tipoItem = tipoItem;
        this.tituloItem = tituloItem;
    }

    public Integer getCodItem() {
        return codItem;
    }

    public void setCodItem(Integer codItem) {
        this.codItem = codItem;
    }

    public Integer getAnoItem() {
        return anoItem;
    }

    public void setAnoItem(Integer anoItem) {
        this.anoItem = anoItem;
    }

    public int getCondicaoItem() {
        return condicaoItem;
    }

    public void setCondicaoItem(int condicaoItem) {
        this.condicaoItem = condicaoItem;
    }

    public Date getDataAquisicaoitem() {
        return dataAquisicaoitem;
    }

    public void setDataAquisicaoitem(Date dataAquisicaoitem) {
        this.dataAquisicaoitem = dataAquisicaoitem;
    }

    public Integer getEdicaoItem() {
        return edicaoItem;
    }

    public void setEdicaoItem(Integer edicaoItem) {
        this.edicaoItem = edicaoItem;
    }

    public String getIdiomaItem() {
        return idiomaItem;
    }

    public void setIdiomaItem(String idiomaItem) {
        this.idiomaItem = idiomaItem;
    }

    public String getLegendaItem() {
        return legendaItem;
    }

    public void setLegendaItem(String legendaItem) {
        this.legendaItem = legendaItem;
    }

    public String getLocalizacaoItem() {
        return localizacaoItem;
    }

    public void setLocalizacaoItem(String localizacaoItem) {
        this.localizacaoItem = localizacaoItem;
    }

    public String getObsItem() {
        return obsItem;
    }

    public void setObsItem(String obsItem) {
        this.obsItem = obsItem;
    }

    public Integer getPaginasItem() {
        return paginasItem;
    }

    public void setPaginasItem(Integer paginasItem) {
        this.paginasItem = paginasItem;
    }

    public Character getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(Character tipoItem) {
        this.tipoItem = tipoItem;
    }

    public String getTituloItem() {
        return tituloItem;
    }

    public void setTituloItem(String tituloItem) {
        this.tituloItem = tituloItem;
    }

    public Integer getVolumeAtualitem() {
        return volumeAtualitem;
    }

    public void setVolumeAtualitem(Integer volumeAtualitem) {
        this.volumeAtualitem = volumeAtualitem;
    }

    @XmlTransient
    public List<Emprestimo> getEmprestimoList() {
        return emprestimoList;
    }

    public void setEmprestimoList(List<Emprestimo> emprestimoList) {
        this.emprestimoList = emprestimoList;
    }

    public Area getAreaItem() {
        return areaItem;
    }

    public void setAreaItem(Area areaItem) {
        this.areaItem = areaItem;
    }

    public Autor getAutorItem() {
        return autorItem;
    }

    public void setAutorItem(Autor autorItem) {
        this.autorItem = autorItem;
    }

    public CasaPub getCasaPubitem() {
        return casaPubitem;
    }

    public void setCasaPubitem(CasaPub casaPubitem) {
        this.casaPubitem = casaPubitem;
    }

    public Classe getCursoItem() {
        return cursoItem;
    }

    public void setCursoItem(Classe cursoItem) {
        this.cursoItem = cursoItem;
    }

    public Colecoes getColecaoItem() {
        return colecaoItem;
    }

    public void setColecaoItem(Colecoes colecaoItem) {
        this.colecaoItem = colecaoItem;
    }

    public Editora getEditoraItem() {
        return editoraItem;
    }

    public void setEditoraItem(Editora editoraItem) {
        this.editoraItem = editoraItem;
    }

    public Genero getGeneroItem() {
        return generoItem;
    }

    public void setGeneroItem(Genero generoItem) {
        this.generoItem = generoItem;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codItem != null ? codItem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Item)) {
            return false;
        }
        Item other = (Item) object;
        if ((this.codItem == null && other.codItem != null) || (this.codItem != null && !this.codItem.equals(other.codItem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Item[ codItem=" + codItem + " ]";
    }
    
}
