/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LogicaControllers;

import Clases.Despacha;
import Clases.DespachaPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Clases.OperadorDespacho;
import Clases.Paquete;
import LogicaControllers.exceptions.NonexistentEntityException;
import LogicaControllers.exceptions.PreexistingEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author andre
 */
public class DespachaJpaController implements Serializable {

    public DespachaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("GestionPaquetesPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Despacha despacha) throws PreexistingEntityException, Exception {
        if (despacha.getDespachaPK() == null) {
            despacha.setDespachaPK(new DespachaPK());
        }
        despacha.getDespachaPK().setIdOperador(despacha.getOperadorDespacho().getIdOperador());
        despacha.getDespachaPK().setCodigoUnico(despacha.getPaquete().getCodigoUnico());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            OperadorDespacho operadorDespacho = despacha.getOperadorDespacho();
            if (operadorDespacho != null) {
                operadorDespacho = em.getReference(operadorDespacho.getClass(), operadorDespacho.getIdOperador());
                despacha.setOperadorDespacho(operadorDespacho);
            }
            Paquete paquete = despacha.getPaquete();
            if (paquete != null) {
                paquete = em.getReference(paquete.getClass(), paquete.getCodigoUnico());
                despacha.setPaquete(paquete);
            }
            em.persist(despacha);
            if (operadorDespacho != null) {
                operadorDespacho.getDespachaCollection().add(despacha);
                operadorDespacho = em.merge(operadorDespacho);
            }
            if (paquete != null) {
                paquete.getDespachaCollection().add(despacha);
                paquete = em.merge(paquete);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDespacha(despacha.getDespachaPK()) != null) {
                throw new PreexistingEntityException("Despacha " + despacha + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Despacha despacha) throws NonexistentEntityException, Exception {
        despacha.getDespachaPK().setIdOperador(despacha.getOperadorDespacho().getIdOperador());
        despacha.getDespachaPK().setCodigoUnico(despacha.getPaquete().getCodigoUnico());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Despacha persistentDespacha = em.find(Despacha.class, despacha.getDespachaPK());
            OperadorDespacho operadorDespachoOld = persistentDespacha.getOperadorDespacho();
            OperadorDespacho operadorDespachoNew = despacha.getOperadorDespacho();
            Paquete paqueteOld = persistentDespacha.getPaquete();
            Paquete paqueteNew = despacha.getPaquete();
            if (operadorDespachoNew != null) {
                operadorDespachoNew = em.getReference(operadorDespachoNew.getClass(), operadorDespachoNew.getIdOperador());
                despacha.setOperadorDespacho(operadorDespachoNew);
            }
            if (paqueteNew != null) {
                paqueteNew = em.getReference(paqueteNew.getClass(), paqueteNew.getCodigoUnico());
                despacha.setPaquete(paqueteNew);
            }
            despacha = em.merge(despacha);
            if (operadorDespachoOld != null && !operadorDespachoOld.equals(operadorDespachoNew)) {
                operadorDespachoOld.getDespachaCollection().remove(despacha);
                operadorDespachoOld = em.merge(operadorDespachoOld);
            }
            if (operadorDespachoNew != null && !operadorDespachoNew.equals(operadorDespachoOld)) {
                operadorDespachoNew.getDespachaCollection().add(despacha);
                operadorDespachoNew = em.merge(operadorDespachoNew);
            }
            if (paqueteOld != null && !paqueteOld.equals(paqueteNew)) {
                paqueteOld.getDespachaCollection().remove(despacha);
                paqueteOld = em.merge(paqueteOld);
            }
            if (paqueteNew != null && !paqueteNew.equals(paqueteOld)) {
                paqueteNew.getDespachaCollection().add(despacha);
                paqueteNew = em.merge(paqueteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                DespachaPK id = despacha.getDespachaPK();
                if (findDespacha(id) == null) {
                    throw new NonexistentEntityException("The despacha with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(DespachaPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Despacha despacha;
            try {
                despacha = em.getReference(Despacha.class, id);
                despacha.getDespachaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The despacha with id " + id + " no longer exists.", enfe);
            }
            OperadorDespacho operadorDespacho = despacha.getOperadorDespacho();
            if (operadorDespacho != null) {
                operadorDespacho.getDespachaCollection().remove(despacha);
                operadorDespacho = em.merge(operadorDespacho);
            }
            Paquete paquete = despacha.getPaquete();
            if (paquete != null) {
                paquete.getDespachaCollection().remove(despacha);
                paquete = em.merge(paquete);
            }
            em.remove(despacha);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Despacha> findDespachaEntities() {
        return findDespachaEntities(true, -1, -1);
    }

    public List<Despacha> findDespachaEntities(int maxResults, int firstResult) {
        return findDespachaEntities(false, maxResults, firstResult);
    }

    private List<Despacha> findDespachaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Despacha.class));
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

    public Despacha findDespacha(DespachaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Despacha.class, id);
        } finally {
            em.close();
        }
    }

    public int getDespachaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Despacha> rt = cq.from(Despacha.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
