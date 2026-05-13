/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LogicaControllers;

import Clases.HistorialEstado;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Clases.Paquete;
import Clases.Ubicacion;
import LogicaControllers.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author andre
 */
public class HistorialEstadoJpaController implements Serializable {

    public HistorialEstadoJpaController() {
        this.emf = Persistence.createEntityManagerFactory("GestionPaquetesPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(HistorialEstado historialEstado) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paquete codigoUnico = historialEstado.getCodigoUnico();
            if (codigoUnico != null) {
                codigoUnico = em.getReference(codigoUnico.getClass(), codigoUnico.getCodigoUnico());
                historialEstado.setCodigoUnico(codigoUnico);
            }
            Ubicacion ubicacion = historialEstado.getUbicacion();
            if (ubicacion != null) {
                ubicacion = em.getReference(ubicacion.getClass(), ubicacion.getIdLocal());
                historialEstado.setUbicacion(ubicacion);
            }
            em.persist(historialEstado);
            if (codigoUnico != null) {
                codigoUnico.getHistorialEstadoCollection().add(historialEstado);
                codigoUnico = em.merge(codigoUnico);
            }
            if (ubicacion != null) {
                ubicacion.getHistorialEstadoCollection().add(historialEstado);
                ubicacion = em.merge(ubicacion);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(HistorialEstado historialEstado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HistorialEstado persistentHistorialEstado = em.find(HistorialEstado.class, historialEstado.getIdHistorial());
            Paquete codigoUnicoOld = persistentHistorialEstado.getCodigoUnico();
            Paquete codigoUnicoNew = historialEstado.getCodigoUnico();
            Ubicacion ubicacionOld = persistentHistorialEstado.getUbicacion();
            Ubicacion ubicacionNew = historialEstado.getUbicacion();
            if (codigoUnicoNew != null) {
                codigoUnicoNew = em.getReference(codigoUnicoNew.getClass(), codigoUnicoNew.getCodigoUnico());
                historialEstado.setCodigoUnico(codigoUnicoNew);
            }
            if (ubicacionNew != null) {
                ubicacionNew = em.getReference(ubicacionNew.getClass(), ubicacionNew.getIdLocal());
                historialEstado.setUbicacion(ubicacionNew);
            }
            historialEstado = em.merge(historialEstado);
            if (codigoUnicoOld != null && !codigoUnicoOld.equals(codigoUnicoNew)) {
                codigoUnicoOld.getHistorialEstadoCollection().remove(historialEstado);
                codigoUnicoOld = em.merge(codigoUnicoOld);
            }
            if (codigoUnicoNew != null && !codigoUnicoNew.equals(codigoUnicoOld)) {
                codigoUnicoNew.getHistorialEstadoCollection().add(historialEstado);
                codigoUnicoNew = em.merge(codigoUnicoNew);
            }
            if (ubicacionOld != null && !ubicacionOld.equals(ubicacionNew)) {
                ubicacionOld.getHistorialEstadoCollection().remove(historialEstado);
                ubicacionOld = em.merge(ubicacionOld);
            }
            if (ubicacionNew != null && !ubicacionNew.equals(ubicacionOld)) {
                ubicacionNew.getHistorialEstadoCollection().add(historialEstado);
                ubicacionNew = em.merge(ubicacionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = historialEstado.getIdHistorial();
                if (findHistorialEstado(id) == null) {
                    throw new NonexistentEntityException("The historialEstado with id " + id + " no longer exists.");
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
            HistorialEstado historialEstado;
            try {
                historialEstado = em.getReference(HistorialEstado.class, id);
                historialEstado.getIdHistorial();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The historialEstado with id " + id + " no longer exists.", enfe);
            }
            Paquete codigoUnico = historialEstado.getCodigoUnico();
            if (codigoUnico != null) {
                codigoUnico.getHistorialEstadoCollection().remove(historialEstado);
                codigoUnico = em.merge(codigoUnico);
            }
            Ubicacion ubicacion = historialEstado.getUbicacion();
            if (ubicacion != null) {
                ubicacion.getHistorialEstadoCollection().remove(historialEstado);
                ubicacion = em.merge(ubicacion);
            }
            em.remove(historialEstado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<HistorialEstado> findHistorialEstadoEntities() {
        return findHistorialEstadoEntities(true, -1, -1);
    }

    public List<HistorialEstado> findHistorialEstadoEntities(int maxResults, int firstResult) {
        return findHistorialEstadoEntities(false, maxResults, firstResult);
    }

    private List<HistorialEstado> findHistorialEstadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(HistorialEstado.class));
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

    public HistorialEstado findHistorialEstado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(HistorialEstado.class, id);
        } finally {
            em.close();
        }
    }

    public int getHistorialEstadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<HistorialEstado> rt = cq.from(HistorialEstado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
