/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.servicios.gestionterritorial.entidades;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entidad que encapsula los datos de los usuarios
 * Se vincula con:
 *      Agente,
 *      Docente,
 *      Rol,
 *      AdmEntidad
 * @author rincostante
 */
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne /*(fetch=FetchType.LAZY)*/
    @JoinColumn(name="rol_id")
    private Rol rol;
    
    /**
     * Campo de texto que indica el nombre del usuario
     */        
    @Column (nullable=false, length=20, unique=true)
    @NotNull(message = "{entidades.fieldNotNullError}")
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 20)
    private String nombre;     
    
    /**
     * Campo de texto que indica la clave de acceso del usuario
     */        
    @Column (nullable=false, length=50, unique=true)
    @NotNull(message = "{entidades.fieldNotNullError}")
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 50)
    private String calve; 
    
    /**
     * Campo de tipo AdmEntidad que encapsula los datos propios para su trazabilidad.
     */
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @NotNull(message = "{enitdades.objectNotNullError}") 
    private AdminEntidad admin;
    
    /**
     * Este campo solo está en true cuando el usuario es registrado o
     * su contraseña blanqueada. El sistema validará este campo en cada 
     * inicio de sesión, en caso de ser verdadero redirecciona al cambio de contraseña,
     * la que una vez actualizada permitirá al usuario reiniciar sesión y operar normalemente el sistema.
     */
    @Column(nullable=false)
    @NotNull(message = "El campo 'primeraVez' no puede quedar vacío")
    private boolean primeraVez = true;    
    
    /**
     * Campo que muestra los Apellidos y nombres personales del docente
     */
    @Transient
    String apYNom;
    
    /**
     * Campo que muestra el número de documento personal del docente
     */
    @Transient
    String documento;    

    public boolean isPrimeraVez() {
        return primeraVez;
    }

    public void setPrimeraVez(boolean primeraVez) {
        this.primeraVez = primeraVez;
    }

    /**
     *
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     *
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getApYNom() {
        return apYNom;
    }

    public void setApYNom(String apYNom) {
        this.apYNom = apYNom;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /**
     *
     * @return
     */
    public AdminEntidad getAdmin() {
        return admin;
    }

    /**
     *
     * @param admin
     */
    public void setAdmin(AdminEntidad admin) {
        this.admin = admin;
    }

    /**
     *
     * @return
     */
    public String getCalve() {
        return calve;
    }

    /**
     *
     * @param calve
     */
    public void setCalve(String calve) {
        this.calve = calve;
    }

    /**
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     *
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "ar.gob.ambiente.servicios.gestionterritorial.entidades.Usuario[ id=" + id + " ]";
    }

    public Object getAdminentidad() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
