/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.avianmigration.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.malone.avianmigration.sql.FileManipulator;
import static edu.malone.avianmigration.utilities.ServletFunctions.*;
import org.json.JSONArray;

/**
 *
 * @author Cory Edwards
 */
@WebServlet("/download_file")
public class DownloadFile extends HttpServlet
{   
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException
    {
        String user = getUsername(request.getCookies());
        setUsername(response, user);
        
        if(isGuest(user))
        {
            if(request.getParameter("name") != null && request.getParameter("id") != null && request.getParameter("type") != null)
            {
                try(OutputStream os = response.getOutputStream())
                {
                    String fileName = request.getParameter("name");
                    String fileType = request.getParameter("type");
                    String fileId = request.getParameter("id");
                    
                    JSONArray files = FileManipulator.getInstance().getFiles(user);
                    boolean contains = false;
                    for(int i = 0; i < files.length(); i++)
                    {
                        if(files.getJSONObject(i).getString("id").equals(fileId))
                        {
                            contains = true;
                            break;
                        }
                    }

                    if(contains)
                    {
                        //This must be set along with the filename. The filename determines the actual name of the file and nothing else can.
                        response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "." + fileType +"\"");

                        FileManipulator.getInstance().downloadFile(fileId, fileType, os);

                        os.flush();
                    }
                }
                catch(Exception ex)
                {
                    Logger.getLogger(DownloadFile.class.getName()).log(Level.SEVERE, "Failed to download file.", ex);
                }
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
