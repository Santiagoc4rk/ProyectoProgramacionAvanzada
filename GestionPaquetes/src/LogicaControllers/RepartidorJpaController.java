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
import Clases.Entrega;
import Clases.Repartidor;
import LogicaControllers.exceptions.IllegalOrphanException;
import LogicaControllers.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author andre
 */
public class RepartidorJpaController implements Serializable {

    public RepartidorJpaController() {
        this.emf = Persistence.createEntityManagerFactory("GestionPaquetesPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Repartidor repartidor) {
        if (repartidor.getEntregaCollection() == null) {
            repartidor.setEntregaCollection(new ArrayList<Entrega>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona cedula = repartidor.getCedula();
            if (cedula != null) {
                cedula = em.getReference(cedula.getClass(), cedula.getCedula());
                repartidor.setCedula(cedula);
            }
            Collection<Entrega> attachedEntregaCollection = new ArrayList<Entrega>();
            for (Entrega entregaCollectionEntregaToAttach : repartidor.getEntregaCollection()) {
                entregaCollectionEntregaToAttach = em.getReference(entregaCollectionEntregaToAttach.getClass(), entregaCollectionEntregaToAttach.getEntregaPK());
                attachedEntregaCollection.add(entregaCollectionEntregaToAttach);
            }
            repartidor.setEntregaCollection(attachedEntregaCollection);
            em.persist(repartidor);
            if (cedula != null) {
                cedula.getRepartidorCollection().add(repartidor);
                cedula = em.merge(cedula);
            }
            for (Entrega entregaCollectionEntrega : repartidor.getEntregaCollection()) {
                Repartidor oldRepartidorOfEntregaCollectionEntrega = entregaCollectionEntrega.getRepartidor();
                entregaCollectionEntrega.setRepartidor(repartidor);
                entregaCollectionEntrega = em.merge(entregaCollectionEntrega);
                if (oldRepartidorOfEntregaCollectionEntrega != null) {
                    oldRepartidorOfEntregaCollectionEntrega.getEntregaCollection().remove(entregaCollectionEntrega);
                    oldRepartidorOfEntregaCollectionEntrega = em.merge(oldRepartidorOfEntregaCollectionEntrega);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Repartidor repartidor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Repartidor persistentRepartidor = em.find(Repartidor.class, repartidor.getIdRepartidor());
            Persona cedulaOld = persistentRepartidor.getCedula();
            Persona cedulaNew = repartidor.getCedula();
            Collection<Entrega> entregaCollectionOld = persistentRepartidor.getEntregaCollection();
            Collection<Entrega> entregaCollectionNew = repartidor.getEntregaCollection();
            List<String> illegalOrphanMessages = null;
            for (Entrega entregaCollectionOldEntrega : entregaCollectionOld) {
                if (!entregaCollectionNew.contains(entregaCollectionOldEntrega)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Entrega " + entregaCollectionOldEntrega + " since its repartidor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (cedulaNew != null) {
                cedulaNew = em.getReference(cedulaNew.getClass(), cedulaNew.getCedula());
                repartidor.setCedula(cedulaNew);
            }
            Collection<Entrega> attachedEntregaCollectionNew = new ArrayList<Entrega>();
            for (Entrega entregaCollectionNewEntregaToAttach : entregaCollectionNew) {
                entregaCollectionNewEntregaToAttach = em.getReference(entregaCollectionNewEntregaToAttach.getClass(), entregaCollectionNewEntregaToAttach.getEntregaPK());
                attachedEntregaCollectionNew.add(entregaCollectionNewEntregaToAttach);
            }
            entregaCollectionNew = attachedEntregaCollectionNew;
            repartidor.setEntregaCollection(entregaCollectionNew);
            repartidor = em.merge(repartidor);
            if (cedulaOld != null && !cedulaOld.equals(cedulaNew)) {
                cedulaOld.getRepartidorCollection().remove(repartidor);
                cedulaOld = em.merge(cedulaOld);
            }
            if (cedulaNew != null && !cedulaNew.equals(cedulaOld)) {
                cedulaNew.getRepartidorCollection().add(repartidor);
                cedulaNew = em.merge(cedulaNew);
            }
            for (Entrega entregaCollectionNewEntrega : entregaCollectionNew) {
                if (!entregaCollectionOld.contains(entregaCollectionNewEntrega)) {
                    Repartidor oldRepartidorOfEntregaCollectionNewEntrega = entregaCollectionNewEntrega.getRepartidor();
                    entregaCollectionNewEntrega.setRepartidor(repartidor);
                    entregaCollectionNewEntrega = em.merge(entregaCollectionNewEntrega);
                    if (oldRepartidorOfEntregaCollectionNewEntrega != null && !oldRepartidorOfEntregaCollectionNewEntrega.equals(repartidor)) {
                        oldRepartidorOfEntregaCollectionNewEntrega.getEntregaCollection().remove(entregaCollectionNewEntrega);
                        oldRepartidorOfEntregaCollectionNewEntrega = em.merge(oldRepartidorOfEntregaCollectionNewEntrega);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = repartidor.getIdRepartidor();
                if (findRepartidor(id) == null) {
                    throw new NonexistentEntityException("The repartidor with id " + id + " no longer exists.");
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
            Repartidor repartidor;
            try {
                repartidor = em.getReference(Repartidor.class, id);
                repartidor.getIdRepartidor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The repartidor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Entrega> entregaCollectionOrphanCheck = repartidor.getEntregaCollection();
            for (Entrega entregaCollectionOrphanCheckEntrega : entregaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Repartidor (" + repartidor + ") cannot be destroyed since the Entrega " + entregaCollectionOrphanCheckEntrega + " in its entregaCollection field has a non-nullable repartidor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Persona cedula = repartidor.getCedula();
            if (cedula != null) {
                cedula.getRepartidorCollection().remove(repartidor);
                cedula = em.merge(cedula);
            }
            em.remove(repartidor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Repartidor> findRepartidorEntities() {
        return findRepartidorEntities(true, -1, -1);
    }

    public List<Repartidor> findRepartidorEntities(int maxResults, int firstResult) {
        return findRepartidorEntities(false, maxResults, firstResult);
    }

    private List<Repartidor> findRepartidorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Repartidor.class));
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

    public Repartidor findRepartidor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Repartidor.class, id);
        } finally {
            em.close();
        }
    }

    public int getRepartidorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Repartidor> rt = cq.from(Repartidor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
