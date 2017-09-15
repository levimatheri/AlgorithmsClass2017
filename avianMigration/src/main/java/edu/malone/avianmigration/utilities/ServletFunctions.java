/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.avianmigration.utilities;

import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import edu.malone.avianmigration.servlets.GetSample;
import edu.malone.avianmigration.sql.Access;
import edu.malone.avianmigration.sql.Table;
import org.apache.commons.configuration2.Configuration;
import org.json.JSONException;

/**
 *
 * @author lmmuriuki1
 */
public class ServletFunctions {
    public static Configuration properties;
    
    /**
     * Get the username from a cookie that is in an array of cookies
     * @param cookies The array of cookies that has the user cookie in it.
     * @return The username.
     */
    public static String getUsername(Cookie[] cookies)
    {
        CheckCookie cookie = null;
        if(cookies != null)
        {
            for(Cookie tempCookie : cookies)
            {
                if(tempCookie.getName().equals("object"))
                {
                    if(!tempCookie.getValue().equals(""))
                    {
                        cookie = new CheckCookie(tempCookie);
                        try
                        {
                            System.out.println(cookie.printCookie());
                        }
                        catch(JSONException ex)
                        {
                            Logger.getLogger(GetSample.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                }
            }
        }
        
        if(cookie == null)
        {
            return "guest";
        }
        else if(cookie.hasCookieExpired())
        {
            return "guest";
        }
        else if(!cookie.verifyCookie())
        {
            return "guest";
        }
        else
        {
            try
            {
                return cookie.getValue("username");
            }
            catch(JSONException ex)
            {
                Logger.getLogger(GetSample.class.getName()).log(Level.SEVERE, null, ex);
                return "guest";
            }
        }
    }
    
    /**
     * Set or refresh the username in a cookie
     * @param response The response that will have the cookie
     * @param user The username
     */
    public static void setUsername(HttpServletResponse response, String user)
    {
        Cookie tempCookie = new Cookie("user", user);
        tempCookie.setMaxAge(new CheckCookie(null).convertToSeconds());
        response.addCookie(tempCookie);
    }
    
    /**
     * Test if a username labels a user as a guest or a full user. MAY NEED TO LOOK AT SQL
     * TO CHECK FOR VALIDITY.
     * @param user The username
     * @return True iff the username is a valid user on this site, false otherwise.
     */
    public static boolean isGuest(String user)
    {
        return user != null && !user.equals("") && !user.equals("guest");
    }
    
    public static String buildQuery(String variables, boolean top, HashMap<String, String> parameterPairs) throws Exception
    {
        /**
        * Depending on what tab was chosen by the user, the 
        * corresponding String will be filled correctly. Later,
        * it will check to see which String is empty and which one
        * isn't to determine which view to get the data from and 
        * how it will do so.
        */
        String main;
        String bird = "";
        

        String view;
        String timeString;
        
        String variable = "";
        
        if(parameterPairs.get("select").equals("gen"))
        {
            view = "MAIN_VIEW";
            timeString = "[Date and time recorded]";
        }
        //if historical radio is clicked, use [FAD] instead
        else
        {
            view = "HISTORICAL_VIEW";
            timeString = "[First Arrival Date]";
        }
        
        
        if(variables != null && !variables.equals(""))
            main = "SELECT  "  + variables + " FROM NSFCourter2016.dbo." + view;
        else
            main = "SELECT * FROM NSFCourter2016.dbo." + view;

        //Will hold the WHERE part of the query.
        StringBuilder query = new StringBuilder("");

        //Get all of the parameters that came to the server.
        Set<String> parameters = parameterPairs.keySet();

        //Go through each one.
        for(String parameter : parameters.toArray(new String[0]))
        {
            //Get the parameter's data.
            String inputOption = parameterPairs.get(parameter);
            String[] options;
          
            Logger.getLogger(ServletFunctions.class.getName()).log(Level.FINE, inputOption);

            //Check which parameter is chosen.
            switch(parameter)
            {
                //climate divisions
                case "cd":
                    //If not already set, set the correct starting String. 
                    options = inputOption.split(",");

                    if(query.toString().contains("WHERE"))
                        query.append(" AND (");
                    else
                        query.append(" WHERE ");


                    for(String option : options)
                    {
                        String[] secondOptions = option.split("/");

                        if(query.toString().contains("[STATE]")){
                            query.deleteCharAt(query.length() - 1);
                            query.append(" OR [STATE]= '").append(secondOptions[0]).append("'").append(" AND [Climate division name]= '").append(secondOptions[1]).append("')");
                        }
                        else 
                            query.append("([STATE]= '").append(secondOptions[0]).append("'").append(" AND [Climate division name]= '").append(secondOptions[1]).append("')");
                    }
                    break;

                //lat long ranges
                case "ll":
                    if(main.isEmpty())
                             main = "SELECT * FROM NSFCourter2016.DBO." + view + " ";

                    options = inputOption.split(",");

                    if(query.toString().contains("WHERE"))
                        query.append(" AND (");
                    else
                        query.append(" WHERE (");

                    //Multiple lat long ranges can come in so parse through and add each one.
                    for(String option : options)
                    {
                        String[] secondOptions = option.split("/");

                        if(query.toString().contains("[Latitude] BETWEEN"))
                            query.append(" OR ([Latitude] BETWEEN ").append(secondOptions[0]).append(" AND ").append(secondOptions[1]).append(" AND [Longitude] BETWEEN ").append(secondOptions[2]).append(" AND ").append(secondOptions[3]).append(")");
                        else 
                            query.append("([Latitude] BETWEEN ").append(secondOptions[0]).append(" AND ").append(secondOptions[1]).append(" AND [Longitude] BETWEEN ").append(secondOptions[2]).append(" AND ").append(secondOptions[3]).append(")");
                    }
                    query.append(")");
                    break;

                //States
                case "st":
                    if(main.isEmpty())
                             main = "SELECT * FROM NSFCourter2016.DBO." + view + " ";

                    options = inputOption.split(",");

                    if(query.toString().contains("WHERE"))
                        query.append(" AND [State] IN (");
                    else
                        query.append(" WHERE [State] IN (");

                    query.append("'").append(options[0]).append("'");

                    for(int i = 1; i < options.length; i++)
                    {
                        //Since states are VARCHARs int the databse, they must begin and end with a '.
                        query.append(",'").append(options[i]).append("'");
                    }

                    query.append(")");
                    break;

                //Year range
                case "yr":
                    if(main.isEmpty())
                             main = "SELECT * FROM NSFCourter2016.DBO." + view + " ";

                    options = inputOption.split(",");

                    for(String option : options)
                    {
                        if(query.toString().contains("WHERE"))
                            query.append(" AND DATEPART(YEAR, ").append(timeString).append(") BETWEEN ").append(option.split("/")[0]).append(" AND ").append(option.split("/")[1]);
                        else
                            query.append(" WHERE DATEPART(YEAR, ").append(timeString).append(") BETWEEN ").append(option.split("/")[0]).append(" AND ").append(option.split("/")[1]);
                    }
                    //System.out.println(query.toString());
                    break;

                //Month range
                case "mh":
                    if(main.isEmpty())
                             main = "SELECT * FROM NSFCourter2016.DBO." + view + " ";

                    options = inputOption.split(",");

                    for(String option : options)
                    {
                        if(query.toString().contains("WHERE"))
                            query.append(" AND DATEPART(MONTH, [Date and time recorded]) BETWEEN ").append(option.split("/")[0]).append(" AND ").append(option.split("/")[1]);
                        else
                            query.append(" WHERE DATEPART(MONTH, [Date and time recorded]) BETWEEN ").append(option.split("/")[0]).append(" AND ").append(option.split("/")[1]);
                    }
                    break;

                //Day range
                case "dy":
                    if(main.isEmpty())
                             main = "SELECT * FROM NSFCourter2016.DBO." + view + " ";

                    options = inputOption.split(",");

                    for(String option : options)
                    {
                        //grab the date of the month using the day of the week
                        if(query.toString().contains("WHERE"))
                            query.append(" AND DATEPART(DW, ").append(timeString).append(") BETWEEN ").append(option.split("/")[0]).append(" AND ").append(option.split("/")[1]);
                        else
                            query.append(" WHERE DATEPART(DW, ").append(timeString).append(") BETWEEN ").append(option.split("/")[0]).append(" AND ").append(option.split("/")[1]);
                    }
                    break;

                //Date range
                case "dt":
                    if(main.isEmpty())
                             main = "SELECT * FROM NSFCourter2016.DBO." + view + " ";

                    options = inputOption.split(",");

                    for(String option : options)
                    {
                        if(query.toString().contains("WHERE"))
                        {
                            //CONVERT(VARCHAR(8), timeString, 112) converts [Date and time recorded] which is a datetime variable to the format --> YYYYMMDD
                            query.append(" AND CONVERT(VARCHAR(8), ").append(timeString).append(", 112) BETWEEN ").append(option.split("/")[0].replaceAll("-", "")).append(" AND ").append(option.split("/")[1].replaceAll("-", ""));
                        }
                        else
                            query.append(" WHERE CONVERT(VARCHAR(8), ").append(timeString).append(", 112) BETWEEN ").append(option.split("/")[0].replaceAll("-", "")).append(" AND ").append(option.split("/")[1].replaceAll("-", ""));
                    }

                    //query.append(" ORDER BY ").append(timeString); //chronological order
                    break;

                //Am or PM
                case "ap":
                    if(main.isEmpty())
                             main = "SELECT * FROM NSFCourter2016.DBO." + view + " ";

                    if(query.toString().contains("WHERE"))
                        query.append(" AND FORMAT(").append(timeString).append(", 'tt')='").append(inputOption).append("'");
                    else
                        query.append(" WHERE FORMAT(").append(timeString).append(", 'tt')='").append(inputOption).append("'");
                    break;

                //Bird taxonomies
                case "ty":
                    if(bird.isEmpty())
                        bird = "SELECT * FROM NSFCourter2016.DBO.BIRD_VIEW ";

                    if(query.toString().contains("WHERE"))
                            query.append(" AND [Taxonomy #] IN (").append(inputOption).append(")");
                        else
                            query.append("WHERE [Taxonomy #] IN (").append(inputOption).append(")");
                    break;

                //Bird names
                case "bn":
                    if(bird.isEmpty())
                        bird = "SELECT * FROM NSFCourter2016.DBO.BIRD_VIEW ";

                    options = inputOption.split("/");
                    String type = options[0];
                    String[] birds = options[1].split(",");

                    for(int i = 0; i < birds.length; i++)
                        birds[i] = birds[i].replaceAll("'", "''");

                    //For birdNameType names
                    switch(type)
                    {
                        //For common names
                        case "s":
                            if(query.toString().contains("WHERE"))
                                query.append(" AND [Scientific Name] IN (");
                            else
                                query.append(" WHERE [Scientific Name] IN (");
                            query.append("'").append(birds[0]).append("'");
                            for(int i = 1; i < birds.length; i++)
                            {
                                query.append(",'").append(birds[i]).append("'");
                            }
                            query.append(")");
                            break;
                        //For family names
                        case "p":
                            if(query.toString().contains("WHERE"))
                                query.append(" AND [Common Name] IN (");
                            else
                                query.append(" WHERE [Common Name] IN (");
                            query.append("'").append(birds[0]).append("'");
                            for(int i = 1; i < birds.length; i++)
                            {
                                query.append(",'").append(birds[i]).append("'");
                            }
                            query.append(")");
                            break;
                        //For order names
                        case "f":
                            if(query.toString().contains("WHERE"))
                                query.append(" AND [Family Name] IN (");
                            else
                                query.append(" WHERE [Family Name] IN (");
                            query.append("'").append(birds[0]).append("'");
                            for(int i = 1; i < birds.length; i++)
                            {
                                query.append(",'").append(birds[i]).append("'");
                            }
                            query.append(")");
                            break;
                        case "o":
                            if(query.toString().contains("WHERE"))
                                query.append(" AND [Order Name] IN (");
                            else
                                query.append(" WHERE [Order Name] IN (");
                            query.append("'").append(birds[0]).append("'");
                            for(int i = 1; i < birds.length; i++)
                            {
                                query.append(",'").append(birds[i]).append("'");
                            }
                            query.append(")");
                            break;
                        default:
                            break;
                    }
                    break;

                //Long or short migration
                case "ls":
                    if(bird.isEmpty())
                        bird = "SELECT * FROM NSFCourter2016.DBO.BIRD_VIEW ";

                    if(query.toString().contains("WHERE"))
                        query.append(" AND [Long or Short Migration]='").append(inputOption).append("'");
                    else
                        query.append(" WHERE [Long or Short Migration]='").append(inputOption).append("'");
                    break;

                //Feeding guilds
                case "fg":
                    if(bird.isEmpty())
                        bird = "SELECT * FROM NSFCourter2016.DBO.BIRD_VIEW ";

                    options = inputOption.split(",");

                    if(query.toString().contains("WHERE"))
                        query.append(" AND [Feeding Guild] IN (");
                    else
                        query.append(" WHERE [Feeding Guild] IN (");

                    query.append("'").append(options[0]).append("'");

                    for(int i = 1; i < options.length; i++)
                    {
                        query.append(",'").append(options[i]).append("'");
                    }
                    query.append(")");
                    break;

                //Habitat in the east, west, or both
                case "ew":
                    if(bird.isEmpty())
                        bird = "SELECT * FROM NSFCourter2016.DBO.BIRD_VIEW ";

                    if(query.toString().contains("WHERE"))
                        query.append(" AND [Habbitat in East, West, or Both]='").append(inputOption).append("'");
                    else
                        query.append(" WHERE [Habbitat in East, West, or Both]='").append(inputOption).append("'");
                    break;

                //Observer names
                case "on":
                    if(main.isEmpty())
                        main = "SELECT * FROM NSFCourter2016.DBO." + view + " ";

                    options = inputOption.split(",");

                    for(int i = 0; i < options.length; i++)
                        options[i] = options[i].replaceAll("'", "''");

                    if(query.toString().contains("WHERE"))
                        query.append(" AND [Observer Name] IN (");
                    else
                        query.append(" WHERE [Observer Name] IN (");

                    query.append("'").append(options[0]).append("'");

                    for(int i = 1; i < options.length; i++)
                    {
                        query.append(",'").append(options[i]).append("'");
                    }
                    query.append(")");
                    break;

                //Observer ids
                case "od":
                    if(main.isEmpty())
                             main = "SELECT * FROM NSFCourter2016.DBO." + view + " ";

                    if(query.toString().contains("WHERE"))
                        query.append(" AND [Observer ID] IN (").append(inputOption).append(")");
                    else
                        query.append(" WHERE [Observer ID] IN (").append(inputOption).append(")");
                    break;
            }
        }
        
        if(variables != null)
        {
//            if(variables.contains("Date"))
//                query.append(" ORDER BY [Date and time recorded]");
        }
                
        
        if(top)
        {
            main = main.replaceAll("SELECT ", ("SELECT DISTINCT TOP(100)"));
            bird = bird.replaceAll("SELECT ", ("SELECT DISTINCT TOP(100)"));
        }
        
        for(String parameter : parameters.toArray(new String[0]))
        {
            //Get the parameter's data.
            String inputOption = parameterPairs.get(parameter);
            String[] options;
          
            Logger.getLogger(ServletFunctions.class.getName()).log(Level.FINE, inputOption);
            
            
            switch(parameter)
            {
                case "ct":
                    options = inputOption.split("/");
                    
                    String compare = "";
                    switch(options[0])
                    {
                        case "0":
                            compare = ">=";
                            break;
                            
                        case "1":
                            compare = ">";
                            break;
                            
                        case "2":
                            compare = "<=";
                            break;
                            
                        case "3":
                            compare = "<";
                            break;
                            
                        case "4":
                            compare = "=";
                            break;
                    }
                    //System.out.println("Query: " + query.toString());
                    
                    Table tempTable = Access.getInstance().getTable("WITH MAIN AS (SELECT * FROM NSFCourter2016.DBO.MAIN_VIEW" + query.toString() + ") " +
                                        "SELECT * FROM (SELECT [Observer ID], COUNT([Observer ID]) AS [Number of checlists]"
                                        + " FROM MAIN GROUP BY [Observer ID]) AS M WHERE [Number of checlists]" + compare + options[1] + " ORDER BY  [Observer ID] ASC", new Object[]{}, "project");
                    
                    String observers = "";
                    
                    while(tempTable.next())
                    {
                        if(observers.isEmpty())
                            observers += tempTable.getInt("Observer ID");
                        else
                            observers += "," + tempTable.getInt("Observer ID");
                    }
                    
                    if(!observers.isEmpty())
                    {
                        if(main.isEmpty())
                                main = "SELECT" + top + " * FROM NSFCourter2016.DBO." + view + " ";

                       if(query.toString().contains("WHERE"))
                           query.append(" AND [Observer ID] IN (").append(observers).append(")");
                       else
                           query.append("WHERE [Observer ID] IN (").append(observers).append(")");
                    }
                    break;
                //If the # of submitted checklists option was checkd for calculations.
                case "cs":
                    options = inputOption.split("/");

                    String operation = null;
                    switch(options[0])
                    {
                        case "ge":
                            operation = ">=";
                            break;
                        case "g":
                            operation = ">";
                            break;
                        case "le":
                            operation = "<=";
                            break;
                        case "l":
                            operation = "<";
                            break;
                        case "e":
                            operation = "IN";                           
                            break;
                    }

                    //Include group checklists.
//                    if(options[0].equals("yes"))
//                    {
//                        results = access.executeProcedure("NSFCourter2016.dbo.GET_OBSERVERS_WITH_CHECKLISTS_W_GROUPS", new String[]{query.toString(), operation + options[2], top});
//                    }
//                    else
//                    {
//                        results = access.executeProcedure("NSFCourter2016.dbo.GET_OBSERVERS_WITH_CHECKLISTS_WO_GROUPS", new String[]{query.toString(), operation + options[2], top});
//                    }

                    Table results = null;
                    
                    if(variables == null)
                        variables = "[Observer ID]";
                    
                    //This is whithout groups.
                    results = Access.getInstance().getTable("WITH MAIN AS (SELECT * FROM NSFCourter2016.DBO.MAIN_VIEW " + query.toString() + ")"
                        + "SELECT " + top + " N.*, M.[Number of checklists] FROM  "
                            + "(SELECT  * FROM "
                                + "(SELECT [Observer ID], COUNT([Observer ID]) AS [Number of checklists] FROM MAIN GROUP BY [Observer ID]) AS TEMP"
                            + " WHERE [Number of checklists] " + operation + " (" + options[1] + ")) AS M, "
                            + "(SELECT  " + variables + " FROM MAIN) AS N "
                        + "WHERE N.[OBSERVER ID] = M.[OBSERVER ID]", new Object[]{}, "project");
                    break;
                //Count number of a variable
                case "cl":
                    variable = inputOption;

                    if(variables != null)
                    {
                        //variables = request.getParameter("ckbx");
                        
                        if(!variables.equals(""))
                            variables += ",";
                    }
                    else
                        variables = "";
                    
                    if(variables.contains(",[" + variable + "]"))
                        variables = variables.replace(",[" + variable + "]", "");
                    
                    else if(variables.contains("[" + variable + "],"))
                        variables = variables.replace("[" + variable + "],", "");
                    
                    //Check to see if this is ok or not. --- works fine, just added distinct
                    results = Access.getInstance().getTable("SELECT DISTINCT" + top + " N.*, M.[Count of varaible] FROM "
                        + "(SELECT [" + variable + "], COUNT([" + variable + "]) AS [Count of varaible] FROM NSFCourter2016.dbo.MAIN_VIEW " + query.toString() + " GROUP BY [" + variable + "]) AS M, "
                            + "(SELECT " + variables + "[" + variable + "] FROM NSFCourter2016.dbo.MAIN_VIEW " + query.toString() + ") AS N WHERE N.[" + variable + "] = M.[" + variable + "]", new Object[]{}, "project"); 
                    break;
                    
                case "bs":
                    options = inputOption.split("/");
                    String type = options[0];
                    //String variable;
                    
                    switch(type)
                    {
                        case "s":
                            variable = "SCI_NAME";
                            break;
                        case "p":
                            variable = "PRIM_NAME";
                            break;
                        case "t":
                            variable = "TAXONOMY";
                            break;
                        default:
                            break;
                    }
                    
                    options = options[1].split(",");
                    String names = "[" + options[0] + "]";
                    
                    for(int i = 1; i < options.length; i++)
                        names += ",[" + options[i] + "]";
                    
                    if(variables != null)
                    {
                        //variables = request.getParameter("ckbx");
                        
                        if(variables.equals(""))
                        {
                            Table columns = Access.getInstance().getTable("SELECT TOP(1) * FROM [NSFCourter2016].[dbo].[MAIN_VIEW]", new Object[]{}, "project");
                        
                            for(int i = 1; i < columns.getColumnCount() + 1; i++)
                                variables += "[" + columns.getColumnName(i) + "],";
                        }
                        else
                            variables += ",";
                    }
                    else
                    {
                        variables = "";
                        Table columns = Access.getInstance().getTable("SELECT TOP(1) * FROM [NSFCourter2016].[dbo].[MAIN_VIEW]", new Object[]{}, "project");
                        
                        for(int i = 1; i < columns.getColumnCount() + 1; i++)
                            variables += "[" + columns.getColumnName(i) + "],";
                    }
                    
                    results = Access.getInstance().getTable("SELECT " + top + " " + variables + names + " FROM" +
                            "(SELECT tmp.*, tmptwo.* From " +
                            "(SELECT * FROM [NSFCourter2016].[dbo].[MAIN_VIEW]) as tmp, " +
                            "(SELECT M.VAR_SELECTED, N.SUB, N.NUM_BIRDS FROM " +
                            "(SELECT TAXONOMY, " + variable + " AS VAR_SELECTED FROM BIRDS) AS M, " +
                            "(SELECT BIRD_TAXONOMY, SUBMISSION_ID AS SUB, CASE WHEN NUM_BIRDS >= 0 THEN NUM_BIRDS ELSE 1 END AS NUM_BIRDS FROM OBSERVATION_BIRD) AS N " +
                            "WHERE M.TAXONOMY = N.BIRD_TAXONOMY) as tmptwo " +
                            "WHERE tmp.[Submission ID of checlist] = tmptwo.SUB) AS TOTAL_TABLE pivot " +
                            "(SUM(NUM_BIRDS) FOR VAR_SELECTED IN (" + names + ")) as tbl " + query.toString(),
                            new Object[]{}, "project");
                    break;
                    
                default:
                    if(!bird.isEmpty())
                    {
                        //System.out.println(bird + query.toString());
                        results = Access.getInstance().getTable(bird + query.toString(), new Object[]{}, "project");
                    }
                    else if(!main.isEmpty())
                    {
                        //System.out.println(main + query.toString());
                        results = Access.getInstance().getTable(main + query.toString(), new Object[]{}, "project");
                    }
            }
            
        }  
        //System.out.println("query output: " + main + query.toString());
        return main + query.toString();
    }
}
