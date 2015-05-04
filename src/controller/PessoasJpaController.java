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
import entity.Classe;
import entity.Emprestimo;
import entity.Pessoas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author fp0520141051
 */
public class PessoasJpaController implements Serializable {

    public PessoasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pessoas pessoas) throws PreexistingEntityException, Exception {
        if (pessoas.getEmprestimoList() == null) {
            pessoas.setEmprestimoList(new ArrayList<Emprestimo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Classe classePes = pessoas.getClassePes();
            if (classePes != null) {
                classePes = em.getReference(classePes.getClass(), classePes.getCodClas());
                pessoas.setClassePes(classePes);
            }
            Emprestimo empPes = pessoas.getEmpPes();
            if (empPes != null) {
                empPes = em.getReference(empPes.getClass(), empPes.getCodEmp());
                pessoas.setEmpPes(empPes);
            }
            List<Emprestimo> attachedEmprestimoList = new ArrayList<Emprestimo>();
            for (Emprestimo emprestimoListEmprestimoToAttach : pessoas.getEmprestimoList()) {
                emprestimoListEmprestimoToAttach = em.getReference(emprestimoListEmprestimoToAttach.getClass(), emprestimoListEmprestimoToAttach.getCodEmp());
                attachedEmprestimoList.add(emprestimoListEmprestimoToAttach);
            }
            pessoas.setEmprestimoList(attachedEmprestimoList);
            em.persist(pessoas);
            if (classePes != null) {
                classePes.getPessoasList().add(pessoas);
                classePes = em.merge(classePes);
            }
            if (empPes != null) {
                Pessoas oldPesEmpOfEmpPes = empPes.getPesEmp();
                if (oldPesEmpOfEmpPes != null) {
                    oldPesEmpOfEmpPes.setEmpPes(null);
                    oldPesEmpOfEmpPes = em.merge(oldPesEmpOfEmpPes);
                }
                empPes.setPesEmp(pessoas);
                empPes = em.merge(empPes);
            }
            for (Emprestimo emprestimoListEmprestimo : pessoas.getEmprestimoList()) {
                Pessoas oldPesEmpOfEmprestimoListEmprestimo = emprestimoListEmprestimo.getPesEmp();
                emprestimoListEmprestimo.setPesEmp(pessoas);
                emprestimoListEmprestimo = em.merge(emprestimoListEmprestimo);
                if (oldPesEmpOfEmprestimoListEmprestimo != null) {
                    oldPesEmpOfEmprestimoListEmprestimo.getEmprestimoList().remove(emprestimoListEmprestimo);
                    oldPesEmpOfEmprestimoListEmprestimo = em.merge(oldPesEmpOfEmprestimoListEmprestimo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPessoas(pessoas.getCodPes()) != null) {
                throw new PreexistingEntityException("Pessoas " + pessoas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pessoas pessoas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoas persistentPessoas = em.find(Pessoas.class, pessoas.getCodPes());
            Classe classePesOld = persistentPessoas.getClassePes();
            Classe classePesNew = pessoas.getClassePes();
            Emprestimo empPesOld = persistentPessoas.getEmpPes();
            Emprestimo empPesNew = pessoas.getEmpPes();
            List<Emprestimo> emprestimoListOld = persistentPessoas.getEmprestimoList();
            List<Emprestimo> emprestimoListNew = pessoas.getEmprestimoList();
            List<String> illegalOrphanMessages = null;
            if (empPesOld != null && !empPesOld.equals(empPesNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Emprestimo " + empPesOld + " since its pesEmp field is not nullable.");
            }
            for (Emprestimo emprestimoListOldEmprestimo : emprestimoListOld) {
                if (!emprestimoListNew.contains(emprestimoListOldEmprestimo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Emprestimo " + emprestimoListOldEmprestimo + " since its pesEmp field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (classePesNew != null) {
                classePesNew = em.getReference(classePesNew.getClass(), classePesNew.getCodClas());
                pessoas.setClassePes(classePesNew);
            }
            if (empPesNew != null) {
                empPesNew = em.getReference(empPesNew.getClass(), empPesNew.getCodEmp());
                pessoas.setEmpPes(empPesNew);
            }
            List<Emprestimo> attachedEmprestimoListNew = new ArrayList<Emprestimo>();
            for (Emprestimo emprestimoListNewEmprestimoToAttach : emprestimoListNew) {
                emprestimoListNewEmprestimoToAttach = em.getReference(emprestimoListNewEmprestimoToAttach.getClass(), emprestimoListNewEmprestimoToAttach.getCodEmp());
                attachedEmprestimoListNew.add(emprestimoListNewEmprestimoToAttach);
            }
            emprestimoListNew = attachedEmprestimoListNew;
            pessoas.setEmprestimoList(emprestimoListNew);
            pessoas = em.merge(pessoas);
            if (classePesOld != null && !classePesOld.equals(classePesNew)) {
                classePesOld.getPessoasList().remove(pessoas);
                classePesOld = em.merge(classePesOld);
            }
            if (classePesNew != null && !classePesNew.equals(classePesOld)) {
                classePesNew.getPessoasList().add(pessoas);
                classePesNew = em.merge(classePesNew);
            }
            if (empPesNew != null && !empPesNew.equals(empPesOld)) {
                Pessoas oldPesEmpOfEmpPes = empPesNew.getPesEmp();
                if (oldPesEmpOfEmpPes != null) {
                    oldPesEmpOfEmpPes.setEmpPes(null);
                    oldPesEmpOfEmpPes = em.merge(oldPesEmpOfEmpPes);
                }
                empPesNew.setPesEmp(pessoas);
                empPesNew = em.merge(empPesNew);
            }
            for (Emprestimo emprestimoListNewEmprestimo : emprestimoListNew) {
                if (!emprestimoListOld.contains(emprestimoListNewEmprestimo)) {
                    Pessoas oldPesEmpOfEmprestimoListNewEmprestimo = emprestimoListNewEmprestimo.getPesEmp();
                    emprestimoListNewEmprestimo.setPesEmp(pessoas);
                    emprestimoListNewEmprestimo = em.merge(emprestimoListNewEmprestimo);
                    if (oldPesEmpOfEmprestimoListNewEmprestimo != null && !oldPesEmpOfEmprestimoListNewEmprestimo.equals(pessoas)) {
                        oldPesEmpOfEmprestimoListNewEmprestimo.getEmprestimoList().remove(emprestimoListNewEmprestimo);
                        oldPesEmpOfEmprestimoListNewEmprestimo = em.merge(oldPesEmpOfEmprestimoListNewEmprestimo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pessoas.getCodPes();
                if (findPessoas(id) == null) {
                    throw new NonexistentEntityException("The pessoas with id " + id + " no longer exists.");
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
            Pessoas pessoas;
            try {
                pessoas = em.getReference(Pessoas.class, id);
                pessoas.getCodPes();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pessoas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Emprestimo empPesOrphanCheck = pessoas.getEmpPes();
            if (empPesOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pessoas (" + pessoas + ") cannot be destroyed since the Emprestimo " + empPesOrphanCheck + " in its empPes field has a non-nullable pesEmp field.");
            }
            List<Emprestimo> emprestimoListOrphanCheck = pessoas.getEmprestimoList();
            for (Emprestimo emprestimoListOrphanCheckEmprestimo : emprestimoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pessoas (" + pessoas + ") cannot be destroyed since the Emprestimo " + emprestimoListOrphanCheckEmprestimo + " in its emprestimoList field has a non-nullable pesEmp field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Classe classePes = pessoas.getClassePes();
            if (classePes != null) {
                classePes.getPessoasList().remove(pessoas);
                classePes = em.merge(classePes);
            }
            em.remove(pessoas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pessoas> findPessoasEntities() {
        return findPessoasEntities(true, -1, -1);
    }

    public List<Pessoas> findPessoasEntities(int maxResults, int firstResult) {
        return findPessoasEntities(false, maxResults, firstResult);
    }

    private List<Pessoas> findPessoasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pessoas.class));
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

    public Pessoas findPessoas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pessoas.class, id);
        } finally {
            em.close();
        }
    }

    public int getPessoasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pessoas> rt = cq.from(Pessoas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
