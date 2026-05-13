/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LogicaControllers;

import Clases.Entrega;
import Clases.EntregaPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Clases.Paquete;
import Clases.Repartidor;
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
public class EntregaJpaController implements Serializable {

    public EntregaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("GestionPaquetesPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Entrega entrega) throws PreexistingEntityException, Exception {
        if (entrega.getEntregaPK() == null) {
            entrega.setEntregaPK(new EntregaPK());
        }
        entrega.getEntregaPK().setIdRepartidor(entrega.getRepartidor().getIdRepartidor());
        entrega.getEntregaPK().setCodigoUnico(entrega.getPaquete().getCodigoUnico());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paquete paquete = entrega.getPaquete();
            if (paquete != null) {
                paquete = em.getReference(paquete.getClass(), paquete.getCodigoUnico());
                entrega.setPaquete(paquete);
            }
            Repartidor repartidor = entrega.getRepartidor();
            if (repartidor != null) {
                repartidor = em.getReference(repartidor.getClass(), repartidor.getIdRepartidor());
                entrega.setRepartidor(repartidor);
            }
            em.persist(entrega);
            if (paquete != null) {
                paquete.getEntregaCollection().add(entrega);
                paquete = em.merge(paquete);
            }
            if (repartidor != null) {
                repartidor.getEntregaCollection().add(entrega);
                repartidor = em.merge(repartidor);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEntrega(entrega.getEntregaPK()) != null) {
                throw new PreexistingEntityException("Entrega " + entrega + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Entrega entrega) throws NonexistentEntityException, Exception {
        entrega.getEntregaPK().setIdRepartidor(entrega.getRepartidor().getIdRepartidor());
        entrega.getEntregaPK().setCodigoUnico(entrega.getPaquete().getCodigoUnico());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entrega persistentEntrega = em.find(Entrega.class, entrega.getEntregaPK());
            Paquete paqueteOld = persistentEntrega.getPaquete();
            Paquete paqueteNew = entrega.getPaquete();
            Repartidor repartidorOld = persistentEntrega.getRepartidor();
            Repartidor repartidorNew = entrega.getRepartidor();
            if (paqueteNew != null) {
                paqueteNew = em.getReference(paqueteNew.getClass(), paqueteNew.getCodigoUnico());
                entrega.setPaquete(paqueteNew);
            }
            if (repartidorNew != null) {
                repartidorNew = em.getReference(repartidorNew.getClass(), repartidorNew.getIdRepartidor());
                entrega.setRepartidor(repartidorNew);
            }
            entrega = em.merge(entrega);
            if (paqueteOld != null && !paqueteOld.equals(paqueteNew)) {
                paqueteOld.getEntregaCollection().remove(entrega);
                paqueteOld = em.merge(paqueteOld);
            }
            if (paqueteNew != null && !paqueteNew.equals(paqueteOld)) {
                paqueteNew.getEntregaCollection().add(entrega);
                paqueteNew = em.merge(paqueteNew);
            }
            if (repartidorOld != null && !repartidorOld.equals(repartidorNew)) {
                repartidorOld.getEntregaCollection().remove(entrega);
                repartidorOld = em.merge(repartidorOld);
            }
            if (repartidorNew != null && !repartidorNew.equals(repartidorOld)) {
                repartidorNew.getEntregaCollection().add(entrega);
                repartidorNew = em.merge(repartidorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                EntregaPK id = entrega.getEntregaPK();
                if (findEntrega(id) == null) {
                    throw new NonexistentEntityException("The entrega with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(EntregaPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entrega entrega;
            try {
                entrega = em.getReference(Entrega.class, id);
                entrega.getEntregaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The entrega with id " + id + " no longer exists.", enfe);
            }
            Paquete paquete = entrega.getPaquete();
            if (paquete != null) {
                paquete.getEntregaCollection().remove(entrega);
                paquete = em.merge(paquete);
            }
            Repartidor repartidor = entrega.getRepartidor();
            if (repartidor != null) {
                repartidor.getEntregaCollection().remove(entrega);
                repartidor = em.merge(repartidor);
            }
            em.remove(entrega);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Entrega> findEntregaEntities() {
        return findEntregaEntities(true, -1, -1);
    }

    public List<Entrega> findEntregaEntities(int maxResults, int firstResult) {
        return findEntregaEntities(false, maxResults, firstResult);
    }

    private List<Entrega> findEntregaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Entrega.class));
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

    public Entrega findEntrega(EntregaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Entrega.class, id);
        } finally {
            em.close();
        }
    }

    public int getEntregaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Entrega> rt = cq.from(Entrega.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
