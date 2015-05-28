/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.servicios.gestionterritorial.servicios;

import ar.gob.ambiente.servicios.gestionterritorial.entidades.CentroPoblado;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.CentroPobladoTipo;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Departamento;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.EspecificidadDeRegion;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Municipio;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Provincia;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Region;
import ar.gob.ambiente.servicios.gestionterritorial.facades.CentroPobladoFacade;
import ar.gob.ambiente.servicios.gestionterritorial.facades.CentroPobladoTipoFacade;
import ar.gob.ambiente.servicios.gestionterritorial.facades.DepartamentoFacade;
import ar.gob.ambiente.servicios.gestionterritorial.facades.EspecificidadDeRegionFacade;
import ar.gob.ambiente.servicios.gestionterritorial.facades.MunicipioFacade;
import ar.gob.ambiente.servicios.gestionterritorial.facades.ProvinciaFacade;
import ar.gob.ambiente.servicios.gestionterritorial.facades.RegionFacade;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author Administrador
 */
@Stateless
@LocalBean
public class CentrosPobServicio {
    
    @EJB
    private CentroPobladoFacade centroPobladoFacade;
    
    @EJB
    private DepartamentoFacade departamentoFacade;
    
    @EJB
    private MunicipioFacade municipioFacade;
    
    @EJB
    private RegionFacade regionFacade;
    
    @EJB
    private ProvinciaFacade provinciaFacade;
    
    @EJB
    private CentroPobladoTipoFacade centroPobladoTipoFacade;
    
    @EJB
    private EspecificidadDeRegionFacade espRegFacade;
    private static final Logger logger = Logger.getLogger(CentroPoblado.class.getName());

    public List<CentroPoblado> getCentrosPorDeptoYTipo(Long idDepto, Long idTipo) {
        List<CentroPoblado> lstCentros = new ArrayList();
        Date date;
        try{
            lstCentros = centroPobladoFacade.getCentrosXDeptoTipo(idDepto, idTipo);
            logger.log(Level.INFO, "Ejecutando el método getCentrosXDeptoTipo() desde el servicio");
        }
        catch (Exception ex){
            date = new Date(System.currentTimeMillis());
            logger.log(Level.SEVERE, "Hubo un error al ejecutar el método getCentrosXDeptoTipo() desde el servicio de Centros Poblados. " + date + ". ", ex);
        }        
        return lstCentros;
    }
    
    public List<CentroPoblado> getCentrosPorProvYTipo(Long idProv, Long idTipo) {
        List<CentroPoblado> lstCentros = new ArrayList();
        Date date;
        try{
            lstCentros = centroPobladoFacade.getCentrosXProvTipo(idProv, idTipo);
            logger.log(Level.INFO, "Ejecutando el método getCentrosPorProvYTipo() desde el servicio");
        }
        catch (Exception ex){
            date = new Date(System.currentTimeMillis());
            logger.log(Level.SEVERE, "Hubo un error al ejecutar el método getCentrosPorProvYTipo() desde el servicio de Centros Poblados. " + date + ". ", ex);
        }      
        return lstCentros;
    }
    
    public List<CentroPoblado> getCentrosPorRegionYTipo(Long idRegion, Long idTipo) {
        List<CentroPoblado> lstCentros = new ArrayList();
        Date date;
        try{
            lstCentros = centroPobladoFacade.getCentrosXRegionTipo(idRegion, idTipo);
            logger.log(Level.INFO, "Ejecutando el método getCentrosPorRegionYTipo() desde el servicio");
        }
        catch (Exception ex){
            date = new Date(System.currentTimeMillis());
            logger.log(Level.SEVERE, "Hubo un error al ejecutar el método getCentrosPorRegionYTipo() desde el servicio de Centros Poblados. " + date + ". ", ex);
        }      
        return lstCentros;
    }
    
    public List<Departamento> getDeptosPorProvincia(Long idProv){
        List<Departamento> lstDeptos = new ArrayList();
        Date date;
        try{
            lstDeptos = departamentoFacade.getDeptosXIdProv(idProv);
            logger.log(Level.INFO, "Ejecutando el método getDeptosPorProvincia() desde el servicio");
        }
        catch (Exception ex){
            date = new Date(System.currentTimeMillis());
            logger.log(Level.SEVERE, "Hubo un error al ejecutar el método getDeptosPorProvincia() desde el servicio de Centros Poblados. " + date + ". ", ex);
        }      
        return lstDeptos;
    }
    
    public List<Municipio> getMunicipiosPorProvincia(Long idProv){
        List<Municipio> lstMunicipios = new ArrayList();
        Date date;
        try{
            lstMunicipios = municipioFacade.getMunicipioXIdProv(idProv);
            logger.log(Level.INFO, "Ejecutando el método getMunicipiosPorProvincia() desde el servicio");
        }
        catch (Exception ex){
            date = new Date(System.currentTimeMillis());
            logger.log(Level.SEVERE, "Hubo un error al ejecutar el método getMunicipiosPorProvincia() desde el servicio de Centros Poblados. " + date + ". ", ex);
        }      
        return lstMunicipios;
    }
    
