/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.avianmigration.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.malone.avianmigration.sql.Access;
import edu.malone.avianmigration.sql.Table;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 *
 * @author lmmuriuki1
 */
@WebServlet(name = "GetBirdData", urlPatterns = {"/GetBirdData"})
public class GetBirdData extends HttpServlet {

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
            throws ServletException, IOException, Exception {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            JSONObject birdNameType = new JSONObject();
                        
            JSONArray birdNameArray = new JSONArray();
            switch(request.getParameter("id"))
            {
                case "sci_radio":
                    Table sci_names = Access.getInstance().getTable("SELECT [Scientific Name] FROM [NSFCourter2016].[dbo].[BIRD_VIEW]", new Object[]{}, "project");

                    while(sci_names.next())
                    {
                        birdNameArray.put(sci_names.getString("Scientific Name"));
                    }                  

                    birdNameType.put("sci_names", birdNameArray);

                    sci_names.close();

                break;

                case "comm_radio":
                    Table comm_names = Access.getInstance().getTable("SELECT [Common Name] FROM [NSFCourter2016].[dbo].[BIRD_VIEW]", new Object[]{}, "project");

                    while(comm_names.next())
                    {
                        birdNameArray.put(comm_names.getString("Common Name"));
                    }

                    birdNameType.put("comm_names", birdNameArray);

                    comm_names.close();

                break;

                case "tax_radio":
                    Table taxonomy_names = Access.getInstance().getTable("SELECT [Taxonomy #] FROM [NSFCourter2016].[dbo].[BIRD_VIEW]", new Object[]{}, "project");

                    while(taxonomy_names.next())
                    {
                        birdNameArray.put(taxonomy_names.getString("Taxonomy #"));
                    }

                    birdNameType.put("taxonomy_names", birdNameArray);

                    taxonomy_names.close();

                break;  
            }
            
            out.write(birdNameType.toString());
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(GetOrderedYears.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(GetOrderedYears.class.getName()).log(Level.SEVERE, null, ex);
        }
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
