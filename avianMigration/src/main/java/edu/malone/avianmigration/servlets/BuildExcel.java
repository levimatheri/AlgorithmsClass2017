/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.avianmigration.servlets;

import java.io.IOException;
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
import edu.malone.avianmigration.sql.FileManipulator;
import edu.malone.avianmigration.sql.Table;
import static edu.malone.avianmigration.utilities.ServletFunctions.*;

/**
 *
 * @author Cory Edwards
 */
@WebServlet("/application")
public class BuildExcel extends HttpServlet
{   
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException
    {
        String user = getUsername(request.getCookies());
        setUsername(response, user);
        
        if(isGuest(user))
        {
            try 
            {
                HashMap<String, String> parameterPairs = new HashMap();
                
                //Get all of the parameters that came to the server.
                Enumeration<String> parameters = request.getParameterNames();
                String parameter;
                
                //Go through each one.
                while(parameters.hasMoreElements())
                {
                    parameter = parameters.nextElement();
                    parameterPairs.put(parameter, request.getParameter(parameter));
                }
                
                String query = buildQuery(request.getParameter("ckbx"), false, parameterPairs);
                Table results = Access.getInstance().getTable(query, new Object[]{}, "project");
                FileManipulator.getInstance().uploadFile("xlsx", results, user);
            } 
            catch (Exception ex)
            {
                Logger.getLogger(BuildExcel.class.getName()).log(Level.SEVERE, "Failed to build excel document", ex);
            }
        }
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException
    {
        doGet(request, response);
    }
}
