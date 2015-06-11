/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.servicios.gestionterritorial.managedBeans;

import ar.gob.ambiente.servicios.gestionterritorial.entidades.AdminEntidad;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.CentroPoblado;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.CentroPobladoTipo;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Departamento;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Provincia;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Usuario;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.util.JsfUtil;
import ar.gob.ambiente.servicios.gestionterritorial.facades.CentroPobladoFacade;
import ar.gob.ambiente.servicios.gestionterritorial.facades.CentroPobladoTipoFacade;
import ar.gob.ambiente.servicios.gestionterritorial.facades.DepartamentoFacade;
import ar.gob.ambiente.servicios.gestionterritorial.facades.ProvinciaFacade;
import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpSession;


/**
 *
 * @author epassarelli
 */

public class MbCentroPoblado implements Serializable {

    private CentroPoblado current;
    private DataModel items = null;

    @EJB
    private DepartamentoFacade departamentoFacade;

    @EJB
    private CentroPobladoTipoFacade centroPobladoTipoFacade;
   
    @EJB
    private ProvinciaFacade provFacade;
           
    @EJB
    private CentroPobladoFacade centroPobladoFacade;
    private List<Departamento> listaDepartamentos; 
    private List<CentroPobladoTipo> listaTiposCP;
    private List<Provincia> listaProvincias; 
    private boolean iniciado;
    private Provincia selectProvincia; 
    private Provincia provSelected;
    private int update; // 0=updateNormal | 1=deshabiliar | 2=habilitar 
    private MbLogin login;
    private Usuario usLogeado;    
    private List<CentroPoblado> listado;
    private List<CentroPoblado> listadoFilter;
    private List<String> listaNombres;

    /**
     * Creates a new instance of MbCentroPoblado
     */
    public MbCentroPoblado() {
    }

