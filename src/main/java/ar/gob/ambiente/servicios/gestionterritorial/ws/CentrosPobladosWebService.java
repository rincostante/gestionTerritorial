/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.servicios.gestionterritorial.ws;

import ar.gob.ambiente.servicios.gestionterritorial.entidades.CentroPoblado;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.CentroPobladoTipo;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Departamento;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.EspecificidadDeRegion;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Municipio;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Provincia;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Region;
import ar.gob.ambiente.servicios.gestionterritorial.servicios.CentrosPobServicio;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author rincostante
 */
@WebService(serviceName = "CentrosPobladosWebService")
@Stateless()
public class CentrosPobladosWebService {
    @EJB
    private CentrosPobServicio ejbRef;

    @WebMethod(operationName = "buscarCentrosPorDeptoYTipo")
    public List<CentroPoblado> getCentrosPorDeptoYTipo(@WebParam(name = "idDepto") Long idDepto, @WebParam(name = "idTipo") Long idTipo) {
        return ejbRef.getCentrosPorDeptoYTipo(idDepto, idTipo);
    }

    @WebMethod(operationName = "buscarCentrosPorProvYTipo")
    public List<CentroPoblado> getCentrosPorProvYTipo(@WebParam(name = "idProv") Long idProv, @WebParam(name = "idTipo") Long idTipo) {
        return ejbRef.getCentrosPorProvYTipo(idProv, idTipo);
    }

    @WebMethod(operationName = "buscarCentrosPorRegionYTipo")
    public List<CentroPoblado> getCentrosPorRegionYTipo(@WebParam(name = "idRegion") Long idRegion, @WebParam(name = "idTipo") Long idTipo) {
        return ejbRef.getCentrosPorRegionYTipo(idRegion, idTipo);
    }

    @WebMethod(operationName = "buscarDeptosPorProvincia")
    public List<Departamento> getDeptosPorProvincia(@WebParam(name = "idProv") Long idProv) {
        return ejbRef.getDeptosPorProvincia(idProv);
    }

    @WebMethod(operationName = "buscarMunicipiosPorProvincia")
    public List<Municipio> getMunicipiosPorProvincia(@WebParam(name = "idProv") Long idProv) {
        return ejbRef.getMunicipiosPorProvincia(idProv);
    }
    
    @WebMethod(operationName = "buscarMunicipiosPorId")
    public Municipio getMunicipiosPorId(@WebParam(name = "idMuni") Long idMuni) {
        return ejbRef.getMunicipioPorId(idMuni);
    }

    @WebMethod(operationName = "buscarRegionesPorProvincia")
    public List<Region> getRegionesPorProvincia(@WebParam(name = "idProv") Long idProv) {
        return ejbRef.getRegionesPorProvincia(idProv);
    }

    @WebMethod(operationName = "buscarRegionesPorEspecif")
    public List<Region> getRegionesPorEspecif(@WebParam(name = "idEspecif") Long idEspecif) {
        return ejbRef.getRegionesPorEspecif(idEspecif);
    }

    @WebMethod(operationName = "buscarProvinciasPorRegion")
    public List<Provincia> getProvinciasPorRegion(@WebParam(name = "idRegion") Long idRegion) {
        return ejbRef.getProvinciasPorRegion(idRegion);
    }

    @WebMethod(operationName = "verProvincias")
    public List<Provincia> getProvincias() {
        return ejbRef.getProvincias();
    }

    @WebMethod(operationName = "verTiposCentros")
    public List<CentroPobladoTipo> getTiposCentros() {
        return ejbRef.getTiposCentros();
    }

    @WebMethod(operationName = "verEspecifRegion")
    public List<EspecificidadDeRegion> getEspecifRegion() {
        return ejbRef.getEspecifRegion();
    
    }
    
    /**
     * Web service operation
     * @param id
     * @return 
     */
    @WebMethod(operationName = "buscarCentroPoblado")
    public CentroPoblado getCentroPoblado(@WebParam(name = "id") Long id) {
        return ejbRef.getCentroPoblado(id);
    }
}
