/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.avianmigration.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.malone.avianmigration.sql.Access;
import edu.malone.avianmigration.sql.FileManipulator;
import edu.malone.avianmigration.utilities.ServletFunctions;

/**
 *
 * @author Cory Edwards
 */
@WebServlet("/sign_up")
public class SignUp extends HttpServlet
{   
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException
    {
        if(request.getParameter("email") != null && request.getParameter("username") != null && request.getParameter("description") != null)
        {
            try
            {
                String tableName = ServletFunctions.properties.getString("name.database");
                int id = Access.getInstance().execute("INSERT INTO NSFServiceProvider.dbo.USERS (USER_NAME, EMAIL) VALUES (?, ?)", new Object[]{request.getParameter("username"), request.getParameter("email")}, "sp");
                Access.getInstance().execute("INSERT INTO NSFServiceProvider.dbo." + tableName + " (USER_ID, VALIDATED, DESCRIPTION, SCOPE_ID) VALUES (?, CAST(1 AS BIT), ?, 1)", new Object[]{id, request.getParameter("description")}, "sp");
            }
            catch(SQLException ex)
            {
                Logger.getLogger(SignUp.class.getName()).log(Level.SEVERE, null, ex);
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
