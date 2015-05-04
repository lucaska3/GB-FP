/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import entity.Colecoes;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Item;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author fp0520141051
 */
public class ColecoesJpaController implements Serializable {

    public ColecoesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Colecoes colecoes) throws PreexistingEntityException, Exception {
        if (colecoes.getItemList() == null) {
            colecoes.setItemList(new ArrayList<Item>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Item> attachedItemList = new ArrayList<Item>();
            for (Item itemListItemToAttach : colecoes.getItemList()) {
                itemListItemToAttach = em.getReference(itemListItemToAttach.getClass(), itemListItemToAttach.getCodItem());
                attachedItemList.add(itemListItemToAttach);
            }
            colecoes.setItemList(attachedItemList);
            em.persist(colecoes);
            for (Item itemListItem : colecoes.getItemList()) {
                Colecoes oldColecaoItemOfItemListItem = itemListItem.getColecaoItem();
                itemListItem.setColecaoItem(colecoes);
                itemListItem = em.merge(itemListItem);
                if (oldColecaoItemOfItemListItem != null) {
                    oldColecaoItemOfItemListItem.getItemList().remove(itemListItem);
                    oldColecaoItemOfItemListItem = em.merge(oldColecaoItemOfItemListItem);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findColecoes(colecoes.getCodCol()) != null) {
                throw new PreexistingEntityException("Colecoes " + colecoes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Colecoes colecoes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Colecoes persistentColecoes = em.find(Colecoes.class, colecoes.getCodCol());
            List<Item> itemListOld = persistentColecoes.getItemList();
            List<Item> itemListNew = colecoes.getItemList();
            List<Item> attachedItemListNew = new ArrayList<Item>();
            for (Item itemListNewItemToAttach : itemListNew) {
                itemListNewItemToAttach = em.getReference(itemListNewItemToAttach.getClass(), itemListNewItemToAttach.getCodItem());
                attachedItemListNew.add(itemListNewItemToAttach);
            }
            itemListNew = attachedItemListNew;
            colecoes.setItemList(itemListNew);
            colecoes = em.merge(colecoes);
            for (Item itemListOldItem : itemListOld) {
                if (!itemListNew.contains(itemListOldItem)) {
                    itemListOldItem.setColecaoItem(null);
                    itemListOldItem = em.merge(itemListOldItem);
                }
            }
            for (Item itemListNewItem : itemListNew) {
                if (!itemListOld.contains(itemListNewItem)) {
                    Colecoes oldColecaoItemOfItemListNewItem = itemListNewItem.getColecaoItem();
                    itemListNewItem.setColecaoItem(colecoes);
                    itemListNewItem = em.merge(itemListNewItem);
                    if (oldColecaoItemOfItemListNewItem != null && !oldColecaoItemOfItemListNewItem.equals(colecoes)) {
                        oldColecaoItemOfItemListNewItem.getItemList().remove(itemListNewItem);
                        oldColecaoItemOfItemListNewItem = em.merge(oldColecaoItemOfItemListNewItem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = colecoes.getCodCol();
                if (findColecoes(id) == null) {
                    throw new NonexistentEntityException("The colecoes with id " + id + " no longer exists.");
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
            Colecoes colecoes;
            try {
                colecoes = em.getReference(Colecoes.class, id);
                colecoes.getCodCol();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The colecoes with id " + id + " no longer exists.", enfe);
            }
            List<Item> itemList = colecoes.getItemList();
            for (Item itemListItem : itemList) {
                itemListItem.setColecaoItem(null);
                itemListItem = em.merge(itemListItem);
            }
            em.remove(colecoes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Colecoes> findColecoesEntities() {
        return findColecoesEntities(true, -1, -1);
    }

    public List<Colecoes> findColecoesEntities(int maxResults, int firstResult) {
        return findColecoesEntities(false, maxResults, firstResult);
    }

    private List<Colecoes> findColecoesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Colecoes.class));
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

    public Colecoes findColecoes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Colecoes.class, id);
        } finally {
            em.close();
        }
    }

    public int getColecoesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Colecoes> rt = cq.from(Colecoes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
