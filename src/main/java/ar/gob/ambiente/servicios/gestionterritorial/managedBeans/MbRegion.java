/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.servicios.gestionterritorial.managedBeans;

import ar.gob.ambiente.servicios.gestionterritorial.entidades.AdminEntidad;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.EspecificidadDeRegion;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Provincia;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Region;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Usuario;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.util.JsfUtil;
import ar.gob.ambiente.servicios.gestionterritorial.facades.EspecificidadDeRegionFacade;
import ar.gob.ambiente.servicios.gestionterritorial.facades.ProvinciaFacade;
import ar.gob.ambiente.servicios.gestionterritorial.facades.RegionFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;


/**
 *
 * @author epassarelli
 */
public class MbRegion implements Serializable{

    private Region current;
    private List<Provincia> provVinc;
    private List<Provincia> provVincFilter;
    private List<Provincia> provDisp;
    private List<Provincia> provDispFilter;
    private List<Provincia> provincias;
    private List<Provincia> provinciasFilter;
    private List<Provincia> listProvincias;
    private boolean asignaProvincia; 
    private List<Region> listRegion;  


    @EJB
    private RegionFacade regionFacade;
    
    @EJB
    private ProvinciaFacade provFacade;
    
    @EJB
    private EspecificidadDeRegionFacade espRegionFacade;

    private Region regionSelected;
    private Usuario usLogeado;
    private List<EspecificidadDeRegion> listaEspecificidadDeRegion;   
    private MbLogin login;   
    private boolean iniciado;
    //private int update; // 0=updateNormal | 1=deshabiliar | 2=habilitar




    /**
     * Creates a new instance of MbRegion
     */
    public MbRegion() {
    }

    @PostConstruct
    public void init(){
        iniciado = false;
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        login = (MbLogin)ctx.getSessionMap().get("mbLogin");
        usLogeado = login.getUsLogeado();
    }
    
    /********************************
     ** Getters y Setters ***********
     ********************************/
    
    public List<Provincia> getProvVincFilter() {
        return provVincFilter;
    }

    public void setProvVincFilter(List<Provincia> provVincFilter) {
        this.provVincFilter = provVincFilter;
    }

    public List<Provincia> getProvDispFilter() {
        return provDispFilter;
    }

    public void setProvDispFilter(List<Provincia> provDispFilter) {
        this.provDispFilter = provDispFilter;
    }
    
    public List<Provincia> getProvinciasFilter() {
        return provinciasFilter;
    }

    public void setProvinciasFilter(List<Provincia> provinciasFilter) {
        this.provinciasFilter = provinciasFilter;
    }

    public List<Region> getListRegion() {
        if(listRegion == null){
            listRegion = getFacade().getHabilitadas();
        }
        return listRegion;
    }
    
    public void setListRegion(List<Region> listRegion) {
        this.listRegion = listRegion;
    }

    public List<Provincia> getProvincias() {
        return provincias;
    }

    public void setProvincias(List<Provincia> provincias) {
        this.provincias = provincias;
    }

    public List<Provincia> getListProvincias() {
        return listProvincias;
    }

    public void setListProvincias(List<Provincia> listProvincias) {
        this.listProvincias = listProvincias;
    }

    public boolean isAsignaProvincia() {
        return asignaProvincia;
    }

    public void setAsignaProvincia(boolean asignaProvincia) {
        this.asignaProvincia = asignaProvincia;
    }

    public List<Provincia> getProvVinc() {
        return provVinc;
    }

    public void setProvVinc(List<Provincia> provVinc) {
        this.provVinc = provVinc;
    }

    public List<Provincia> getProvDisp() {
        return provDisp;
    }

    public void setProvDisp(List<Provincia> provDisp) {
        this.provDisp = provDisp;
    }

    public List<EspecificidadDeRegion> getListaEspecificidadDeRegion() {
        return listaEspecificidadDeRegion;
    }

    public void setListaEspecificidadDeRegion(List<EspecificidadDeRegion> listaEspecificidadDeRegion) {
        this.listaEspecificidadDeRegion = listaEspecificidadDeRegion;
    }

    public Region getRegionSelected() {
        return regionSelected;
    }

    public void setRegionSelected(Region regionSelected) {
        this.regionSelected = regionSelected;
    }

    public Usuario getUsLogeado() {
        return usLogeado;
    }

