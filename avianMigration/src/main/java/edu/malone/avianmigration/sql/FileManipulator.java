/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.avianmigration.sql;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.imageio.ImageIO;
//import edu.malone.avianmigration.utilities.AnimatedGifEncoder;
import edu.malone.avianmigration.utilities.ServletFunctions;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Cory Edwards
 */
public class FileManipulator {
    
    //Name of the project being used. (Hint: it is part of the URL)
    private final String projectName;
    private final String tableName;
    
    //2 threads makes excel sheets build at 100% cpu on 4 cores.
    private static final ExecutorService SHEETS = Executors.newFixedThreadPool(1);
    private final int numberOfCells = 600000; //Number of allowed cells per excel sheet
    
    /**
     * 
     * @param project Name of the project being used. (Hint: it is part of the URL)
     */
    private FileManipulator()
    {
        projectName = ServletFunctions.properties.getString("name.website");
        tableName = ServletFunctions.properties.getString("name.database");
    }
    
    public static FileManipulator getInstance()
    {
        return FileManipulatorHolder.getInstance();
    }
    
    private static class FileManipulatorHolder
    {
        private static FileManipulator instance = null;
        
        private static FileManipulator getInstance()
        {
            if(instance == null)
                instance = new FileManipulator();
            
            return instance;
        }
    }
    
