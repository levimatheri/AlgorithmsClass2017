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
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletResponse;
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
    
    //Since each sheet is a different major, there could be any number of sheets.
    //This is used to hold and keep track of them all.
    ArrayList<XSSFSheet> sheets = new ArrayList();
    
    //The 3 different styles used in the workbook.
    XSSFCellStyle cellStyleEven;
    XSSFCellStyle cellStyleOdd;
    XSSFCellStyle header;
    
    public void export(HttpServletResponse response, Table results, OutputStream fileOut, String user, String fileName) throws IOException
    {
        try(XSSFWorkbook wb = new XSSFWorkbook())
        {
            sheets.clear();
            
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
            
            XSSFSheet temp = createSheet(wb, results, "main");
            if(temp != null)
            {
                sheets.add(temp);
                while(results.next())
                {
                    addDataRow(wb, sheets.get(sheets.size() - 1), results);
                }
            }
            
            for(XSSFSheet sheet : sheets)
            {
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
            
//            InformationBox.infoBox(null, "Results printed successfully.", "Results");
        
            File filesDirectory = new File("..//..//files_for_download");
            File[] files = filesDirectory.listFiles();
            double userSize = 0.0;


            if(files != null)
            {
                for(File file : files)
                {
                    if(file.isFile())
                    {
                        if(file.getName().contains(user))
                        {
                            userSize += (double) file.length() / 1000000;
                        }
                    }
                }
            }

            try(ByteArrayOutputStream tempSizeHolder = new ByteArrayOutputStream())
            {
                wb.write(tempSizeHolder);
                byte[] bytes = tempSizeHolder.toByteArray();
                userSize += (double) bytes.length / 1000000;
            }
            catch(Exception ex){}


            int userSizeLimit = 10;
            double percentage = (double) Math.round((userSize / userSizeLimit) * 10000) / 100;

            if(percentage >= 100)
            {

            }
            else
            {
                File output = File.createTempFile(fileName, ".xlsx");

                try(FileOutputStream out = new FileOutputStream(output))
                {
                    wb.write(out);

                     // Sender's email ID needs to be mentioned
                    String from = "avianMigration@malone.edu";

                    // Get system properties
                    Properties properties = System.getProperties();

                    // Setup mail server
                    properties.put("mail.smtp.host", "smtp.malone.edu");
                    properties.put("mail.smtp.port", "25");

                    // Get the default Session object.
                    Session session = Session.getDefaultInstance(properties);

                    // Create a default MimeMessage object.
                    MimeMessage message = new MimeMessage(session);

                    // Set From: header field of the header.
                    message.setFrom(new InternetAddress(from));

                    // Set To: header field of the header.
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(user));
                    
                    // Set Subject: header field
                    message.setSubject("Results");

                    // Create the message part 
                    BodyPart messageBodyPart = new MimeBodyPart();

                    // Fill the message
                    messageBodyPart.setText("Query results");

                    // Create a multipar message
                    Multipart multipart = new MimeMultipart();

                    // Set text message part
                    multipart.addBodyPart(messageBodyPart);

                    // Part two is attachment
                    messageBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(output);
                    messageBodyPart.setDataHandler(new DataHandler(source));

                    messageBodyPart.setFileName(fileName + ".xlsx");
                    multipart.addBodyPart(messageBodyPart);

                    // Send the complete message parts
                    message.setContent(multipart);

                    // Send message
                    Transport.send(message);
                    System.out.println("Sent message successfully....");
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }


                //Use the created file output stream to write the excel file.
                wb.write(fileOut);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
//            InformationBox.errorBox(null, "Error while printing the results.", "Results");
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