    public void setUsLogeado(Usuario usLogeado) {
        this.usLogeado = usLogeado;
    }
    
 
    /********************************
     ** Métodos para el datamodel **
     ********************************/
    /**
     * @return La entidad gestionada
     */
    public Region getSelected() {
        if (current == null) {
            current = new Region();
        }
        return current;
    }    
    
    
    /*******************************
     ** Métodos de inicialización **
     *******************************/
    /**
     * Método para inicializar el listado de los Actividades Planificadass habilitadas
     * @return acción para el listado de entidades
     */
    public String prepareList() {
        iniciado = true;
        asignaProvincia = false;
        recreateModel();
        if(provVinc != null){
            provVinc.clear();
        }
        if(provDisp != null){
            provDisp.clear();
        }
        return "list";
    } 
    
     /**
     * 
     * @return 
     */
    public String prepareListaDes() {
        recreateModel();
        asignaProvincia = false;
        if(provVinc != null){
            provVinc.clear();
        }
        if(provDisp != null){
            provDisp.clear();
        }
        return "listaDes";
    }     
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareView() {
        asignaProvincia = false;
        current = regionSelected;
        provVinc = current.getProvincias();
        return "view";
    }
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareViewDes() {
        asignaProvincia = false;
        current = regionSelected;
        provVinc = current.getProvincias();
        return "viewDes";
    }

    /** (Probablemente haya que embeberlo con el listado para una misma vista)
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        // cargo los list para el combo de Especificidad De Region
        listaEspecificidadDeRegion = espRegionFacade.getActivos();
     
        // cargo la tabla de Provincias
        provincias = provFacade.getActivos();

        current = new Region();
        return "new";
    }

    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        //cargo los list para los combos
        listaEspecificidadDeRegion = espRegionFacade.getActivos();
        current = regionSelected;
        asignaProvincia = true;
        provVinc = current.getProvincias();
        provDisp = cargarProvinciasDisponibles();
        return "edit";
    }
    
    /**
     *
     * @return
     */
    public String prepareInicio(){
        recreateModel();
        return "/faces/index";
    }
    
