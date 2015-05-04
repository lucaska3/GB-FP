/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Area;
import entity.Autor;
import entity.CasaPub;
import entity.Classe;
import entity.Colecoes;
import entity.Editora;
import entity.Genero;
import entity.Emprestimo;
import entity.Item;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author fp0520141051
 */
public class ItemJpaController implements Serializable {

    public ItemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Item item) throws PreexistingEntityException, Exception {
        if (item.getEmprestimoList() == null) {
            item.setEmprestimoList(new ArrayList<Emprestimo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Area areaItem = item.getAreaItem();
            if (areaItem != null) {
                areaItem = em.getReference(areaItem.getClass(), areaItem.getCodArea());
                item.setAreaItem(areaItem);
            }
            Autor autorItem = item.getAutorItem();
            if (autorItem != null) {
                autorItem = em.getReference(autorItem.getClass(), autorItem.getCodAutor());
                item.setAutorItem(autorItem);
            }
            CasaPub casaPubitem = item.getCasaPubitem();
            if (casaPubitem != null) {
                casaPubitem = em.getReference(casaPubitem.getClass(), casaPubitem.getCodCasa());
                item.setCasaPubitem(casaPubitem);
            }
            Classe cursoItem = item.getCursoItem();
            if (cursoItem != null) {
                cursoItem = em.getReference(cursoItem.getClass(), cursoItem.getCodClas());
                item.setCursoItem(cursoItem);
            }
            Colecoes colecaoItem = item.getColecaoItem();
            if (colecaoItem != null) {
                colecaoItem = em.getReference(colecaoItem.getClass(), colecaoItem.getCodCol());
                item.setColecaoItem(colecaoItem);
            }
            Editora editoraItem = item.getEditoraItem();
            if (editoraItem != null) {
                editoraItem = em.getReference(editoraItem.getClass(), editoraItem.getCodEditora());
                item.setEditoraItem(editoraItem);
            }
            Genero generoItem = item.getGeneroItem();
            if (generoItem != null) {
                generoItem = em.getReference(generoItem.getClass(), generoItem.getCodGenero());
                item.setGeneroItem(generoItem);
            }
            List<Emprestimo> attachedEmprestimoList = new ArrayList<Emprestimo>();
            for (Emprestimo emprestimoListEmprestimoToAttach : item.getEmprestimoList()) {
                emprestimoListEmprestimoToAttach = em.getReference(emprestimoListEmprestimoToAttach.getClass(), emprestimoListEmprestimoToAttach.getCodEmp());
                attachedEmprestimoList.add(emprestimoListEmprestimoToAttach);
            }
            item.setEmprestimoList(attachedEmprestimoList);
            em.persist(item);
            if (areaItem != null) {
                areaItem.getItemList().add(item);
                areaItem = em.merge(areaItem);
            }
            if (autorItem != null) {
                autorItem.getItemList().add(item);
                autorItem = em.merge(autorItem);
            }
            if (casaPubitem != null) {
                casaPubitem.getItemList().add(item);
                casaPubitem = em.merge(casaPubitem);
            }
            if (cursoItem != null) {
                cursoItem.getItemList().add(item);
                cursoItem = em.merge(cursoItem);
            }
            if (colecaoItem != null) {
                colecaoItem.getItemList().add(item);
                colecaoItem = em.merge(colecaoItem);
            }
            if (editoraItem != null) {
                editoraItem.getItemList().add(item);
                editoraItem = em.merge(editoraItem);
            }
            if (generoItem != null) {
                generoItem.getItemList().add(item);
                generoItem = em.merge(generoItem);
            }
            for (Emprestimo emprestimoListEmprestimo : item.getEmprestimoList()) {
                Item oldItemEmpOfEmprestimoListEmprestimo = emprestimoListEmprestimo.getItemEmp();
                emprestimoListEmprestimo.setItemEmp(item);
                emprestimoListEmprestimo = em.merge(emprestimoListEmprestimo);
                if (oldItemEmpOfEmprestimoListEmprestimo != null) {
                    oldItemEmpOfEmprestimoListEmprestimo.getEmprestimoList().remove(emprestimoListEmprestimo);
                    oldItemEmpOfEmprestimoListEmprestimo = em.merge(oldItemEmpOfEmprestimoListEmprestimo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findItem(item.getCodItem()) != null) {
                throw new PreexistingEntityException("Item " + item + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Item item) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Item persistentItem = em.find(Item.class, item.getCodItem());
            Area areaItemOld = persistentItem.getAreaItem();
            Area areaItemNew = item.getAreaItem();
            Autor autorItemOld = persistentItem.getAutorItem();
            Autor autorItemNew = item.getAutorItem();
            CasaPub casaPubitemOld = persistentItem.getCasaPubitem();
            CasaPub casaPubitemNew = item.getCasaPubitem();
            Classe cursoItemOld = persistentItem.getCursoItem();
            Classe cursoItemNew = item.getCursoItem();
            Colecoes colecaoItemOld = persistentItem.getColecaoItem();
            Colecoes colecaoItemNew = item.getColecaoItem();
            Editora editoraItemOld = persistentItem.getEditoraItem();
            Editora editoraItemNew = item.getEditoraItem();
            Genero generoItemOld = persistentItem.getGeneroItem();
            Genero generoItemNew = item.getGeneroItem();
            List<Emprestimo> emprestimoListOld = persistentItem.getEmprestimoList();
            List<Emprestimo> emprestimoListNew = item.getEmprestimoList();
            List<String> illegalOrphanMessages = null;
            for (Emprestimo emprestimoListOldEmprestimo : emprestimoListOld) {
                if (!emprestimoListNew.contains(emprestimoListOldEmprestimo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Emprestimo " + emprestimoListOldEmprestimo + " since its itemEmp field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (areaItemNew != null) {
                areaItemNew = em.getReference(areaItemNew.getClass(), areaItemNew.getCodArea());
                item.setAreaItem(areaItemNew);
            }
            if (autorItemNew != null) {
                autorItemNew = em.getReference(autorItemNew.getClass(), autorItemNew.getCodAutor());
                item.setAutorItem(autorItemNew);
            }
            if (casaPubitemNew != null) {
                casaPubitemNew = em.getReference(casaPubitemNew.getClass(), casaPubitemNew.getCodCasa());
                item.setCasaPubitem(casaPubitemNew);
            }
            if (cursoItemNew != null) {
                cursoItemNew = em.getReference(cursoItemNew.getClass(), cursoItemNew.getCodClas());
                item.setCursoItem(cursoItemNew);
            }
            if (colecaoItemNew != null) {
                colecaoItemNew = em.getReference(colecaoItemNew.getClass(), colecaoItemNew.getCodCol());
                item.setColecaoItem(colecaoItemNew);
            }
            if (editoraItemNew != null) {
                editoraItemNew = em.getReference(editoraItemNew.getClass(), editoraItemNew.getCodEditora());
                item.setEditoraItem(editoraItemNew);
            }
            if (generoItemNew != null) {
                generoItemNew = em.getReference(generoItemNew.getClass(), generoItemNew.getCodGenero());
                item.setGeneroItem(generoItemNew);
            }
            List<Emprestimo> attachedEmprestimoListNew = new ArrayList<Emprestimo>();
            for (Emprestimo emprestimoListNewEmprestimoToAttach : emprestimoListNew) {
                emprestimoListNewEmprestimoToAttach = em.getReference(emprestimoListNewEmprestimoToAttach.getClass(), emprestimoListNewEmprestimoToAttach.getCodEmp());
                attachedEmprestimoListNew.add(emprestimoListNewEmprestimoToAttach);
            }
            emprestimoListNew = attachedEmprestimoListNew;
            item.setEmprestimoList(emprestimoListNew);
            item = em.merge(item);
            if (areaItemOld != null && !areaItemOld.equals(areaItemNew)) {
                areaItemOld.getItemList().remove(item);
                areaItemOld = em.merge(areaItemOld);
            }
            if (areaItemNew != null && !areaItemNew.equals(areaItemOld)) {
                areaItemNew.getItemList().add(item);
                areaItemNew = em.merge(areaItemNew);
            }
            if (autorItemOld != null && !autorItemOld.equals(autorItemNew)) {
                autorItemOld.getItemList().remove(item);
                autorItemOld = em.merge(autorItemOld);
            }
            if (autorItemNew != null && !autorItemNew.equals(autorItemOld)) {
                autorItemNew.getItemList().add(item);
                autorItemNew = em.merge(autorItemNew);
            }
            if (casaPubitemOld != null && !casaPubitemOld.equals(casaPubitemNew)) {
                casaPubitemOld.getItemList().remove(item);
                casaPubitemOld = em.merge(casaPubitemOld);
            }
            if (casaPubitemNew != null && !casaPubitemNew.equals(casaPubitemOld)) {
                casaPubitemNew.getItemList().add(item);
                casaPubitemNew = em.merge(casaPubitemNew);
            }
            if (cursoItemOld != null && !cursoItemOld.equals(cursoItemNew)) {
                cursoItemOld.getItemList().remove(item);
                cursoItemOld = em.merge(cursoItemOld);
            }
            if (cursoItemNew != null && !cursoItemNew.equals(cursoItemOld)) {
                cursoItemNew.getItemList().add(item);
                cursoItemNew = em.merge(cursoItemNew);
            }
            if (colecaoItemOld != null && !colecaoItemOld.equals(colecaoItemNew)) {
                colecaoItemOld.getItemList().remove(item);
                colecaoItemOld = em.merge(colecaoItemOld);
            }
            if (colecaoItemNew != null && !colecaoItemNew.equals(colecaoItemOld)) {
                colecaoItemNew.getItemList().add(item);
                colecaoItemNew = em.merge(colecaoItemNew);
            }
            if (editoraItemOld != null && !editoraItemOld.equals(editoraItemNew)) {
                editoraItemOld.getItemList().remove(item);
                editoraItemOld = em.merge(editoraItemOld);
            }
            if (editoraItemNew != null && !editoraItemNew.equals(editoraItemOld)) {
                editoraItemNew.getItemList().add(item);
                editoraItemNew = em.merge(editoraItemNew);
            }
            if (generoItemOld != null && !generoItemOld.equals(generoItemNew)) {
                generoItemOld.getItemList().remove(item);
                generoItemOld = em.merge(generoItemOld);
            }
            if (generoItemNew != null && !generoItemNew.equals(generoItemOld)) {
                generoItemNew.getItemList().add(item);
                generoItemNew = em.merge(generoItemNew);
            }
            for (Emprestimo emprestimoListNewEmprestimo : emprestimoListNew) {
                if (!emprestimoListOld.contains(emprestimoListNewEmprestimo)) {
                    Item oldItemEmpOfEmprestimoListNewEmprestimo = emprestimoListNewEmprestimo.getItemEmp();
                    emprestimoListNewEmprestimo.setItemEmp(item);
                    emprestimoListNewEmprestimo = em.merge(emprestimoListNewEmprestimo);
                    if (oldItemEmpOfEmprestimoListNewEmprestimo != null && !oldItemEmpOfEmprestimoListNewEmprestimo.equals(item)) {
                        oldItemEmpOfEmprestimoListNewEmprestimo.getEmprestimoList().remove(emprestimoListNewEmprestimo);
                        oldItemEmpOfEmprestimoListNewEmprestimo = em.merge(oldItemEmpOfEmprestimoListNewEmprestimo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = item.getCodItem();
                if (findItem(id) == null) {
                    throw new NonexistentEntityException("The item with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Item item;
            try {
                item = em.getReference(Item.class, id);
                item.getCodItem();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The item with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Emprestimo> emprestimoListOrphanCheck = item.getEmprestimoList();
            for (Emprestimo emprestimoListOrphanCheckEmprestimo : emprestimoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Item (" + item + ") cannot be destroyed since the Emprestimo " + emprestimoListOrphanCheckEmprestimo + " in its emprestimoList field has a non-nullable itemEmp field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Area areaItem = item.getAreaItem();
            if (areaItem != null) {
                areaItem.getItemList().remove(item);
                areaItem = em.merge(areaItem);
            }
            Autor autorItem = item.getAutorItem();
            if (autorItem != null) {
                autorItem.getItemList().remove(item);
                autorItem = em.merge(autorItem);
            }
            CasaPub casaPubitem = item.getCasaPubitem();
            if (casaPubitem != null) {
                casaPubitem.getItemList().remove(item);
                casaPubitem = em.merge(casaPubitem);
            }
            Classe cursoItem = item.getCursoItem();
            if (cursoItem != null) {
                cursoItem.getItemList().remove(item);
                cursoItem = em.merge(cursoItem);
            }
            Colecoes colecaoItem = item.getColecaoItem();
            if (colecaoItem != null) {
                colecaoItem.getItemList().remove(item);
                colecaoItem = em.merge(colecaoItem);
            }
            Editora editoraItem = item.getEditoraItem();
            if (editoraItem != null) {
                editoraItem.getItemList().remove(item);
                editoraItem = em.merge(editoraItem);
            }
            Genero generoItem = item.getGeneroItem();
            if (generoItem != null) {
                generoItem.getItemList().remove(item);
                generoItem = em.merge(generoItem);
            }
            em.remove(item);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Item> findItemEntities() {
        return findItemEntities(true, -1, -1);
    }

    public List<Item> findItemEntities(int maxResults, int firstResult) {
        return findItemEntities(false, maxResults, firstResult);
    }

    private List<Item> findItemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Item.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Item findItem(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Item.class, id);
        } finally {
            em.close();
        }
    }

    public int getItemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Item> rt = cq.from(Item.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
