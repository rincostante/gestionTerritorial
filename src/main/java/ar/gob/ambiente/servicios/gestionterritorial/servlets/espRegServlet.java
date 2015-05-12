/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.servicios.gestionterritorial.servlets;

import ar.gob.ambiente.servicios.gestionterritorial.entidades.CentroPoblado;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.CentroPobladoTipo;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Departamento;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.EspecificidadDeRegion;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Municipio;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Provincia;
import ar.gob.ambiente.servicios.gestionterritorial.entidades.Region;
import ar.gob.ambiente.servicios.gestionterritorial.facades.CentroPobladoFacade;
import ar.gob.ambiente.servicios.gestionterritorial.facades.EspecificidadDeRegionFacade;
import ar.gob.ambiente.servicios.gestionterritorial.servicios.CentrosPobServicio;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author lagarcia
 */
public class espRegServlet extends HttpServlet {

    @EJB
    private CentrosPobServicio centroPobServicio;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            /*
            // Centros por parámetro
            Long idRegion = Long.valueOf(6);
            Long idProv = Long.valueOf(1);
            Long idDepto = Long.valueOf(1);
            Long idTipo = Long.valueOf(1);
            
            //List<CentroPoblado> centros = centroPobServicio.getCentrosPorDeptoYTipo(idDepto, idTipo);
            //List<CentroPoblado> centros = centroPobServicio.getCentrosPorProvYTipo(idProv, idTipo);
            List<CentroPoblado> centros = centroPobServicio.getCentrosPorRegionYTipo(idRegion, idTipo);
            
            System.out.println("El primer centro es: " + centros.get(0).getNombre());
            
            
            //Por Provincia
            Long idProvincia = Long.valueOf(1);
            
            //List<Departamento> provincias = centroPobServicio.getDeptosPorProvincia(idProvincia);
            //List<Municipio> municipios = centroPobServicio.getMunicipiosPorProvincia(idProvincia);
            List<Region> regiones = centroPobServicio.getRegionesPorProvincia(idProvincia);
            
            System.out.println("El primer depto es: " + regiones.get(0).getNombre());
            
            // Regiones por especificidad
            Long idEspReg = Long.valueOf(4);
            List<Region> regiones = centroPobServicio.getRegionesPorEspecif(idEspReg);
            System.out.println("La primera región es: " + regiones.get(0).getNombre());
            
            // todas las provincias
            List<Provincia> provincias = centroPobServicio.getProvincias();
            System.out.println("La primera provincia es: " + provincias.get(0).getNombre());
            
            // todos los tipos de centros poblados
            List<CentroPobladoTipo> tiposDeCentros = centroPobServicio.getTiposCentros();
            System.out.println("El primer tipo de centro es: " + tiposDeCentros.get(0).getNombre());
            */
            
            // todas las especificidades de región
            List<EspecificidadDeRegion> especificidades = centroPobServicio.getEspecifRegion();
            System.out.println("La primera especificidad es: " + especificidades.get(0).getNombre());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
