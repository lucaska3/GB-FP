/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import entity.CasaPub;
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
public class CasaPubJpaController implements Serializable {

    public CasaPubJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CasaPub casaPub) throws PreexistingEntityException, Exception {
        if (casaPub.getItemList() == null) {
            casaPub.setItemList(new ArrayList<Item>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Item> attachedItemList = new ArrayList<Item>();
            for (Item itemListItemToAttach : casaPub.getItemList()) {
                itemListItemToAttach = em.getReference(itemListItemToAttach.getClass(), itemListItemToAttach.getCodItem());
                attachedItemList.add(itemListItemToAttach);
            }
            casaPub.setItemList(attachedItemList);
            em.persist(casaPub);
            for (Item itemListItem : casaPub.getItemList()) {
                CasaPub oldCasaPubitemOfItemListItem = itemListItem.getCasaPubitem();
                itemListItem.setCasaPubitem(casaPub);
                itemListItem = em.merge(itemListItem);
                if (oldCasaPubitemOfItemListItem != null) {
                    oldCasaPubitemOfItemListItem.getItemList().remove(itemListItem);
                    oldCasaPubitemOfItemListItem = em.merge(oldCasaPubitemOfItemListItem);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCasaPub(casaPub.getCodCasa()) != null) {
                throw new PreexistingEntityException("CasaPub " + casaPub + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CasaPub casaPub) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CasaPub persistentCasaPub = em.find(CasaPub.class, casaPub.getCodCasa());
            List<Item> itemListOld = persistentCasaPub.getItemList();
            List<Item> itemListNew = casaPub.getItemList();
            List<Item> attachedItemListNew = new ArrayList<Item>();
            for (Item itemListNewItemToAttach : itemListNew) {
                itemListNewItemToAttach = em.getReference(itemListNewItemToAttach.getClass(), itemListNewItemToAttach.getCodItem());
                attachedItemListNew.add(itemListNewItemToAttach);
            }
            itemListNew = attachedItemListNew;
            casaPub.setItemList(itemListNew);
            casaPub = em.merge(casaPub);
            for (Item itemListOldItem : itemListOld) {
                if (!itemListNew.contains(itemListOldItem)) {
                    itemListOldItem.setCasaPubitem(null);
                    itemListOldItem = em.merge(itemListOldItem);
                }
            }
            for (Item itemListNewItem : itemListNew) {
                if (!itemListOld.contains(itemListNewItem)) {
                    CasaPub oldCasaPubitemOfItemListNewItem = itemListNewItem.getCasaPubitem();
                    itemListNewItem.setCasaPubitem(casaPub);
                    itemListNewItem = em.merge(itemListNewItem);
                    if (oldCasaPubitemOfItemListNewItem != null && !oldCasaPubitemOfItemListNewItem.equals(casaPub)) {
                        oldCasaPubitemOfItemListNewItem.getItemList().remove(itemListNewItem);
                        oldCasaPubitemOfItemListNewItem = em.merge(oldCasaPubitemOfItemListNewItem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = casaPub.getCodCasa();
                if (findCasaPub(id) == null) {
                    throw new NonexistentEntityException("The casaPub with id " + id + " no longer exists.");
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
            CasaPub casaPub;
            try {
                casaPub = em.getReference(CasaPub.class, id);
                casaPub.getCodCasa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The casaPub with id " + id + " no longer exists.", enfe);
            }
            List<Item> itemList = casaPub.getItemList();
            for (Item itemListItem : itemList) {
                itemListItem.setCasaPubitem(null);
                itemListItem = em.merge(itemListItem);
            }
            em.remove(casaPub);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CasaPub> findCasaPubEntities() {
        return findCasaPubEntities(true, -1, -1);
    }

    public List<CasaPub> findCasaPubEntities(int maxResults, int firstResult) {
        return findCasaPubEntities(false, maxResults, firstResult);
    }

    private List<CasaPub> findCasaPubEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CasaPub.class));
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

    public CasaPub findCasaPub(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CasaPub.class, id);
        } finally {
            em.close();
        }
    }

    public int getCasaPubCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CasaPub> rt = cq.from(CasaPub.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
