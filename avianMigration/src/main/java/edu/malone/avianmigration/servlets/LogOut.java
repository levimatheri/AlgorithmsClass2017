/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.avianmigration.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.malone.avianmigration.utilities.ServletFunctions;

/**
 *
 * @author cjedwards1
 */
@WebServlet("/log_out")
public class LogOut extends HttpServlet
{   
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException
    {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) 
        {
            for (Cookie cookie : cookies)
            {
                if (cookie.getName().equals("object") || cookie.getName().equals("user")) 
                {
                    cookie.setValue(null);
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
            if(!ServletFunctions.properties.getBoolean("testing", Boolean.FALSE))
            {
                String url = ((HttpServletRequest)request).getRequestURL().toString();
                response.sendRedirect("https://10.40.40.100/Shibboleth.sso/Logout?return=" + url.substring(0, url.length() - 8));
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
