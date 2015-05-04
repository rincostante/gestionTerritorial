/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.servicios.gestionterritorial.facades;

import ar.gob.ambiente.servicios.gestionterritorial.entidades.Departamento;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Provincia;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

// **** Prueba de Git

/**
 *
 * @author rincostante
 */
@Stateless
public class DepartamentoFacade extends AbstractFacade<Departamento> {
    @PersistenceContext(unitName = "ar.gob.ambiente.servicios_gestionTerritorial_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DepartamentoFacade() {
        super(Departamento.class);
    }

    /**
     * Método que devuelve todas las Departamentos que contienen la cadena recibida como parámetro 
     * dentro de alguno de sus campos string, en este caso el nombre.
     * @param stringParam: cadena que buscará en todos los campos de tipo varchar de la tabla correspondiente
     * @return: El conjunto de resultados provenientes de la búsqueda. 
     */      
    public List<Departamento> getXString(String stringParam){
        em = getEntityManager();        
        List<Departamento> result; 
        
        String queryString = "SELECT depto FROM Departamento depto "
                + "WHERE depto.nombre LIKE :stringParam "
                + "AND depto.adminentidad.habilitado = true"; 
        
        Query q = em.createQuery(queryString)
                .setParameter("stringParam", "%" + stringParam + "%");  
        
        result = q.getResultList();       
        return result;
    }

    /**
     * Metodo que verifica si ya existe la entidad.
     * @param aBuscar: es la cadena que buscara para ver si ya existe en la BDD
     * @param prov
     * @return: devuelve True o False
     */
    public boolean noExiste(String aBuscar, Provincia prov){
        em = getEntityManager();
        String queryString = "SELECT depto FROM Departamento depto "
                + "WHERE depto.nombre = :stringParam "
                + "AND depto.provincia = :prov "
                + "AND depto.adminentidad.habilitado = true";
        
        Query q = em.createQuery(queryString)
                .setParameter("stringParam", aBuscar)
                .setParameter("prov", prov);
        return q.getResultList().isEmpty();
    }  
    
    /**
     * Metodo que verifica si ya existe la entidad.
     * @param aBuscar: es la cadena que buscara para ver si ya existe en la BDD
     * @param prov
     * @return: devuelve True o False
     */
    public Departamento getExistente(String aBuscar, Provincia prov){
        em = getEntityManager();
        String queryString = "SELECT depto FROM Departamento depto "
                + "WHERE depto.nombre = :stringParam "
                + "AND depto.provincia = :prov "
                + "AND depto.adminentidad.habilitado = true";
        
        Query q = em.createQuery(queryString)
                .setParameter("stringParam", aBuscar)
                .setParameter("prov", prov);
        return (Departamento)q.getSingleResult();
    }        
    
    /**
     * Método que verifica si la entidad tiene dependencia (Hijos)
     * @param id: ID de la entidad
     * @return: True o False
     */
    
    public boolean tieneDependencias(Long id){
        em = getEntityManager();        
        
        String queryString = "SELECT pob FROM CentroPoblado pob " 
                + "WHERE pob.departamento.id = :idParam "
                + "AND pob.adminentidad.habilitado = true";        
        
        Query q = em.createQuery(queryString)
                .setParameter("idParam", id);
        
        return q.getResultList().isEmpty();
    }
    
    
    /**
     * Metodo para el autocompletado de la búsqueda por nombre
     * @return 
     */
    public List<String> getNombres(){
        em = getEntityManager();
        String queryString = "SELECT dep.nombre FROM Departamento dep "
                + "AND dep.adminentidad.habilitado = true";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    } 


   /**
     * Método que devuelve un LIST con las entidades HABILITADAS
     * @return: True o False
     */
    public List<Departamento> getActivos(){
        em = getEntityManager();        
        List<Departamento> result;
        String queryString = "SELECT dpto FROM Departamento dpto " 
                + "WHERE dpto.adminentidad.habilitado = true";                   
        Query q = em.createQuery(queryString);
        result = q.getResultList();
        return result;
    }      

    
   /**
     * Método que devuelve un LIST con TODOS los departamentos de la provincia
     * @param prov: entidad Provincia
     * @return: True o False
     */
    public List<Departamento> getPorProvincia(Provincia prov){
        em = getEntityManager();        
        List<Departamento> result;
        String queryString = "SELECT dpto FROM Departamento dpto " 
                + "WHERE dpto.provincia = :objParam "
                + "AND dpto.adminentidad.habilitado = true";

        Query q = em.createQuery(queryString)
                .setParameter("objParam", prov);        
        
        result = q.getResultList();
        return result;
    }      
    
    public List<Departamento> getDeptosXIdProv(Long idProv){
        em = getEntityManager();     
        String queryString = "SELECT dpto FROM Departamento dpto "
                + "WHERE dpto.provincia.id = :idProv "
                + "AND dpto.adminentidad.habilitado = true";
        Query q = em.createQuery(queryString)
                .setParameter("idProv", idProv);  
        return q.getResultList();
    }
}
