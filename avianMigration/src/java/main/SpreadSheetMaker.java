package main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author cjedwards1
 * 
 * Take a list of strings of column headers which are the variables used 
 * and the actual list of students as holder to create and hold the data
 * in an excel sheet.
 */
public class SpreadSheetMaker {
    
    //The 3 different styles used in the workbook.
    XSSFCellStyle cellStyleEven;
    XSSFCellStyle cellStyleOdd;
    XSSFCellStyle header;
    
    public void export(Table results, String user, String fileName) throws IOException
    {
        try(XSSFWorkbook wb = new XSSFWorkbook())
        {
            formatWorkBook(wb);
            
            XSSFSheet sheet = createSheet(wb, results, "main");
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
            
            if(getUserPercentage(wb, user) >= 100)
            {
                failedSendMail();
            }
            else
            {
                //Send the file through email, and save it to the server.
                File output = new File("..//..//files_for_download//" + fileName + ".xlsx");
                try(FileOutputStream out = new FileOutputStream(output))
                {
                    wb.write(out);
                    successSendEmail(output);
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    private double getUserPercentage(XSSFWorkbook wb, String user)
    {
        //Get all user files.
        File filesDirectory = new File("..//..//files_for_download");
        
        //Put files into array.
        File[] files = filesDirectory.listFiles();
        
        //Keep track of how much data the user has in mega bytes.
        double userSize = 0.0;

        if(files != null) //Make sure there are files to look at.
        {
            for(File file : files) //Go over each file.
            {
                if(file.isFile()) //Make sure it is a file and not a directory.
                {
                    if(file.getName().contains(user)) //If the file name contains the user then it is his.
                    {
                        userSize += (double) file.length() / 1000000; //Get size of file in mega bytes.
                    }
                }
            }
        }

        //Get how big the new file will be.
        try(ByteArrayOutputStream tempSizeHolder = new ByteArrayOutputStream())
        {
            wb.write(tempSizeHolder);
            byte[] bytes = tempSizeHolder.toByteArray();
            userSize += (double) bytes.length / 1000000;
        }
        catch(Exception ex){}


        //In mega bytes
        int userSizeLimit = 2;
        return (double) (userSize / userSizeLimit) * 100; //% of data used.
    }
    
    private void formatWorkBook(XSSFWorkbook wb)
    {
        //Create the cell style for the header row of the sheet.
        header = wb.createCellStyle();

        //Set both verticle and horizontal alignments to center for the text
        header.setAlignment(CellStyle.ALIGN_CENTER);
        header.setVerticalAlignment(VerticalAlignment.CENTER);

        //Create the font style that will be used by the header.
        XSSFFont headerFont = wb.createFont();

        //Set the font to be bold.
        headerFont.setBold(true);

        //Set the size of the font to be 14.
        headerFont.setFontHeightInPoints((short) 14);

        //Set the font to be used.
        headerFont.setFontName("Times New Roman");

        //Set the color of the font.
        headerFont.setColor(IndexedColors.WHITE.getIndex());

        //Set the new font style for the header.
        header.setFont(headerFont);

        //Set the background of the cell to be a grey color and make it visible
        header.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        header.setFillPattern(CellStyle.SOLID_FOREGROUND);

        //Set both left and right border to be a medium thikness bold line.
        header.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
        header.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);

        //Now do the same thing as the header with 2 new styles for
        //even and odd rows.
        cellStyleOdd = wb.createCellStyle();
        cellStyleOdd.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyleOdd.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFFont oddFont = wb.createFont();
        oddFont.setBold(false);
        oddFont.setFontHeightInPoints((short) 12);
        oddFont.setFontName("Times New Roman");
        oddFont.setColor(IndexedColors.WHITE.getIndex());
        cellStyleOdd.setFont(oddFont);
        cellStyleOdd.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        cellStyleOdd.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cellStyleOdd.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
        cellStyleOdd.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);

        cellStyleEven = wb.createCellStyle();
        cellStyleEven.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyleEven.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFFont evenFont = wb.createFont();
        evenFont.setBold(false);
        evenFont.setFontHeightInPoints((short) 12);
        evenFont.setFontName("Times New Roman");
        evenFont.setColor(IndexedColors.BLACK.getIndex());
        cellStyleEven.setFont(evenFont);
        cellStyleEven.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyleEven.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cellStyleEven.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
        cellStyleEven.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
    }
    
    private void failedSendMail()
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
            email.addTo("cjedwards1@malone.edu");
            email.setFrom("no-reply-avianMigration@malone.edu");
            email.setSubject("Error");
            email.setMsg("Creating this file would put you over your limit. Please delete a file and try again.");

            // send the email
            email.send();
            System.out.println("Sent message successfully....");
        }
        catch(EmailException ex)
        {
            Logger.getLogger(SpreadSheetMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void successSendEmail(File fileOut)
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
            email.addTo("cjedwards1@malone.edu");
            email.setFrom("no-reply-avianMigration@malone.edu");
            email.setSubject("Results");
            email.setMsg("Your file has been added successfully. Here is the file.");

            // add the attachment
            email.attach(fileOut);

            // send the email
            email.send();
            System.out.println("Sent message successfully....");
        }
        catch(EmailException ex)
        {
            Logger.getLogger(SpreadSheetMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Create a new sheet.
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
            CreationHelper createHelper = wb.getCreationHelper();

            //Create a new row in the sheet(this will be the header row).
            XSSFRow row = sheet.createRow(0);
            
            //Go over the rest of the cells in the first row and set them 
            //accordingly based on what is in the columns list.
            for(int i = 0; i < results.getColumnCount(); i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(createHelper.createRichTextString(results.getColumnName(i + 1)));
            }
            
            return sheet;
        }
        
        return null;
    }
    
    /**
     * Add a student to a sheet.
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
        CreationHelper createHelper = wb.getCreationHelper();
        
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
}
