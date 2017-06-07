/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author cjedwards1
 */
//This is the name the servlet takes on when the website calls for it.
//Basically it will be what goes in the url.
@WebServlet("/submit_job")
public class EntryPoint extends HttpServlet
{
    //The class that will be used to hold an abstract connection to the sql server.
    public Access access = new Access();
    
    //The class that will make and hold a spreadsheet every time the user 
    //clicks the submit button.
//    public SpreadSheetMaker ssm = new SpreadSheetMaker();

    //This is called when the website submits a "Get" request to this servlet.
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException
    {
        //Get all of the data for the autocomplete options for the text boxes.
        if(request.getParameter("vnd") != null)
        {
            //Cannot be used due to limitations with HTML, save code for later.
            //This was not finished.
//            JSONObject data = new JSONObject();
//            
//            response.setContentType("application/json");
//            
//            try(PrintWriter out = response.getWriter())
//            {
//                Table observers = access.getTable("SELECT DISTINCT [Observer name] FROM NSFCourter2016.dbo.MAIN_VIEW");
//                Table science = access.getTable("SELECT DISTINCT [Scientific Name] FROM NSFCourter2016.dbo.BIRD_VIEW");
//                Table common = access.getTable("SELECT DISTINCT [Common Name] FROM NSFCourter2016.dbo.BIRD_VIEW");
//                Table family = access.getTable("SELECT DISTINCT [Family Name] FROM NSFCourter2016.dbo.BIRD_VIEW");
//                Table order = access.getTable("SELECT DISTINCT [Order Name] FROM NSFCourter2016.dbo.BIRD_VIEW");
//                
//                JSONArray array = new JSONArray();
//                
//                while(observers.next())
//                {
//                    array.put(observers.getObject(1));
//                }
//                data.put("observers", array);
//                
//                array = new JSONArray();
//                
//                while(science.next())
//                {
//                    array.put(science.getObject(1));
//                }
//                data.put("science", array);
//                
//                array = new JSONArray();
//                
//                while(common.next())
//                {
//                    array.put(common.getObject(1));
//                }
//                data.put("common", array);
//                
//                array = new JSONArray();
//                
//                while(family.next())
//                {
//                    array.put(family.getObject(1));
//                }
//                data.put("family", array);
//                
//                array = new JSONArray();
//                
//                while(order.next())
//                {
//                    array.put(order.getObject(1));
//                }
//                data.put("order", array);
//                
//                //Write the object to the printstream.
//                out.write(data.toString());
//                
//                //Flush the stream to reset.
//                out.flush();
//                
//            }
//            catch(Exception ex)
//            {
//                
//            }
        }
        
        //Get and return the list of files for a user.
        else if(request.getParameter("files") != null)
        {
            try(PrintWriter out = response.getWriter())
            {
                String user = request.getParameter("user");
                JSONObject files = new JSONObject();
                JSONArray listFiles = new JSONArray();
                
                System.out.println("Directory: " + System.getProperty("user.dir"));
                File folder = new File("..\\webapps\\avianMigration\\query_files");
                File[] listOfFiles = folder.listFiles();
                
                if(listOfFiles != null)
                {
                    for(File tempFile : listOfFiles)
                    {
                        if(tempFile.isFile())
                            if(tempFile.getName().contains(user))
                                listFiles.put(tempFile.getName());
                    }
                }
                files.put("files", listFiles);
                
                 //Write the object to the printstream.
                out.write(files.toString());
                
                //Flush the stream to reset.
                out.flush();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        
        else if(request.getParameter("yearText") != null)
        {
            
            //System.out.println("Got it!");
            response.setContentType("application/json");
            
            try (PrintWriter out = response.getWriter())
            {
                Table rows = access.getTable("SELECT DISTINCT YEAR([Date and time recorded]) AS DATE FROM NSFCourter2016.dbo.MAIN_VIEW GROUP BY [Date and time recorded] HAVING YEAR([Date and time recorded]) "
                        + "BETWEEN MIN(YEAR([Date and time recorded])) AND MAX(YEAR([Date and time recorded])) ORDER BY DATE");
                
                //Creat the returning json object.
                JSONObject returnObject = new JSONObject();
                
                //Set up the json array that will hold the variable names.
                JSONArray array = new JSONArray();
              
                //rowNames.next();
                while(rows.next())
                {
                    array.put(rows.getInt("DATE"));
                }              
                //Insert the array into the returning object.
                returnObject.put("years", array);
                System.out.println(returnObject.toString());
                
                //Write the object to the printstream.
                out.write(returnObject.toString());
                
                //Flush the stream to reset.
                out.flush();
                
                //Close the table object.
                rows.close();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            
        }
        
        else if(request.getParameter("vars") != null)
        {
            //Set return type to json object.
            response.setContentType("application/json");
            
            //Print writers are for returning text.
            try (PrintWriter out = response.getWriter())
            {
                //Get simply the first row of the view.
                Table columnNames = access.getTable("SELECT TOP(1) * FROM NSFCourter2016.dbo.MAIN_VIEW");
                
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
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            response.setContentType("application/json");
            
            try (PrintWriter out = response.getWriter())
            {
                /**
                 * Depending on what tab was chosen by the user, the 
                 * corresponding String will be filled correctly. Later,
                 * it will check to see which String is empty and which one
                 * isn't to determine which view to get the data from and 
                 * how it will do so.
                 */
                String main = "";
                String bird = "";
                
                String view;
                
                if(request.getParameter("select").equals("gen"))
                    view = "MAIN_VIEW";
                else
                    view = "HISTORICAL_VIEW";
                
                //Will hold the WHERE part of the query.
                StringBuilder query = new StringBuilder("");
                
                //The returning json object with at most 1000 results to
                //save on memory.
                JSONObject returnObject = new JSONObject();
                
                //Get all of the parameters that came to the server.
                Enumeration<String> parameters = request.getParameterNames();
                String parameter;
                
                //Go through each one.
                while(parameters.hasMoreElements())
                {
                    parameter = parameters.nextElement();
                    
                    //Get the parameter's data.
                    String inputOption = request.getParameter(parameter);
                    String[] options;
                    
                    //Check which parameter is chosen.
                    switch(parameter)
                    {
                        //climate divisions
                        case "cd":
                            //If not already set, set the correct starting String.
                            if(main.isEmpty())
                                 main = "SELECT * FROM NSFCourter2016.DBO." + view + " ";

                            //Check whether the query already has the WHERE or not to add the parameter correctly.
                            if(query.toString().contains("WHERE"))
                                query.append(" and [Climate Division ID] IN (").append(inputOption).append(")");
                            else
                                query.append("WHERE [Climate Division ID] IN (").append(inputOption).append(")");
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
                                    query.append(" AND DATEPART(YEAR, [Date and time recorded]) BETWEEN ").append(option.split("/")[0]).append(" AND ").append(option.split("/")[1]);
                                else
                                    query.append(" WHERE DATEPART(YEAR, [Date and time recorded]) BETWEEN ").append(option.split("/")[0]).append(" AND ").append(option.split("/")[1]);
                            }
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
                                if(query.toString().contains("WHERE"))
                                    query.append(" AND DATEPART(DW, [Date and time recorded]) BETWEEN ").append(option.split("/")[0]).append(" AND ").append(option.split("/")[1]);
                                else
                                    query.append(" WHERE DATEPART(DW, [Date and time recorded]) BETWEEN ").append(option.split("/")[0]).append(" AND ").append(option.split("/")[1]);
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
                                    //CONVERT(VARCHAR(8), [Date and time recorded], 112) converts [Date and time recorded] which is a datetime variable to the format --> YYYYMMDD
                                    query.append(" AND CONVERT(VARCHAR(8), [Date and time recorded], 112) BETWEEN ").append(option.split("/")[0].replaceAll("-", "")).append(" AND ").append(option.split("/")[1].replaceAll("-", ""));
                                }
                                else
                                    query.append(" CONVERT(VARCHAR(8), [Date and time recorded], 112) BETWEEN ").append(option.split("/")[0].replaceAll("-", "")).append(" AND ").append(option.split("/")[1].replaceAll("-", ""));
                            }
                            break;
                        
                        //Am or PM
                        case "ap":
                            if(main.isEmpty())
                                     main = "SELECT * FROM NSFCourter2016.DBO." + view + " ";
                            
                            if(query.toString().contains("WHERE"))
                                query.append(" AND FORMAT([Date and time recorded], 'tt')='").append(inputOption).append("'");
                            else
                                query.append(" WHERE FORMAT([Date and time recorded], 'tt')='").append(inputOption).append("'");
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
                            
                            //For scientific names
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
                                query.append("WHERE [Observer ID] IN (").append(inputOption).append(")");
                            break;
                    }
                }
                
                String top = "";
                
                if(request.getParameter("application") == null)
                {
                    top = " TOP(100) ";
                    main = main.replaceAll("SELECT", "SELECT TOP(100) ");
                    bird = bird.replaceAll("SELECT", "SELECT TOP(100) ");
                }
                
                //Must be after the other options to have the WHERE clause set up 
                //and before the calculations because there will be an updated
                //WHERE clause going into them.
                if(request.getParameter("ct") != null)
                {
                    String[] options = request.getParameter("ct").split("/");
                    
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
                    
                    Table tempTable = access.getTable("WITH MAIN AS (SELECT " + top + " * FROM NSFCourter2016.DBO.MAIN_VIEW" + query.toString() + ") " +
                                        "SELECT * FROM (SELECT [Observer ID], COUNT([Observer ID]) AS [Number of checlists]"
                                        + " FROM MAIN GROUP BY [Observer ID]) AS M WHERE [Number of checlists]" + compare + options[1] + " ORDER BY  [Observer ID] ASC");
                    
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
                }
                
                Table results = null;
                
                //If the # of submitted checklists option was checkd for calculations.
                if(request.getParameter("cs") != null)
                {
                    String[] options = request.getParameter("cs").split("/");

                    String operation = null;
                    switch(options[1])
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
                            operation = "=";
                            break;
                    }

                    //Include group checklists.
                    if(options[0].equals("yes"))
                    {
                        results = access.executeProcedure("NSFCourter2016.dbo.GET_OBSERVERS_WITH_CHECKLISTS_W_GROUPS", new String[]{query.toString(), operation + options[2], top});
                    }
                    else
                    {
                        results = access.executeProcedure("NSFCourter2016.dbo.GET_OBSERVERS_WITH_CHECKLISTS_WO_GROUPS", new String[]{query.toString(), operation + options[2], top});
                    }
                }
                
                //Count number of a variable
                else if(request.getParameter("cl") != null)
                {
                    String variable = request.getParameter("cl");
                    results = access.getTable("SELECT " + top + " [" + variable + "], COUNT([" + variable + "]) AS [Count of varaible] FROM NSFCourter2016.dbo.MAIN_VIEW" + query.toString() + " GROUP BY [" + variable + "]");
                }
                
                //Get the number of a certain list of birds for a group of checklists
                else if(request.getParameter("bs") != null)
                {
                    String[] options = request.getParameter("bs").split("/");
                    String type = options[0];
                    String variable = "";
                    
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
                    
                    System.out.println(variable);
                    System.out.println(names);
                    System.out.println(query.toString());
                    results = access.executeProcedure("NSFCourter2016.dbo.PIVOT_BIRD", new String[]{variable, names, query.toString(), top});
                }
                
                //Else the nn option was select I.E. none
                else
                {
                    if(!bird.isEmpty())
                    {
                        System.out.println(bird + query.toString());
                        results = access.getTable(bird + query.toString());
                    }
                    else if(!main.isEmpty())
                    {
                        System.out.println(main + query.toString());
                        results = access.getTable(main + query.toString());
                    }
                }
                
                //If anything came back
                if(results != null)
                {
                    //If application is not null then the user clicked on 
                    //export which means we got all posible data and want
                    //to make the spreadsheet and save it for the user.
                    if(request.getParameter("application") != null)
                    {
                        //Create the spreadsheet.
                        String user = "jcourter@malone.edu";
                        new SpreadSheetMaker().export(results, user,
                                user + " " + new SimpleDateFormat("yyyyMMddHHmmss").format(new Timestamp(System.currentTimeMillis())));
                        
                    }
                    
                    //The user just wants a sample of the data, so we 
                    //forced sql to only give us the first 100 rows to 
                    //speed things up.
                    else
                    {
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
                        out.flush();
                    }
                    results.close();
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    //If a "Post" request is called just simply pass everything over to the "Get"
    //request side since at this point once they both reach the server they 
    //are the same thing.
   @Override
   public void doPost(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException 
   {
        doGet(request, response);
   }
}