    /**
     * Método que se ejecuta luego de instanciada la clase e inicializa los datos del usuario
     */
    @PostConstruct
    public void init(){
        iniciado = false;
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        login = (MbLogin)ctx.getSessionMap().get("mbLogin");
        usLogeado = login.getUsLogeado();
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

    /********************************
     ** Getters y Setters ***********
     ********************************/ 
    public CentroPoblado getCurrent() {
        return current;
    }

    public void setCurrent(CentroPoblado current) {
        this.current = current;
    }
    
    public Provincia getProvSelected() {
        return provSelected;
    }

    public void setProvSelected(Provincia provSelected) {
        this.provSelected = provSelected;
    }

    public List<CentroPoblado> getListado() {
        return listado;
    }

    public void setListado(List<CentroPoblado> listado) {
        this.listado = listado;
    }

    public List<CentroPoblado> getListadoFilter() {
        return listadoFilter;
    }

    public void setListadoFilter(List<CentroPoblado> listadoFilter) {
        this.listadoFilter = listadoFilter;
    }

    public List<Departamento> getListaDepartamentos() {
        return listaDepartamentos;
    }

    public void setListaDepartamentos(List<Departamento> listaDepartamentos) {
        this.listaDepartamentos = listaDepartamentos;
    }

    public List<CentroPobladoTipo> getListaTiposCP() {
        return listaTiposCP;
    }

    public void setListaTiposCP(List<CentroPobladoTipo> listaTiposCP) {
        this.listaTiposCP = listaTiposCP;
    }


    public List<Provincia> getListaProvincias() {
        return listaProvincias;
    }

    public void setListaProvincias(List<Provincia> listaProvincias) {
        this.listaProvincias = listaProvincias;
    }

    public Provincia getSelectProvincia() {
        return selectProvincia;
    }

    public void setSelectProvincia(Provincia selectProvincia) {
        this.selectProvincia = selectProvincia;
    } 
    
    /**
     * @return La entidad gestionada
     */
    public CentroPoblado getSelected() {
        if (current == null) {
            current = new CentroPoblado();
        }
        return current;
    }

    public DataModel getItems() {
        if (items == null) {
            //items = getPagination().createPageDataModel();
            items = new ListDataModel(getFacade().findAll());
        }
        return items;
    }    
    
    public List<String> getListaNombres() {
        return listaNombres;
    }

    public void setListaNombres(List<String> listaNombres) {
        this.listaNombres = listaNombres;
    }
    
    /*******************************
     ** Métodos de inicialización **
     *******************************/
    /**
     * Método para inicializar el listado de los Participantes autorizados
     * @return acción para el listado de entidades
     */
    public String prepareList() {
        recreateModel();
        return "list";
    }     
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareView() {
        return "view";
    }    
    
    /**
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        listaProvincias = provFacade.getActivos();
        listaDepartamentos = departamentoFacade.getActivos();
        listaTiposCP = centroPobladoTipoFacade.getActivos();
        current = new CentroPoblado();
        return "new";
    }    
    
    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        //cargo los list para los combos
        listaTiposCP = centroPobladoTipoFacade.getActivos();
        listaProvincias = provFacade.getActivos();
        listaDepartamentos = departamentoFacade.getActivos();
        return "edit";
    }    
    
    public String prepareInicio(){
        recreateModel();
        return "/faces/index";
    } 
   
    
    /*************************
    ** Métodos de operación **
    **************************/
    /**
     * Método que inserta un nuevo Centro Poblado en la base de datos, previamente genera una entidad de administración
     * con los datos necesarios y luego se la asigna a la persona
     * @return mensaje que notifica la inserción
     */
    public String create() {
        // Creación de la entidad de administración y asignación
        Date date = new Date(System.currentTimeMillis());
        AdminEntidad admEnt = new AdminEntidad();
        admEnt.setFechaAlta(date);
        admEnt.setHabilitado(true);
        admEnt.setUsAlta(usLogeado);
        current.setAdminentidad(admEnt);        
        try {
            if(getFacade().noExiste(current.getNombre(), current.getDepartamento())){
                getFacade().create(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CentroPobladoCreated"));
                return "view";
            }else{
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("CreateCentroPobladoExistente"));
                return null;
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("CentroPobladoCreatedErrorOccured"));
            return null;
        }
    }
    
    /**
     * @return mensaje que notifica la actualización
     */
    public String update() {
        Date date = new Date(System.currentTimeMillis());
        CentroPoblado centroPoblado;
        
        // actualizamos según el valor de update
        if(update == 1){
            current.getAdminentidad().setFechaBaja(date);
            current.getAdminentidad().setUsBaja(usLogeado);
            current.getAdminentidad().setHabilitado(false);
        }
        if(update == 2){
            current.getAdminentidad().setFechaModif(date);
            current.getAdminentidad().setUsModif(usLogeado);
            current.getAdminentidad().setHabilitado(true);
            current.getAdminentidad().setFechaBaja(null);
            current.getAdminentidad().setUsBaja(usLogeado);
        }
        if(update == 0){
            current.getAdminentidad().setFechaModif(date);
            current.getAdminentidad().setUsModif(usLogeado);
        }
        // acualizo
        try {
            if(update == 0){
                if(getFacade().noExiste(current.getNombre(), current.getDepartamento())){
                    getFacade().edit(current);
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CentroPobladoUpdated"));
                    return "view";
                }else{
                    centroPoblado = getFacade().getExistente(current.getNombre(), current.getDepartamento());
                    if(centroPoblado.getId() == current.getId()){
                        getFacade().edit(current);
                        JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CentroPobladoUpdated"));
                        return "view";
                    }else{
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("CreateCentroPobladoExistente"));
                        return null;
                    }
                }
            }else if(update == 1){
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CentroPobladoDeshabilitado"));
                return "view";
            }else{
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CentroPobladoHabilitado"));
                return "view";
            }         
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("CentroPobladoUpdatedErrorOccured"));
            return null;
        }
    }
    
        public void habilitar() {
            update = 2;
            update();        
            recreateModel();
        }
    
        
    /*************************
    ** Métodos de selección **
    **************************/
   
     /**
      * 
     */    
    public void deshabilitar() {
        update = 1;
        update();        
        recreateModel();
    } 
    
    /**
     * @param id equivalente al id de la entidad persistida
     * @return la entidad correspondiente
     */
    public CentroPoblado getCentroPoblado(java.lang.Long id) {
        return getFacade().find(id);
    } 
   

    /**
     * Método que deshabilita la entidad
     * @param event
     */    
    public void provinciaChangeListener(ValueChangeEvent event){
        provSelected = (Provincia)event.getNewValue();
        listaDepartamentos = departamentoFacade.getPorProvincia(provSelected);
    }

    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private CentroPobladoFacade getFacade() {
        return centroPobladoFacade;
    }    
    
    /**
     * Restea la entidad
     */
    private void recreateModel() {
        items = null;
    }      
    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = CentroPoblado.class)
    public static class CentroPobladoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MbCentroPoblado controller = (MbCentroPoblado) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbCentroPoblado");
            return controller.getCentroPoblado(getKey(value));
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

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof CentroPoblado) {
                CentroPoblado o = (CentroPoblado) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + CentroPoblado.class.getName());
            }
        }
    }             
}
