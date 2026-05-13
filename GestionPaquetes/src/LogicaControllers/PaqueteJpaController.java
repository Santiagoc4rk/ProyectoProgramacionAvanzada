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
import Clases.Cliente;
import Clases.Recepcionista;
import Clases.Tarifa;
import Clases.Factura;
import java.util.ArrayList;
import java.util.Collection;
import Clases.HistorialEstado;
import Clases.Despacha;
import Clases.Entrega;
import Clases.Paquete;
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
public class PaqueteJpaController implements Serializable {

    public PaqueteJpaController() {
        this.emf = Persistence.createEntityManagerFactory("GestionPaquetesPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Paquete paquete) {
        if (paquete.getFacturaCollection() == null) {
            paquete.setFacturaCollection(new ArrayList<Factura>());
        }
        if (paquete.getHistorialEstadoCollection() == null) {
            paquete.setHistorialEstadoCollection(new ArrayList<HistorialEstado>());
        }
        if (paquete.getDespachaCollection() == null) {
            paquete.setDespachaCollection(new ArrayList<Despacha>());
        }
        if (paquete.getEntregaCollection() == null) {
            paquete.setEntregaCollection(new ArrayList<Entrega>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente idCliente = paquete.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getIdCliente());
                paquete.setIdCliente(idCliente);
            }
            Recepcionista idRecepcionista = paquete.getIdRecepcionista();
            if (idRecepcionista != null) {
                idRecepcionista = em.getReference(idRecepcionista.getClass(), idRecepcionista.getIdRecepcionista());
                paquete.setIdRecepcionista(idRecepcionista);
            }
            Tarifa idTarifa = paquete.getIdTarifa();
            if (idTarifa != null) {
                idTarifa = em.getReference(idTarifa.getClass(), idTarifa.getIdTarifa());
                paquete.setIdTarifa(idTarifa);
            }
            Collection<Factura> attachedFacturaCollection = new ArrayList<Factura>();
            for (Factura facturaCollectionFacturaToAttach : paquete.getFacturaCollection()) {
                facturaCollectionFacturaToAttach = em.getReference(facturaCollectionFacturaToAttach.getClass(), facturaCollectionFacturaToAttach.getIdFactura());
                attachedFacturaCollection.add(facturaCollectionFacturaToAttach);
            }
            paquete.setFacturaCollection(attachedFacturaCollection);
            Collection<HistorialEstado> attachedHistorialEstadoCollection = new ArrayList<HistorialEstado>();
            for (HistorialEstado historialEstadoCollectionHistorialEstadoToAttach : paquete.getHistorialEstadoCollection()) {
                historialEstadoCollectionHistorialEstadoToAttach = em.getReference(historialEstadoCollectionHistorialEstadoToAttach.getClass(), historialEstadoCollectionHistorialEstadoToAttach.getIdHistorial());
                attachedHistorialEstadoCollection.add(historialEstadoCollectionHistorialEstadoToAttach);
            }
            paquete.setHistorialEstadoCollection(attachedHistorialEstadoCollection);
            Collection<Despacha> attachedDespachaCollection = new ArrayList<Despacha>();
            for (Despacha despachaCollectionDespachaToAttach : paquete.getDespachaCollection()) {
                despachaCollectionDespachaToAttach = em.getReference(despachaCollectionDespachaToAttach.getClass(), despachaCollectionDespachaToAttach.getDespachaPK());
                attachedDespachaCollection.add(despachaCollectionDespachaToAttach);
            }
            paquete.setDespachaCollection(attachedDespachaCollection);
            Collection<Entrega> attachedEntregaCollection = new ArrayList<Entrega>();
            for (Entrega entregaCollectionEntregaToAttach : paquete.getEntregaCollection()) {
                entregaCollectionEntregaToAttach = em.getReference(entregaCollectionEntregaToAttach.getClass(), entregaCollectionEntregaToAttach.getEntregaPK());
                attachedEntregaCollection.add(entregaCollectionEntregaToAttach);
            }
            paquete.setEntregaCollection(attachedEntregaCollection);
            em.persist(paquete);
            if (idCliente != null) {
                idCliente.getPaqueteCollection().add(paquete);
                idCliente = em.merge(idCliente);
            }
            if (idRecepcionista != null) {
                idRecepcionista.getPaqueteCollection().add(paquete);
                idRecepcionista = em.merge(idRecepcionista);
            }
            if (idTarifa != null) {
                idTarifa.getPaqueteCollection().add(paquete);
                idTarifa = em.merge(idTarifa);
            }
            for (Factura facturaCollectionFactura : paquete.getFacturaCollection()) {
                Paquete oldPaquetecodigounicoOfFacturaCollectionFactura = facturaCollectionFactura.getPaquetecodigounico();
                facturaCollectionFactura.setPaquetecodigounico(paquete);
                facturaCollectionFactura = em.merge(facturaCollectionFactura);
                if (oldPaquetecodigounicoOfFacturaCollectionFactura != null) {
                    oldPaquetecodigounicoOfFacturaCollectionFactura.getFacturaCollection().remove(facturaCollectionFactura);
                    oldPaquetecodigounicoOfFacturaCollectionFactura = em.merge(oldPaquetecodigounicoOfFacturaCollectionFactura);
                }
            }
            for (HistorialEstado historialEstadoCollectionHistorialEstado : paquete.getHistorialEstadoCollection()) {
                Paquete oldCodigoUnicoOfHistorialEstadoCollectionHistorialEstado = historialEstadoCollectionHistorialEstado.getCodigoUnico();
                historialEstadoCollectionHistorialEstado.setCodigoUnico(paquete);
                historialEstadoCollectionHistorialEstado = em.merge(historialEstadoCollectionHistorialEstado);
                if (oldCodigoUnicoOfHistorialEstadoCollectionHistorialEstado != null) {
                    oldCodigoUnicoOfHistorialEstadoCollectionHistorialEstado.getHistorialEstadoCollection().remove(historialEstadoCollectionHistorialEstado);
                    oldCodigoUnicoOfHistorialEstadoCollectionHistorialEstado = em.merge(oldCodigoUnicoOfHistorialEstadoCollectionHistorialEstado);
                }
            }
            for (Despacha despachaCollectionDespacha : paquete.getDespachaCollection()) {
                Paquete oldPaqueteOfDespachaCollectionDespacha = despachaCollectionDespacha.getPaquete();
                despachaCollectionDespacha.setPaquete(paquete);
                despachaCollectionDespacha = em.merge(despachaCollectionDespacha);
                if (oldPaqueteOfDespachaCollectionDespacha != null) {
                    oldPaqueteOfDespachaCollectionDespacha.getDespachaCollection().remove(despachaCollectionDespacha);
                    oldPaqueteOfDespachaCollectionDespacha = em.merge(oldPaqueteOfDespachaCollectionDespacha);
                }
            }
            for (Entrega entregaCollectionEntrega : paquete.getEntregaCollection()) {
                Paquete oldPaqueteOfEntregaCollectionEntrega = entregaCollectionEntrega.getPaquete();
                entregaCollectionEntrega.setPaquete(paquete);
                entregaCollectionEntrega = em.merge(entregaCollectionEntrega);
                if (oldPaqueteOfEntregaCollectionEntrega != null) {
                    oldPaqueteOfEntregaCollectionEntrega.getEntregaCollection().remove(entregaCollectionEntrega);
                    oldPaqueteOfEntregaCollectionEntrega = em.merge(oldPaqueteOfEntregaCollectionEntrega);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Paquete paquete) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paquete persistentPaquete = em.find(Paquete.class, paquete.getCodigoUnico());
            Cliente idClienteOld = persistentPaquete.getIdCliente();
            Cliente idClienteNew = paquete.getIdCliente();
            Recepcionista idRecepcionistaOld = persistentPaquete.getIdRecepcionista();
            Recepcionista idRecepcionistaNew = paquete.getIdRecepcionista();
            Tarifa idTarifaOld = persistentPaquete.getIdTarifa();
            Tarifa idTarifaNew = paquete.getIdTarifa();
            Collection<Factura> facturaCollectionOld = persistentPaquete.getFacturaCollection();
            Collection<Factura> facturaCollectionNew = paquete.getFacturaCollection();
            Collection<HistorialEstado> historialEstadoCollectionOld = persistentPaquete.getHistorialEstadoCollection();
            Collection<HistorialEstado> historialEstadoCollectionNew = paquete.getHistorialEstadoCollection();
            Collection<Despacha> despachaCollectionOld = persistentPaquete.getDespachaCollection();
            Collection<Despacha> despachaCollectionNew = paquete.getDespachaCollection();
            Collection<Entrega> entregaCollectionOld = persistentPaquete.getEntregaCollection();
            Collection<Entrega> entregaCollectionNew = paquete.getEntregaCollection();
            List<String> illegalOrphanMessages = null;
            for (Factura facturaCollectionOldFactura : facturaCollectionOld) {
                if (!facturaCollectionNew.contains(facturaCollectionOldFactura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Factura " + facturaCollectionOldFactura + " since its paquetecodigounico field is not nullable.");
                }
            }
            for (HistorialEstado historialEstadoCollectionOldHistorialEstado : historialEstadoCollectionOld) {
                if (!historialEstadoCollectionNew.contains(historialEstadoCollectionOldHistorialEstado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain HistorialEstado " + historialEstadoCollectionOldHistorialEstado + " since its codigoUnico field is not nullable.");
                }
            }
            for (Despacha despachaCollectionOldDespacha : despachaCollectionOld) {
                if (!despachaCollectionNew.contains(despachaCollectionOldDespacha)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Despacha " + despachaCollectionOldDespacha + " since its paquete field is not nullable.");
                }
            }
            for (Entrega entregaCollectionOldEntrega : entregaCollectionOld) {
                if (!entregaCollectionNew.contains(entregaCollectionOldEntrega)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Entrega " + entregaCollectionOldEntrega + " since its paquete field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getIdCliente());
                paquete.setIdCliente(idClienteNew);
            }
            if (idRecepcionistaNew != null) {
                idRecepcionistaNew = em.getReference(idRecepcionistaNew.getClass(), idRecepcionistaNew.getIdRecepcionista());
                paquete.setIdRecepcionista(idRecepcionistaNew);
            }
            if (idTarifaNew != null) {
                idTarifaNew = em.getReference(idTarifaNew.getClass(), idTarifaNew.getIdTarifa());
                paquete.setIdTarifa(idTarifaNew);
            }
            Collection<Factura> attachedFacturaCollectionNew = new ArrayList<Factura>();
            for (Factura facturaCollectionNewFacturaToAttach : facturaCollectionNew) {
                facturaCollectionNewFacturaToAttach = em.getReference(facturaCollectionNewFacturaToAttach.getClass(), facturaCollectionNewFacturaToAttach.getIdFactura());
                attachedFacturaCollectionNew.add(facturaCollectionNewFacturaToAttach);
            }
            facturaCollectionNew = attachedFacturaCollectionNew;
            paquete.setFacturaCollection(facturaCollectionNew);
            Collection<HistorialEstado> attachedHistorialEstadoCollectionNew = new ArrayList<HistorialEstado>();
            for (HistorialEstado historialEstadoCollectionNewHistorialEstadoToAttach : historialEstadoCollectionNew) {
                historialEstadoCollectionNewHistorialEstadoToAttach = em.getReference(historialEstadoCollectionNewHistorialEstadoToAttach.getClass(), historialEstadoCollectionNewHistorialEstadoToAttach.getIdHistorial());
                attachedHistorialEstadoCollectionNew.add(historialEstadoCollectionNewHistorialEstadoToAttach);
            }
            historialEstadoCollectionNew = attachedHistorialEstadoCollectionNew;
            paquete.setHistorialEstadoCollection(historialEstadoCollectionNew);
            Collection<Despacha> attachedDespachaCollectionNew = new ArrayList<Despacha>();
            for (Despacha despachaCollectionNewDespachaToAttach : despachaCollectionNew) {
                despachaCollectionNewDespachaToAttach = em.getReference(despachaCollectionNewDespachaToAttach.getClass(), despachaCollectionNewDespachaToAttach.getDespachaPK());
                attachedDespachaCollectionNew.add(despachaCollectionNewDespachaToAttach);
            }
            despachaCollectionNew = attachedDespachaCollectionNew;
            paquete.setDespachaCollection(despachaCollectionNew);
            Collection<Entrega> attachedEntregaCollectionNew = new ArrayList<Entrega>();
            for (Entrega entregaCollectionNewEntregaToAttach : entregaCollectionNew) {
                entregaCollectionNewEntregaToAttach = em.getReference(entregaCollectionNewEntregaToAttach.getClass(), entregaCollectionNewEntregaToAttach.getEntregaPK());
                attachedEntregaCollectionNew.add(entregaCollectionNewEntregaToAttach);
            }
            entregaCollectionNew = attachedEntregaCollectionNew;
            paquete.setEntregaCollection(entregaCollectionNew);
            paquete = em.merge(paquete);
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getPaqueteCollection().remove(paquete);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getPaqueteCollection().add(paquete);
                idClienteNew = em.merge(idClienteNew);
            }
            if (idRecepcionistaOld != null && !idRecepcionistaOld.equals(idRecepcionistaNew)) {
                idRecepcionistaOld.getPaqueteCollection().remove(paquete);
                idRecepcionistaOld = em.merge(idRecepcionistaOld);
            }
            if (idRecepcionistaNew != null && !idRecepcionistaNew.equals(idRecepcionistaOld)) {
                idRecepcionistaNew.getPaqueteCollection().add(paquete);
                idRecepcionistaNew = em.merge(idRecepcionistaNew);
            }
            if (idTarifaOld != null && !idTarifaOld.equals(idTarifaNew)) {
                idTarifaOld.getPaqueteCollection().remove(paquete);
                idTarifaOld = em.merge(idTarifaOld);
            }
            if (idTarifaNew != null && !idTarifaNew.equals(idTarifaOld)) {
                idTarifaNew.getPaqueteCollection().add(paquete);
                idTarifaNew = em.merge(idTarifaNew);
            }
            for (Factura facturaCollectionNewFactura : facturaCollectionNew) {
                if (!facturaCollectionOld.contains(facturaCollectionNewFactura)) {
                    Paquete oldPaquetecodigounicoOfFacturaCollectionNewFactura = facturaCollectionNewFactura.getPaquetecodigounico();
                    facturaCollectionNewFactura.setPaquetecodigounico(paquete);
                    facturaCollectionNewFactura = em.merge(facturaCollectionNewFactura);
                    if (oldPaquetecodigounicoOfFacturaCollectionNewFactura != null && !oldPaquetecodigounicoOfFacturaCollectionNewFactura.equals(paquete)) {
                        oldPaquetecodigounicoOfFacturaCollectionNewFactura.getFacturaCollection().remove(facturaCollectionNewFactura);
                        oldPaquetecodigounicoOfFacturaCollectionNewFactura = em.merge(oldPaquetecodigounicoOfFacturaCollectionNewFactura);
                    }
                }
            }
            for (HistorialEstado historialEstadoCollectionNewHistorialEstado : historialEstadoCollectionNew) {
                if (!historialEstadoCollectionOld.contains(historialEstadoCollectionNewHistorialEstado)) {
                    Paquete oldCodigoUnicoOfHistorialEstadoCollectionNewHistorialEstado = historialEstadoCollectionNewHistorialEstado.getCodigoUnico();
                    historialEstadoCollectionNewHistorialEstado.setCodigoUnico(paquete);
                    historialEstadoCollectionNewHistorialEstado = em.merge(historialEstadoCollectionNewHistorialEstado);
                    if (oldCodigoUnicoOfHistorialEstadoCollectionNewHistorialEstado != null && !oldCodigoUnicoOfHistorialEstadoCollectionNewHistorialEstado.equals(paquete)) {
                        oldCodigoUnicoOfHistorialEstadoCollectionNewHistorialEstado.getHistorialEstadoCollection().remove(historialEstadoCollectionNewHistorialEstado);
                        oldCodigoUnicoOfHistorialEstadoCollectionNewHistorialEstado = em.merge(oldCodigoUnicoOfHistorialEstadoCollectionNewHistorialEstado);
                    }
                }
            }
            for (Despacha despachaCollectionNewDespacha : despachaCollectionNew) {
                if (!despachaCollectionOld.contains(despachaCollectionNewDespacha)) {
                    Paquete oldPaqueteOfDespachaCollectionNewDespacha = despachaCollectionNewDespacha.getPaquete();
                    despachaCollectionNewDespacha.setPaquete(paquete);
                    despachaCollectionNewDespacha = em.merge(despachaCollectionNewDespacha);
                    if (oldPaqueteOfDespachaCollectionNewDespacha != null && !oldPaqueteOfDespachaCollectionNewDespacha.equals(paquete)) {
                        oldPaqueteOfDespachaCollectionNewDespacha.getDespachaCollection().remove(despachaCollectionNewDespacha);
                        oldPaqueteOfDespachaCollectionNewDespacha = em.merge(oldPaqueteOfDespachaCollectionNewDespacha);
                    }
                }
            }
            for (Entrega entregaCollectionNewEntrega : entregaCollectionNew) {
                if (!entregaCollectionOld.contains(entregaCollectionNewEntrega)) {
                    Paquete oldPaqueteOfEntregaCollectionNewEntrega = entregaCollectionNewEntrega.getPaquete();
                    entregaCollectionNewEntrega.setPaquete(paquete);
                    entregaCollectionNewEntrega = em.merge(entregaCollectionNewEntrega);
                    if (oldPaqueteOfEntregaCollectionNewEntrega != null && !oldPaqueteOfEntregaCollectionNewEntrega.equals(paquete)) {
                        oldPaqueteOfEntregaCollectionNewEntrega.getEntregaCollection().remove(entregaCollectionNewEntrega);
                        oldPaqueteOfEntregaCollectionNewEntrega = em.merge(oldPaqueteOfEntregaCollectionNewEntrega);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = paquete.getCodigoUnico();
                if (findPaquete(id) == null) {
                    throw new NonexistentEntityException("The paquete with id " + id + " no longer exists.");
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
            Paquete paquete;
            try {
                paquete = em.getReference(Paquete.class, id);
                paquete.getCodigoUnico();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The paquete with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Factura> facturaCollectionOrphanCheck = paquete.getFacturaCollection();
            for (Factura facturaCollectionOrphanCheckFactura : facturaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paquete (" + paquete + ") cannot be destroyed since the Factura " + facturaCollectionOrphanCheckFactura + " in its facturaCollection field has a non-nullable paquetecodigounico field.");
            }
            Collection<HistorialEstado> historialEstadoCollectionOrphanCheck = paquete.getHistorialEstadoCollection();
            for (HistorialEstado historialEstadoCollectionOrphanCheckHistorialEstado : historialEstadoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paquete (" + paquete + ") cannot be destroyed since the HistorialEstado " + historialEstadoCollectionOrphanCheckHistorialEstado + " in its historialEstadoCollection field has a non-nullable codigoUnico field.");
            }
            Collection<Despacha> despachaCollectionOrphanCheck = paquete.getDespachaCollection();
            for (Despacha despachaCollectionOrphanCheckDespacha : despachaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paquete (" + paquete + ") cannot be destroyed since the Despacha " + despachaCollectionOrphanCheckDespacha + " in its despachaCollection field has a non-nullable paquete field.");
            }
            Collection<Entrega> entregaCollectionOrphanCheck = paquete.getEntregaCollection();
            for (Entrega entregaCollectionOrphanCheckEntrega : entregaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paquete (" + paquete + ") cannot be destroyed since the Entrega " + entregaCollectionOrphanCheckEntrega + " in its entregaCollection field has a non-nullable paquete field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cliente idCliente = paquete.getIdCliente();
            if (idCliente != null) {
                idCliente.getPaqueteCollection().remove(paquete);
                idCliente = em.merge(idCliente);
            }
            Recepcionista idRecepcionista = paquete.getIdRecepcionista();
            if (idRecepcionista != null) {
                idRecepcionista.getPaqueteCollection().remove(paquete);
                idRecepcionista = em.merge(idRecepcionista);
            }
            Tarifa idTarifa = paquete.getIdTarifa();
            if (idTarifa != null) {
                idTarifa.getPaqueteCollection().remove(paquete);
                idTarifa = em.merge(idTarifa);
            }
            em.remove(paquete);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Paquete> findPaqueteEntities() {
        return findPaqueteEntities(true, -1, -1);
    }

    public List<Paquete> findPaqueteEntities(int maxResults, int firstResult) {
        return findPaqueteEntities(false, maxResults, firstResult);
    }

    private List<Paquete> findPaqueteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Paquete.class));
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

    public Paquete findPaquete(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Paquete.class, id);
        } finally {
            em.close();
        }
    }

    public int getPaqueteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Paquete> rt = cq.from(Paquete.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