    /**
     * Download file with given id and type to the response OutputStream.
     * @param id Id of the file that is wanted.
     * @param type The file's type (png, gif, xlsx, ...).
     * @param os The response OutputStream to write to.
     * @return The file name the user wants the file to be called.
     */
    public String downloadFile(String id, String type, OutputStream os)
    {
        try
        {
            Table file = Access.getInstance().getTable("SELECT FILE_ID, FILE_NAME, FILE_TYPE FROM NSFServiceProvider.dbo.FILES WHERE FILE_TYPE = ? AND FILE_ID = ?", new Object[]{type, id}, "sp");
            if(file.next())
            {
                File f = new File("C:\\Server Files\\" + projectName + "\\" + file.getString("FILE_ID") + "." + file.getString("FILE_TYPE"));
                FileInputStream in = new FileInputStream(f);
                IOUtils.copy(in, os);
                return file.getString("FILE_NAME") + "." + file.getString("FILE_TYPE");
            }
            return null;
            
        }
        catch (Exception ex) 
        {
            Logger.getLogger(FileManipulator.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * Refresh the file's expiration date to today's date.
     * @param id The file's id
     * @return True iff the method succeeds, false otherwise.
     */
    public boolean refreshFile(String id, String user)
    {
        try 
        {
            Table userId = Access.getInstance().getTable("SELECT USER_NAME FROM NSFServiceProvider.dbo.USERS WHERE USER_NAME = ?", new Object[]{user}, "sp");
            if(userId.next())
            {
                System.out.println("UPDATE NSFServiceProvider.dbo.FILES SET DATE = GETDATE() WHERE FILE_ID = " + id + " AND USER_NAME = " + userId.getInt("USER_NAME"));
                Access.getInstance().execute("UPDATE NSFServiceProvider.dbo.FILES SET DATE = GETDATE() WHERE FILE_ID = ? AND USER_NAME = ?", new Object[]{id, userId.getInt("USER_NAME")}, "sp");
                return true;
            }
            else
                return false;
        }
        catch (Exception ex)
        {
            Logger.getLogger(FileManipulator.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    /**
     * Completely remove a file from the server.
     * @param id The id of the file
     * @param type The file type
     * @return True iff the file is deleted from Server Files and metadata is deleted from SQL successfully, false otherwise.
     */
    public boolean deleteFile(String id, String type, String user)
    {
        try
        {
            Table userId = Access.getInstance().getTable("SELECT APPID FROM NSFServiceProvider.dbo.USERS WHERE USER_NAME = ?", new Object[]{user}, "sp");
            if(userId.next())
            {
                int value = Access.getInstance().execute("DELETE FROM NSFServiceProvider.dbo.FILES WHERE [FILE_ID]= ? AND USER_ID = ?", new Object[]{id, userId.getInt("APPID")}, "sp");
                Logger.getLogger(FileManipulator.class.getName()).log(Level.INFO, "Access returned {0}", value);

                File myFile = new File("C:\\Server Files\\" + projectName + "\\" + id + "." + type);

                return myFile.delete();
            }
            else
                return false;
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(FileManipulator.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    /**
     * Get a json array of the files the user has on the server.
     * @param user The username
     * @return A complete json array of json objects with file metadata.
     */
    public JSONArray getFiles(String user)
    {
        try
        {
            //ABS(DATEDIFF(DAY, DATEADD(DD, 7, DATE), GETDATE())) AS NEXT_DATE returns the number of days until the file expires.
            //The expiration date is set to 7 days right now.
            Table filesTable = Access.getInstance().getTable("SELECT M.* FROM "
                                + "(SELECT *, ABS(DATEDIFF(DAY, DATEADD(DD, 7, DATE), GETDATE())) AS NEXT_DATE FROM NSFServiceProvider.dbo.FILES) AS M, "
                                + "(SELECT APPID FROM NSFServiceProvider.dbo.USERS WHERE USER_NAME = ?) AS N "
                            + "WHERE M.USER_ID = N.APPID", new Object[]{user}, "sp");
            JSONArray files = new JSONArray();

            while(filesTable.next())
            {
                JSONObject file = new JSONObject();
                file.put("id", filesTable.getString("FILE_ID"));
                file.put("name", filesTable.getString("FILE_NAME"));
                file.put("type", filesTable.getString("FILE_TYPE"));
                file.put("date", filesTable.getDate("DATE"));
                file.put("next date", filesTable.getInt("NEXT_DATE"));
                file.put("size", filesTable.getDouble("SIZE"));
                file.put("auto", filesTable.getBoolean("AUTO_REFRESH"));

                if(filesTable.getBoolean("AUTO_REFRESH"))
                {
                    refreshFile(filesTable.getString("FILE_ID"), user);
                }

                files.put(file);
            }
            
            return files;
        }
        catch(Exception ex)
        {
            Logger.getLogger(FileManipulator.class.getName()).log(Level.SEVERE, null, ex);
            return new JSONArray();
        }
    }
    
    /**
     * Changes whether auto refresh is set to true or false for the file in the database.
     * @param auto The boolean to set auto refresh.
     * @param id The file's id
     * @return True iff the method succeeds, false otherwise.
     */
    public boolean changeAutoRefresh(boolean auto, String id, String user)
    {
        try 
        {
            Table userId = Access.getInstance().getTable("SELECT APPID FROM NSFServiceProvider.dbo.USERS WHERE USER_NAME = ?", new Object[]{user}, "sp");
            if(userId.next())
            {
                int value;
                if(auto)
                    value = Access.getInstance().execute("UPDATE NSFServiceProvider.dbo.FILES SET AUTO_REFRESH = CAST(1 AS BIT) WHERE FILE_ID = ? AND USER_ID = ?", new Object[]{id, userId.getInt("APPID")}, "sp");
                else
                    value = Access.getInstance().execute("UPDATE NSFServiceProvider.dbo.FILES SET AUTO_REFRESH = CAST(0 AS BIT) WHERE FILE_ID = ? AND USER_ID = ?", new Object[]{id, userId.getInt("APPID")}, "sp");
                
                Logger.getLogger(FileManipulator.class.getName()).log(Level.INFO, "Access returned {0}", value);
            }
            else
                return false;
            
            return true;
        }
        catch (Exception ex)
        {
            Logger.getLogger(FileManipulator.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    /**
     * Input a file onto the server. This will put the file in the project folder
     * inside C://Server files, and will input the metadata into SQL.
     * @param type The files type (png, xlsx, or gif).
     * @param data The data for the file (png is data URL, gif is {{names of pngs}, {ints of colors}, boolean of whether to delete pngs after}, xlsx is).
     * @param user The user name of the user.
     * @return The newly inputed file if successful, and null otherwise.
     */
    public synchronized File uploadFile(String type, Object data, String user)
    {
        try
        {
            Table userTable = Access.getInstance().getTable("SELECT USER_NAME, EMAIL FROM NSFServiceProvider.dbo.USERS WHERE USER_NAME = ?", new Object[]{user}, "sp");
            
            if(userTable.next())
            {
                switch(type)
                {
                    case "png":
                        return buildPng(data.toString(),  userTable.getString("EMAIL"), user, userTable.getInt("APPID"));
                    case "xlsx":
                        Table results = (Table) data;
                        int files = (int) Math.ceil((results.numberOfRows() * results.getColumnCount()) / (double) numberOfCells);

                        if(files > 1)
                        {
                            for(int i = 1; i <= files; i++)
                            {
                                String fileName = "file(" + i + ")";
                                Table table = results.cloneTable(((i - 1) * numberOfCells) + (i == 0 ? 0 : 1), i * numberOfCells);

                                SHEETS.submit(() -> 
                                {
                                    buildSheet(table, user, user + "" + System.nanoTime() + "" + new Random().nextInt(1500), fileName);
                                });
                            }
                        }
                        else
                        {
                            buildSheet(results, user, user + "" + System.nanoTime(), "file");
                        }
                        return null;
//                    case "gif":
//                        Object[] temp = (Object[]) data;
//                        return buildGif(user, userTable.getInt("APPID"), (String[]) temp[0], (boolean) temp[1]);
                    default:
                        return null;
                }
            }
            else
                return null;
        }
        catch(Exception ex)
        {
            Logger.getLogger(FileManipulator.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    private void buildSheet(Table results, String user, String fileName, String fileNumber)
    {
        try(XSSFWorkbook wb = new XSSFWorkbook())
        {
            //The 3 different styles used in the workbook.
            XSSFCellStyle cellStyleEven;
            XSSFCellStyle cellStyleOdd;
            XSSFCellStyle header;
            
            //Build the defaults for the colors in case properties fails.
            List<Integer> defaultWhite = new ArrayList();
            defaultWhite.add(255);
            defaultWhite.add(255);
            defaultWhite.add(255);
            
            List<Integer> defaultGray = new ArrayList();
            defaultWhite.add(102);
            defaultWhite.add(102);
            defaultWhite.add(102);
            
            List<Integer> defaultBlack = new ArrayList();
            defaultWhite.add(0);
            defaultWhite.add(0);
            defaultWhite.add(0);
            
            //The colors in r, g, b form for the different parts.
            List<Integer> headerFontColor = ServletFunctions.properties.getList(Integer.class, "header.font.color", defaultWhite);
            List<Integer> headerFillColor = ServletFunctions.properties.getList(Integer.class, "header.fill.color", defaultGray);
            List<Integer> oddFontColor = ServletFunctions.properties.getList(Integer.class, "odd.font.color", defaultWhite);
            List<Integer> oddFillColor = ServletFunctions.properties.getList(Integer.class, "odd.fill.color", defaultGray);
            List<Integer> evenFontColor = ServletFunctions.properties.getList(Integer.class, "even.font.color", defaultBlack);
            List<Integer> evenFillColor = ServletFunctions.properties.getList(Integer.class, "even.fill.color", defaultWhite);
            
            //Create the cell style for the header row of the sheet.
            header = wb.createCellStyle();

            //Set both verticle and horizontal alignments to center for the text
            header.setAlignment(ServletFunctions.properties.getShort("header.h.align", (short) 2));
            header.setVerticalAlignment(ServletFunctions.properties.getShort("header.v.align", (short) 2));

            //Create the font style that will be used by the header.
            XSSFFont headerFont = wb.createFont();

            //Set the font to be bold.
            headerFont.setBold(ServletFunctions.properties.getBoolean("header.bold", Boolean.TRUE));

            //Set the size of the font to be 14.
            headerFont.setFontHeightInPoints((short) (ServletFunctions.properties.getShort("font.size", (short) 12) + 2));

            //Set the font to be used.
            headerFont.setFontName(ServletFunctions.properties.getString("font.name", "Times New Roman"));

            //Set the color of the font.
            headerFont.setColor(new XSSFColor(new Color(headerFontColor.get(0), headerFontColor.get(1), headerFontColor.get(2))));

            //Set the new font style for the header.
            header.setFont(headerFont);

            //Set the background of the cell to be a grey color and make it visible
            header.setFillForegroundColor(new XSSFColor(new Color(headerFillColor.get(0), headerFillColor.get(1), headerFillColor.get(2))));
            header.setFillPattern(ServletFunctions.properties.getShort("header.fill.pattern", (short) 1));

            //Set both left and right border to be a medium thikness bold line.
            header.setBorderLeft(ServletFunctions.properties.getShort("header.border", (short) 2));
            header.setBorderRight(ServletFunctions.properties.getShort("header.border", (short) 2));

            //Now do the same thing as the header with 2 new styles for
            //even and odd rows.
            cellStyleOdd = wb.createCellStyle();
            cellStyleOdd.setAlignment(ServletFunctions.properties.getShort("odd.h.align", (short) 2));
            cellStyleOdd.setVerticalAlignment(ServletFunctions.properties.getShort("odd.v.align", (short) 2));
            XSSFFont oddFont = wb.createFont();
            oddFont.setBold(ServletFunctions.properties.getBoolean("odd.bold", Boolean.FALSE));
            oddFont.setFontHeightInPoints(ServletFunctions.properties.getShort("font.size", (short) 12));
            oddFont.setFontName(ServletFunctions.properties.getString("font.name", "Times New Roman"));
            oddFont.setColor(new XSSFColor(new Color(oddFontColor.get(0), oddFontColor.get(1), oddFontColor.get(2))));
            cellStyleOdd.setFont(oddFont);
            cellStyleOdd.setFillForegroundColor(new XSSFColor(new Color(oddFillColor.get(0), oddFillColor.get(2), oddFillColor.get(2))));
            cellStyleOdd.setFillPattern(ServletFunctions.properties.getShort("odd.fill.pattern", (short) 1));
            cellStyleOdd.setBorderLeft(ServletFunctions.properties.getShort("odd.border", (short) 2));
            cellStyleOdd.setBorderRight(ServletFunctions.properties.getShort("odd.border", (short) 2));

            cellStyleEven = wb.createCellStyle();
            cellStyleEven.setAlignment(ServletFunctions.properties.getShort("even.h.align", (short) 2));
            cellStyleEven.setVerticalAlignment(ServletFunctions.properties.getShort("even.v.align", (short) 2));
            XSSFFont evenFont = wb.createFont();
            evenFont.setBold(ServletFunctions.properties.getBoolean("even.bold", Boolean.FALSE));
            evenFont.setFontHeightInPoints(ServletFunctions.properties.getShort("font.size", (short) 12));
            evenFont.setFontName(ServletFunctions.properties.getString("font.name", "Times New Roman"));
            evenFont.setColor(new XSSFColor(new Color(evenFontColor.get(0), evenFontColor.get(1), evenFontColor.get(2))));
            cellStyleEven.setFont(evenFont);
            cellStyleEven.setFillForegroundColor(new XSSFColor(new Color(evenFillColor.get(0), evenFillColor.get(1), evenFillColor.get(2))));
            cellStyleEven.setFillPattern(ServletFunctions.properties.getShort("even.fill.pattern", (short) 1));
            cellStyleEven.setBorderLeft(ServletFunctions.properties.getShort("even.border", (short) 2));
            cellStyleEven.setBorderRight(ServletFunctions.properties.getShort("even.border", (short) 2));
            
            XSSFSheet sheet = createSheet(wb, results, ServletFunctions.properties.getString("sheet.name", "edu.malone.westnilespread"));
            if(sheet != null)
            {
                while(results.next())
                {
                    addDataRow(wb, sheet, results);
                }
                
                //Setting the cell style to header for all header columns.
                for(int n = 0; n < sheet.getRow(0).getLastCellNum(); n++)
                    sheet.getRow(0).getCell(n).setCellStyle(header);
                
                //Setting the cell style for the odd and even rows
                for(int x = 1; x <= sheet.getLastRowNum(); x++)
                {
                    if(x % 2 == 0 && x != 1)
                    {
                        for(int n = 0; n < sheet.getRow(x).getLastCellNum(); n++)
                            sheet.getRow(x).getCell(n).setCellStyle(cellStyleOdd);
                    }
                    else
                    {
                        for(int n = 0; n < sheet.getRow(x).getLastCellNum(); n++)
                            sheet.getRow(x).getCell(n).setCellStyle(cellStyleEven);
                    }
                }

                //Autosize every column now so that all the changes and cells
                //can be easily seen.
                for(int n = 0; n < sheet.getRow(0).getLastCellNum(); n++)
                    sheet.autoSizeColumn(n);
            }
            
            Table userInfo = Access.getInstance().getTable("SELECT APPID, EMAIL FROM NSFServiceProvider.dbo.USERS WHERE USER_NAME = ?", new Object[]{user}, "sp");
            if(userInfo.next())
            {
                if(!checkExcelDoc(wb, user))
                {
                    failedSendMail(userInfo.getString("EMAIL"), "excel sheet", "The sheet would go over the your data limit.");
                }
                else
                {
                    File output = new File("C:\\Server Files\\WestNileSpread\\" + fileName + ".xlsx");

                    try(FileOutputStream out = new FileOutputStream(output))
                    {
                        wb.write(out);

                        Access.getInstance().execute("INSERT INTO NSFServiceProvider.dbo.FILES (FILE_ID, AUTO_REFRESH, FILE_NAME, FILE_TYPE, DATE, USER_ID, SIZE, PROJECT_NAME) VALUES (?, CAST(0 AS BIT), ?, 'xlsx', GETDATE(), ?, ?, ?)", new Object[]{fileName, fileNumber, userInfo.getInt("APPID"), calcFileSize(output), projectName}, "sp");

                        successSendEmail(userInfo.getString("EMAIL"));
                    }
                    catch(Exception ex)
                    {
                        failedSendMail(userInfo.getString("EMAIL"), "excel sheet", "Failed to save the sheet.");
                        Logger.getLogger(FileManipulator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        catch(Exception ex)
        {
            Logger.getLogger(FileManipulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Check if a user has the space to hold an excel sheet.
     * @param wb The workbook that is the excel sheet.
     * @param user The username
     * @return True iff the excel sheet can fit in the unallocated allowed user space, false otherwise.
     */
    private boolean checkExcelDoc(XSSFWorkbook wb, String user)
    {
        double userAmount = getUserSpace(user);
        int userAllowedAmount = getUserAllowedSpace(user);
        
        //Get how big the new file will be.
        try(ByteArrayOutputStream tempSizeHolder = new ByteArrayOutputStream())
        {
            wb.write(tempSizeHolder);
            byte[] bytes = tempSizeHolder.toByteArray();
            userAmount += (double) bytes.length / 1000000;
        }
        catch(Exception ex){ex.printStackTrace();}

        return userAmount <= userAllowedAmount;
    }
    
    /**
     * When an excel sheet cannot be made, send an email to the user.
     * @param userEmail User's email
     */
    private void failedSendMail(String userEmail, String fileType, String message)
    {
        try 
        {
            //Something may happen before we send the email, so restate the map
            //before setting up the email to make sure it sends properly.
            MailcapCommandMap mailMap = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mailMap.addMailcap("multipart/mixed;;x-java-content-handler=com.sun.mail.handlers.multipart_mixed");

            // Create the email message
            MultiPartEmail email = new MultiPartEmail();
            email.setHostName("smtp.malone.edu");
            email.setSmtpPort(25);
            email.addTo(userEmail);
            email.setFrom("no-reply-WestNileSpread@malone.edu");
            email.setSubject("Error");
            email.setMsg("\"Could not create the " + fileType + ".<br />" + message + "\"");

            // send the email
            email.send();
        }
        catch(EmailException ex)
        {
            Logger.getLogger(FileManipulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * When an excel sheet is successfully made, send an email to the user.
     * @param userEmail User's email
     */
    private void successSendEmail(String userEmail)
    {
        try 
        {
            //Something may happen before we send the email, so restate the map
            //before setting up the email to make sure it sends properly.
            MailcapCommandMap mailMap = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mailMap.addMailcap("multipart/mixed;;x-java-content-handler=com.sun.mail.handlers.multipart_mixed");

            // Create the email message
            MultiPartEmail email = new MultiPartEmail();
            email.setHostName("smtp.malone.edu");
            email.setSmtpPort(25);
            email.addTo(userEmail);
            email.setFrom("no-reply-WestNileSpread@malone.edu");
            email.setSubject("Success");
            email.setMsg("Your file has been added successfully. Go to the downloads tab and click the refresh button at the top right of the screen to see it.");

            // send the email
            email.send();
        }
        catch(EmailException ex)
        {
            Logger.getLogger(FileManipulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Create a new excel sheet.
     * 
     * @param wb The workbook being used.
     * @param major The new major to add.
     * @return The new sheet for the major.
     */
    private XSSFSheet createSheet(XSSFWorkbook wb, Table results, String major)
    {
        if(results != null)
        {
            if(wb.getNumberOfSheets() > 0)
                wb.removeSheetAt(0);
            
            //Create the sheet with the given name.
            XSSFSheet sheet = (XSSFSheet) wb.createSheet(major);

            //Help in creating strings
            XSSFCreationHelper createHelper = wb.getCreationHelper();

            //Create a new row in the sheet(this will be the header row).
            XSSFRow row = sheet.createRow(0);
            
            //Go over the rest of the cells in the first row and set them 
            //accordingly based on what is in the columns list.
            for(int i = 0; i < results.getColumnCount(); i++)
            {
                XSSFCell cell = row.createCell(i);
                cell.setCellValue(createHelper.createRichTextString(results.getColumnName(i + 1)));
            }
            
            return sheet;
        }
        
        return null;
    }
    
    /**
     * Add a student to an excel sheet.
     * 
     * @param sheet The sheet with the student's major.
     * @param wb The workbook being used.
     * @param instance The values for the student.
     * @param passed The prediction from the machine.
     * @param name The student's name.
     */
    private void addDataRow(XSSFWorkbook wb, XSSFSheet sheet,Table results) throws SQLException
    {
        //Create a new row in the sheet at the end.
        XSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
        
        //Help in creating strings
        XSSFCreationHelper createHelper = wb.getCreationHelper();
        
        for(int n = 0; n < results.getColumnCount(); n++)
        {
            if(results.getObject(n + 1) instanceof Double)
                row.createCell(n).setCellValue(results.getDouble(n + 1));
            else if(results.getObject(n + 1) instanceof Integer)
                row.createCell(n).setCellValue(Double.parseDouble(Integer.toString(results.getInt(n + 1))));
            else
                row.createCell(n).setCellValue(createHelper.createRichTextString(results.getString(n + 1)));
        }
    }
    
    /**
     * Change the name of the file in the database. This name is used when the
     * file is to be downloaded. It will be the name of the file that is returned to the user.
     * @param name The new name of the file.
     * @param id The file's id.
     * @return True iff the method succeeded, false otherwise.
     */
    public boolean changeFileName(String name, String id, String user)
    {
        try 
        {
            Table userId = Access.getInstance().getTable("SELECT APPID FROM NSFServiceProvider.dbo.USERS WHERE USER_NAME = ?", new Object[]{user}, "sp");
            if(userId.next())
            {
                Access.getInstance().execute("UPDATE NSFServiceProvider.dbo.FILES SET FILE_NAME = ? WHERE FILE_ID = ?, AND USER_ID = ?", new Object[]{name, id, userId.getInt("APPID")}, "sp");
                return true;
            }
            else
                return false;
        }
        catch (Exception ex) 
        {
            Logger.getLogger(FileManipulator.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    /**
     * Get the size of an already uploaded file in MB.
     * @param id The file's id.
     * @param fileType The file's type.
     * @return The file's size or 0.0 if the file could not be found or is actually 0.0 MB.
     * @throws Exception Any SQL exceptions that appear while getting the file metadata.
     */
    public double getFileSize(String id, String fileType) throws Exception
    {
        Table fileTable = Access.getInstance().getTable("SELECT SIZE FROM NSFServiceProvider.dbo.FILES WHERE FILE_TYPE = ? AND FILE_ID = ?", new Object[]{fileType, id}, "sp");
        if(fileTable.next())
        {
            return fileTable.getDouble("SIZE");
        }
        else
            return 0.0;
    }
    
    /**
     * Calculate the size of a file in MBs.
     * @param file The file to get size of.
     * @return The size of the file in MB.
     */
    public double calcFileSize(File file)
    {
        return (file.length() / 1000000.0);
    }
    
    /**
     * Get the amount of space the user is allowed on the server based on whatever 
     * scope they are given.
     * @param user The user name.
     * @return The amount of MBs allowed for the user.
     */
    public int getUserAllowedSpace(String user)
    {
        try
        {
            Table userId = Access.getInstance().getTable("SELECT APPID FROM NSFServiceProvider.dbo.USERS WHERE USER_NAME = ?", new Object[]{user}, "sp");
            if(userId.next())
            {
                Table userScope = Access.getInstance().getTable("SELECT SCOPE_ID FROM NSFServiceProvider.dbo." + tableName + " WHERE USER_ID = ?", new Object[]{userId.getInt("APPID")}, "sp");

                if(userScope.next())
                {
                    Table scopeSize = Access.getInstance().getTable("SELECT MB_CAP FROM NSFServiceProvider.dbo.SCOPE WHERE APPID = ?", new Object[]{userScope.getInt("SCOPE_ID")}, "sp");

                    if(scopeSize.next())
                        return scopeSize.getInt("MB_CAP");
                }
            }
        }
        catch (Exception ex) 
        {
            Logger.getLogger(FileManipulator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    /**
     * Get the amount of space the user currently holds on the server.
     * @param user The user name.
     * @return The amount of space the user currently takes up.
     */
    public double getUserSpace(String user)
    {
        try
        {
            Table userId = Access.getInstance().getTable("SELECT APPID FROM NSFServiceProvider.dbo.USERS WHERE USER_NAME = ?", new Object[]{user}, "sp");

            if(userId.next())
            {
                Table total = Access.getInstance().getTable("SELECT SUM(SIZE) AS TOTAL_SIZE FROM NSFServiceProvider.dbo.FILES WHERE USER_ID = ? AND PROJECT_NAME = ?", new Object[]{userId.getInt("APPID"), projectName}, "sp");
                if(total.next())
                {
                    if(total.getDouble("TOTAL_SIZE") != null)
                        return total.getDouble("TOTAL_SIZE");
                    else 
                        return 0.0;
                }
            }
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(FileManipulator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0.0;
    }
    
    /**
     * Get the % of space the user has used out of his or her allowed space.
     * @param user The user name.
     * @return The % of space used.
     */
    public double getUserPercent(String user)
    {
        double userAmount = getUserSpace(user);
        int userAllowedAmount = getUserAllowedSpace(user);
        
        if(userAllowedAmount != 0 && userAmount != 0.0)
            return (double) (userAmount / userAllowedAmount);
        else 
            return 0.0;
    }
    
    //check if file would go over a users allowed amount
    /**
     * Check if a file will go over the users allowed limit if it is to be uploaded.
     * @param user The user name.
     * @param file The file.
     * @return True iff the user's space + the file's size does not exceed the user's allowed space.
     */
    public boolean checkFile(String user, File file)
    {
        double userAmount = getUserSpace(user) + calcFileSize(file);
        int userAllowedAmount = getUserAllowedSpace(user);
        return userAmount <= userAllowedAmount;
    }
    
    /**
     * Build a png file for the user.
     * @param data The data URL from the server.
     * @param user The user name.
     * @param userID The user id.
     * @return A png file.
     * @throws Base64DecodingException
     * @throws IOException
     * @throws SQLException 
     */
    private File buildPng(String data, String email, String user, int userID) throws IOException, SQLException
    {
        
        byte[] imageBytes = Base64.getDecoder().decode(data);
        BufferedImage inputImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
        String fileName = user + "" + System.nanoTime();
        File outputFile = new File("C:\\Server Files\\" + projectName + "\\" + fileName + ".png");
        ImageIO.write(inputImage, "png", outputFile);

        if(checkFile(user, outputFile))
            Access.getInstance().execute("INSERT INTO NSFServiceProvider.dbo.FILES (FILE_ID, AUTO_REFRESH, FILE_NAME, FILE_TYPE, DATE, USER_ID, SIZE, PROJECT_NAME) VALUES (?, CAST(0 AS BIT), ?, 'png', GETDATE(), ?, ?, ?)", new Object[]{fileName, fileName, userID, calcFileSize(outputFile), projectName}, "sp");
        else
        {
            failedSendMail(email, "png file", "Creating the file out go over your data limit.");
            
            if(outputFile.delete())
                return null;
            else
                throw new FileNotFoundException("File:" + outputFile.getCanonicalPath() + " was not successfully deleted.");
        }
        return outputFile;
    }
    
    /**
     * Build a gif file for the user.
     * @param user The username.
     * @param userID The user id.
     * @param names An array of png file ids to be used in the gif.
     * @param deleteFiles True to delete files after used, false to not do anything.
     * @return A gif file.
     * @throws IOException
     * @throws SQLException 
     */
//    public File buildGif(String user, int userID, String[] names, boolean deleteFiles) throws IOException, SQLException
//    {
//        String fileName = user + "" + System.nanoTime();
//        File file = new File("C:\\Server Files\\" + projectName + "\\" + fileName + ".gif");
//        
//        try(BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file)))
//        {
//            AnimatedGifEncoder e = new AnimatedGifEncoder();
//            e.start(output);
//            e.setDelay(2000);   // 2 frame per sec
//            e.setRepeat(0);     //0 means repeat indefinitly
//            for (String name : names)
//            {
//                File imageFile = new File("C:\\Server Files\\" + projectName + "\\" + name);
//                BufferedImage nextImage = ImageIO.read(imageFile);
//                e.addFrame(nextImage);
//                if(deleteFiles)
//                {
//                    if(imageFile.exists())
//                    {
//                        try
//                        {
//                            if(!imageFile.delete())
//                                throw new FileNotFoundException("File:" + imageFile.getCanonicalPath() + " was not successfully deleted.");
//                            
//                            Access.getInstance().execute("DELETE FROM NSFServiceProvider.dbo.FILES WHERE FILE_ID = ?", new Object[]{name}, "sp");
//                        }
//                        catch(Exception ex)
//                        {
//                            Logger.getLogger(FileManipulator.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                }
//            }
//            e.finish();
//        }
//            
//        if(checkFile(user, file))
//            Access.getInstance().execute("INSERT INTO NSFServiceProvider.dbo.FILES (FILE_ID, AUTO_REFRESH, FILE_NAME, FILE_TYPE, DATE, USER_ID, SIZE, PROJECT_NAME) VALUES (?, CAST(0 AS BIT), ?, 'gif', GETDATE(), ?, ?, ?)", new Object[]{fileName, fileName, userID, calcFileSize(file), projectName}, "sp");
//        else
//        {
//            failedSendMail(user, "gif file", "Creating this file would go over your data limit.");
//            
//            if(file.delete())
//                return null;
//            else
//                throw new FileNotFoundException("File:" + file.getCanonicalPath() + " was not successfully deleted.");
//        }
//        return file;
//    }
    
    /**
     * File from the server must be converted to a usable format before they 
     * can be made into a gif.
     * @param image The png file to be converted.
     * @param colors The array of int rgb values to asssine. 
     * @return A converted image.
     */
//    public BufferedImage makeCompatible(BufferedImage image, int[] colors) 
//    {
//        int w = image.getWidth();
//        int h = image.getHeight();
//        
//        //Colors length to the power of 3 for each color in rgb.
//        byte[] red = new byte[colors.length * colors.length * colors.length];
//        byte[] green = new byte[colors.length * colors.length * colors.length];
//        byte[] blue = new byte[colors.length * colors.length * colors.length];
//        
//        int numColors = 0;
//        for(int r : colors)
//        {
//            for(int g : colors)
//            {
//                for(int b : colors)
//                {
//                    Color temp = new Color(r, g, b);
//                    red[numColors] = (byte) temp.getRed();
//                    green[numColors] = (byte) temp.getGreen();
//                    blue[numColors] = (byte) temp.getBlue();
//                    numColors++;
//                }
//            }
//        }
//
//        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_INDEXED, new IndexColorModel(16, colors.length * colors.length * colors.length,
//                red,
//                green,
//                blue,
//                0));
//        Graphics2D g = result.createGraphics();
//        g.drawRenderedImage(image, new AffineTransform()); //or some other drawImage function
//        g.dispose();
//
//        return result;
//    }   
}
