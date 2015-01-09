/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.servicios.gestionterritorial.facades;

import ar.gob.ambiente.servicios.gestionterritorial.entidades.EspecificidadDeRegion;
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
public class EspecificidadDeRegionFacade extends AbstractFacade<EspecificidadDeRegion> {
    @PersistenceContext(unitName = "ar.gob.ambiente.servicios_gestionTerritorial_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }    

    /**
     *
     */
    public EspecificidadDeRegionFacade() {
        super(EspecificidadDeRegion.class);
    }
    
    /**
     * Método que devuelve todas las Especificidades de Región que contienen la cadena recibida como parámetro 
     * dentro de alguno de sus campos string, en este caso el nombre.
     * @param stringParam: cadena que buscará en todos los campos de tipo varchar de la tabla correspondiente
     * @return: El conjunto de resultados provenientes de la búsqueda. 
     */      
    public List<EspecificidadDeRegion> getXString(String stringParam){
        em = getEntityManager();
        List<EspecificidadDeRegion> result;
        String queryString = "SELECT * FROM especificidadderegion WHERE nombre LIKE '%" + stringParam + "%'";
        Query q = em.createNativeQuery(queryString, EspecificidadDeRegion.class);
        result = q.getResultList();
        return result;
    }

    /**
     * Metodo que verifica si ya existe la entidad.
     * @param nombre: es la cadena que buscara para ver si ya existe en la BDD
     * @return: devuelve True o False
     */
    public boolean existe(String nombre){
        em = getEntityManager();
        String queryString = "SELECT * FROM especificidadderegion WHERE nombre = '" + nombre + "'";
        Query q = em.createNativeQuery(queryString, EspecificidadDeRegion.class);
        return q.getResultList().isEmpty();
    }    
    
    /**
     * Método que verifica si la entidad tiene dependencia (Hijos)
     * @param id: ID de la entidad
     * @return: True o False
     */
    public boolean tieneDependencias(Long id){
        em = getEntityManager();
        String queryString = "SELECT * FROM region WHERE especificidadderegion_id = " + id;
        Query q = em.createNativeQuery(queryString, EspecificidadDeRegion.class);
        return q.getResultList().isEmpty();
    }
    

    
}