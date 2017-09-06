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
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author lmmuriuki1
 */
@WebServlet(name = "GetVariableNames", urlPatterns = {"/GetVariableNames"})
public class GetVariableNames extends HttpServlet {

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
             //Get simply the first row of the view.
                Table columnNames = Access.getInstance().getTable("SELECT TOP(1) * FROM NSFCourter2016.dbo.MAIN_VIEW", new Object[]{}, "project");
                
                //Creat the returning json object.
                JSONObject returnObject = new JSONObject();
                
                //Set up the json array that will hold the variable names.
                JSONArray array = new JSONArray();
                
                columnNames.next();
                for(int i = 1; i < columnNames.getColumnCount() + 1; i++)
                {
                    array.put(columnNames.getColumnName(i));
                }
                
                //Insert the array into the returning object.
                returnObject.put("names", array);
                System.out.println(returnObject.toString());
                
                //Write the object to the printstream.
                out.write(returnObject.toString());
                
                //Flush the stream to reset.
                out.flush();
                
                //Close the table object.
                columnNames.close();
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
            Logger.getLogger(GetVariableNames.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(GetVariableNames.class.getName()).log(Level.SEVERE, null, ex);
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
