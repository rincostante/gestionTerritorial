/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.servicios.gestionterritorial.facades;

import ar.gob.ambiente.servicios.gestionterritorial.entidades.CentroPoblado;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Departamento;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


/**
 *
 * @author epassarelli
 */

@Stateless
public class CentroPobladoFacade extends AbstractFacade<CentroPoblado> {
    @PersistenceContext(unitName = "ar.gob.ambiente.servicios_gestionTerritorial_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CentroPobladoFacade() {
        super(CentroPoblado.class);
    }
    
    /**
     * Método que devuelve todas las Especies que contienen la cadena recibida como parámetro 
     * dentro de alguno de sus campos string, en este caso el nombre.
     * @param stringParam: cadena que buscará en todos los campos de tipo varchar de la tabla correspondiente
     * @return: El conjunto de resultados provenientes de la búsqueda. 
     */      
    public List<CentroPoblado> getXString(String stringParam){
        em = getEntityManager();
        List<CentroPoblado> result;
        
        String queryString = "SELECT cp.* FROM CentroPoblado cp "
                + "WHERE cp.nombre LIKE :stringParam "
                + "AND cp.adminentidad.habilitado =true";
        
        Query q = em.createQuery(queryString)
                .setParameter("stringParam", "%" + stringParam + "%");        
        
        result = q.getResultList();
        return result;
    }
     
    public List<String> getNombre(){
        em = getEntityManager();
        String queryString = "SELECT cp.nombre FROM CentroPoblado cp";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }

    /**
     * Metodo que verifica si ya existe la entidad.
     * @param nombre
     * @param depto
     * @return: devuelve True o False
     */
    public boolean noExiste(String nombre, Departamento depto){
        em = getEntityManager();
        String queryString = "SELECT cp FROM CentroPoblado cp "
                + "WHERE cp.nombre = :stringParam "
                + "AND cp.departamento = :depto";
        Query q = em.createQuery(queryString)
                .setParameter("stringParam", nombre)
                .setParameter("depto", depto);
        return q.getResultList().isEmpty();
    }  

    /**
     * Método que obtiene un Centro Poblado existente según los datos recibidos como parámetro
     * @param nombre
     * @param depto
     * @return 
     */
    public CentroPoblado getExistente(String nombre, Departamento depto){
        List<CentroPoblado> lCp;
        em = getEntityManager();
        String queryString = "SELECT cp FROM CentroPoblado cp "
                + "WHERE cp.nombre = :stringParam "
                + "AND cp.departamento = :depto";
        Query q = em.createQuery(queryString)
                .setParameter("stringParam", nombre)
                .setParameter("depto", depto);
        lCp = q.getResultList();
        if(!lCp.isEmpty()){
            return lCp.get(0);
        }else{
            return null;
        }
    }    
    
    /**
     * Método creado pera las migraciones
     * @param nomCentro
     * @param nomDepto
     * @return 
     */
    public CentroPoblado getByNomCentroYNomDepto(String nomCentro, String nomDepto){
        List<CentroPoblado> lCp;
        em = getEntityManager();
        String queryString = "SELECT cp FROM CentroPoblado cp "
                + "WHERE cp.nombre = :nomCentro "
                + "AND cp.departamento.nombre = :nomDepto";
        Query q = em.createQuery(queryString)
                .setParameter("nomCentro", nomCentro)
                .setParameter("nomDepto", nomDepto);
        lCp = q.getResultList();
        if(!lCp.isEmpty()){
            return lCp.get(0);
        }else{
            return null;
        }
    }
    
    public List<CentroPoblado> getCentrosXDepto(Long idDepto){
        em = getEntityManager();
        String queryString = "SELECT cp FROM CentroPoblado cp "
                + "WHERE cp.departamento.id = :idDepto "
                + "AND cp.adminentidad.habilitado = true";
        Query q = em.createQuery(queryString)
                .setParameter("idDepto", idDepto);
        return q.getResultList();         
    }    
    
    public List<CentroPoblado> getCentrosXDeptoTipo(Long idDepto, Long idTipo){
        em = getEntityManager();
        String queryString = "SELECT cp FROM CentroPoblado cp "
                + "WHERE cp.departamento.id = :idDepto "
                + "AND cp.centroPobladoTipo.id = :idTipo "
                + "AND cp.adminentidad.habilitado = true";
        Query q = em.createQuery(queryString)
                .setParameter("idDepto", idDepto)
                .setParameter("idTipo", idTipo);
        return q.getResultList();         
    }
    
    public List<CentroPoblado> getCentrosXProvTipo(Long idProv, Long idTipo){
        em = getEntityManager();
        String queryString = "SELECT cp FROM CentroPoblado cp "
                + "WHERE cp.departamento.provincia.id = :idProv "
                + "AND cp.centroPobladoTipo.id = :idTipo "
                + "AND cp.adminentidad.habilitado = true";
        Query q = em.createQuery(queryString)
                .setParameter("idProv", idProv)
                .setParameter("idTipo", idTipo);
        return q.getResultList();     
    }
    
    public List<CentroPoblado> getCentrosXRegionTipo(Long idRegion, Long idTipo){
        em = getEntityManager();
        String queryString = "SELECT cp FROM CentroPoblado cp "
                + "INNER JOIN cp.departamento depto "
                + "INNER JOIN depto.provincia prov "
                + "INNER JOIN prov.regiones reg "
                + "WHERE reg.id = :idRegion "
                + "AND cp.centroPobladoTipo.id = :idTipo "
                + "AND cp.adminentidad.habilitado = true ";    
        Query q = em.createQuery(queryString)
                .setParameter("idRegion", idRegion)
                .setParameter("idTipo", idTipo);
        return q.getResultList(); 
    }
}
