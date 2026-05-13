/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LogicaControllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Clases.Persona;
import Clases.Supervisor;
import LogicaControllers.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author andre
 */
public class SupervisorJpaController implements Serializable {

    public SupervisorJpaController() {
        this.emf = Persistence.createEntityManagerFactory("GestionPaquetesPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Supervisor supervisor) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona cedula = supervisor.getCedula();
            if (cedula != null) {
                cedula = em.getReference(cedula.getClass(), cedula.getCedula());
                supervisor.setCedula(cedula);
            }
            em.persist(supervisor);
            if (cedula != null) {
                cedula.getSupervisorCollection().add(supervisor);
                cedula = em.merge(cedula);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Supervisor supervisor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Supervisor persistentSupervisor = em.find(Supervisor.class, supervisor.getIdSupervisor());
            Persona cedulaOld = persistentSupervisor.getCedula();
            Persona cedulaNew = supervisor.getCedula();
            if (cedulaNew != null) {
                cedulaNew = em.getReference(cedulaNew.getClass(), cedulaNew.getCedula());
                supervisor.setCedula(cedulaNew);
            }
            supervisor = em.merge(supervisor);
            if (cedulaOld != null && !cedulaOld.equals(cedulaNew)) {
                cedulaOld.getSupervisorCollection().remove(supervisor);
                cedulaOld = em.merge(cedulaOld);
            }
            if (cedulaNew != null && !cedulaNew.equals(cedulaOld)) {
                cedulaNew.getSupervisorCollection().add(supervisor);
                cedulaNew = em.merge(cedulaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = supervisor.getIdSupervisor();
                if (findSupervisor(id) == null) {
                    throw new NonexistentEntityException("The supervisor with id " + id + " no longer exists.");
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
            Supervisor supervisor;
            try {
                supervisor = em.getReference(Supervisor.class, id);
                supervisor.getIdSupervisor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The supervisor with id " + id + " no longer exists.", enfe);
            }
            Persona cedula = supervisor.getCedula();
            if (cedula != null) {
                cedula.getSupervisorCollection().remove(supervisor);
                cedula = em.merge(cedula);
            }
            em.remove(supervisor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Supervisor> findSupervisorEntities() {
        return findSupervisorEntities(true, -1, -1);
    }

    public List<Supervisor> findSupervisorEntities(int maxResults, int firstResult) {
        return findSupervisorEntities(false, maxResults, firstResult);
    }

    private List<Supervisor> findSupervisorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Supervisor.class));
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

    public Supervisor findSupervisor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Supervisor.class, id);
        } finally {
            em.close();
        }
    }

    public int getSupervisorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Supervisor> rt = cq.from(Supervisor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
