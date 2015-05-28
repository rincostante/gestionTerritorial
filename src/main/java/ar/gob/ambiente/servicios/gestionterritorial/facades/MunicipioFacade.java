/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.servicios.gestionterritorial.facades;

import ar.gob.ambiente.servicios.gestionterritorial.entidades.Departamento;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Municipio;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Provincia;

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
public class MunicipioFacade extends AbstractFacade<Municipio> {
    @PersistenceContext(unitName = "ar.gob.ambiente.servicios_gestionTerritorial_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MunicipioFacade() {
        super(Municipio.class);
    }

    /**
     * Método que devuelve todas las Municipios que contienen la cadena recibida como parámetro 
     * dentro de alguno de sus campos string, en este caso el nombre.
     * @param aBuscar: cadena que buscará en todos los campos de tipo varchar de la tabla correspondiente
     * @return: El conjunto de resultados provenientes de la búsqueda. 
     */      
    public List<Municipio> getXString(String aBuscar){
        em = getEntityManager();
        List<Municipio> result;
        String queryString = "SELECT mun FROM Municipio mun "
                + "WHERE mun.nombre LIKE :stringParam";    
        Query q = em.createQuery(queryString)
                .setParameter("stringParam", "%" + aBuscar + "%"); 
        result = q.getResultList();
        return result;
    }    

    /**
     * Metodo para el autocompletado de la búsqueda por nombre
     * @return 
     */
    public List<String> getNombres(){
        em = getEntityManager();
        String queryString = "SELECT mun.nombre FROM Municipio mun ";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    } 
    
    /**
     * Método que obtiene un Centro Poblado existente según los datos recibidos como parámetro
     * @param nombre
     * @param depto
     * @return 
     */ 
    
    public Municipio getExistente(String nombre, Departamento depto){
        List<Municipio> lCp;
        em = getEntityManager();
        String queryString = "SELECT mu FROM Municipio mu "
                + "WHERE mu.nombre = :stringParam "
                + "AND mu.departamento = :depto";
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
     * Metodo que verifica si ya existe la entidad.
     * @param nombre
     * @param prov
     * @return: devuelve True o False
     */
    public boolean noExiste(String nombre, Provincia prov){
        em = getEntityManager();
        String queryString = "SELECT mu FROM Municipio mu "
                + "WHERE mu.nombre = :stringParam "
                + "AND mu.provincia = :prov";
        Query q = em.createQuery(queryString)
                .setParameter("stringParam", nombre)
                .setParameter("prov", prov);
        return q.getResultList().isEmpty();
    }  
    
    public List<Municipio> getMunicipioXIdProv(Long idProv){
        em = getEntityManager();
        String queryString = "SELECT mu FROM Municipio mu "
                + "WHERE mu.provincia.id = :idProv "
                + "AND mu.adminentidad.habilitado = true";
        Query q = em.createQuery(queryString)
                .setParameter("idProv", idProv);
        return q.getResultList();
    }
}
