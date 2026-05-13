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
import Clases.Recepcionista;
import java.util.ArrayList;
import java.util.Collection;
import Clases.OperadorDespacho;
import Clases.HistorialEstado;
import Clases.Ubicacion;
import LogicaControllers.exceptions.IllegalOrphanException;
import LogicaControllers.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author andre
 */
public class UbicacionJpaController implements Serializable {

    public UbicacionJpaController() {
        this.emf = Persistence.createEntityManagerFactory("GestionPaquetesPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ubicacion ubicacion) {
        if (ubicacion.getRecepcionistaCollection() == null) {
            ubicacion.setRecepcionistaCollection(new ArrayList<Recepcionista>());
        }
        if (ubicacion.getOperadorDespachoCollection() == null) {
            ubicacion.setOperadorDespachoCollection(new ArrayList<OperadorDespacho>());
        }
        if (ubicacion.getHistorialEstadoCollection() == null) {
            ubicacion.setHistorialEstadoCollection(new ArrayList<HistorialEstado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Recepcionista> attachedRecepcionistaCollection = new ArrayList<Recepcionista>();
            for (Recepcionista recepcionistaCollectionRecepcionistaToAttach : ubicacion.getRecepcionistaCollection()) {
                recepcionistaCollectionRecepcionistaToAttach = em.getReference(recepcionistaCollectionRecepcionistaToAttach.getClass(), recepcionistaCollectionRecepcionistaToAttach.getIdRecepcionista());
                attachedRecepcionistaCollection.add(recepcionistaCollectionRecepcionistaToAttach);
            }
            ubicacion.setRecepcionistaCollection(attachedRecepcionistaCollection);
            Collection<OperadorDespacho> attachedOperadorDespachoCollection = new ArrayList<OperadorDespacho>();
            for (OperadorDespacho operadorDespachoCollectionOperadorDespachoToAttach : ubicacion.getOperadorDespachoCollection()) {
                operadorDespachoCollectionOperadorDespachoToAttach = em.getReference(operadorDespachoCollectionOperadorDespachoToAttach.getClass(), operadorDespachoCollectionOperadorDespachoToAttach.getIdOperador());
                attachedOperadorDespachoCollection.add(operadorDespachoCollectionOperadorDespachoToAttach);
            }
            ubicacion.setOperadorDespachoCollection(attachedOperadorDespachoCollection);
            Collection<HistorialEstado> attachedHistorialEstadoCollection = new ArrayList<HistorialEstado>();
            for (HistorialEstado historialEstadoCollectionHistorialEstadoToAttach : ubicacion.getHistorialEstadoCollection()) {
                historialEstadoCollectionHistorialEstadoToAttach = em.getReference(historialEstadoCollectionHistorialEstadoToAttach.getClass(), historialEstadoCollectionHistorialEstadoToAttach.getIdHistorial());
                attachedHistorialEstadoCollection.add(historialEstadoCollectionHistorialEstadoToAttach);
            }
            ubicacion.setHistorialEstadoCollection(attachedHistorialEstadoCollection);
            em.persist(ubicacion);
            for (Recepcionista recepcionistaCollectionRecepcionista : ubicacion.getRecepcionistaCollection()) {
                Ubicacion oldLocalOfRecepcionistaCollectionRecepcionista = recepcionistaCollectionRecepcionista.getLocal();
                recepcionistaCollectionRecepcionista.setLocal(ubicacion);
                recepcionistaCollectionRecepcionista = em.merge(recepcionistaCollectionRecepcionista);
                if (oldLocalOfRecepcionistaCollectionRecepcionista != null) {
                    oldLocalOfRecepcionistaCollectionRecepcionista.getRecepcionistaCollection().remove(recepcionistaCollectionRecepcionista);
                    oldLocalOfRecepcionistaCollectionRecepcionista = em.merge(oldLocalOfRecepcionistaCollectionRecepcionista);
                }
            }
            for (OperadorDespacho operadorDespachoCollectionOperadorDespacho : ubicacion.getOperadorDespachoCollection()) {
                Ubicacion oldBodegaOfOperadorDespachoCollectionOperadorDespacho = operadorDespachoCollectionOperadorDespacho.getBodega();
                operadorDespachoCollectionOperadorDespacho.setBodega(ubicacion);
                operadorDespachoCollectionOperadorDespacho = em.merge(operadorDespachoCollectionOperadorDespacho);
                if (oldBodegaOfOperadorDespachoCollectionOperadorDespacho != null) {
                    oldBodegaOfOperadorDespachoCollectionOperadorDespacho.getOperadorDespachoCollection().remove(operadorDespachoCollectionOperadorDespacho);
                    oldBodegaOfOperadorDespachoCollectionOperadorDespacho = em.merge(oldBodegaOfOperadorDespachoCollectionOperadorDespacho);
                }
            }
            for (HistorialEstado historialEstadoCollectionHistorialEstado : ubicacion.getHistorialEstadoCollection()) {
                Ubicacion oldUbicacionOfHistorialEstadoCollectionHistorialEstado = historialEstadoCollectionHistorialEstado.getUbicacion();
                historialEstadoCollectionHistorialEstado.setUbicacion(ubicacion);
                historialEstadoCollectionHistorialEstado = em.merge(historialEstadoCollectionHistorialEstado);
                if (oldUbicacionOfHistorialEstadoCollectionHistorialEstado != null) {
                    oldUbicacionOfHistorialEstadoCollectionHistorialEstado.getHistorialEstadoCollection().remove(historialEstadoCollectionHistorialEstado);
                    oldUbicacionOfHistorialEstadoCollectionHistorialEstado = em.merge(oldUbicacionOfHistorialEstadoCollectionHistorialEstado);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ubicacion ubicacion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ubicacion persistentUbicacion = em.find(Ubicacion.class, ubicacion.getIdLocal());
            Collection<Recepcionista> recepcionistaCollectionOld = persistentUbicacion.getRecepcionistaCollection();
            Collection<Recepcionista> recepcionistaCollectionNew = ubicacion.getRecepcionistaCollection();
            Collection<OperadorDespacho> operadorDespachoCollectionOld = persistentUbicacion.getOperadorDespachoCollection();
            Collection<OperadorDespacho> operadorDespachoCollectionNew = ubicacion.getOperadorDespachoCollection();
            Collection<HistorialEstado> historialEstadoCollectionOld = persistentUbicacion.getHistorialEstadoCollection();
            Collection<HistorialEstado> historialEstadoCollectionNew = ubicacion.getHistorialEstadoCollection();
            List<String> illegalOrphanMessages = null;
            for (Recepcionista recepcionistaCollectionOldRecepcionista : recepcionistaCollectionOld) {
                if (!recepcionistaCollectionNew.contains(recepcionistaCollectionOldRecepcionista)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Recepcionista " + recepcionistaCollectionOldRecepcionista + " since its local field is not nullable.");
                }
            }
            for (OperadorDespacho operadorDespachoCollectionOldOperadorDespacho : operadorDespachoCollectionOld) {
                if (!operadorDespachoCollectionNew.contains(operadorDespachoCollectionOldOperadorDespacho)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain OperadorDespacho " + operadorDespachoCollectionOldOperadorDespacho + " since its bodega field is not nullable.");
                }
            }
            for (HistorialEstado historialEstadoCollectionOldHistorialEstado : historialEstadoCollectionOld) {
                if (!historialEstadoCollectionNew.contains(historialEstadoCollectionOldHistorialEstado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain HistorialEstado " + historialEstadoCollectionOldHistorialEstado + " since its ubicacion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Recepcionista> attachedRecepcionistaCollectionNew = new ArrayList<Recepcionista>();
            for (Recepcionista recepcionistaCollectionNewRecepcionistaToAttach : recepcionistaCollectionNew) {
                recepcionistaCollectionNewRecepcionistaToAttach = em.getReference(recepcionistaCollectionNewRecepcionistaToAttach.getClass(), recepcionistaCollectionNewRecepcionistaToAttach.getIdRecepcionista());
                attachedRecepcionistaCollectionNew.add(recepcionistaCollectionNewRecepcionistaToAttach);
            }
            recepcionistaCollectionNew = attachedRecepcionistaCollectionNew;
            ubicacion.setRecepcionistaCollection(recepcionistaCollectionNew);
            Collection<OperadorDespacho> attachedOperadorDespachoCollectionNew = new ArrayList<OperadorDespacho>();
            for (OperadorDespacho operadorDespachoCollectionNewOperadorDespachoToAttach : operadorDespachoCollectionNew) {
                operadorDespachoCollectionNewOperadorDespachoToAttach = em.getReference(operadorDespachoCollectionNewOperadorDespachoToAttach.getClass(), operadorDespachoCollectionNewOperadorDespachoToAttach.getIdOperador());
                attachedOperadorDespachoCollectionNew.add(operadorDespachoCollectionNewOperadorDespachoToAttach);
            }
            operadorDespachoCollectionNew = attachedOperadorDespachoCollectionNew;
            ubicacion.setOperadorDespachoCollection(operadorDespachoCollectionNew);
            Collection<HistorialEstado> attachedHistorialEstadoCollectionNew = new ArrayList<HistorialEstado>();
            for (HistorialEstado historialEstadoCollectionNewHistorialEstadoToAttach : historialEstadoCollectionNew) {
                historialEstadoCollectionNewHistorialEstadoToAttach = em.getReference(historialEstadoCollectionNewHistorialEstadoToAttach.getClass(), historialEstadoCollectionNewHistorialEstadoToAttach.getIdHistorial());
                attachedHistorialEstadoCollectionNew.add(historialEstadoCollectionNewHistorialEstadoToAttach);
            }
            historialEstadoCollectionNew = attachedHistorialEstadoCollectionNew;
            ubicacion.setHistorialEstadoCollection(historialEstadoCollectionNew);
            ubicacion = em.merge(ubicacion);
            for (Recepcionista recepcionistaCollectionNewRecepcionista : recepcionistaCollectionNew) {
                if (!recepcionistaCollectionOld.contains(recepcionistaCollectionNewRecepcionista)) {
                    Ubicacion oldLocalOfRecepcionistaCollectionNewRecepcionista = recepcionistaCollectionNewRecepcionista.getLocal();
                    recepcionistaCollectionNewRecepcionista.setLocal(ubicacion);
                    recepcionistaCollectionNewRecepcionista = em.merge(recepcionistaCollectionNewRecepcionista);
                    if (oldLocalOfRecepcionistaCollectionNewRecepcionista != null && !oldLocalOfRecepcionistaCollectionNewRecepcionista.equals(ubicacion)) {
                        oldLocalOfRecepcionistaCollectionNewRecepcionista.getRecepcionistaCollection().remove(recepcionistaCollectionNewRecepcionista);
                        oldLocalOfRecepcionistaCollectionNewRecepcionista = em.merge(oldLocalOfRecepcionistaCollectionNewRecepcionista);
                    }
                }
            }
            for (OperadorDespacho operadorDespachoCollectionNewOperadorDespacho : operadorDespachoCollectionNew) {
                if (!operadorDespachoCollectionOld.contains(operadorDespachoCollectionNewOperadorDespacho)) {
                    Ubicacion oldBodegaOfOperadorDespachoCollectionNewOperadorDespacho = operadorDespachoCollectionNewOperadorDespacho.getBodega();
                    operadorDespachoCollectionNewOperadorDespacho.setBodega(ubicacion);
                    operadorDespachoCollectionNewOperadorDespacho = em.merge(operadorDespachoCollectionNewOperadorDespacho);
                    if (oldBodegaOfOperadorDespachoCollectionNewOperadorDespacho != null && !oldBodegaOfOperadorDespachoCollectionNewOperadorDespacho.equals(ubicacion)) {
                        oldBodegaOfOperadorDespachoCollectionNewOperadorDespacho.getOperadorDespachoCollection().remove(operadorDespachoCollectionNewOperadorDespacho);
                        oldBodegaOfOperadorDespachoCollectionNewOperadorDespacho = em.merge(oldBodegaOfOperadorDespachoCollectionNewOperadorDespacho);
                    }
                }
            }
            for (HistorialEstado historialEstadoCollectionNewHistorialEstado : historialEstadoCollectionNew) {
                if (!historialEstadoCollectionOld.contains(historialEstadoCollectionNewHistorialEstado)) {
                    Ubicacion oldUbicacionOfHistorialEstadoCollectionNewHistorialEstado = historialEstadoCollectionNewHistorialEstado.getUbicacion();
                    historialEstadoCollectionNewHistorialEstado.setUbicacion(ubicacion);
                    historialEstadoCollectionNewHistorialEstado = em.merge(historialEstadoCollectionNewHistorialEstado);
                    if (oldUbicacionOfHistorialEstadoCollectionNewHistorialEstado != null && !oldUbicacionOfHistorialEstadoCollectionNewHistorialEstado.equals(ubicacion)) {
                        oldUbicacionOfHistorialEstadoCollectionNewHistorialEstado.getHistorialEstadoCollection().remove(historialEstadoCollectionNewHistorialEstado);
                        oldUbicacionOfHistorialEstadoCollectionNewHistorialEstado = em.merge(oldUbicacionOfHistorialEstadoCollectionNewHistorialEstado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ubicacion.getIdLocal();
                if (findUbicacion(id) == null) {
                    throw new NonexistentEntityException("The ubicacion with id " + id + " no longer exists.");
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
            Ubicacion ubicacion;
            try {
                ubicacion = em.getReference(Ubicacion.class, id);
                ubicacion.getIdLocal();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ubicacion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Recepcionista> recepcionistaCollectionOrphanCheck = ubicacion.getRecepcionistaCollection();
            for (Recepcionista recepcionistaCollectionOrphanCheckRecepcionista : recepcionistaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ubicacion (" + ubicacion + ") cannot be destroyed since the Recepcionista " + recepcionistaCollectionOrphanCheckRecepcionista + " in its recepcionistaCollection field has a non-nullable local field.");
            }
            Collection<OperadorDespacho> operadorDespachoCollectionOrphanCheck = ubicacion.getOperadorDespachoCollection();
            for (OperadorDespacho operadorDespachoCollectionOrphanCheckOperadorDespacho : operadorDespachoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ubicacion (" + ubicacion + ") cannot be destroyed since the OperadorDespacho " + operadorDespachoCollectionOrphanCheckOperadorDespacho + " in its operadorDespachoCollection field has a non-nullable bodega field.");
            }
            Collection<HistorialEstado> historialEstadoCollectionOrphanCheck = ubicacion.getHistorialEstadoCollection();
            for (HistorialEstado historialEstadoCollectionOrphanCheckHistorialEstado : historialEstadoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ubicacion (" + ubicacion + ") cannot be destroyed since the HistorialEstado " + historialEstadoCollectionOrphanCheckHistorialEstado + " in its historialEstadoCollection field has a non-nullable ubicacion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(ubicacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ubicacion> findUbicacionEntities() {
        return findUbicacionEntities(true, -1, -1);
    }

    public List<Ubicacion> findUbicacionEntities(int maxResults, int firstResult) {
        return findUbicacionEntities(false, maxResults, firstResult);
    }

    private List<Ubicacion> findUbicacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ubicacion.class));
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

    public Ubicacion findUbicacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ubicacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getUbicacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ubicacion> rt = cq.from(Ubicacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
