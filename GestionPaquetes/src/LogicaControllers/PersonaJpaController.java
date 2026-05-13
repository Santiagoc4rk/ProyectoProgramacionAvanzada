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
import Clases.Cliente;
import Clases.Persona;
import Clases.Repartidor;
import Clases.Supervisor;
import LogicaControllers.exceptions.IllegalOrphanException;
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
public class PersonaJpaController implements Serializable {

    public PersonaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("GestionPaquetesPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Persona persona) throws PreexistingEntityException, Exception {
        if (persona.getRecepcionistaCollection() == null) {
            persona.setRecepcionistaCollection(new ArrayList<Recepcionista>());
        }
        if (persona.getOperadorDespachoCollection() == null) {
            persona.setOperadorDespachoCollection(new ArrayList<OperadorDespacho>());
        }
        if (persona.getClienteCollection() == null) {
            persona.setClienteCollection(new ArrayList<Cliente>());
        }
        if (persona.getRepartidorCollection() == null) {
            persona.setRepartidorCollection(new ArrayList<Repartidor>());
        }
        if (persona.getSupervisorCollection() == null) {
            persona.setSupervisorCollection(new ArrayList<Supervisor>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Recepcionista> attachedRecepcionistaCollection = new ArrayList<Recepcionista>();
            for (Recepcionista recepcionistaCollectionRecepcionistaToAttach : persona.getRecepcionistaCollection()) {
                recepcionistaCollectionRecepcionistaToAttach = em.getReference(recepcionistaCollectionRecepcionistaToAttach.getClass(), recepcionistaCollectionRecepcionistaToAttach.getIdRecepcionista());
                attachedRecepcionistaCollection.add(recepcionistaCollectionRecepcionistaToAttach);
            }
            persona.setRecepcionistaCollection(attachedRecepcionistaCollection);
            Collection<OperadorDespacho> attachedOperadorDespachoCollection = new ArrayList<OperadorDespacho>();
            for (OperadorDespacho operadorDespachoCollectionOperadorDespachoToAttach : persona.getOperadorDespachoCollection()) {
                operadorDespachoCollectionOperadorDespachoToAttach = em.getReference(operadorDespachoCollectionOperadorDespachoToAttach.getClass(), operadorDespachoCollectionOperadorDespachoToAttach.getIdOperador());
                attachedOperadorDespachoCollection.add(operadorDespachoCollectionOperadorDespachoToAttach);
            }
            persona.setOperadorDespachoCollection(attachedOperadorDespachoCollection);
            Collection<Cliente> attachedClienteCollection = new ArrayList<Cliente>();
            for (Cliente clienteCollectionClienteToAttach : persona.getClienteCollection()) {
                clienteCollectionClienteToAttach = em.getReference(clienteCollectionClienteToAttach.getClass(), clienteCollectionClienteToAttach.getIdCliente());
                attachedClienteCollection.add(clienteCollectionClienteToAttach);
            }
            persona.setClienteCollection(attachedClienteCollection);
            Collection<Repartidor> attachedRepartidorCollection = new ArrayList<Repartidor>();
            for (Repartidor repartidorCollectionRepartidorToAttach : persona.getRepartidorCollection()) {
                repartidorCollectionRepartidorToAttach = em.getReference(repartidorCollectionRepartidorToAttach.getClass(), repartidorCollectionRepartidorToAttach.getIdRepartidor());
                attachedRepartidorCollection.add(repartidorCollectionRepartidorToAttach);
            }
            persona.setRepartidorCollection(attachedRepartidorCollection);
            Collection<Supervisor> attachedSupervisorCollection = new ArrayList<Supervisor>();
            for (Supervisor supervisorCollectionSupervisorToAttach : persona.getSupervisorCollection()) {
                supervisorCollectionSupervisorToAttach = em.getReference(supervisorCollectionSupervisorToAttach.getClass(), supervisorCollectionSupervisorToAttach.getIdSupervisor());
                attachedSupervisorCollection.add(supervisorCollectionSupervisorToAttach);
            }
            persona.setSupervisorCollection(attachedSupervisorCollection);
            em.persist(persona);
            for (Recepcionista recepcionistaCollectionRecepcionista : persona.getRecepcionistaCollection()) {
                Persona oldCedulaOfRecepcionistaCollectionRecepcionista = recepcionistaCollectionRecepcionista.getCedula();
                recepcionistaCollectionRecepcionista.setCedula(persona);
                recepcionistaCollectionRecepcionista = em.merge(recepcionistaCollectionRecepcionista);
                if (oldCedulaOfRecepcionistaCollectionRecepcionista != null) {
                    oldCedulaOfRecepcionistaCollectionRecepcionista.getRecepcionistaCollection().remove(recepcionistaCollectionRecepcionista);
                    oldCedulaOfRecepcionistaCollectionRecepcionista = em.merge(oldCedulaOfRecepcionistaCollectionRecepcionista);
                }
            }
            for (OperadorDespacho operadorDespachoCollectionOperadorDespacho : persona.getOperadorDespachoCollection()) {
                Persona oldCedulaOfOperadorDespachoCollectionOperadorDespacho = operadorDespachoCollectionOperadorDespacho.getCedula();
                operadorDespachoCollectionOperadorDespacho.setCedula(persona);
                operadorDespachoCollectionOperadorDespacho = em.merge(operadorDespachoCollectionOperadorDespacho);
                if (oldCedulaOfOperadorDespachoCollectionOperadorDespacho != null) {
                    oldCedulaOfOperadorDespachoCollectionOperadorDespacho.getOperadorDespachoCollection().remove(operadorDespachoCollectionOperadorDespacho);
                    oldCedulaOfOperadorDespachoCollectionOperadorDespacho = em.merge(oldCedulaOfOperadorDespachoCollectionOperadorDespacho);
                }
            }
            for (Cliente clienteCollectionCliente : persona.getClienteCollection()) {
                Persona oldCedulaOfClienteCollectionCliente = clienteCollectionCliente.getCedula();
                clienteCollectionCliente.setCedula(persona);
                clienteCollectionCliente = em.merge(clienteCollectionCliente);
                if (oldCedulaOfClienteCollectionCliente != null) {
                    oldCedulaOfClienteCollectionCliente.getClienteCollection().remove(clienteCollectionCliente);
                    oldCedulaOfClienteCollectionCliente = em.merge(oldCedulaOfClienteCollectionCliente);
                }
            }
            for (Repartidor repartidorCollectionRepartidor : persona.getRepartidorCollection()) {
                Persona oldCedulaOfRepartidorCollectionRepartidor = repartidorCollectionRepartidor.getCedula();
                repartidorCollectionRepartidor.setCedula(persona);
                repartidorCollectionRepartidor = em.merge(repartidorCollectionRepartidor);
                if (oldCedulaOfRepartidorCollectionRepartidor != null) {
                    oldCedulaOfRepartidorCollectionRepartidor.getRepartidorCollection().remove(repartidorCollectionRepartidor);
                    oldCedulaOfRepartidorCollectionRepartidor = em.merge(oldCedulaOfRepartidorCollectionRepartidor);
                }
            }
            for (Supervisor supervisorCollectionSupervisor : persona.getSupervisorCollection()) {
                Persona oldCedulaOfSupervisorCollectionSupervisor = supervisorCollectionSupervisor.getCedula();
                supervisorCollectionSupervisor.setCedula(persona);
                supervisorCollectionSupervisor = em.merge(supervisorCollectionSupervisor);
                if (oldCedulaOfSupervisorCollectionSupervisor != null) {
                    oldCedulaOfSupervisorCollectionSupervisor.getSupervisorCollection().remove(supervisorCollectionSupervisor);
                    oldCedulaOfSupervisorCollectionSupervisor = em.merge(oldCedulaOfSupervisorCollectionSupervisor);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPersona(persona.getCedula()) != null) {
                throw new PreexistingEntityException("Persona " + persona + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Persona persona) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona persistentPersona = em.find(Persona.class, persona.getCedula());
            Collection<Recepcionista> recepcionistaCollectionOld = persistentPersona.getRecepcionistaCollection();
            Collection<Recepcionista> recepcionistaCollectionNew = persona.getRecepcionistaCollection();
            Collection<OperadorDespacho> operadorDespachoCollectionOld = persistentPersona.getOperadorDespachoCollection();
            Collection<OperadorDespacho> operadorDespachoCollectionNew = persona.getOperadorDespachoCollection();
            Collection<Cliente> clienteCollectionOld = persistentPersona.getClienteCollection();
            Collection<Cliente> clienteCollectionNew = persona.getClienteCollection();
            Collection<Repartidor> repartidorCollectionOld = persistentPersona.getRepartidorCollection();
            Collection<Repartidor> repartidorCollectionNew = persona.getRepartidorCollection();
            Collection<Supervisor> supervisorCollectionOld = persistentPersona.getSupervisorCollection();
            Collection<Supervisor> supervisorCollectionNew = persona.getSupervisorCollection();
            List<String> illegalOrphanMessages = null;
            for (Recepcionista recepcionistaCollectionOldRecepcionista : recepcionistaCollectionOld) {
                if (!recepcionistaCollectionNew.contains(recepcionistaCollectionOldRecepcionista)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Recepcionista " + recepcionistaCollectionOldRecepcionista + " since its cedula field is not nullable.");
                }
            }
            for (OperadorDespacho operadorDespachoCollectionOldOperadorDespacho : operadorDespachoCollectionOld) {
                if (!operadorDespachoCollectionNew.contains(operadorDespachoCollectionOldOperadorDespacho)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain OperadorDespacho " + operadorDespachoCollectionOldOperadorDespacho + " since its cedula field is not nullable.");
                }
            }
            for (Cliente clienteCollectionOldCliente : clienteCollectionOld) {
                if (!clienteCollectionNew.contains(clienteCollectionOldCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cliente " + clienteCollectionOldCliente + " since its cedula field is not nullable.");
                }
            }
            for (Repartidor repartidorCollectionOldRepartidor : repartidorCollectionOld) {
                if (!repartidorCollectionNew.contains(repartidorCollectionOldRepartidor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Repartidor " + repartidorCollectionOldRepartidor + " since its cedula field is not nullable.");
                }
            }
            for (Supervisor supervisorCollectionOldSupervisor : supervisorCollectionOld) {
                if (!supervisorCollectionNew.contains(supervisorCollectionOldSupervisor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Supervisor " + supervisorCollectionOldSupervisor + " since its cedula field is not nullable.");
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
            persona.setRecepcionistaCollection(recepcionistaCollectionNew);
            Collection<OperadorDespacho> attachedOperadorDespachoCollectionNew = new ArrayList<OperadorDespacho>();
            for (OperadorDespacho operadorDespachoCollectionNewOperadorDespachoToAttach : operadorDespachoCollectionNew) {
                operadorDespachoCollectionNewOperadorDespachoToAttach = em.getReference(operadorDespachoCollectionNewOperadorDespachoToAttach.getClass(), operadorDespachoCollectionNewOperadorDespachoToAttach.getIdOperador());
                attachedOperadorDespachoCollectionNew.add(operadorDespachoCollectionNewOperadorDespachoToAttach);
            }
            operadorDespachoCollectionNew = attachedOperadorDespachoCollectionNew;
            persona.setOperadorDespachoCollection(operadorDespachoCollectionNew);
            Collection<Cliente> attachedClienteCollectionNew = new ArrayList<Cliente>();
            for (Cliente clienteCollectionNewClienteToAttach : clienteCollectionNew) {
                clienteCollectionNewClienteToAttach = em.getReference(clienteCollectionNewClienteToAttach.getClass(), clienteCollectionNewClienteToAttach.getIdCliente());
                attachedClienteCollectionNew.add(clienteCollectionNewClienteToAttach);
            }
            clienteCollectionNew = attachedClienteCollectionNew;
            persona.setClienteCollection(clienteCollectionNew);
            Collection<Repartidor> attachedRepartidorCollectionNew = new ArrayList<Repartidor>();
            for (Repartidor repartidorCollectionNewRepartidorToAttach : repartidorCollectionNew) {
                repartidorCollectionNewRepartidorToAttach = em.getReference(repartidorCollectionNewRepartidorToAttach.getClass(), repartidorCollectionNewRepartidorToAttach.getIdRepartidor());
                attachedRepartidorCollectionNew.add(repartidorCollectionNewRepartidorToAttach);
            }
            repartidorCollectionNew = attachedRepartidorCollectionNew;
            persona.setRepartidorCollection(repartidorCollectionNew);
            Collection<Supervisor> attachedSupervisorCollectionNew = new ArrayList<Supervisor>();
            for (Supervisor supervisorCollectionNewSupervisorToAttach : supervisorCollectionNew) {
                supervisorCollectionNewSupervisorToAttach = em.getReference(supervisorCollectionNewSupervisorToAttach.getClass(), supervisorCollectionNewSupervisorToAttach.getIdSupervisor());
                attachedSupervisorCollectionNew.add(supervisorCollectionNewSupervisorToAttach);
            }
            supervisorCollectionNew = attachedSupervisorCollectionNew;
            persona.setSupervisorCollection(supervisorCollectionNew);
            persona = em.merge(persona);
            for (Recepcionista recepcionistaCollectionNewRecepcionista : recepcionistaCollectionNew) {
                if (!recepcionistaCollectionOld.contains(recepcionistaCollectionNewRecepcionista)) {
                    Persona oldCedulaOfRecepcionistaCollectionNewRecepcionista = recepcionistaCollectionNewRecepcionista.getCedula();
                    recepcionistaCollectionNewRecepcionista.setCedula(persona);
                    recepcionistaCollectionNewRecepcionista = em.merge(recepcionistaCollectionNewRecepcionista);
                    if (oldCedulaOfRecepcionistaCollectionNewRecepcionista != null && !oldCedulaOfRecepcionistaCollectionNewRecepcionista.equals(persona)) {
                        oldCedulaOfRecepcionistaCollectionNewRecepcionista.getRecepcionistaCollection().remove(recepcionistaCollectionNewRecepcionista);
                        oldCedulaOfRecepcionistaCollectionNewRecepcionista = em.merge(oldCedulaOfRecepcionistaCollectionNewRecepcionista);
                    }
                }
            }
            for (OperadorDespacho operadorDespachoCollectionNewOperadorDespacho : operadorDespachoCollectionNew) {
                if (!operadorDespachoCollectionOld.contains(operadorDespachoCollectionNewOperadorDespacho)) {
                    Persona oldCedulaOfOperadorDespachoCollectionNewOperadorDespacho = operadorDespachoCollectionNewOperadorDespacho.getCedula();
                    operadorDespachoCollectionNewOperadorDespacho.setCedula(persona);
                    operadorDespachoCollectionNewOperadorDespacho = em.merge(operadorDespachoCollectionNewOperadorDespacho);
                    if (oldCedulaOfOperadorDespachoCollectionNewOperadorDespacho != null && !oldCedulaOfOperadorDespachoCollectionNewOperadorDespacho.equals(persona)) {
                        oldCedulaOfOperadorDespachoCollectionNewOperadorDespacho.getOperadorDespachoCollection().remove(operadorDespachoCollectionNewOperadorDespacho);
                        oldCedulaOfOperadorDespachoCollectionNewOperadorDespacho = em.merge(oldCedulaOfOperadorDespachoCollectionNewOperadorDespacho);
                    }
                }
            }
            for (Cliente clienteCollectionNewCliente : clienteCollectionNew) {
                if (!clienteCollectionOld.contains(clienteCollectionNewCliente)) {
                    Persona oldCedulaOfClienteCollectionNewCliente = clienteCollectionNewCliente.getCedula();
                    clienteCollectionNewCliente.setCedula(persona);
                    clienteCollectionNewCliente = em.merge(clienteCollectionNewCliente);
                    if (oldCedulaOfClienteCollectionNewCliente != null && !oldCedulaOfClienteCollectionNewCliente.equals(persona)) {
                        oldCedulaOfClienteCollectionNewCliente.getClienteCollection().remove(clienteCollectionNewCliente);
                        oldCedulaOfClienteCollectionNewCliente = em.merge(oldCedulaOfClienteCollectionNewCliente);
                    }
                }
            }
            for (Repartidor repartidorCollectionNewRepartidor : repartidorCollectionNew) {
                if (!repartidorCollectionOld.contains(repartidorCollectionNewRepartidor)) {
                    Persona oldCedulaOfRepartidorCollectionNewRepartidor = repartidorCollectionNewRepartidor.getCedula();
                    repartidorCollectionNewRepartidor.setCedula(persona);
                    repartidorCollectionNewRepartidor = em.merge(repartidorCollectionNewRepartidor);
                    if (oldCedulaOfRepartidorCollectionNewRepartidor != null && !oldCedulaOfRepartidorCollectionNewRepartidor.equals(persona)) {
                        oldCedulaOfRepartidorCollectionNewRepartidor.getRepartidorCollection().remove(repartidorCollectionNewRepartidor);
                        oldCedulaOfRepartidorCollectionNewRepartidor = em.merge(oldCedulaOfRepartidorCollectionNewRepartidor);
                    }
                }
            }
            for (Supervisor supervisorCollectionNewSupervisor : supervisorCollectionNew) {
                if (!supervisorCollectionOld.contains(supervisorCollectionNewSupervisor)) {
                    Persona oldCedulaOfSupervisorCollectionNewSupervisor = supervisorCollectionNewSupervisor.getCedula();
                    supervisorCollectionNewSupervisor.setCedula(persona);
                    supervisorCollectionNewSupervisor = em.merge(supervisorCollectionNewSupervisor);
                    if (oldCedulaOfSupervisorCollectionNewSupervisor != null && !oldCedulaOfSupervisorCollectionNewSupervisor.equals(persona)) {
                        oldCedulaOfSupervisorCollectionNewSupervisor.getSupervisorCollection().remove(supervisorCollectionNewSupervisor);
                        oldCedulaOfSupervisorCollectionNewSupervisor = em.merge(oldCedulaOfSupervisorCollectionNewSupervisor);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = persona.getCedula();
                if (findPersona(id) == null) {
                    throw new NonexistentEntityException("The persona with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona persona;
            try {
                persona = em.getReference(Persona.class, id);
                persona.getCedula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The persona with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Recepcionista> recepcionistaCollectionOrphanCheck = persona.getRecepcionistaCollection();
            for (Recepcionista recepcionistaCollectionOrphanCheckRecepcionista : recepcionistaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Recepcionista " + recepcionistaCollectionOrphanCheckRecepcionista + " in its recepcionistaCollection field has a non-nullable cedula field.");
            }
            Collection<OperadorDespacho> operadorDespachoCollectionOrphanCheck = persona.getOperadorDespachoCollection();
            for (OperadorDespacho operadorDespachoCollectionOrphanCheckOperadorDespacho : operadorDespachoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the OperadorDespacho " + operadorDespachoCollectionOrphanCheckOperadorDespacho + " in its operadorDespachoCollection field has a non-nullable cedula field.");
            }
            Collection<Cliente> clienteCollectionOrphanCheck = persona.getClienteCollection();
            for (Cliente clienteCollectionOrphanCheckCliente : clienteCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Cliente " + clienteCollectionOrphanCheckCliente + " in its clienteCollection field has a non-nullable cedula field.");
            }
            Collection<Repartidor> repartidorCollectionOrphanCheck = persona.getRepartidorCollection();
            for (Repartidor repartidorCollectionOrphanCheckRepartidor : repartidorCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Repartidor " + repartidorCollectionOrphanCheckRepartidor + " in its repartidorCollection field has a non-nullable cedula field.");
            }
            Collection<Supervisor> supervisorCollectionOrphanCheck = persona.getSupervisorCollection();
            for (Supervisor supervisorCollectionOrphanCheckSupervisor : supervisorCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Supervisor " + supervisorCollectionOrphanCheckSupervisor + " in its supervisorCollection field has a non-nullable cedula field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(persona);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Persona> findPersonaEntities() {
        return findPersonaEntities(true, -1, -1);
    }

    public List<Persona> findPersonaEntities(int maxResults, int firstResult) {
        return findPersonaEntities(false, maxResults, firstResult);
    }

    private List<Persona> findPersonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Persona.class));
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

    public Persona findPersona(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Persona.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Persona> rt = cq.from(Persona.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
