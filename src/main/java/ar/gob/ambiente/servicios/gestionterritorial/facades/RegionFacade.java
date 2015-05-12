/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.servicios.gestionterritorial.facades;


import ar.gob.ambiente.servicios.gestionterritorial.entidades.EspecificidadDeRegion;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Region;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author rincostante
 */
@Stateless
public class RegionFacade extends AbstractFacade<Region> {
    @PersistenceContext(unitName = "ar.gob.ambiente.servicios_gestionTerritorial_war_1.0-SNAPSHOTPU")
    private EntityManager em;


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RegionFacade() {
        super(Region.class);
    }
 
   /**
     * Método que verifica si la Region puede ser eliminada
     * @param id: Id de la region que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT prov.id FROM Provincia prov "
                + "INNER JOIN prov.regiones reg "
                + "WHERE reg.id = :id";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    } 
    /**
     * Método para validad que no exista una Actividad Planificada con este nombre ya ingresado
     * @param nombre
     * @param espReg
     * @return 
     */
    public boolean noExiste(String nombre, EspecificidadDeRegion espReg){
        em = getEntityManager();
        String queryString = "SELECT reg FROM Region reg "
                + "WHERE reg.nombre = :nombre "
                + "AND reg.especificidadderegion = :espReg";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre)
                .setParameter("espReg", espReg);
        return q.getResultList().isEmpty();
    }    
    /**
     * Método que obtiene una Region existente según los datos recibidos como parámetro
     * @param nombre
     * @param espReg
     * @return 
     */
    public Region getExistente(String nombre, EspecificidadDeRegion espReg){
        List<Region> lProv;
        String queryString = "SELECT reg FROM Region reg "
                + "WHERE reg.nombre = :nombre "
                + "AND reg.especificidadderegion = :espReg";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre)
                .setParameter("espReg", espReg);
        lProv = q.getResultList();
        if(!lProv.isEmpty()){
            return lProv.get(0);
        }else{
            return null;
        }
    }     
    
    /**
     * Método que devuelve todas los Actividades Planificadas habilitadas y vigentes
     * @return 
     */
    public List<Region> getHabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT reg FROM Region reg "
                + "WHERE reg.adminentidad.habilitado = true";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }

    /**
     * Método que devuelve todas los Actividades Planificadas deshabilitadas
     * @return 
     */
    public List<Region> getDeshabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT reg FROM Region reg "
                + "WHERE reg.adminentidad.habilitado = false";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }  
 
    public List<Region> getRegionesXidProv(Long idProv){
        em = getEntityManager();
        String queryString = "SELECT reg FROM Region reg "
                + "INNER JOIN reg.provincias prov "
                + "WHERE prov.id = :idProv "
                + "AND reg.adminentidad.habilitado = true";
        Query q = em.createQuery(queryString)
                .setParameter("idProv", idProv);
        return q.getResultList();
    }
    
    public List<Region> getRegionesXidEspecif(Long idEspecif){
        em = getEntityManager();
        String queryString = "SELECT reg FROM Region reg "
                + "WHERE reg.especificidadderegion.id = :idEspecif "
                + "AND reg.adminentidad.habilitado = true";
        Query q = em.createQuery(queryString)
                .setParameter("idEspecif", idEspecif);
        return q.getResultList();
    }
}      
     
