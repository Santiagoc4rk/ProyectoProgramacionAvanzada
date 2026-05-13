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
import Clases.Paquete;
import java.util.ArrayList;
import java.util.Collection;
import Clases.Cliente;
import Clases.Recepcionista;
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
public class RecepcionistaJpaController implements Serializable {

    public RecepcionistaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("GestionPaquetesPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Recepcionista recepcionista) {
        if (recepcionista.getPaqueteCollection() == null) {
            recepcionista.setPaqueteCollection(new ArrayList<Paquete>());
        }
        if (recepcionista.getClienteCollection() == null) {
            recepcionista.setClienteCollection(new ArrayList<Cliente>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona cedula = recepcionista.getCedula();
            if (cedula != null) {
                cedula = em.getReference(cedula.getClass(), cedula.getCedula());
                recepcionista.setCedula(cedula);
            }
            Ubicacion local = recepcionista.getLocal();
            if (local != null) {
                local = em.getReference(local.getClass(), local.getIdLocal());
                recepcionista.setLocal(local);
            }
            Collection<Paquete> attachedPaqueteCollection = new ArrayList<Paquete>();
            for (Paquete paqueteCollectionPaqueteToAttach : recepcionista.getPaqueteCollection()) {
                paqueteCollectionPaqueteToAttach = em.getReference(paqueteCollectionPaqueteToAttach.getClass(), paqueteCollectionPaqueteToAttach.getCodigoUnico());
                attachedPaqueteCollection.add(paqueteCollectionPaqueteToAttach);
            }
            recepcionista.setPaqueteCollection(attachedPaqueteCollection);
            Collection<Cliente> attachedClienteCollection = new ArrayList<Cliente>();
            for (Cliente clienteCollectionClienteToAttach : recepcionista.getClienteCollection()) {
                clienteCollectionClienteToAttach = em.getReference(clienteCollectionClienteToAttach.getClass(), clienteCollectionClienteToAttach.getIdCliente());
                attachedClienteCollection.add(clienteCollectionClienteToAttach);
            }
            recepcionista.setClienteCollection(attachedClienteCollection);
            em.persist(recepcionista);
            if (cedula != null) {
                cedula.getRecepcionistaCollection().add(recepcionista);
                cedula = em.merge(cedula);
            }
            if (local != null) {
                local.getRecepcionistaCollection().add(recepcionista);
                local = em.merge(local);
            }
            for (Paquete paqueteCollectionPaquete : recepcionista.getPaqueteCollection()) {
                Recepcionista oldIdRecepcionistaOfPaqueteCollectionPaquete = paqueteCollectionPaquete.getIdRecepcionista();
                paqueteCollectionPaquete.setIdRecepcionista(recepcionista);
                paqueteCollectionPaquete = em.merge(paqueteCollectionPaquete);
                if (oldIdRecepcionistaOfPaqueteCollectionPaquete != null) {
                    oldIdRecepcionistaOfPaqueteCollectionPaquete.getPaqueteCollection().remove(paqueteCollectionPaquete);
                    oldIdRecepcionistaOfPaqueteCollectionPaquete = em.merge(oldIdRecepcionistaOfPaqueteCollectionPaquete);
                }
            }
            for (Cliente clienteCollectionCliente : recepcionista.getClienteCollection()) {
                Recepcionista oldIdRecepcionistaOfClienteCollectionCliente = clienteCollectionCliente.getIdRecepcionista();
                clienteCollectionCliente.setIdRecepcionista(recepcionista);
                clienteCollectionCliente = em.merge(clienteCollectionCliente);
                if (oldIdRecepcionistaOfClienteCollectionCliente != null) {
                    oldIdRecepcionistaOfClienteCollectionCliente.getClienteCollection().remove(clienteCollectionCliente);
                    oldIdRecepcionistaOfClienteCollectionCliente = em.merge(oldIdRecepcionistaOfClienteCollectionCliente);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Recepcionista recepcionista) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Recepcionista persistentRecepcionista = em.find(Recepcionista.class, recepcionista.getIdRecepcionista());
            Persona cedulaOld = persistentRecepcionista.getCedula();
            Persona cedulaNew = recepcionista.getCedula();
            Ubicacion localOld = persistentRecepcionista.getLocal();
            Ubicacion localNew = recepcionista.getLocal();
            Collection<Paquete> paqueteCollectionOld = persistentRecepcionista.getPaqueteCollection();
            Collection<Paquete> paqueteCollectionNew = recepcionista.getPaqueteCollection();
            Collection<Cliente> clienteCollectionOld = persistentRecepcionista.getClienteCollection();
            Collection<Cliente> clienteCollectionNew = recepcionista.getClienteCollection();
            List<String> illegalOrphanMessages = null;
            for (Paquete paqueteCollectionOldPaquete : paqueteCollectionOld) {
                if (!paqueteCollectionNew.contains(paqueteCollectionOldPaquete)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Paquete " + paqueteCollectionOldPaquete + " since its idRecepcionista field is not nullable.");
                }
            }
            for (Cliente clienteCollectionOldCliente : clienteCollectionOld) {
                if (!clienteCollectionNew.contains(clienteCollectionOldCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cliente " + clienteCollectionOldCliente + " since its idRecepcionista field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (cedulaNew != null) {
                cedulaNew = em.getReference(cedulaNew.getClass(), cedulaNew.getCedula());
                recepcionista.setCedula(cedulaNew);
            }
            if (localNew != null) {
                localNew = em.getReference(localNew.getClass(), localNew.getIdLocal());
                recepcionista.setLocal(localNew);
            }
            Collection<Paquete> attachedPaqueteCollectionNew = new ArrayList<Paquete>();
            for (Paquete paqueteCollectionNewPaqueteToAttach : paqueteCollectionNew) {
                paqueteCollectionNewPaqueteToAttach = em.getReference(paqueteCollectionNewPaqueteToAttach.getClass(), paqueteCollectionNewPaqueteToAttach.getCodigoUnico());
                attachedPaqueteCollectionNew.add(paqueteCollectionNewPaqueteToAttach);
            }
            paqueteCollectionNew = attachedPaqueteCollectionNew;
            recepcionista.setPaqueteCollection(paqueteCollectionNew);
            Collection<Cliente> attachedClienteCollectionNew = new ArrayList<Cliente>();
            for (Cliente clienteCollectionNewClienteToAttach : clienteCollectionNew) {
                clienteCollectionNewClienteToAttach = em.getReference(clienteCollectionNewClienteToAttach.getClass(), clienteCollectionNewClienteToAttach.getIdCliente());
                attachedClienteCollectionNew.add(clienteCollectionNewClienteToAttach);
            }
            clienteCollectionNew = attachedClienteCollectionNew;
            recepcionista.setClienteCollection(clienteCollectionNew);
            recepcionista = em.merge(recepcionista);
            if (cedulaOld != null && !cedulaOld.equals(cedulaNew)) {
                cedulaOld.getRecepcionistaCollection().remove(recepcionista);
                cedulaOld = em.merge(cedulaOld);
            }
            if (cedulaNew != null && !cedulaNew.equals(cedulaOld)) {
                cedulaNew.getRecepcionistaCollection().add(recepcionista);
                cedulaNew = em.merge(cedulaNew);
            }
            if (localOld != null && !localOld.equals(localNew)) {
                localOld.getRecepcionistaCollection().remove(recepcionista);
                localOld = em.merge(localOld);
            }
            if (localNew != null && !localNew.equals(localOld)) {
                localNew.getRecepcionistaCollection().add(recepcionista);
                localNew = em.merge(localNew);
            }
            for (Paquete paqueteCollectionNewPaquete : paqueteCollectionNew) {
                if (!paqueteCollectionOld.contains(paqueteCollectionNewPaquete)) {
                    Recepcionista oldIdRecepcionistaOfPaqueteCollectionNewPaquete = paqueteCollectionNewPaquete.getIdRecepcionista();
                    paqueteCollectionNewPaquete.setIdRecepcionista(recepcionista);
                    paqueteCollectionNewPaquete = em.merge(paqueteCollectionNewPaquete);
                    if (oldIdRecepcionistaOfPaqueteCollectionNewPaquete != null && !oldIdRecepcionistaOfPaqueteCollectionNewPaquete.equals(recepcionista)) {
                        oldIdRecepcionistaOfPaqueteCollectionNewPaquete.getPaqueteCollection().remove(paqueteCollectionNewPaquete);
                        oldIdRecepcionistaOfPaqueteCollectionNewPaquete = em.merge(oldIdRecepcionistaOfPaqueteCollectionNewPaquete);
                    }
                }
            }
            for (Cliente clienteCollectionNewCliente : clienteCollectionNew) {
                if (!clienteCollectionOld.contains(clienteCollectionNewCliente)) {
                    Recepcionista oldIdRecepcionistaOfClienteCollectionNewCliente = clienteCollectionNewCliente.getIdRecepcionista();
                    clienteCollectionNewCliente.setIdRecepcionista(recepcionista);
                    clienteCollectionNewCliente = em.merge(clienteCollectionNewCliente);
                    if (oldIdRecepcionistaOfClienteCollectionNewCliente != null && !oldIdRecepcionistaOfClienteCollectionNewCliente.equals(recepcionista)) {
                        oldIdRecepcionistaOfClienteCollectionNewCliente.getClienteCollection().remove(clienteCollectionNewCliente);
                        oldIdRecepcionistaOfClienteCollectionNewCliente = em.merge(oldIdRecepcionistaOfClienteCollectionNewCliente);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = recepcionista.getIdRecepcionista();
                if (findRecepcionista(id) == null) {
                    throw new NonexistentEntityException("The recepcionista with id " + id + " no longer exists.");
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
            Recepcionista recepcionista;
            try {
                recepcionista = em.getReference(Recepcionista.class, id);
                recepcionista.getIdRecepcionista();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recepcionista with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Paquete> paqueteCollectionOrphanCheck = recepcionista.getPaqueteCollection();
            for (Paquete paqueteCollectionOrphanCheckPaquete : paqueteCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Recepcionista (" + recepcionista + ") cannot be destroyed since the Paquete " + paqueteCollectionOrphanCheckPaquete + " in its paqueteCollection field has a non-nullable idRecepcionista field.");
            }
            Collection<Cliente> clienteCollectionOrphanCheck = recepcionista.getClienteCollection();
            for (Cliente clienteCollectionOrphanCheckCliente : clienteCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Recepcionista (" + recepcionista + ") cannot be destroyed since the Cliente " + clienteCollectionOrphanCheckCliente + " in its clienteCollection field has a non-nullable idRecepcionista field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Persona cedula = recepcionista.getCedula();
            if (cedula != null) {
                cedula.getRecepcionistaCollection().remove(recepcionista);
                cedula = em.merge(cedula);
            }
            Ubicacion local = recepcionista.getLocal();
            if (local != null) {
                local.getRecepcionistaCollection().remove(recepcionista);
                local = em.merge(local);
            }
            em.remove(recepcionista);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Recepcionista> findRecepcionistaEntities() {
        return findRecepcionistaEntities(true, -1, -1);
    }

    public List<Recepcionista> findRecepcionistaEntities(int maxResults, int firstResult) {
        return findRecepcionistaEntities(false, maxResults, firstResult);
    }

    private List<Recepcionista> findRecepcionistaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Recepcionista.class));
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

    public Recepcionista findRecepcionista(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Recepcionista.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecepcionistaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Recepcionista> rt = cq.from(Recepcionista.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
