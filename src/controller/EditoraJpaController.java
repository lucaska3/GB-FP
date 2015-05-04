/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import entity.Editora;
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
public class EditoraJpaController implements Serializable {

    public EditoraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Editora editora) throws PreexistingEntityException, Exception {
        if (editora.getItemList() == null) {
            editora.setItemList(new ArrayList<Item>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Item> attachedItemList = new ArrayList<Item>();
            for (Item itemListItemToAttach : editora.getItemList()) {
                itemListItemToAttach = em.getReference(itemListItemToAttach.getClass(), itemListItemToAttach.getCodItem());
                attachedItemList.add(itemListItemToAttach);
            }
            editora.setItemList(attachedItemList);
            em.persist(editora);
            for (Item itemListItem : editora.getItemList()) {
                Editora oldEditoraItemOfItemListItem = itemListItem.getEditoraItem();
                itemListItem.setEditoraItem(editora);
                itemListItem = em.merge(itemListItem);
                if (oldEditoraItemOfItemListItem != null) {
                    oldEditoraItemOfItemListItem.getItemList().remove(itemListItem);
                    oldEditoraItemOfItemListItem = em.merge(oldEditoraItemOfItemListItem);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEditora(editora.getCodEditora()) != null) {
                throw new PreexistingEntityException("Editora " + editora + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Editora editora) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Editora persistentEditora = em.find(Editora.class, editora.getCodEditora());
            List<Item> itemListOld = persistentEditora.getItemList();
            List<Item> itemListNew = editora.getItemList();
            List<Item> attachedItemListNew = new ArrayList<Item>();
            for (Item itemListNewItemToAttach : itemListNew) {
                itemListNewItemToAttach = em.getReference(itemListNewItemToAttach.getClass(), itemListNewItemToAttach.getCodItem());
                attachedItemListNew.add(itemListNewItemToAttach);
            }
            itemListNew = attachedItemListNew;
            editora.setItemList(itemListNew);
            editora = em.merge(editora);
            for (Item itemListOldItem : itemListOld) {
                if (!itemListNew.contains(itemListOldItem)) {
                    itemListOldItem.setEditoraItem(null);
                    itemListOldItem = em.merge(itemListOldItem);
                }
            }
            for (Item itemListNewItem : itemListNew) {
                if (!itemListOld.contains(itemListNewItem)) {
                    Editora oldEditoraItemOfItemListNewItem = itemListNewItem.getEditoraItem();
                    itemListNewItem.setEditoraItem(editora);
                    itemListNewItem = em.merge(itemListNewItem);
                    if (oldEditoraItemOfItemListNewItem != null && !oldEditoraItemOfItemListNewItem.equals(editora)) {
                        oldEditoraItemOfItemListNewItem.getItemList().remove(itemListNewItem);
                        oldEditoraItemOfItemListNewItem = em.merge(oldEditoraItemOfItemListNewItem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = editora.getCodEditora();
                if (findEditora(id) == null) {
                    throw new NonexistentEntityException("The editora with id " + id + " no longer exists.");
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
            Editora editora;
            try {
                editora = em.getReference(Editora.class, id);
                editora.getCodEditora();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The editora with id " + id + " no longer exists.", enfe);
            }
            List<Item> itemList = editora.getItemList();
            for (Item itemListItem : itemList) {
                itemListItem.setEditoraItem(null);
                itemListItem = em.merge(itemListItem);
            }
            em.remove(editora);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Editora> findEditoraEntities() {
        return findEditoraEntities(true, -1, -1);
    }

    public List<Editora> findEditoraEntities(int maxResults, int firstResult) {
        return findEditoraEntities(false, maxResults, firstResult);
    }

    private List<Editora> findEditoraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Editora.class));
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

    public Editora findEditora(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Editora.class, id);
        } finally {
            em.close();
        }
    }

    public int getEditoraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Editora> rt = cq.from(Editora.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
