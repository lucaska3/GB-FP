/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import entity.Classe;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Pessoas;
import java.util.ArrayList;
import java.util.List;
import entity.Item;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author fp0520141051
 */
public class ClasseJpaController implements Serializable {

    public ClasseJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Classe classe) throws PreexistingEntityException, Exception {
        if (classe.getPessoasList() == null) {
            classe.setPessoasList(new ArrayList<Pessoas>());
        }
        if (classe.getItemList() == null) {
            classe.setItemList(new ArrayList<Item>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Pessoas> attachedPessoasList = new ArrayList<Pessoas>();
            for (Pessoas pessoasListPessoasToAttach : classe.getPessoasList()) {
                pessoasListPessoasToAttach = em.getReference(pessoasListPessoasToAttach.getClass(), pessoasListPessoasToAttach.getCodPes());
                attachedPessoasList.add(pessoasListPessoasToAttach);
            }
            classe.setPessoasList(attachedPessoasList);
            List<Item> attachedItemList = new ArrayList<Item>();
            for (Item itemListItemToAttach : classe.getItemList()) {
                itemListItemToAttach = em.getReference(itemListItemToAttach.getClass(), itemListItemToAttach.getCodItem());
                attachedItemList.add(itemListItemToAttach);
            }
            classe.setItemList(attachedItemList);
            em.persist(classe);
            for (Pessoas pessoasListPessoas : classe.getPessoasList()) {
                Classe oldClassePesOfPessoasListPessoas = pessoasListPessoas.getClassePes();
                pessoasListPessoas.setClassePes(classe);
                pessoasListPessoas = em.merge(pessoasListPessoas);
                if (oldClassePesOfPessoasListPessoas != null) {
                    oldClassePesOfPessoasListPessoas.getPessoasList().remove(pessoasListPessoas);
                    oldClassePesOfPessoasListPessoas = em.merge(oldClassePesOfPessoasListPessoas);
                }
            }
            for (Item itemListItem : classe.getItemList()) {
                Classe oldCursoItemOfItemListItem = itemListItem.getCursoItem();
                itemListItem.setCursoItem(classe);
                itemListItem = em.merge(itemListItem);
                if (oldCursoItemOfItemListItem != null) {
                    oldCursoItemOfItemListItem.getItemList().remove(itemListItem);
                    oldCursoItemOfItemListItem = em.merge(oldCursoItemOfItemListItem);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findClasse(classe.getCodClas()) != null) {
                throw new PreexistingEntityException("Classe " + classe + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Classe classe) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Classe persistentClasse = em.find(Classe.class, classe.getCodClas());
            List<Pessoas> pessoasListOld = persistentClasse.getPessoasList();
            List<Pessoas> pessoasListNew = classe.getPessoasList();
            List<Item> itemListOld = persistentClasse.getItemList();
            List<Item> itemListNew = classe.getItemList();
            List<Pessoas> attachedPessoasListNew = new ArrayList<Pessoas>();
            for (Pessoas pessoasListNewPessoasToAttach : pessoasListNew) {
                pessoasListNewPessoasToAttach = em.getReference(pessoasListNewPessoasToAttach.getClass(), pessoasListNewPessoasToAttach.getCodPes());
                attachedPessoasListNew.add(pessoasListNewPessoasToAttach);
            }
            pessoasListNew = attachedPessoasListNew;
            classe.setPessoasList(pessoasListNew);
            List<Item> attachedItemListNew = new ArrayList<Item>();
            for (Item itemListNewItemToAttach : itemListNew) {
                itemListNewItemToAttach = em.getReference(itemListNewItemToAttach.getClass(), itemListNewItemToAttach.getCodItem());
                attachedItemListNew.add(itemListNewItemToAttach);
            }
            itemListNew = attachedItemListNew;
            classe.setItemList(itemListNew);
            classe = em.merge(classe);
            for (Pessoas pessoasListOldPessoas : pessoasListOld) {
                if (!pessoasListNew.contains(pessoasListOldPessoas)) {
                    pessoasListOldPessoas.setClassePes(null);
                    pessoasListOldPessoas = em.merge(pessoasListOldPessoas);
                }
            }
            for (Pessoas pessoasListNewPessoas : pessoasListNew) {
                if (!pessoasListOld.contains(pessoasListNewPessoas)) {
                    Classe oldClassePesOfPessoasListNewPessoas = pessoasListNewPessoas.getClassePes();
                    pessoasListNewPessoas.setClassePes(classe);
                    pessoasListNewPessoas = em.merge(pessoasListNewPessoas);
                    if (oldClassePesOfPessoasListNewPessoas != null && !oldClassePesOfPessoasListNewPessoas.equals(classe)) {
                        oldClassePesOfPessoasListNewPessoas.getPessoasList().remove(pessoasListNewPessoas);
                        oldClassePesOfPessoasListNewPessoas = em.merge(oldClassePesOfPessoasListNewPessoas);
                    }
                }
            }
            for (Item itemListOldItem : itemListOld) {
                if (!itemListNew.contains(itemListOldItem)) {
                    itemListOldItem.setCursoItem(null);
                    itemListOldItem = em.merge(itemListOldItem);
                }
            }
            for (Item itemListNewItem : itemListNew) {
                if (!itemListOld.contains(itemListNewItem)) {
                    Classe oldCursoItemOfItemListNewItem = itemListNewItem.getCursoItem();
                    itemListNewItem.setCursoItem(classe);
                    itemListNewItem = em.merge(itemListNewItem);
                    if (oldCursoItemOfItemListNewItem != null && !oldCursoItemOfItemListNewItem.equals(classe)) {
                        oldCursoItemOfItemListNewItem.getItemList().remove(itemListNewItem);
                        oldCursoItemOfItemListNewItem = em.merge(oldCursoItemOfItemListNewItem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = classe.getCodClas();
                if (findClasse(id) == null) {
                    throw new NonexistentEntityException("The classe with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Classe classe;
            try {
                classe = em.getReference(Classe.class, id);
                classe.getCodClas();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The classe with id " + id + " no longer exists.", enfe);
            }
            List<Pessoas> pessoasList = classe.getPessoasList();
            for (Pessoas pessoasListPessoas : pessoasList) {
                pessoasListPessoas.setClassePes(null);
                pessoasListPessoas = em.merge(pessoasListPessoas);
            }
            List<Item> itemList = classe.getItemList();
            for (Item itemListItem : itemList) {
                itemListItem.setCursoItem(null);
                itemListItem = em.merge(itemListItem);
            }
            em.remove(classe);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Classe> findClasseEntities() {
        return findClasseEntities(true, -1, -1);
    }

    public List<Classe> findClasseEntities(int maxResults, int firstResult) {
        return findClasseEntities(false, maxResults, firstResult);
    }

    private List<Classe> findClasseEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Classe.class));
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

    public Classe findClasse(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Classe.class, id);
        } finally {
            em.close();
        }
    }

    public int getClasseCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Classe> rt = cq.from(Classe.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
