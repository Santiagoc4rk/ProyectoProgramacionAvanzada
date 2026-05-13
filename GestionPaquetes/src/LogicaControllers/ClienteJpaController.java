/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LogicaControllers;

import Clases.Cliente;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Clases.Persona;
import Clases.Recepcionista;
import Clases.Paquete;
import LogicaControllers.exceptions.IllegalOrphanException;
import LogicaControllers.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Persistence;
//
/**
 *
 * @author andre
 */
public class ClienteJpaController implements Serializable {

    public ClienteJpaController() {
        this.emf = Persistence.createEntityManagerFactory("GestionPaquetesPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) {
        if (cliente.getPaqueteCollection() == null) {
            cliente.setPaqueteCollection(new ArrayList<Paquete>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona cedula = cliente.getCedula();
            if (cedula != null) {
                cedula = em.getReference(cedula.getClass(), cedula.getCedula());
                cliente.setCedula(cedula);
            }
            Recepcionista idRecepcionista = cliente.getIdRecepcionista();
            if (idRecepcionista != null) {
                idRecepcionista = em.getReference(idRecepcionista.getClass(), idRecepcionista.getIdRecepcionista());
                cliente.setIdRecepcionista(idRecepcionista);
            }
            Collection<Paquete> attachedPaqueteCollection = new ArrayList<Paquete>();
            for (Paquete paqueteCollectionPaqueteToAttach : cliente.getPaqueteCollection()) {
                paqueteCollectionPaqueteToAttach = em.getReference(paqueteCollectionPaqueteToAttach.getClass(), paqueteCollectionPaqueteToAttach.getCodigoUnico());
                attachedPaqueteCollection.add(paqueteCollectionPaqueteToAttach);
            }
            cliente.setPaqueteCollection(attachedPaqueteCollection);
            em.persist(cliente);
            if (cedula != null) {
                cedula.getClienteCollection().add(cliente);
                cedula = em.merge(cedula);
            }
            if (idRecepcionista != null) {
                idRecepcionista.getClienteCollection().add(cliente);
                idRecepcionista = em.merge(idRecepcionista);
            }
            for (Paquete paqueteCollectionPaquete : cliente.getPaqueteCollection()) {
                Cliente oldIdClienteOfPaqueteCollectionPaquete = paqueteCollectionPaquete.getIdCliente();
                paqueteCollectionPaquete.setIdCliente(cliente);
                paqueteCollectionPaquete = em.merge(paqueteCollectionPaquete);
                if (oldIdClienteOfPaqueteCollectionPaquete != null) {
                    oldIdClienteOfPaqueteCollectionPaquete.getPaqueteCollection().remove(paqueteCollectionPaquete);
                    oldIdClienteOfPaqueteCollectionPaquete = em.merge(oldIdClienteOfPaqueteCollectionPaquete);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getIdCliente());
            Persona cedulaOld = persistentCliente.getCedula();
            Persona cedulaNew = cliente.getCedula();
            Recepcionista idRecepcionistaOld = persistentCliente.getIdRecepcionista();
            Recepcionista idRecepcionistaNew = cliente.getIdRecepcionista();
            Collection<Paquete> paqueteCollectionOld = persistentCliente.getPaqueteCollection();
            Collection<Paquete> paqueteCollectionNew = cliente.getPaqueteCollection();
            List<String> illegalOrphanMessages = null;
            for (Paquete paqueteCollectionOldPaquete : paqueteCollectionOld) {
                if (!paqueteCollectionNew.contains(paqueteCollectionOldPaquete)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Paquete " + paqueteCollectionOldPaquete + " since its idCliente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (cedulaNew != null) {
                cedulaNew = em.getReference(cedulaNew.getClass(), cedulaNew.getCedula());
                cliente.setCedula(cedulaNew);
            }
            if (idRecepcionistaNew != null) {
                idRecepcionistaNew = em.getReference(idRecepcionistaNew.getClass(), idRecepcionistaNew.getIdRecepcionista());
                cliente.setIdRecepcionista(idRecepcionistaNew);
            }
            Collection<Paquete> attachedPaqueteCollectionNew = new ArrayList<Paquete>();
            for (Paquete paqueteCollectionNewPaqueteToAttach : paqueteCollectionNew) {
                paqueteCollectionNewPaqueteToAttach = em.getReference(paqueteCollectionNewPaqueteToAttach.getClass(), paqueteCollectionNewPaqueteToAttach.getCodigoUnico());
                attachedPaqueteCollectionNew.add(paqueteCollectionNewPaqueteToAttach);
            }
            paqueteCollectionNew = attachedPaqueteCollectionNew;
            cliente.setPaqueteCollection(paqueteCollectionNew);
            cliente = em.merge(cliente);
            if (cedulaOld != null && !cedulaOld.equals(cedulaNew)) {
                cedulaOld.getClienteCollection().remove(cliente);
                cedulaOld = em.merge(cedulaOld);
            }
            if (cedulaNew != null && !cedulaNew.equals(cedulaOld)) {
                cedulaNew.getClienteCollection().add(cliente);
                cedulaNew = em.merge(cedulaNew);
            }
            if (idRecepcionistaOld != null && !idRecepcionistaOld.equals(idRecepcionistaNew)) {
                idRecepcionistaOld.getClienteCollection().remove(cliente);
                idRecepcionistaOld = em.merge(idRecepcionistaOld);
            }
            if (idRecepcionistaNew != null && !idRecepcionistaNew.equals(idRecepcionistaOld)) {
                idRecepcionistaNew.getClienteCollection().add(cliente);
                idRecepcionistaNew = em.merge(idRecepcionistaNew);
            }
            for (Paquete paqueteCollectionNewPaquete : paqueteCollectionNew) {
                if (!paqueteCollectionOld.contains(paqueteCollectionNewPaquete)) {
                    Cliente oldIdClienteOfPaqueteCollectionNewPaquete = paqueteCollectionNewPaquete.getIdCliente();
                    paqueteCollectionNewPaquete.setIdCliente(cliente);
                    paqueteCollectionNewPaquete = em.merge(paqueteCollectionNewPaquete);
                    if (oldIdClienteOfPaqueteCollectionNewPaquete != null && !oldIdClienteOfPaqueteCollectionNewPaquete.equals(cliente)) {
                        oldIdClienteOfPaqueteCollectionNewPaquete.getPaqueteCollection().remove(paqueteCollectionNewPaquete);
                        oldIdClienteOfPaqueteCollectionNewPaquete = em.merge(oldIdClienteOfPaqueteCollectionNewPaquete);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cliente.getIdCliente();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
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
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getIdCliente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Paquete> paqueteCollectionOrphanCheck = cliente.getPaqueteCollection();
            for (Paquete paqueteCollectionOrphanCheckPaquete : paqueteCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the Paquete " + paqueteCollectionOrphanCheckPaquete + " in its paqueteCollection field has a non-nullable idCliente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Persona cedula = cliente.getCedula();
            if (cedula != null) {
                cedula.getClienteCollection().remove(cliente);
                cedula = em.merge(cedula);
            }
            Recepcionista idRecepcionista = cliente.getIdRecepcionista();
            if (idRecepcionista != null) {
                idRecepcionista.getClienteCollection().remove(cliente);
                idRecepcionista = em.merge(idRecepcionista);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
