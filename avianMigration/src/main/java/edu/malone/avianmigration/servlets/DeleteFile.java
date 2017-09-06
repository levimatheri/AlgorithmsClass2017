/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.avianmigration.servlets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.malone.avianmigration.sql.FileManipulator;
import static edu.malone.avianmigration.utilities.ServletFunctions.*;


/**
 *
 * @author Cory Edwards
 */
@WebServlet("/delete_file")
public class DeleteFile extends HttpServlet
{   
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException
    {
        String user = getUsername(request.getCookies());
        setUsername(response, user);
        
        if(isGuest(user))
        {
            if(request.getParameter("id") != null && request.getParameter("type") != null)
            {
                String fileId = request.getParameter("id");
                String fileType = request.getParameter("type");
                Logger.getLogger(DeleteFile.class.getName()).log(Level.INFO, "{0} has {1}been deleted.", 
                        new Object[]{fileId, FileManipulator.getInstance().deleteFile(fileId, fileType, user) ? "" : "not "});
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
