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
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author lmmuriuki1
 */
@WebServlet(name = "GetOrderedYears", urlPatterns = {"/GetOrderedYears"})
public class GetOrderedYears extends HttpServlet {

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
            //System.out.println("Here!!");
            Table rows = null;
            if(request.getParameter("hist_clicked") == null)
            {
                rows = Access.getInstance().getTable("SELECT DISTINCT YEAR([Date and time recorded]) AS DATE FROM [NSFCourter2016].[dbo].[MAIN_VIEW] ORDER BY DATE", new Object[]{}, "project");
            }     

            else 
            {
                rows = Access.getInstance().getTable("SELECT DISTINCT [Reference Year] AS DATE FROM [NSFCourter2016].[dbo].[HISTORICAL_VIEW] ORDER BY DATE", new Object[]{}, "project");
            }
                
            //Creat the returning json object.
            JSONObject returnObject = new JSONObject();

            //Set up the json array that will hold the variable names.
            JSONArray array = new JSONArray();

            //rowNames.next();
            while(rows.next())
            {
                array.put(rows.getInt("DATE"));
            }    
            
            //System.out.println("Date array: " + array.toString());
            //Insert the array into the returning object.
            returnObject.put("years", array);
            //System.out.println(returnObject.toString());

            //Write the object to the printstream.
            out.write(returnObject.toString());

            //Flush the stream to reset.
            out.flush();

            //Close the table object.
            rows.close();
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
