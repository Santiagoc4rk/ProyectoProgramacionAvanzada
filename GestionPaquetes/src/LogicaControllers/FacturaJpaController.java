/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LogicaControllers;

import Clases.Factura;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Clases.Paquete;
import LogicaControllers.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author andre
 */
public class FacturaJpaController implements Serializable {

    public FacturaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("GestionPaquetesPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Factura factura) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paquete paquetecodigounico = factura.getPaquetecodigounico();
            if (paquetecodigounico != null) {
                paquetecodigounico = em.getReference(paquetecodigounico.getClass(), paquetecodigounico.getCodigoUnico());
                factura.setPaquetecodigounico(paquetecodigounico);
            }
            em.persist(factura);
            if (paquetecodigounico != null) {
                paquetecodigounico.getFacturaCollection().add(factura);
                paquetecodigounico = em.merge(paquetecodigounico);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Factura factura) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Factura persistentFactura = em.find(Factura.class, factura.getIdFactura());
            Paquete paquetecodigounicoOld = persistentFactura.getPaquetecodigounico();
            Paquete paquetecodigounicoNew = factura.getPaquetecodigounico();
            if (paquetecodigounicoNew != null) {
                paquetecodigounicoNew = em.getReference(paquetecodigounicoNew.getClass(), paquetecodigounicoNew.getCodigoUnico());
                factura.setPaquetecodigounico(paquetecodigounicoNew);
            }
            factura = em.merge(factura);
            if (paquetecodigounicoOld != null && !paquetecodigounicoOld.equals(paquetecodigounicoNew)) {
                paquetecodigounicoOld.getFacturaCollection().remove(factura);
                paquetecodigounicoOld = em.merge(paquetecodigounicoOld);
            }
            if (paquetecodigounicoNew != null && !paquetecodigounicoNew.equals(paquetecodigounicoOld)) {
                paquetecodigounicoNew.getFacturaCollection().add(factura);
                paquetecodigounicoNew = em.merge(paquetecodigounicoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = factura.getIdFactura();
                if (findFactura(id) == null) {
                    throw new NonexistentEntityException("The factura with id " + id + " no longer exists.");
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
            Factura factura;
            try {
                factura = em.getReference(Factura.class, id);
                factura.getIdFactura();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The factura with id " + id + " no longer exists.", enfe);
            }
            Paquete paquetecodigounico = factura.getPaquetecodigounico();
            if (paquetecodigounico != null) {
                paquetecodigounico.getFacturaCollection().remove(factura);
                paquetecodigounico = em.merge(paquetecodigounico);
            }
            em.remove(factura);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Factura> findFacturaEntities() {
        return findFacturaEntities(true, -1, -1);
    }

    public List<Factura> findFacturaEntities(int maxResults, int firstResult) {
        return findFacturaEntities(false, maxResults, firstResult);
    }

    private List<Factura> findFacturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Factura.class));
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

    public Factura findFactura(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Factura.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Factura> rt = cq.from(Factura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
