/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import entity.Emprestimo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Item;
import entity.Pessoas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author fp0520141051
 */
public class EmprestimoJpaController implements Serializable {

    public EmprestimoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Emprestimo emprestimo) throws PreexistingEntityException, Exception {
        if (emprestimo.getPessoasList() == null) {
            emprestimo.setPessoasList(new ArrayList<Pessoas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Item itemEmp = emprestimo.getItemEmp();
            if (itemEmp != null) {
                itemEmp = em.getReference(itemEmp.getClass(), itemEmp.getCodItem());
                emprestimo.setItemEmp(itemEmp);
            }
            Pessoas pesEmp = emprestimo.getPesEmp();
            if (pesEmp != null) {
                pesEmp = em.getReference(pesEmp.getClass(), pesEmp.getCodPes());
                emprestimo.setPesEmp(pesEmp);
            }
            List<Pessoas> attachedPessoasList = new ArrayList<Pessoas>();
            for (Pessoas pessoasListPessoasToAttach : emprestimo.getPessoasList()) {
                pessoasListPessoasToAttach = em.getReference(pessoasListPessoasToAttach.getClass(), pessoasListPessoasToAttach.getCodPes());
                attachedPessoasList.add(pessoasListPessoasToAttach);
            }
            emprestimo.setPessoasList(attachedPessoasList);
            em.persist(emprestimo);
            if (itemEmp != null) {
                itemEmp.getEmprestimoList().add(emprestimo);
                itemEmp = em.merge(itemEmp);
            }
            if (pesEmp != null) {
                pesEmp.getEmprestimoList().add(emprestimo);
                pesEmp = em.merge(pesEmp);
            }
            for (Pessoas pessoasListPessoas : emprestimo.getPessoasList()) {
                Emprestimo oldEmpPesOfPessoasListPessoas = pessoasListPessoas.getEmpPes();
                pessoasListPessoas.setEmpPes(emprestimo);
                pessoasListPessoas = em.merge(pessoasListPessoas);
                if (oldEmpPesOfPessoasListPessoas != null) {
                    oldEmpPesOfPessoasListPessoas.getPessoasList().remove(pessoasListPessoas);
                    oldEmpPesOfPessoasListPessoas = em.merge(oldEmpPesOfPessoasListPessoas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEmprestimo(emprestimo.getCodEmp()) != null) {
                throw new PreexistingEntityException("Emprestimo " + emprestimo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Emprestimo emprestimo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Emprestimo persistentEmprestimo = em.find(Emprestimo.class, emprestimo.getCodEmp());
            Item itemEmpOld = persistentEmprestimo.getItemEmp();
            Item itemEmpNew = emprestimo.getItemEmp();
            Pessoas pesEmpOld = persistentEmprestimo.getPesEmp();
            Pessoas pesEmpNew = emprestimo.getPesEmp();
            List<Pessoas> pessoasListOld = persistentEmprestimo.getPessoasList();
            List<Pessoas> pessoasListNew = emprestimo.getPessoasList();
            if (itemEmpNew != null) {
                itemEmpNew = em.getReference(itemEmpNew.getClass(), itemEmpNew.getCodItem());
                emprestimo.setItemEmp(itemEmpNew);
            }
            if (pesEmpNew != null) {
                pesEmpNew = em.getReference(pesEmpNew.getClass(), pesEmpNew.getCodPes());
                emprestimo.setPesEmp(pesEmpNew);
            }
            List<Pessoas> attachedPessoasListNew = new ArrayList<Pessoas>();
            for (Pessoas pessoasListNewPessoasToAttach : pessoasListNew) {
                pessoasListNewPessoasToAttach = em.getReference(pessoasListNewPessoasToAttach.getClass(), pessoasListNewPessoasToAttach.getCodPes());
                attachedPessoasListNew.add(pessoasListNewPessoasToAttach);
            }
            pessoasListNew = attachedPessoasListNew;
            emprestimo.setPessoasList(pessoasListNew);
            emprestimo = em.merge(emprestimo);
            if (itemEmpOld != null && !itemEmpOld.equals(itemEmpNew)) {
                itemEmpOld.getEmprestimoList().remove(emprestimo);
                itemEmpOld = em.merge(itemEmpOld);
            }
            if (itemEmpNew != null && !itemEmpNew.equals(itemEmpOld)) {
                itemEmpNew.getEmprestimoList().add(emprestimo);
                itemEmpNew = em.merge(itemEmpNew);
            }
            if (pesEmpOld != null && !pesEmpOld.equals(pesEmpNew)) {
                pesEmpOld.getEmprestimoList().remove(emprestimo);
                pesEmpOld = em.merge(pesEmpOld);
            }
            if (pesEmpNew != null && !pesEmpNew.equals(pesEmpOld)) {
                pesEmpNew.getEmprestimoList().add(emprestimo);
                pesEmpNew = em.merge(pesEmpNew);
            }
            for (Pessoas pessoasListOldPessoas : pessoasListOld) {
                if (!pessoasListNew.contains(pessoasListOldPessoas)) {
                    pessoasListOldPessoas.setEmpPes(null);
                    pessoasListOldPessoas = em.merge(pessoasListOldPessoas);
                }
            }
            for (Pessoas pessoasListNewPessoas : pessoasListNew) {
                if (!pessoasListOld.contains(pessoasListNewPessoas)) {
                    Emprestimo oldEmpPesOfPessoasListNewPessoas = pessoasListNewPessoas.getEmpPes();
                    pessoasListNewPessoas.setEmpPes(emprestimo);
                    pessoasListNewPessoas = em.merge(pessoasListNewPessoas);
                    if (oldEmpPesOfPessoasListNewPessoas != null && !oldEmpPesOfPessoasListNewPessoas.equals(emprestimo)) {
                        oldEmpPesOfPessoasListNewPessoas.getPessoasList().remove(pessoasListNewPessoas);
                        oldEmpPesOfPessoasListNewPessoas = em.merge(oldEmpPesOfPessoasListNewPessoas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = emprestimo.getCodEmp();
                if (findEmprestimo(id) == null) {
                    throw new NonexistentEntityException("The emprestimo with id " + id + " no longer exists.");
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
            Emprestimo emprestimo;
            try {
                emprestimo = em.getReference(Emprestimo.class, id);
                emprestimo.getCodEmp();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The emprestimo with id " + id + " no longer exists.", enfe);
            }
            Item itemEmp = emprestimo.getItemEmp();
            if (itemEmp != null) {
                itemEmp.getEmprestimoList().remove(emprestimo);
                itemEmp = em.merge(itemEmp);
            }
            Pessoas pesEmp = emprestimo.getPesEmp();
            if (pesEmp != null) {
                pesEmp.getEmprestimoList().remove(emprestimo);
                pesEmp = em.merge(pesEmp);
            }
            List<Pessoas> pessoasList = emprestimo.getPessoasList();
            for (Pessoas pessoasListPessoas : pessoasList) {
                pessoasListPessoas.setEmpPes(null);
                pessoasListPessoas = em.merge(pessoasListPessoas);
            }
            em.remove(emprestimo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Emprestimo> findEmprestimoEntities() {
        return findEmprestimoEntities(true, -1, -1);
    }

    public List<Emprestimo> findEmprestimoEntities(int maxResults, int firstResult) {
        return findEmprestimoEntities(false, maxResults, firstResult);
    }

    private List<Emprestimo> findEmprestimoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Emprestimo.class));
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

    public Emprestimo findEmprestimo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Emprestimo.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmprestimoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Emprestimo> rt = cq.from(Emprestimo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
