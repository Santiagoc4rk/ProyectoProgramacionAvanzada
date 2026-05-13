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
import Clases.Paquete;
import Clases.Tarifa;
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
public class TarifaJpaController implements Serializable {

    public TarifaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("GestionPaquetesPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tarifa tarifa) {
        if (tarifa.getPaqueteCollection() == null) {
            tarifa.setPaqueteCollection(new ArrayList<Paquete>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Paquete> attachedPaqueteCollection = new ArrayList<Paquete>();
            for (Paquete paqueteCollectionPaqueteToAttach : tarifa.getPaqueteCollection()) {
                paqueteCollectionPaqueteToAttach = em.getReference(paqueteCollectionPaqueteToAttach.getClass(), paqueteCollectionPaqueteToAttach.getCodigoUnico());
                attachedPaqueteCollection.add(paqueteCollectionPaqueteToAttach);
            }
            tarifa.setPaqueteCollection(attachedPaqueteCollection);
            em.persist(tarifa);
            for (Paquete paqueteCollectionPaquete : tarifa.getPaqueteCollection()) {
                Tarifa oldIdTarifaOfPaqueteCollectionPaquete = paqueteCollectionPaquete.getIdTarifa();
                paqueteCollectionPaquete.setIdTarifa(tarifa);
                paqueteCollectionPaquete = em.merge(paqueteCollectionPaquete);
                if (oldIdTarifaOfPaqueteCollectionPaquete != null) {
                    oldIdTarifaOfPaqueteCollectionPaquete.getPaqueteCollection().remove(paqueteCollectionPaquete);
                    oldIdTarifaOfPaqueteCollectionPaquete = em.merge(oldIdTarifaOfPaqueteCollectionPaquete);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tarifa tarifa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarifa persistentTarifa = em.find(Tarifa.class, tarifa.getIdTarifa());
            Collection<Paquete> paqueteCollectionOld = persistentTarifa.getPaqueteCollection();
            Collection<Paquete> paqueteCollectionNew = tarifa.getPaqueteCollection();
            List<String> illegalOrphanMessages = null;
            for (Paquete paqueteCollectionOldPaquete : paqueteCollectionOld) {
                if (!paqueteCollectionNew.contains(paqueteCollectionOldPaquete)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Paquete " + paqueteCollectionOldPaquete + " since its idTarifa field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Paquete> attachedPaqueteCollectionNew = new ArrayList<Paquete>();
            for (Paquete paqueteCollectionNewPaqueteToAttach : paqueteCollectionNew) {
                paqueteCollectionNewPaqueteToAttach = em.getReference(paqueteCollectionNewPaqueteToAttach.getClass(), paqueteCollectionNewPaqueteToAttach.getCodigoUnico());
                attachedPaqueteCollectionNew.add(paqueteCollectionNewPaqueteToAttach);
            }
            paqueteCollectionNew = attachedPaqueteCollectionNew;
            tarifa.setPaqueteCollection(paqueteCollectionNew);
            tarifa = em.merge(tarifa);
            for (Paquete paqueteCollectionNewPaquete : paqueteCollectionNew) {
                if (!paqueteCollectionOld.contains(paqueteCollectionNewPaquete)) {
                    Tarifa oldIdTarifaOfPaqueteCollectionNewPaquete = paqueteCollectionNewPaquete.getIdTarifa();
                    paqueteCollectionNewPaquete.setIdTarifa(tarifa);
                    paqueteCollectionNewPaquete = em.merge(paqueteCollectionNewPaquete);
                    if (oldIdTarifaOfPaqueteCollectionNewPaquete != null && !oldIdTarifaOfPaqueteCollectionNewPaquete.equals(tarifa)) {
                        oldIdTarifaOfPaqueteCollectionNewPaquete.getPaqueteCollection().remove(paqueteCollectionNewPaquete);
                        oldIdTarifaOfPaqueteCollectionNewPaquete = em.merge(oldIdTarifaOfPaqueteCollectionNewPaquete);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tarifa.getIdTarifa();
                if (findTarifa(id) == null) {
                    throw new NonexistentEntityException("The tarifa with id " + id + " no longer exists.");
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
            Tarifa tarifa;
            try {
                tarifa = em.getReference(Tarifa.class, id);
                tarifa.getIdTarifa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tarifa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Paquete> paqueteCollectionOrphanCheck = tarifa.getPaqueteCollection();
            for (Paquete paqueteCollectionOrphanCheckPaquete : paqueteCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarifa (" + tarifa + ") cannot be destroyed since the Paquete " + paqueteCollectionOrphanCheckPaquete + " in its paqueteCollection field has a non-nullable idTarifa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tarifa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tarifa> findTarifaEntities() {
        return findTarifaEntities(true, -1, -1);
    }

    public List<Tarifa> findTarifaEntities(int maxResults, int firstResult) {
        return findTarifaEntities(false, maxResults, firstResult);
    }

    private List<Tarifa> findTarifaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tarifa.class));
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

    public Tarifa findTarifa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tarifa.class, id);
        } finally {
            em.close();
        }
    }

    public int getTarifaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tarifa> rt = cq.from(Tarifa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
