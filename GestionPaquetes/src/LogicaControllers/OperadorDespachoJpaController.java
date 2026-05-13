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
import Clases.Ubicacion;
import Clases.Despacha;
import Clases.OperadorDespacho;
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
public class OperadorDespachoJpaController implements Serializable {

    public OperadorDespachoJpaController() {
        this.emf = Persistence.createEntityManagerFactory("GestionPaquetesPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(OperadorDespacho operadorDespacho) {
        if (operadorDespacho.getDespachaCollection() == null) {
            operadorDespacho.setDespachaCollection(new ArrayList<Despacha>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona cedula = operadorDespacho.getCedula();
            if (cedula != null) {
                cedula = em.getReference(cedula.getClass(), cedula.getCedula());
                operadorDespacho.setCedula(cedula);
            }
            Ubicacion bodega = operadorDespacho.getBodega();
            if (bodega != null) {
                bodega = em.getReference(bodega.getClass(), bodega.getIdLocal());
                operadorDespacho.setBodega(bodega);
            }
            Collection<Despacha> attachedDespachaCollection = new ArrayList<Despacha>();
            for (Despacha despachaCollectionDespachaToAttach : operadorDespacho.getDespachaCollection()) {
                despachaCollectionDespachaToAttach = em.getReference(despachaCollectionDespachaToAttach.getClass(), despachaCollectionDespachaToAttach.getDespachaPK());
                attachedDespachaCollection.add(despachaCollectionDespachaToAttach);
            }
            operadorDespacho.setDespachaCollection(attachedDespachaCollection);
            em.persist(operadorDespacho);
            if (cedula != null) {
                cedula.getOperadorDespachoCollection().add(operadorDespacho);
                cedula = em.merge(cedula);
            }
            if (bodega != null) {
                bodega.getOperadorDespachoCollection().add(operadorDespacho);
                bodega = em.merge(bodega);
            }
            for (Despacha despachaCollectionDespacha : operadorDespacho.getDespachaCollection()) {
                OperadorDespacho oldOperadorDespachoOfDespachaCollectionDespacha = despachaCollectionDespacha.getOperadorDespacho();
                despachaCollectionDespacha.setOperadorDespacho(operadorDespacho);
                despachaCollectionDespacha = em.merge(despachaCollectionDespacha);
                if (oldOperadorDespachoOfDespachaCollectionDespacha != null) {
                    oldOperadorDespachoOfDespachaCollectionDespacha.getDespachaCollection().remove(despachaCollectionDespacha);
                    oldOperadorDespachoOfDespachaCollectionDespacha = em.merge(oldOperadorDespachoOfDespachaCollectionDespacha);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(OperadorDespacho operadorDespacho) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            OperadorDespacho persistentOperadorDespacho = em.find(OperadorDespacho.class, operadorDespacho.getIdOperador());
            Persona cedulaOld = persistentOperadorDespacho.getCedula();
            Persona cedulaNew = operadorDespacho.getCedula();
            Ubicacion bodegaOld = persistentOperadorDespacho.getBodega();
            Ubicacion bodegaNew = operadorDespacho.getBodega();
            Collection<Despacha> despachaCollectionOld = persistentOperadorDespacho.getDespachaCollection();
            Collection<Despacha> despachaCollectionNew = operadorDespacho.getDespachaCollection();
            List<String> illegalOrphanMessages = null;
            for (Despacha despachaCollectionOldDespacha : despachaCollectionOld) {
                if (!despachaCollectionNew.contains(despachaCollectionOldDespacha)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Despacha " + despachaCollectionOldDespacha + " since its operadorDespacho field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (cedulaNew != null) {
                cedulaNew = em.getReference(cedulaNew.getClass(), cedulaNew.getCedula());
                operadorDespacho.setCedula(cedulaNew);
            }
            if (bodegaNew != null) {
                bodegaNew = em.getReference(bodegaNew.getClass(), bodegaNew.getIdLocal());
                operadorDespacho.setBodega(bodegaNew);
            }
            Collection<Despacha> attachedDespachaCollectionNew = new ArrayList<Despacha>();
            for (Despacha despachaCollectionNewDespachaToAttach : despachaCollectionNew) {
                despachaCollectionNewDespachaToAttach = em.getReference(despachaCollectionNewDespachaToAttach.getClass(), despachaCollectionNewDespachaToAttach.getDespachaPK());
                attachedDespachaCollectionNew.add(despachaCollectionNewDespachaToAttach);
            }
            despachaCollectionNew = attachedDespachaCollectionNew;
            operadorDespacho.setDespachaCollection(despachaCollectionNew);
            operadorDespacho = em.merge(operadorDespacho);
            if (cedulaOld != null && !cedulaOld.equals(cedulaNew)) {
                cedulaOld.getOperadorDespachoCollection().remove(operadorDespacho);
                cedulaOld = em.merge(cedulaOld);
            }
            if (cedulaNew != null && !cedulaNew.equals(cedulaOld)) {
                cedulaNew.getOperadorDespachoCollection().add(operadorDespacho);
                cedulaNew = em.merge(cedulaNew);
            }
            if (bodegaOld != null && !bodegaOld.equals(bodegaNew)) {
                bodegaOld.getOperadorDespachoCollection().remove(operadorDespacho);
                bodegaOld = em.merge(bodegaOld);
            }
            if (bodegaNew != null && !bodegaNew.equals(bodegaOld)) {
                bodegaNew.getOperadorDespachoCollection().add(operadorDespacho);
                bodegaNew = em.merge(bodegaNew);
            }
            for (Despacha despachaCollectionNewDespacha : despachaCollectionNew) {
                if (!despachaCollectionOld.contains(despachaCollectionNewDespacha)) {
                    OperadorDespacho oldOperadorDespachoOfDespachaCollectionNewDespacha = despachaCollectionNewDespacha.getOperadorDespacho();
                    despachaCollectionNewDespacha.setOperadorDespacho(operadorDespacho);
                    despachaCollectionNewDespacha = em.merge(despachaCollectionNewDespacha);
                    if (oldOperadorDespachoOfDespachaCollectionNewDespacha != null && !oldOperadorDespachoOfDespachaCollectionNewDespacha.equals(operadorDespacho)) {
                        oldOperadorDespachoOfDespachaCollectionNewDespacha.getDespachaCollection().remove(despachaCollectionNewDespacha);
                        oldOperadorDespachoOfDespachaCollectionNewDespacha = em.merge(oldOperadorDespachoOfDespachaCollectionNewDespacha);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = operadorDespacho.getIdOperador();
                if (findOperadorDespacho(id) == null) {
                    throw new NonexistentEntityException("The operadorDespacho with id " + id + " no longer exists.");
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
            OperadorDespacho operadorDespacho;
            try {
                operadorDespacho = em.getReference(OperadorDespacho.class, id);
                operadorDespacho.getIdOperador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The operadorDespacho with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Despacha> despachaCollectionOrphanCheck = operadorDespacho.getDespachaCollection();
            for (Despacha despachaCollectionOrphanCheckDespacha : despachaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This OperadorDespacho (" + operadorDespacho + ") cannot be destroyed since the Despacha " + despachaCollectionOrphanCheckDespacha + " in its despachaCollection field has a non-nullable operadorDespacho field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Persona cedula = operadorDespacho.getCedula();
            if (cedula != null) {
                cedula.getOperadorDespachoCollection().remove(operadorDespacho);
                cedula = em.merge(cedula);
            }
            Ubicacion bodega = operadorDespacho.getBodega();
            if (bodega != null) {
                bodega.getOperadorDespachoCollection().remove(operadorDespacho);
                bodega = em.merge(bodega);
            }
            em.remove(operadorDespacho);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<OperadorDespacho> findOperadorDespachoEntities() {
        return findOperadorDespachoEntities(true, -1, -1);
    }

    public List<OperadorDespacho> findOperadorDespachoEntities(int maxResults, int firstResult) {
        return findOperadorDespachoEntities(false, maxResults, firstResult);
    }

    private List<OperadorDespacho> findOperadorDespachoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(OperadorDespacho.class));
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

    public OperadorDespacho findOperadorDespacho(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(OperadorDespacho.class, id);
        } finally {
            em.close();
        }
    }

    public int getOperadorDespachoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<OperadorDespacho> rt = cq.from(OperadorDespacho.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
