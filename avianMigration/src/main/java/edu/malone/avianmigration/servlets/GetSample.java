/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.avianmigration.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.malone.avianmigration.sql.Access;
import edu.malone.avianmigration.sql.Table;
import static edu.malone.avianmigration.utilities.ServletFunctions.buildQuery;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author lmmuriuki1
 */
@WebServlet(name = "GetSample", urlPatterns = {"/GetSample"})
public class GetSample extends HttpServlet {

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
            throws ServletException, IOException, JSONException, Exception {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            HashMap<String, String> parameterPairs = new HashMap();
            JSONObject returnObject = new JSONObject();

            //Get all of the parameters that came to the server.
            Enumeration<String> parameters = request.getParameterNames();
            String parameter;

            //Go through each one.
            while(parameters.hasMoreElements())
            {
                parameter = parameters.nextElement();
                parameterPairs.put(parameter, request.getParameter(parameter));
            }

            String query = buildQuery(request.getParameter("ckbx"), true, parameterPairs);
            //System.out.println("Query: " + query);
        

            Table results = Access.getInstance().getTable(query, new Object[]{}, "project");
            JSONArray order = new JSONArray();

            for(int i = 0; i < results.getColumnCount() + 1; i++)
            {
                results.setRow(0);
                JSONArray array = new JSONArray();

                //On the first run, add the number to show how many 
                //records came back up to 100.
                if(i == 0)
                {
                    int x = 1;
                    while(results.next())
                    {
                        array.put(results.getRow(), x);
                        x++;
                    }
                    order.put(i, "#");
                    returnObject.put("#", array);
                }
                else
                {
                    while(results.next())
                    {
                        array.put(results.getRow(), results.getObject(i));
                    }

                    order.put(i,results.getColumnName(i));
                    returnObject.put(results.getColumnName(i), array);
                }
            }

            returnObject.put("order", order);
            out.write(returnObject.toString());
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
            Logger.getLogger(GetSample.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(GetSample.class.getName()).log(Level.SEVERE, null, ex);
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
