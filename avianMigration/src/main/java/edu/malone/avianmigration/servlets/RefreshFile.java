/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.avianmigration.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.malone.avianmigration.sql.FileManipulator;
import static edu.malone.avianmigration.utilities.ServletFunctions.getUsername;
import static edu.malone.avianmigration.utilities.ServletFunctions.isGuest;
import static edu.malone.avianmigration.utilities.ServletFunctions.setUsername;

/**
 *
 * @author Cory Edwards
 */
@WebServlet("/refresh_file")
public class RefreshFile extends HttpServlet
{   
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException
    {
        String user = getUsername(request.getCookies());
        setUsername(response, user);
        
        if(isGuest(user))
        {
            if(request.getParameter("id") != null)
            {
                String fileId = request.getParameter("id");
                FileManipulator.getInstance().refreshFile(fileId, user);
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
