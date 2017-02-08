/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ttlchecher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author cjedwards1
 */
public class TTLChecker
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnsupportedEncodingException, ParseException, FileNotFoundException, IOException
    {
        // TODO code application logic here
        File filesDirectory = new File("..//..//files_for_download");
        File[] files = filesDirectory.listFiles();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        
        //String emailsSent = new Scanner(new File("..//sentEmails.txt")).useDelimiter("\\Z").next();
            
        if(files != null)
        {
            for(File file : files)
            {
                if(file.isFile())
                {
                    
                    //TimeStamp of file
                    String time = file.getName().substring(file.getName().length() - 19).substring(0, 14);
                    
                    //Turn the timestamp into a date.
                    Date fileDate = dateFormat.parse(time);
                    
                    //Set the date to be 7 days later to check if it is expired or not.
                    fileDate.setTime(fileDate.getTime() + TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS));
                    
                    //Is the new timestamp come before or after the current timestamp?
                    if(fileDate.before(dateFormat.parse(dateFormat.format(new Timestamp(System.currentTimeMillis())))))
                        file.delete();
                    
                    //Now check if the file is going to expire tomorrow in order to email the user to warn them.
                    fileDate.setTime(fileDate.getTime() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
                    
                }
            }
        }
    }
    
}