    public Municipio getMunicipioPorId(Long idMunicipio){
        Municipio result;
        Date date;
        try{
            result = municipioFacade.find(idMunicipio);
            logger.log(Level.INFO, "Ejecutando el método getMunicipioPorId() desde el servicio");
            return result;
        }catch(Exception ex){
            date = new Date(System.currentTimeMillis());
            logger.log(Level.SEVERE, "Hubo un error al ejecutar el método getMunicipioPorId() desde el servicio de Centros Poblados. " + date + ". ", ex);
            return null;
        }
    }
    
    public List<Region> getRegionesPorProvincia(Long idProv){
        List<Region> lstRegiones = new ArrayList();
        Date date;
        try{
            lstRegiones = regionFacade.getRegionesXidProv(idProv);
            logger.log(Level.INFO, "Ejecutando el método getRegionesPorProvincia() desde el servicio");
        }
        catch (Exception ex){
            date = new Date(System.currentTimeMillis());
            logger.log(Level.SEVERE, "Hubo un error al ejecutar el método getRegionesPorProvincia() desde el servicio de Centros Poblados. " + date + ". ", ex);
        }      
        return lstRegiones;
    }
    
    public List<Region> getRegionesPorEspecif(Long idEspecif){
        List<Region> lstRegiones = new ArrayList();
        Date date;
        try{
            lstRegiones = regionFacade.getRegionesXidEspecif(idEspecif);
            logger.log(Level.INFO, "Ejecutando el método getRegionesPorEspecif() desde el servicio");
        }
        catch (Exception ex){
            date = new Date(System.currentTimeMillis());
            logger.log(Level.SEVERE, "Hubo un error al ejecutar el método getRegionesPorEspecif() desde el servicio de Centros Poblados. " + date + ". ", ex);
        }      
        return lstRegiones;
    }
    
    public List<Provincia> getProvinciasPorRegion(Long idRegion){
        List<Provincia> lstProvincias = new ArrayList();
        Date date;
        try{
            lstProvincias = provinciaFacade.getProvXIdRegion(idRegion);
            logger.log(Level.INFO, "Ejecutando el método getProvinciasPorRegion() desde el servicio");
        }
        catch (Exception ex){
            date = new Date(System.currentTimeMillis());
            logger.log(Level.SEVERE, "Hubo un error al ejecutar el método getProvinciasPorRegion() desde el servicio de Centros Poblados. " + date + ". ", ex);
        }      
        return lstProvincias;
    }    
    
    public List<Provincia> getProvincias(){
        List<Provincia> lstProvincias = new ArrayList();
        Date date;
        try{
            lstProvincias = provinciaFacade.getActivos();
            logger.log(Level.INFO, "Ejecutando el método getProvincias() desde el servicio");
        }
        catch (Exception ex){
            date = new Date(System.currentTimeMillis());
            logger.log(Level.SEVERE, "Hubo un error al ejecutar el método getProvincias() desde el servicio de Centros Poblados. " + date + ". ", ex);
        }      
        return lstProvincias;
    }
    
    public List<CentroPobladoTipo> getTiposCentros(){
        List<CentroPobladoTipo> lstTipoCentro = new ArrayList();
        Date date;
        try{
            lstTipoCentro = centroPobladoTipoFacade.getActivos();
            logger.log(Level.INFO, "Ejecutando el método getTiposCentros() desde el servicio");
        }
        catch (Exception ex){
            date = new Date(System.currentTimeMillis());
            logger.log(Level.SEVERE, "Hubo un error al ejecutar el método getTiposCentros() desde el servicio de Centros Poblados. " + date + ". ", ex);
        }      
        return lstTipoCentro;
    }    
    
    public List<EspecificidadDeRegion> getEspecifRegion(){
        List<EspecificidadDeRegion> lstEpecificidades = new ArrayList();
        Date date;
        try{
            lstEpecificidades = espRegFacade.getActivos();
            logger.log(Level.INFO, "Ejecutando el método getEspecifRegion() desde el servicio");
        }
        catch (Exception ex){
            date = new Date(System.currentTimeMillis());
            logger.log(Level.SEVERE, "Hubo un error al ejecutar el método getEspecifRegion() desde el servicio de Centros Poblados. " + date + ". ", ex);
        }      
        return lstEpecificidades;
    }     
    
    public CentroPoblado getCentroPoblado(Long id) {
        CentroPoblado centro;
        Date date;
        try{
            centro = centroPobladoFacade.find(id);
            logger.log(Level.INFO, "Ejecutando el método getCentroPoblado() desde el servicio");
            return centro;
        }
        catch (Exception ex){
            date = new Date(System.currentTimeMillis());
            logger.log(Level.SEVERE, "Hubo un error al ejecutar el método getCentroPoblado() desde el servicio de Centros Poblados. " + date + ". ", ex);
            return null;
        }
    }    
}