    /**
     * Método que verifica que la Actividad Planificada que se quiere eliminar no esté siento utilizada por otra entidad
     * @return 
     */
    public String prepareDestroy(){
        current = regionSelected;
        boolean libre = getFacade().getUtilizado(current.getId());

        if (libre){
            // Elimina
            performDestroy();
            recreateModel();
        }else{
            //No Elimina 
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("RegionNonDeletable"));
        }
        provVinc = current.getProvincias();
        return "view";
    }  
    
    /**
     * 
     * @return 
     */
    public String prepareHabilitar(){
        current = regionSelected;
        try{
            // Actualización de datos de administración de la entidad
            Date date = new Date(System.currentTimeMillis());
            current.getAdminentidad().setFechaModif(date);
            current.getAdminentidad().setUsModif(usLogeado);
            current.getAdminentidad().setHabilitado(true);
            current.getAdminentidad().setUsBaja(null);
            current.getAdminentidad().setFechaBaja(null);

            // Actualizo
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("RegionHabilitada"));
            provVinc = current.getProvincias();
            return "view";
        }catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("RegionHabilitadaErrorOccured"));
            return null; 
        }
    }     

    /*************************
    ** Métodos de operación **
    **************************/
    /**
     * Método que inserta una nueva provincia en la base de datos, previamente genera una entidad de administración
     * con los datos necesarios y luego se la asigna a la región
     * @return mensaje que notifica la inserción
     */
    public String create() {
        if(current.getProvincias().isEmpty()){
            JsfUtil.addSuccessMessage("La Región que está guardando debe estar vinculada, al menos, a una Provincia.");
            return null;
        }else{
            try {
                if(getFacade().noExiste(current.getNombre(), current.getEspecificidadderegion())){
                    // Creación de la entidad de administración y asignación
                    Date date = new Date(System.currentTimeMillis());
                    AdminEntidad admEnt = new AdminEntidad();
                    admEnt.setFechaAlta(date);
                    admEnt.setHabilitado(true);
                    admEnt.setUsAlta(usLogeado);
                    current.setAdminentidad(admEnt);

                    // Inserción
                    getFacade().create(current);
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("RegionCreated"));
                    listaEspecificidadDeRegion.clear();
                    provincias.clear();
                    provVinc = current.getProvincias();
                    return "view";
                }else{
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("RegionExistente"));
                    return null;
                }
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("RegionCreatedErrorOccured"));
                return null;
            }
        }
    }

    /**
     * Método que actualiza una nueva Provincia en la base de datos.
     * Previamente actualiza los datos de administración
     * @return mensaje que notifica la actualización
     */
    public String update() {    
        boolean edito;
        Region sub;
        try {
            sub = getFacade().getExistente(current.getNombre(), current.getEspecificidadderegion());
            if(sub == null){
                edito = true;  
            }else{
                edito = sub.getId().equals(current.getId());
            }
            if(edito){
                // Actualización de datos de administración de la entidad
                Date date = new Date(System.currentTimeMillis());
                current.getAdminentidad().setFechaModif(date);
                current.getAdminentidad().setUsModif(usLogeado);

                // Actualizo
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("RegionUpdated"));
                asignaProvincia = false;
                provDisp.clear();
                listaEspecificidadDeRegion.clear(); 
                return "view";
            }else{
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("RegionExistente"));
                return null; 
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("RegionUpdatedErrorOccured"));
            return null;
        }
    }
    
    /**
     * @return mensaje que notifica el borrado
     */    
    public String destroy() {
        current = regionSelected;
        performDestroy();
        recreateModel();
        return "view";
    }    
    
    /*************************
    ** Métodos de selección **
    **************************/

    /**
     * @param id equivalente al id de la entidad persistida
     * @return la entidad correspondiente
     */
    public Region getRegion(java.lang.Long id) {
        return getFacade().find(id);
    }  
    
    /**
     * Método para revocar la sesión del MB
     * @return 
     */
    public String cleanUp(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
        session.removeAttribute("mbRegion");

        return "inicio";
    }  
    
    
    /**
     * 
     */
    public void verProvincias(){
        provincias = current.getProvincias();
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 950);
        RequestContext.getCurrentInstance().openDialog("", options, null);
    }       

    public void asignarProvincia(Provincia prov){
        provVinc.add(prov);
        provDisp.remove(prov);
        if(provVincFilter != null){
            provVincFilter = null;
        }
        if(provDispFilter != null){
            provDispFilter = null;
        }
    }
    
    public void quitarProvincia(Provincia prov){
        provVinc.remove(prov);
        provDisp.add(prov);
        if(provVincFilter != null){
            provVincFilter = null;
        }
        if(provDispFilter != null){
            provDispFilter = null;
        }
    }
    
    /**
     * Método que borra de la memoria los MB innecesarios al cargar el listado 
     */
    public void iniciar(){
        if(!iniciado){
            String s;
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
            .getExternalContext().getSession(true);
            Enumeration enume = session.getAttributeNames();
            while(enume.hasMoreElements()){
                s = (String)enume.nextElement();
                if(s.substring(0, 2).equals("mb")){
                    if(!s.equals("mbLogin")){
                        session.removeAttribute(s);
                    }
                }
            }
        }
    }    
    
    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private RegionFacade getFacade() {
        return regionFacade;
    }    
    
    /**
     * Restea la entidad
     */
    private void recreateModel() {
        listRegion.clear();
        listRegion = null;
        if(provVincFilter != null){
            provVincFilter = null;
        }
        if(provDispFilter != null){
            provDispFilter = null;
        }
        if(provinciasFilter != null){
            provinciasFilter = null;
        }
    }      
    
    
    /**
     * Método que deshabilita la entidad
     */
    private void performDestroy() {
        try {
            // Actualización de datos de administración de la entidad
            Date date = new Date(System.currentTimeMillis());
            current.getAdminentidad().setFechaBaja(date);
            current.getAdminentidad().setUsBaja(usLogeado);
            current.getAdminentidad().setHabilitado(false);
            
            // Deshabilito la entidad
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("RegionDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("RegionDeletedErrorOccured"));
        }
    }             
    
    /**
     * 
     */
    private List<Provincia> cargarProvinciasDisponibles(){
        List<Provincia> provs = provFacade.getActivos();
        List<Provincia> provsSelect = new ArrayList();
        Iterator itProvs = provs.listIterator();
        while(itProvs.hasNext()){
            Provincia prov = (Provincia)itProvs.next();
            if(!provVinc.contains(prov)){
                provsSelect.add(prov);
            }
        }
        return provsSelect;
    }
    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = Region.class)
    public static class RegionControllerConverter implements Converter {

        /**
         *
         * @param facesContext
         * @param component
         * @param value
         * @return
         */
        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MbRegion controller = (MbRegion) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbRegion");
            return controller.getRegion(getKey(value));
        }

        
        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }
        
        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        /**
         *
         * @param facesContext
         * @param component
         * @param object
         * @return
         */
        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Region) {
                Region o = (Region) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Region.class.getName());
            }
        }
    }                 
}
 

  