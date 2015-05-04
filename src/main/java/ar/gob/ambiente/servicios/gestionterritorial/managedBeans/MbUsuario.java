/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.servicios.gestionterritorial.managedBeans;

import ar.gob.ambiente.servicios.gestionterritorial.entidades.AdminEntidad;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Rol;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Usuario;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.util.CriptPass;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.util.JsfUtil;
import ar.gob.ambiente.servicios.gestionterritorial.facades.RolFacade;
import ar.gob.ambiente.servicios.gestionterritorial.facades.UsuarioFacade;
import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

/**
 *
 * @author rincostante
 */
public class MbUsuario implements Serializable{
    
    private Usuario current;
    private List<Usuario> listado;
    private List<Usuario> listFilter;
    private Usuario usLogeado;
    private MbLogin login;   
    private boolean iniciado;  
    private int update; // 0=updateNormal | 1=deshabiliar | 2=habilitar
    
    @EJB
    private RolFacade rolFacade;
    @EJB
    private UsuarioFacade usuarioFacade;       
    private List<Rol> listaRol; 

    
    /**
     * Creates a new instance of MbUsuario
     */
    public MbUsuario() {
    }
    
    /**
     *
     */
    @PostConstruct
    public void init(){
        update = 0;
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
                    if(!s.equals("mbUsuario") && !s.equals("mbLogin")){
                        session.removeAttribute(s);
                    }
                }
            }
        }
    }      

    /********************************
     ** Getters y Setters *********** 
     ********************************/
    public Usuario getCurrent() {
        return current;
    }

    public void setCurrent(Usuario current) {
        this.current = current;
    }
    
    public List<Rol> getListaRol() {
        return listaRol;
    }

    public void setListaRol(List<Rol> listaRol) {
        this.listaRol = listaRol;
    }
    
    public List<Usuario> getListado() {
        if(listado == null){
            listado = getFacade().findAll();
        }
        return listado;
    }

    public void setListado(List<Usuario> listado) {
        this.listado = listado;
    }

    public List<Usuario> getListFilter() {
        return listFilter;
    }

    public void setListFilter(List<Usuario> listFilter) {
        this.listFilter = listFilter;
    }

    
    /********************************
     ** Métodos de inicialización ***
     ********************************/    
    
    /**
     * @return La entidad gestionada
     */
    public Usuario getSelected() {
        if (current == null) {
            current = new Usuario();
        }
        return current;
    }  
    
    /*
     * Método para inicializar el listado de los Usuarios habilitados
     * @return acción para el listado de entidades
     */
    public String prepareList() {
        iniciado = true;
        //recreateModel();
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
        listaRol = rolFacade.getActivos();
        current = new Usuario();
        return "new";
    }    
    
    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        listaRol = rolFacade.getActivos();
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
     * 
     */
    public void prepareHabilitar(){
        update = 2;
        update();        
    }
    
    /**
     * 
     */
    public void prepareDesHabilitar(){
        update = 1;
        update();        
    }    
    
    /**
     * Métodos para las validaciones
     */
    /**
     * Método para validar que no exista ya una entidad con este nombre al momento de crearla
     * @param arg0: vista jsf que llama al validador
     * @param arg1: objeto de la vista que hace el llamado
     * @param arg2: contenido del campo de texto a validar 
     */
    public void validarInsert(FacesContext arg0, UIComponent arg1, Object arg2){
        validarExistente(arg2);
    }
    
    /**
     * Método para validar que no exista una entidad con este nombre, siempre que dicho nombre no sea el que tenía originalmente
     * @param arg0: vista jsf que llama al validador
     * @param arg1: objeto de la vista que hace el llamado
     * @param arg2: contenido del campo de texto a validar 
     * @throws ValidatorException 
     */
    public void validarUpdate(FacesContext arg0, UIComponent arg1, Object arg2){
        if(!current.getNombre().equals((String)arg2)){
            validarExistente(arg2);
        }
    }
    

    /*************************
    ** Métodos de operación **
    **************************/
    /**
     * Méto que inserta uno nuevo Usuario en la base de datos, previamente genera una entidad de administración
     * con los datos necesarios y luego se la asigna a la persona
     * @return mensaje que notifica la inserción
     */
    public String create() {
        String clave = "";
        String claveEncriptada = "";
        try {
            // Creación de la entidad de administración y asignación
            Date date = new Date(System.currentTimeMillis());
            AdminEntidad admEnt = new AdminEntidad();
            admEnt.setFechaAlta(date);
            admEnt.setHabilitado(true);
            admEnt.setUsAlta(usLogeado);
            current.setAdmin(admEnt);

            // Generación de clave
            clave = CriptPass.generar();

            // la enccripto
            claveEncriptada = CriptPass.encriptar(clave);

            // verifico que no esté siendo usada por otro usuario
            while(!usuarioFacade.verificarContrasenia(claveEncriptada)){
                clave = CriptPass.generar();
                claveEncriptada = CriptPass.encriptar(clave);
            }

            // la asigno
            current.setCalve(claveEncriptada);

            // Inserción
            getFacade().create(current);

            /********************************************************************************
             * Aquí debería enviar el correo al usuario notificando el suceso, **************
             * remitiendo la nueva contraseña y el procedimiento de incicio por primera vez *
             ********************************************************************************/

            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioCreated") + " La clave asignada es: " + clave);
            recreateModel();
            return "view";

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("UsuarioCreatedErrorOccured"));
            return null;
        }
    }    
    
    /**
     * Método que actualiza un nuevo Docente en la base de datos.
     * Previamente actualiza los datos de administración
     * @return mensaje que notifica la actualización
     */
    public String update() {    
        try {
            // Actualización de datos de administración de la entidad según el valor de update
            Date date = new Date(System.currentTimeMillis());
            if(update == 1){
                current.getAdmin().setFechaBaja(date);
                current.getAdmin().setUsBaja(usLogeado);
                current.getAdmin().setHabilitado(false);
            }
            if(update == 2){
                current.getAdmin().setFechaModif(date);
                current.getAdmin().setUsModif(usLogeado);
                current.getAdmin().setHabilitado(true);
                current.getAdmin().setFechaBaja(null);
                current.getAdmin().setUsBaja(usLogeado);
            }
            if(update == 0){
                current.getAdmin().setFechaModif(date);
                current.getAdmin().setUsModif(usLogeado);
            }

            // Actualizo
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioUpdated"));
            recreateModel();
            return "view";
                
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("UsuarioUpdatedErrorOccured"));
            return null;
        }
    }    
    
    /**
     * @param id equivalente al id de la entidad persistida
     * @return la entidad correspondiente
     */
    public Usuario getUsuario(java.lang.Long id) {
        return getFacade().find(id);
    }    
   
    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private UsuarioFacade getFacade() {
        return usuarioFacade;
    }    
    
    /**
     * Restea la entidad
     */
    private void recreateModel() {
        listado.clear();
        listado = null;
    }      
    
    private void validarExistente(Object arg2) throws ValidatorException{
        if(!getFacade().noExiste((String)arg2)){
            throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("CreateTipoCapNombreExistente")));
        }
    }
    
    
    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = Usuario.class)
    public static class UsuarioControllerConverter implements Converter {

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
            MbUsuario controller = (MbUsuario) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbUsuario");
            return controller.getUsuario(getKey(value));
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
            if (object instanceof Usuario) {
                Usuario o = (Usuario) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Usuario.class.getName());
            }
        }
    }               
}

