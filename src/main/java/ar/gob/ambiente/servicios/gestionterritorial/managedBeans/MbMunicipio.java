/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.servicios.gestionterritorial.managedBeans;

import ar.gob.ambiente.servicios.gestionterritorial.entidades.AdminEntidad;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Departamento;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Municipio;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Provincia;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Usuario;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.util.JsfUtil;
import ar.gob.ambiente.servicios.gestionterritorial.facades.DepartamentoFacade;
import ar.gob.ambiente.servicios.gestionterritorial.facades.MunicipioFacade;
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
public class MbMunicipio implements Serializable {
    
    @EJB
    private ProvinciaFacade pciaFacade;

    @EJB
    private DepartamentoFacade dptoFacade;    
    
    @EJB
    private MunicipioFacade muniFacade;
   
    private Municipio current;
    private DataModel items = null;    
    private List<Provincia> listaProvincias;  
    private List<Departamento> comboDepartamentos;
    private Provincia selectProvincia;
    private boolean iniciado;
    private List<Municipio> listado;
    private List<Municipio> listadoFilter;
    private MbLogin login;
    private Usuario usLogeado;
    private int update; // 0=updateNormal | 1=deshabiliar | 2=habilitar 
    
    
    
    /**
     * Creates a new instance of MbMunicipio
     */
    public MbMunicipio() {
    }

   @PostConstruct
   public void init(){
        iniciado = false;
        update = 0;
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        login = (MbLogin)ctx.getSessionMap().get("mbLogin");
        usLogeado = login.getUsLogeado();    
   }
    
    /********************************
     ** Métodos para la navegación **
     ********************************/
    /**
     * @return La entidad gestionada
     */
    public Municipio getSelected() {
        if (current == null) {
            current = new Municipio();
        }
        return current;
    }   
    
    public List<Municipio> getListadoFilter() {
        return listadoFilter;
    }

    public void setListadoFilter(List<Municipio> listadoFilter) {
        this.listadoFilter = listadoFilter;
    }
    
    public DataModel getItems() {
        if (items == null) {
            items = new ListDataModel(getFacade().findAll());
        }
        return items;
    }

    public Municipio getCurrent() {
        return current;
    }

    public void setCurrent(Municipio current) {
        this.current = current;
    }

    public List<Provincia> getListaProvincias() {
        return listaProvincias;
    }

    public void setListaProvincias(List<Provincia> listaProvincias) {
        this.listaProvincias = listaProvincias;
    }

    public List<Departamento> getComboDepartamentos() {
        return comboDepartamentos;
    }

    public void setComboDepartamentos(List<Departamento> comboDepartamentos) {
        this.comboDepartamentos = comboDepartamentos;
    }

    public Provincia getSelectProvincia() {
        return selectProvincia;
    }

    public void setSelectProvincia(Provincia selectProvincia) {
        this.selectProvincia = selectProvincia;
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
    
    /*******************************
     ** Métodos de inicialización **
     *******************************/
    /**
     * @return acción para el listado de entidades
     */
    public String prepareList() {
        recreateModel();
        listaProvincias = pciaFacade.findAll();
        return "list";
    }

    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareView() {
        return "view";
    }

    /** (Probablemente haya que embeberlo con el listado para una misma vista)
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        listaProvincias = pciaFacade.getActivos();
        current = new Municipio();
        return "new";
    }

    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        listaProvincias = pciaFacade.getActivos();
        return "edit";
    }
    
    public String prepareInicio(){
        recreateModel();
        return "/faces/index";
    }

    
    /**
     * Restea la entidad
     */
    private void recreateModel() {
        items = null;
    }

    /*************************
    ** Métodos de operación **
    **************************/
    /**
     * @return 
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
            if(getFacade().noExiste(current.getNombre(), current.getProvincia())){
                getFacade().create(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("MunicipioCreated"));
                return "view";
            }else{
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("MunicipioExistente"));
                return null;
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("MunicipioCreatedErrorOccured"));
            return null;
        }
    }

    public List<Municipio> getListado() {
        return listado;
    }

    public void setListado(List<Municipio> listado) {
        this.listado = listado;
    }

         /**
     * Método que actualiza un nuevo Centro Poblado en la base de datos.
     * Previamente actualiza los datos de administración
     * @return mensaje que notifica la actualización
     */
    public String update() {
        Date date = new Date(System.currentTimeMillis());
        Municipio municipio;
        
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
                if(getFacade().noExiste(current.getNombre(), current.getProvincia())){
                    getFacade().edit(current);
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("MunicipioUpdated"));
                    return "view";
                }else{
                    municipio = getFacade().getExistente(current.getNombre(), current.getDepartamento());
                    if(municipio.getId() == current.getId()){
                        getFacade().edit(current);
                        JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("MunicipioUpdated"));
                        return "view";
                    }else{
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("MunicipioExistente"));
                        return null;
                    }
                }
            }else if(update == 1){
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("MunicipioDeshabilitado"));
                return "view";
            }else{
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("MunicipioHabilitado"));
                return "view";
            }            

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("MunicipioUpdatedErrorOccured"));
            return null;
        }
    }
    
    /**
     * @param event
     * Metodo que recibe como parametro una provincia y carga los Departamentos relacionados a la misma
     * Combo Dependiente
     */
    
    public void departamentoChangeListener(ValueChangeEvent event) {      
        selectProvincia = (Provincia)event.getNewValue();      
        comboDepartamentos = dptoFacade.getPorProvincia(selectProvincia);      
    }    
    
    public void habilitar() {
        update = 2;
        update();        
        recreateModel();
    }  
    
    /**
     * 
     */    
    public void deshabilitar() {
        update = 1;
        update();        
        recreateModel();
    }      
    
    /*************************
    ** Métodos de selección **
    **************************/

    /**
     * @param id equivalente al id de la entidad persistida
     * @return la entidad correspondiente
     */
    public Municipio getMunicipio(java.lang.Long id) {
        return muniFacade.find(id);
    }    
    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private MunicipioFacade getFacade() {
        return muniFacade;
    }

    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = Municipio.class)
    public static class MunicipioControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MbMunicipio controller = (MbMunicipio) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbMunicipio");
            return controller.getMunicipio(getKey(value));
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
            if (object instanceof Municipio) {
                Municipio o = (Municipio) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Municipio.class.getName());
            }
        }
    }          
}
