package edu.malone.avianmigration.utilities;

//package edu.malone.avianmigration;
//
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
//
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.zip.GZIPInputStream;
//import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.HttpClientBuilder;
//import edu.malone.avianmigration.Access;
//import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
//import org.apache.commons.io.IOUtils;
//
///**
// * As of right now, this class servers no purpose. It has been used to upload
// * data from the sources that Jason has found. Later, once all sources 
// * have been recognized, and all data has been inserted into the table. This
// * class will become a standalone update class. It will be set to go off every
// * few months. When it does, it will go out and see if there is any new 
// * data that can be used then automatically add that data.
// * 
// * For now, I will not add any comments since all of this will change to some
// * degree. If you want, whoever may be reading this, you can try and go through
// * and see what everything does and how it works. In here I:
// *      parse through HTML of a website for data
// *      Get files and decode them from an ftp server from NOAA
// *      stream a database from ebird to get any data by year.
// * 
// * @author cjedwards1
// */
//public class WriteImageType 
//{
//    public static String procDate;
//    public static HashMap<String, String> types = new HashMap();
//    public static HashMap<String, File> files = new HashMap();
//    public static HashMap<String, String> converstions = new HashMap();
//    public static FTPClient ftpClient = new FTPClient();
//    
//    static public void main(String args[]) throws Exception 
//    {
//        createJsonFile();
//        getEbirdData();
//        try
//        {            
//            ftpClient.connect("ftp.ncdc.noaa.gov", 21);
//            ftpClient.enterLocalPassiveMode();
//            ftpClient.login("anonymous", "anything");
//            ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
//            int code = ftpClient.getReplyCode();
//            System.out.println(code);
//            
//            setProcDate();
//            setTypes();
//            getFTPFile();
//        }
//        catch(Exception ex)
//        {
//            ex.printStackTrace();
//        }
//        finally
//        {
//            ftpClient.logout();
//            ftpClient.disconnect();
//        }
//            tempGetUsers();
//            getEbirdData();
//            getTaxonomy();
//            
//        System.out.println(getHTML("http://ebird.org/ws1.1/data/notable/geo/recent?detail=full&lng=-76.51&lat=42.46&dist=5&back=30&fmt=json", "ebird.txt"));
//      System.out.println(getHTML("http://www.ncdc.noaa.gov/cdo-web/api/v2/data?datasetid=GHCNDMS&locationid=CLIM:3901&startdate=2010-05-01&enddate=2010-05-01&limit=1000&units=metric", "File1.txt"));
//      System.out.println(getHTML("http://www.ncdc.noaa.gov/cdo-web/api/v2/datatypes?limit=1000", "File2.txt"));
//    }
//    
//    public static void createJsonFile()
//    {
//        Access access = null;
//        try
//        {
//            access = new Access();
//            Table name = access.getTable("SELECT TABLE_NAME FROM NSFCourter2016.information_schema.tables WHERE NOT TABLE_NAME='sysdiagrams' AND TABLE_SCHEMA = 'dbo' AND TABLE_TYPE= 'BASE TABLE'");
//            
//            JSONObject tables = new JSONObject();
//            while(name.next())
//            {
//                JSONArray columnNames = new JSONArray();
//                Table columns = access.getTable("SELECT TOP(1) * FROM NSFCourter2016.dbo." + name.getString(1));
//                
//                columns.next();
//                
//                for(int i = 1; i < columns.getColumnCount() + 1; i++)
//                {
//                    columnNames.put(columns.getColumnName(i));
//                }
//                
//                tables.put(name.getString(1), columnNames);
//            }
//            
//            System.out.println(tables.toString());
////            try (PrintStream out = new PrintStream(new FileOutputStream("Json table/columns list.txt"))) {
////                out.print(tables.toString());
////            }
//        }
//        catch(Exception ex)
//        {
//            ex.printStackTrace();
//        }
//    }
//    
//    public static void setTypes()
//    {
//        types.put("pdsi", "05");
//        types.put("pcpn", "01");
//        types.put("tmpc", "02");
//        types.put("tmax", "27");
//        types.put("tmin", "28");
//        types.put("sp01", "71");
//        types.put("sp03", "73");
//        types.put("sp06", "74");
//        types.put("sp12", "76");
//        types.put("sp24", "77");
//        types.put("norm-pcpn", "01");
//        types.put("norm-tmpc", "02");
//        types.put("norm-tmax", "27");
//        types.put("norm-tmin", "28");
//        
//        converstions.put("pdsi", "PDSI");
//        converstions.put("pcpn", "PRECIPITATION");
//        converstions.put("tmpc", "AVERAGE_TEMP");
//        converstions.put("tmax", "MAXIMUM_TEMP");
//        converstions.put("tmin", "MINIMUM_TEMP");
//        converstions.put("sp01", "ONE_SPI");
//        converstions.put("sp03", "THREE_SPI");
//        converstions.put("sp06", "SIX_SPI");
//        converstions.put("sp12", "TWELVE_SPI");
//        converstions.put("sp24", "TWENTY_FOUR_SPI");
//        converstions.put("norm-pcpn", "NORM_PRECIP");
//        converstions.put("norm-tmpc", "NORM_AVG_TEMP");
//        converstions.put("norm-tmax", "NORM_MAX_TEMP");
//        converstions.put("norm-tmin", "NORM_MIN_TEMP");
//        
//        try
//        {
//            for(String type : types.keySet())
//            {
//                String remoteFile = "/pub/data/cirs/climdiv/climdiv-" + type + "dv-v1.0.0-" + procDate;
//                File downloadFile = File.createTempFile("temp.txt", null);
//                try(OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile)))
//                {
//                    boolean success = ftpClient.retrieveFile(remoteFile, outputStream);
//                    
//                    if(success)
//                    {
//                        System.out.println("File " + type + " retrieved successfully.");
//                        files.put(type, downloadFile);
//                    }
//                    else
//                        System.out.println("File " + type + " failed.");
//
//                }
//                catch(Exception ex)
//                {
//                    ex.printStackTrace();
//                }
//            }
//        }
//        catch(Exception ex)
//        {
//            ex.printStackTrace();
//        }
//    }
//    
//    public static void setProcDate()
//    {
//        try 
//        {
//            String remoteFile1 = "/pub/data/cirs/climdiv/procdate.txt";
//            File downloadFile1 = File.createTempFile("temp.txt", null);
//            OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
//            boolean success = ftpClient.retrieveFile(remoteFile1, outputStream1);
//            outputStream1.close();
//            
// 
//            if(success)
//            {
//                System.out.println("Proccess date retrieved successfully.");
//                
//                try(BufferedReader reader = new BufferedReader(new FileReader(downloadFile1)))
//                {
//                    procDate = reader.readLine();
//                }
//                catch(Exception ex)
//                {
//                    ex.printStackTrace();
//                }
//            }
//            outputStream1.close();
//        }
//        catch(Exception ex)
//        {
//            ex.printStackTrace();
//        }
//    }
//  
//    public static void getFTPFile() throws SQLException, IOException
//    {
//        Access access = null;
//        try 
//        {
//            SimpleDateFormat sdf = new SimpleDateFormat(
//                "MM-dd-yyyy");
//            int day = 1;
//            Calendar cal = Calendar.getInstance();
//            
//            
//            access = new Access();
//            Table climDiv = access.getTable("SELECT CD_ID FROM [NSFCourter2016].[dbo].[CLIMATE_DIV]");
//            
//            for(int year = 2005; year <= 2016; year++)
//            {
//                for(int month = 11, monthCode = 0; month <= 88; month += 7, monthCode++)
//                {
//                    climDiv.setRow(0);
//                    
//                    while(climDiv.next())
//                    {
//                        HashMap<String, Double> data = new HashMap();
//                        String climDivCode = Integer.toString(climDiv.getInt("CD_ID"));
//                        
//                        climDivCode = climDivCode.length() == 3 ? "0" + climDivCode : climDivCode;
//                        for(String type : types.keySet())
//                        {
//                            if(files.get(type) != null)
//                            {
//                                try(BufferedReader reader = new BufferedReader(new FileReader(files.get(type))))
//                                {
//                                    String line;
//
//                                    String find;
//                                    if(type.contains("norm"))
//                                    {
//                                        Double temp = Math.floor(((year - 1901) / 10) - 1);
//                                        String normCode = Integer.toString(temp.intValue());
//
//                                        for(int i = normCode.length(); i < 4; i++)
//                                            normCode = "0" + normCode;
//
//                                        find = climDivCode + types.get(type) + normCode;
//                                    }
//                                    else
//                                        find = climDivCode + types.get(type) + year;
//
////                                    System.out.println(find + " Month: " + month + " Type: " + type);
//                                    while((line = reader.readLine()) != null)
//                                    {
//                                        if(line.substring(0, 10).equals(find))
//                                        {
////                                            System.out.println(line);
////                                            System.out.println("Data: " + line.substring(month, month + 6));
//                                            
//                                            if(!line.substring(month, month + 6).trim().equals("-9.99") && !line.substring(month, month + 6).trim().equals("-99.9"))
//                                                data.put(type, Double.valueOf(line.substring(month, month + 6).trim()));
//                                        }
//                                    }
//
////                                    System.out.println();
////                                    System.out.println();
////                                    System.out.println();
////                                    System.out.println();
////                                    System.out.println();
//                                }
//                                catch(Exception ex)
//                                {
//                                    ex.printStackTrace();
//                                }
//                            }
//                        }
//                    
//                        cal.set(Calendar.YEAR, year);
//                        cal.set(Calendar.MONTH, monthCode);
//                        cal.set(Calendar.DAY_OF_MONTH, day);
//
//                        java.sql.Date date = new java.sql.Date(cal.getTimeInMillis());
//                        System.out.println(sdf.format(date));
//                        String sqlString = "INSERT INTO [NSFCourter2016].[dbo].[CLIMATE] (DATE, CLIM_DIV_ID, ~) VALUES (\'" + sdf.format(date) + "\', " + climDivCode + ", !)";
//                        
//                        for(String dataPoint : data.keySet())
//                        {
//                            sqlString = sqlString.replaceAll("~", converstions.get(dataPoint) + ", ~").replaceAll("!", data.get(dataPoint) + ", !");
//                        }
//                        
//                        System.out.println(sqlString.replaceAll(", ~", "").replaceAll(", !", ""));
//                        access.execute(sqlString.replaceAll(", ~", "").replaceAll(", !", ""));
//                    }
//                }
//            }
//        }
//        catch(Exception ex)
//        {
//            ex.printStackTrace();
//        }
//    }
//    
//  //Used to parse the NOAA website for all the climate divisions' state names,
//  //region names, and overall code.
//  public static void getCDNames()
//  {
//      Access access = null;
//      try
//      {
//          access = new Access();
//          Document doc = Jsoup.connect("http://www.esrl.noaa.gov/psd/data/usclimdivs/data/map.html").get();
//          Elements elem = doc.getElementsByTag("tbody").get(1).parent().getElementsByTag("tr");
//          String state = "";
//          String stateCode = "";
//          String region = "";
//          String regionCode = "";
//          String code = "";
//          ArrayList<String> results = new ArrayList();
//          
//          for(Element element : elem)
//          {
//              for(Element innerElement : element.getElementsByTag("td"))
//              {
////                        System.out.println(innerElement.text());
//                  
//                  if(innerElement.text().contains("("))
//                  {
//                      state = "";
//                      StringTokenizer text = new StringTokenizer(innerElement.text(), "()");
//                      
//                      if(!innerElement.text().contains("-"))
//                      {
//                          while(text.hasMoreTokens())
//                          {
//                              if(text.countTokens() == 1)
//                              {
//                                  if(state.contains("Vermont"))
//                                  {
//                                      stateCode = "43";
//                                      text.nextToken();
//                                  }
//                                  else
//                                      stateCode = text.nextToken();
//                              }
//                              else
//                              {
//                                  state += text.nextToken().trim();
//                              }
//                          }
//                      }
//                      else
//                      {
//                          state += text.nextToken().trim();
//                          stateCode = text.nextToken().trim();
//                      }
//                  }
//                  else
//                  {
//                      String temp = innerElement.text();
//                      if(temp.length() > 1)
//                      {
//                          regionCode = temp.substring(0, 2).trim();
//                          region = temp.substring(2);
//                      }
//                  }
//                  
//              }
//              stateCode = stateCode.length() == 1 ? "0" + stateCode : stateCode;
//              regionCode = regionCode.length() == 1 ? "0" + regionCode : regionCode;
//              code = stateCode + regionCode;
//              results.add(code + ",\'" + state.substring(1) + "\',\'" + region.trim() + "\'");
//          }
//          
//          HashSet<String> hs  = new HashSet<>();
//          hs.addAll(results);
//          results.clear();
//          results.addAll(hs);
//          String[] sr = results.toArray(new String[0]);
//          Arrays.sort(sr);
//          
//          for(String result : sr)
//          {
//              System.out.println(result);
//              access.execute("INSERT INTO NSFCourter2016.dbo.CLIMATE_DIV  VALUES(" + result + ")");
//          }
//      }
//      catch (IOException ex)
//      {
//          Logger.getLogger(WriteImageType.class.getName()).log(Level.SEVERE, null, ex);
//      }
//      catch (SQLException ex)
//      {
//          Logger.getLogger(WriteImageType.class.getName()).log(Level.SEVERE, null, ex);
//      }
//  }
//  
//  public static String getHTML(String urlToRead, String fileName) throws Exception
//  {
//      HttpClient client = HttpClientBuilder.create().build();
//        HttpGet request = new HttpGet(urlToRead);
//
//        // add request header
////        request.addHeader(new BasicHeader("token", "zmLVPbXogSuUbsQLXLvgGGFhblAsRsKP"));
//        HttpResponse response = client.execute(request);
//
//        System.out.println("Response Code : "
//                        + response.getStatusLine().getStatusCode());
//
//        BufferedReader rd = new BufferedReader(
//                new InputStreamReader(response.getEntity().getContent()));
//
//        StringBuilder result = new StringBuilder();
//        String line;
//        while ((line = rd.readLine()) != null) {
//                result.append(line);
//        }
//        
//        if(result.charAt(0) == '[')
//        {
//            result.insert(0, "{ \"results\": ");
//            result.append("}");
//        }
//
//        System.out.println(result.toString());
//        JSONObject json = new JSONObject(result.toString());
//        JSONArray json2 = json.getJSONArray("results");
//        
//        for(int i = 0; i < json2.length(); i++)
//        {
//            System.out.println("Date: " + json2.getJSONObject(i).get("obsDt"));
//            System.out.println("Longitude: " + json2.getJSONObject(i).get("lng"));
//            System.out.println("Latitude: " + json2.getJSONObject(i).get("lat"));
//            System.out.println("State: " + json2.getJSONObject(i).get("subnational1Name"));
//            System.out.println("County: " + json2.getJSONObject(i).get("subnational2Name"));
//            System.out.println("Name: " + json2.getJSONObject(i).get("sciName"));
//            System.out.println("Distance out: 5 km");
//            
//            if(json2.getJSONObject(i).isNull("howMany"))
//                System.out.println("How many: X");
//            else
//                System.out.println("How many: " + json2.getJSONObject(i).get("howMany"));
//            
//            if(json2.getJSONObject(i).isNull("userDisplayName"))
//                System.out.println("userDisplayName: X");
//            else
//                System.out.println("userDisplayName: " + json2.getJSONObject(i).get("userDisplayName"));
//            
//            if(json2.getJSONObject(i).isNull("obsID"))
//                System.out.println("Observer ID: X");
//            else
//                System.out.println("Observer ID: " + json2.getJSONObject(i).get("obsID"));
//            
//            if(json2.getJSONObject(i).isNull("subID"))
//                System.out.println("submision IS: X");
//            else
//            {
//                System.out.println("Submission ID: " + json2.getJSONObject(i).getString("subID"));
//                System.out.println("http://ebird.org/ebird/view/checklist?subID=" + json2.getJSONObject(i).getString("subID"));
//                Document doc = Jsoup.connect("http://ebird.org/ebird/view/checklist?subID=" + json2.getJSONObject(i).getString("subID")).get();
//                Elements elem = doc.getElementsByTag("dd");
//                
//                for (Element element : elem)
//                {
//                    System.out.println(element.parent().getElementsByTag("dt").text() + element.text());
//                }
//                
//                System.out.println();
//            }
//        }
//        
//        try (FileWriter file = new FileWriter(fileName)) 
//        {
//                file.write(json.toString(1));
//        }
//        
////        ArrayList<String> data = new ArrayList();
////        for(int i = 0; i < json2.length(); i++)
////        {
////            data.add(json2.getJSONObject(i).getString("datatype"));
////        }
////        
////        HashSet<String> sorted = new HashSet();
////        sorted.addAll(data);
////        data.clear();
////        data.addAll(sorted);
////        Collections.sort(data);
////        
////        for(String item : data)
////            System.out.println(item);
//        
////        for(int i = 0; i < json2.length(); i++)
////        {
////            System.out.println(json2.get(i));
////        }
//        
//        return result.toString();
//   }
//  
//    public static void getEbirdData() 
//    {
//        Access access = null;
//        try
//        {
//            access = new Access();
//            HttpClient client = HttpClientBuilder.create().build();
//            HttpGet request = new HttpGet("http://ebirddata.ornith.cornell.edu/downloads/erd/ebird_all_species/ebird_us48_data_grouped_by_year_v2014.tar.gz");
//            
//            HttpResponse response = client.execute(request);
//            System.out.println(response.getEntity().getContent());
//            
//            byte[] buffer = new byte[1024];
//            
//            TarArchiveInputStream ta = new TarArchiveInputStream(new FileInputStream(new File("C:\\NSF Projects\\ebird_us48_data_grouped_by_year_v2014_SAMPLE.tar")));
//            
//            TarArchiveEntry entry = null;
//            String ind_files;
//            int offset;
//            
//            FileOutputStream fos = null;
//            while((entry = ta.getNextTarEntry()) != null)
//            {
//                ind_files = entry.getName();
//                System.out.println(ind_files);
//                byte[] content = new byte[(int) entry.getSize()];
//                offset = 0;
//                
//                System.out.println("File name is: " + ind_files);
//                System.out.println("Size is: " + entry.getSize());
//                System.out.println("Byte array length: " + content.length);
//                
//                ta.read(content, offset, content.length - offset);
//                
//                fos = new FileOutputStream(new File("C:\\NSF Projects\\myFILES"));
//                
//                String ind_files_location = entry.getFile().getAbsolutePath();
//                System.out.println(ind_files_location);
//                
//                IOUtils.write(content, fos);
//                
//                fos.close();
//                
//            }
//            
//            ta.close();
//           
//            
//            
//            
//            System.out.println("Done");
//        } 
//        catch(IOException | UnsupportedOperationException e)
//        {
//        }
//    }
//
//}
//           
//            int startYear = 2012;
//            int year;
//            int month;
//            int day;
//            int hour;
//            int min = 0;
////            String sqlStringInsert;
//            PointInPolygon pip = new PointInPolygon();
//            
//            for(;startYear <= 2014; startYear++)
//            {
//                System.out.println("C:\\Users\\cjedwards1\\Downloads\\" + startYear + "\\checklists.csv");
//                Reader in = new FileReader("C:\\Users\\cjedwards1\\Downloads\\" + startYear + "\\checklists.csv");
//                Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
//                String[] birds = null;
//                try(BufferedReader buf = new BufferedReader(new FileReader("C:\\Users\\cjedwards1\\Downloads\\" + startYear + "\\checklists.csv")))
//                {
//                    birds = buf.readLine().split(",");
//                }
//                catch(Exception ex)
//                {
//                    ex.printStackTrace();
//                }
//                
//                int numRecord = 0;
//                for (CSVRecord record : records) 
//                {
//                    System.out.println(numRecord);
//                    numRecord++;
////                    sqlStringInsert = "INSERT INTO [NSFCourter2016].[dbo].[OBSERVATIONS] (~) VALUES (!)";
//                    StringBuilder varBuilder = new StringBuilder("INSERT INTO [NSFCourter2016].[dbo].[OBSERVATIONS] (");
//                    StringBuilder dataBuilder = new StringBuilder("VALUES (");
//                    try
//                    { 
//                        if(record.get("PRIMARY_CHECKLIST_FLAG").equals("1"))
//                        {
//                            year = Integer.parseInt(record.get("YEAR"));
//                            month = Integer.parseInt(record.get("MONTH"));
//                            day = Integer.parseInt(record.get("DAY"));
//                            double hm = Double.parseDouble(record.get("TIME"));
//                            int tempMin = (int) Math.floor(hm);
//                            hour = tempMin;
//                            min = Math.round((float) (hm - tempMin) * 60);
//
//
////                                System.out.println(Integer.parseInt(record.get("COUNT_TYPE").substring(1)));
////                                System.out.println(Integer.parseInt(record.get("OBSERVER_ID").substring(3)));
////                                System.out.println(csvreader.PointInPolygon.getId(Double.parseDouble(record.get("LATITUDE")), Double.parseDouble(record.get("LONGITUDE"))));
////                                System.out.println(record.get("STATE_PROVINCE"));
//
////                            sqlStringInsert = sqlStringInsert.replaceAll("~", "SUBMISSION_ID, ~").replaceAll("!", record.get("SAMPLING_EVENT_ID").replaceAll("S", "") + ", !");
//                            varBuilder.append("SUBMISSION_ID,");
//                            dataBuilder.append(record.get("SAMPLING_EVENT_ID").replaceAll("S", "")).append(",");
//                            
////                            sqlStringInsert = sqlStringInsert.replaceAll("~", "GROUP_ID, ~").replaceAll("!", (record.get("GROUP_ID").equals("?") ? "null" : record.get("GROUP_ID").replaceAll("G", "")) + ", !");
//                            varBuilder.append("GROUP_ID,");
//                            dataBuilder.append(record.get("GROUP_ID").equals("?") ? "null" : record.get("GROUP_ID").replaceAll("G", "")).append(",");
//                            
////                            sqlStringInsert = sqlStringInsert.replaceAll("~", "EFFORT_DISTANCE, ~").replaceAll("!", (record.get("EFFORT_DISTANCE_KM").equals("0") ? "null" : record.get("EFFORT_DISTANCE_KM")) + ", !");
//                            varBuilder.append("EFFORT_DISTANCE,");
//                            dataBuilder.append(record.get("EFFORT_DISTANCE_KM").equals("0") ? "null" : record.get("EFFORT_DISTANCE_KM")).append(",");
//                            
////                            sqlStringInsert = sqlStringInsert.replaceAll("~", "EFFORT_MINUTES, ~").replaceAll("!", (Double.parseDouble(record.get("EFFORT_HRS")) * 60) + ", !");
//                            varBuilder.append("EFFORT_MINUTES,");
//                            dataBuilder.append(Double.parseDouble(record.get("EFFORT_HRS")) * 60).append(",");
//
//                            SimpleDateFormat sdf = new SimpleDateFormat(
//                                "yyyy-MM-dd hh:mm aaa");
//                            Calendar cal = Calendar.getInstance();
//                            cal.set(Calendar.YEAR, year);
////                                cal.set(Calendar.MONTH, month);
//                            cal.set(Calendar.DAY_OF_YEAR, day);
//                            cal.set(Calendar.HOUR_OF_DAY, hour);
//                            cal.set(Calendar.MINUTE, min);
//
//                            java.sql.Date date = new java.sql.Date(cal.getTimeInMillis());
//
//                            cal.set(Calendar.YEAR, year);
////                                cal.set(Calendar.MONTH, month);
//                            cal.set(Calendar.DAY_OF_MONTH, 1);
//                            java.sql.Date cDDate = new java.sql.Date(cal.getTimeInMillis());
//                            SimpleDateFormat cDsdf = new SimpleDateFormat(
//                                "yyyy-MM-dd");
//
////                                System.out.println(sdf.format(date));
////                                System.out.println(cDsdf.format(cDDate));
////                            sqlStringInsert = sqlStringInsert.replaceAll("~", "RECORDED, ~").replaceAll("!", "'" + sdf.format(date) + "', !");
//                            varBuilder.append("RECORDED,");
//                            dataBuilder.append("'").append(sdf.format(date)).append("',");
//                            
////                                System.out.println("SELECT APPID FROM CLIMATE WHERE DATE = '" + sdf.format(cDDate) + "' AND CLIM_DIV_ID = " + csvreader.PointInPolygon.getId(Double.parseDouble(record.get("LATITUDE")), Double.parseDouble(record.get("LONGITUDE"))));
//                            Table temp = access.getTable("SELECT APPID FROM CLIMATE WHERE DATE = '" + cDsdf.format(cDDate) + "' AND CLIM_DIV_ID = " + pip.getId(Double.parseDouble(record.get("LATITUDE")), Double.parseDouble(record.get("LONGITUDE"))));
//
//                            if(temp.next())
//                            {
////                                sqlStringInsert = sqlStringInsert.replaceAll("~", "CLIMATE_ID, ~").replaceAll("!", temp.getInt("APPID") + ", !");
//                                varBuilder.append("CLIMATE_ID,");
//                                dataBuilder.append(temp.getInt("APPID")).append(",");
//                                
////                                sqlStringInsert = sqlStringInsert.replaceAll("~", "OBSERVERS_ID, ~").replaceAll("!", record.get("OBSERVER_ID").replaceAll("obs", "") + ", !");
//                                varBuilder.append("OBSERVERS_ID,");
//                                dataBuilder.append(record.get("OBSERVER_ID").replaceAll("obs", "")).append(",");
//                                
////                                sqlStringInsert = sqlStringInsert.replaceAll("~", "PARTY_SIZE, ~").replaceAll("!", (record.get("NUMBER_OBSERVERS").equals("?") ? "null" : record.get("NUMBER_OBSERVERS")) + ", !");
//                                varBuilder.append("PARTY_SIZE,");
//                                dataBuilder.append(record.get("NUMBER_OBSERVERS").equals("?") ? "null" : record.get("NUMBER_OBSERVERS")).append(",");
//                                
//                                //sqlStringInsert = sqlStringInsert.replaceAll("~", "OBSERVATION_TYPE_ID, ~").replaceAll("!", record.get("COUNT_TYPE").substring(1) + ", !");
//                                varBuilder.append("OBSERVATION_TYPE_ID,");
//                                dataBuilder.append(record.get("COUNT_TYPE").substring(1)).append(",");
//                                
//                                //sqlStringInsert = sqlStringInsert.replaceAll("~", "EFFORT_AREA, ~").replaceAll("!", (record.get("EFFORT_AREA_HA").equals("0") ? "null" : record.get("EFFORT_AREA_HA")) + ", !");
//                                varBuilder.append("EFFORT_AREA,");
//                                dataBuilder.append(record.get("EFFORT_AREA_HA").equals("0") ? "null" : record.get("EFFORT_AREA_HA")).append(",");
//                                
//                                //sqlStringInsert = sqlStringInsert.replaceAll("~", "LAT, ~").replaceAll("!", record.get("LATITUDE") + ", !");
//                                varBuilder.append("LAT,");
//                                dataBuilder.append(record.get("LATITUDE")).append(",");
//                                
//                                //sqlStringInsert = sqlStringInsert.replaceAll("~", "LONG, ~").replaceAll("!", record.get("LONGITUDE") + ", !");
//                                varBuilder.append("LONG");
//                                dataBuilder.append(record.get("LONGITUDE"));
//                                
//                                //sqlStringInsert = sqlStringInsert.replaceAll(", ~", "").replaceAll(", !", "");
//                                varBuilder.append(")");
//                                dataBuilder.append(")");
////                                System.out.println(varBuilder.toString() + dataBuilder.toString());
//                                
////                                    System.out.println(sqlStringInsert);
////                                    System.out.println(record.get("EFFORT_DISTANCE_KM"));
//
//                                if(!access.getTable("SELECT * FROM [NSFCourter2016].[dbo].[OBSERVATIONS] WHERE SUBMISSION_ID=" + record.get("SAMPLING_EVENT_ID").replaceAll("S", "")).next() && !record.get("COUNT_TYPE").substring(1).equals("62"))
//                                {
//                                    access.execute(varBuilder.toString() + dataBuilder.toString());
//                                }
//
//                                for(int i = 19; i < birds.length; i++)
//                                {
////                                        System.out.println(birds[i] + ": " +  record.get(birds[i]));
////                                        System.out.println(birdTaxonomy.getDouble("TAXONOMY"));
//
//                                    String amount = record.get(i);
//                                    amount = amount.equals("X") ? "-1" : amount;
//                                    int number = Integer.parseInt(amount);
//                                    if(!amount.equals("0"))
//                                    {
//                                        Table birdTaxonomy = access.getTable("SELECT TAXONOMY FROM [NSFCourter2016].[dbo].[BIRDS] WHERE SCI_NAME='" + birds[i].replaceAll("'", "''") + "'");
//                                        birdTaxonomy.next();
//                                        
//                                        if(!access.getTable("SELECT * FROM [NSFCourter2016].[dbo].[OBSERVATION_BIRD] WHERE SUBMISSION_ID = " + record.get("SAMPLING_EVENT_ID").replaceAll("S", "") + " AND BIRD_TAXONOMY = " + birdTaxonomy.getDouble("TAXONOMY")).next() && access.getTable("SELECT * FROM [NSFCourter2016].[dbo].[OBSERVATIONS] WHERE SUBMISSION_ID=" + record.get("SAMPLING_EVENT_ID").replaceAll("S", "")).next())
//                                        {
////                                                System.out.println("INSERT INTO [NSFCourter2016].[dbo].[OBSERVATION_BIRD] (SUBMISSION_ID, BIRD_TAXONOMY, NUM_BIRDS) VALUES (" + record.get("SAMPLING_EVENT_ID").substring(1) + "," + birdTaxonomy.getDouble("TAXONOMY") + "," + number + ")");
//                                            access.execute("INSERT INTO [NSFCourter2016].[dbo].[OBSERVATION_BIRD] (SUBMISSION_ID, BIRD_TAXONOMY, NUM_BIRDS) VALUES (" + record.get("SAMPLING_EVENT_ID").replaceAll("S", "") + "," + birdTaxonomy.getDouble("TAXONOMY") + "," + number + ")");
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        else
//                        {
//                            if(!record.get("GROUP_ID").equals("?"))
//                            {
//                                StringBuilder tempBuilder = new StringBuilder("INSERT INTO [NSFCourter2016].[dbo].[GROUPS] (GROUP_ID, OBSERVER_ID) VALUES (");
////                                String tempInsert = "INSERT INTO [NSFCourter2016].[dbo].[GROUPS] (~) VALUES (!)";
////                                tempInsert = tempInsert.replaceAll("~", "GROUP_ID, ~").replaceAll("!", (record.get("GROUP_ID").equals("?") ? "null" : record.get("GROUP_ID").replaceAll("G", "")) + ", !");
////                                tempInsert = tempInsert.replaceAll("~", "OBSERVER_ID, ~").replaceAll("!", record.get("OBSERVER_ID").replaceAll("obs", "") + ", !");
////                                tempInsert = tempInsert.replaceAll(", ~", "").replaceAll(", !", "");
//                                tempBuilder.append(record.get("GROUP_ID").equals("?") ? "null" : record.get("GROUP_ID").replaceAll("G", "")).append(",");
//                                tempBuilder.append(record.get("OBSERVER_ID").replaceAll("obs", ""));
//                                tempBuilder.append(")");
//                                
//                                if(access.getTable("SELECT * FROM [NSFCourter2016].[dbo].[OBSERVATIONS] WHERE GROUP_ID=" + record.get("GROUP_ID").replaceAll("G", "")).next() && !access.getTable("SELECT * FROM [NSFCourter2016].[dbo].[GROUPS] WHERE GROUP_ID=" + record.get("GROUP_ID").replaceAll("G", "")).next())
//                                {
//                                    access.execute(tempBuilder.toString());
//                                }
//                            }
//                        }
//                    }
//                     catch(Exception ex)
//                     {
//                         System.out.println(varBuilder.toString() + dataBuilder.toString());
//                         ex.printStackTrace();
//                     }
//                }
//            }
//            
////            for(TarArchiveEntry currentEntry = ta.getNextTarEntry(); currentEntry != null; currentEntry = ta.getNextTarEntry())
////            {
////                boolean found = false;
////                while(startYear != Calendar.getInstance().get(Calendar.YEAR) + 1)
////                {
////                    if(currentEntry.getName().contains(Integer.toString(startYear)))
////                    {
////                        found = true;
////                        break;
////                    }
////                    currentEntry = ta.getNextTarEntry();
////                }
////                
////                if(found)
////                {
////                    while(!currentEntry.getName().contains("checklists.csv"))
////                    {
////                        currentEntry = ta.getNextTarEntry();
////                    }
////
////                    System.out.println(currentEntry.getName());
////                    try(BufferedReader br = new BufferedReader(new InputStreamReader(ta)))
////                    {
////                        String line = br.readLine();
////                        String[] keys = line.split(",");
////                        HashMap<Integer, String> fileMap = new HashMap();
////                        HashMap<String, String> hashedValues = new HashMap();
////
////                        int keyPlace = 0;
////                        for(String key : keys)
////                        {
////                            fileMap.put(keyPlace++, key);
////                        }
////
////                        while ((line = br.readLine()) != null) 
////                        {
////                            String[] values = line.split(",");
////
////                            Set<Integer> keySet = fileMap.keySet();
////
////                            for(Integer key : keySet.toArray(new Integer[0]))
////                            {
////                                String keyMap = fileMap.get(key);
////
////                                hashedValues.put(keyMap, values[key]);
////                            }
////                            
////                            if(hashedValues.get("PRIMARY_CHECKLIST_FLAG").equals("1"))
////                            {
////                                System.out.println("http://ebird.org/ebird/view/checklist?subID=" + hashedValues.get("SAMPLING_EVENT_ID"));
////                                Document doc = Jsoup.connect("http://ebird.org/ebird/view/checklist?subID=" + hashedValues.get("SAMPLING_EVENT_ID")).get();
////                                Element elem = doc.getElementsByAttributeValue("class", "def-list").last();
////                                System.out.println(elem.getElementsByTag("Strong").text());
//////                                for (Element element : elem)
//////                                {
//////                                    System.out.println(element.getElementsByTag("strong").text() + element.text());
//////                                }
////                                
//////                                year = Integer.parseInt(hashedValues.get("YEAR"));
//////                                month = Integer.parseInt(hashedValues.get("MONTH"));
//////                                day = Integer.parseInt(hashedValues.get("DAY"));
//////                                String[] hourMin = hashedValues.get("TIME").split(".");
//////                                
//////                                if(hourMin.length == 2)
//////                                {
//////                                    hour = Integer.parseInt(hourMin[0]);
//////                                    min = Math.round((Float.parseFloat(hourMin[1]) / 10) * 60);
//////                                }
//////                                else
//////                                {
//////                                    hour = Integer.parseInt(hashedValues.get("TIME"));
//////                                    min = 0;
//////                                }
//////                                
//////                                System.out.println(Integer.parseInt(hashedValues.get("COUNT_TYPE").substring(1)));
////                                System.out.println(Integer.parseInt(hashedValues.get("OBSERVER_ID").substring(3)));
//////                                System.out.println("LATITUDE: " + Double.parseDouble(hashedValues.get("LATITUDE")) + " LONGITUDE: " + Double.parseDouble(hashedValues.get("LONGITUDE")));
//////                                System.out.println(csvreader.PointInPolygon.getId(Double.parseDouble(hashedValues.get("LATITUDE")), Double.parseDouble(hashedValues.get("LONGITUDE"))));
//////                                System.out.println(hashedValues.get("STATE_PROVINCE"));
//////                                
//////                                for(int i = 19; i < hashedValues.size(); i++)
//////                                {
//////                                    System.out.println(fileMap.get(i) + ": " + hashedValues.get(fileMap.get(i)));
//////                                    StringTokenizer birds = new StringTokenizer(fileMap.get(i), "_");
//////                                    String first = birds.nextToken();
//////                                    String second = birds.nextToken();
//////                                    Table birdTaxonomy = access.getTable("SELECT M.TAXONOMY FROM (SELECT * FROM BIRDS WHERE SPECIES_NAME = '" + second + "') AS M, (SELECT * FROM BIRD_GENUS WHERE GENUS = '" + first + "') AS N WHERE M.GENUS_ID = N.APPID");
//////                                    birdTaxonomy.next();
//////                                    System.out.println(birdTaxonomy.getDouble("TAXONOMY"));
//////                                }
////                            }
////                        }
////                        
////                    }
////                    catch(Exception ex)
////                    {
////                        ex.printStackTrace();
////                    }
////                }
////                
////                startYear++;
////            }
//        }
//        catch (Exception e) 
//        {
//            e.printStackTrace();
//        }
//    }
//    
//    public static void getUsers()
//    {
//        Access access = null;
//        try
//        {
//            access = new Access();
//            HttpClient client = HttpClientBuilder.create().build();
//            HttpGet request = new HttpGet("http://ebirddata.ornith.cornell.edu/downloads/erd/ebird_all_species/ebird_us48_data_grouped_by_year_v2014.tar.gz");
//            
//            HttpResponse response = client.execute(request);
//            
//            TarArchiveInputStream ta = new TarArchiveInputStream(new GZIPInputStream(response.getEntity().getContent()));
//           
//            int startYear = 2005;
//            String sqlStringInsert;
//            String sqlStringSelect;
//            
//            for(TarArchiveEntry currentEntry = ta.getNextTarEntry(); currentEntry != null; currentEntry = ta.getNextTarEntry())
//            {
//                boolean found = false;
//                while(startYear != Calendar.getInstance().get(Calendar.YEAR) + 1)
//                {
//                    if(currentEntry.getName().contains(Integer.toString(startYear)))
//                    {
//                        found = true;
//                        break;
//                    }
//                    currentEntry = ta.getNextTarEntry();
//                }
//                
//                if(found)
//                {
//                    while(!currentEntry.getName().contains("checklists.csv"))
//                    {
//                        currentEntry = ta.getNextTarEntry();
//                    }
//
//                    System.out.println(currentEntry.getName());
//                    try(BufferedReader br = new BufferedReader(new InputStreamReader(ta)))
//                    {
//                        String line = br.readLine();
//                        String[] keys = line.split(",");
//                        HashMap<Integer, String> fileMap = new HashMap();
//                        HashMap<String, String> hashedValues = new HashMap();
//
//                        int keyPlace = 0;
//                        for(String key : keys)
//                        {
//                            fileMap.put(keyPlace++, key);
//                        }
//
//                        while ((line = br.readLine()) != null) 
//                        {
//                            String[] values = line.split(",");
//
//                            Set<Integer> keySet = fileMap.keySet();
//
//                            for(Integer key : keySet.toArray(new Integer[0]))
//                            {
//                                String keyMap = fileMap.get(key);
//
//                                hashedValues.put(keyMap, values[key]);
//                            }
//                            
//                            if(hashedValues.get("PRIMARY_CHECKLIST_FLAG").equals("1"))
//                            {
//                                sqlStringInsert = "INSERT INTO [NSFCourter2016].[dbo].[OBSERVERS] (OBSERVER_ID, NAME) VALUES (";
//                                sqlStringSelect = "SELECT * FROM [NSFCourter2016].[dbo].[OBSERVERS] WHERE OBSERVER_ID=";
//                                try
//                                {
//                                    Document doc = Jsoup.connect("http://ebird.org/ebird/view/checklist?subID=" + hashedValues.get("SAMPLING_EVENT_ID")).get();
//                                    Element elem = doc.getElementsByAttributeValue("class", "def-list").last();
//                                    String user = elem.getElementsByTag("Strong").text().replaceAll("'", "''");
//                                    sqlStringInsert += hashedValues.get("OBSERVER_ID").substring(3) + ",'" + user + "')";
//                                    sqlStringSelect += hashedValues.get("OBSERVER_ID").substring(3);
//
//                                    if(!access.getTable(sqlStringSelect).next())
//                                    {
//                                        access.execute(sqlStringInsert);
//                                    }
//                                }
//                                 catch(Exception ex)
//                                 {
//                                     System.out.println(sqlStringInsert);
//                                     ex.printStackTrace();
//                                 }
////                                System.out.println(elem.getElementsByTag("Strong").text());
////                                System.out.println(Integer.parseInt(hashedValues.get("OBSERVER_ID").substring(3)));
//                            }
//                        }
//                        
//                    }
//                    catch(Exception ex)
//                    {
//                        ex.printStackTrace();
//                    }
//                }
//                
//                startYear++;
//            }
//        }
//        catch (Exception e) 
//        {
//            e.printStackTrace();
//        }
//    }
//    
//    public static void tempGetUsers()
//    {
//        Access access = null;
//        try
//        {
//            access = new Access();
////            HttpClient client = HttpClientBuilder.create().build();
////            HttpGet request = new HttpGet("http://ebirddata.ornith.cornell.edu/downloads/erd/ebird_all_species/ebird_us48_data_grouped_by_year_v2014.tar.gz");
////            
////            HttpResponse response = client.execute(request);
////            
////            TarArchiveInputStream ta = new TarArchiveInputStream(new GZIPInputStream(response.getEntity().getContent()));
//           
//            int startYear = 2014;
//            String sqlStringInsert;
//            String sqlStringSelect;
//            
//            for(;startYear <= 2014; startYear++)
//            {
//                System.out.println("C:\\Users\\cjedwards1\\Downloads\\" + startYear + "\\checklists.csv");
//                try(Reader in = new FileReader("C:\\Users\\cjedwards1\\Downloads\\" + startYear + "\\checklists.csv"))
//                {
//                    Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
//                    for (CSVRecord record : records) 
//                    {
//                        sqlStringInsert = "INSERT INTO [NSFCourter2016].[dbo].[OBSERVERS] (OBSERVER_ID, NAME) VALUES (";
//                        sqlStringSelect = "SELECT * FROM [NSFCourter2016].[dbo].[OBSERVERS] WHERE OBSERVER_ID=";
//                        try
//                        { 
//                            sqlStringSelect += record.get("OBSERVER_ID").replaceAll("obs", "");
//                            
//
//                            if(!access.getTable(sqlStringSelect).next())
//                            {
//                                Document doc = Jsoup.connect("http://ebird.org/ebird/view/checklist?subID=" + record.get("SAMPLING_EVENT_ID")).get();
//                                Element elem = doc.getElementsByAttributeValue("class", "def-list").last();
//                                String user = elem.getElementsByTag("Strong").text().replaceAll("'", "''");
//                                sqlStringInsert += record.get("OBSERVER_ID").replaceAll("obs", "") + ",'" + user + "')";
//                                access.execute(sqlStringInsert);
//                            }
////                            System.out.println(record.get("SAMPLING_EVENT_ID"));
//    //                                System.out.println(sqlStringInsert);
//    //                                System.out.println(sqlStringSelect);
//
//
//                        }
//                         catch(Exception ex)
//                         {
////                             System.out.println(sqlStringInsert);
////                             ex.printStackTrace();
//                         }
//                    }
//                }
//                catch(Exception ex)
//                {
//                    ex.printStackTrace();
//                }
//            }
//        }
//        catch (Exception e) 
//        {
//            e.printStackTrace();
//        }
//    }
//
//    public static void getTaxonomy()
//    {
//        Access access = null;
//        try
//        {
//            access = new Access();
//            HashMap<String, String> matches = new HashMap();
//            matches.put("PRIMARY_COM_NAME", "PRIM_NAME");
//            matches.put("ORDER_NAME", "ORDER_NAME");
//            matches.put("TAXON_ORDER", "TAXONOMY");
//            matches.put("SCI_NAME", "SCI_NAME");
//            matches.put("FAMILY_NAME", "FAMILY_NAME");
//
//            try(BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\cjedwards1\\Downloads\\doc\\taxonomy.csv")))
//            {
//                String line = br.readLine();
//                String[] keys = line.split(",");
//                HashMap<Integer, String> fileMap = new HashMap();
//
//                int keyPlace = 0;
//                for(String key : keys)
//                {
//                    boolean keyFound = false;
//                    for(String variableName : matches.keySet())
//                    {
//                            if(key.equals(variableName))
//                            {
//                                fileMap.put(keyPlace++, key);
//                                keyFound = true;
//                                break;
//                            }
//                        
//                    }
//
//                    if(!keyFound)
//                        keyPlace++;
//                }
//
//                while ((line = br.readLine()) != null) 
//                {
//                    String sqlStringBird = "INSERT INTO [NSFCourter2016].[dbo].[BIRDS] (~) VALUES (!)";
//                    String sqlStringOrderInsert = "INSERT INTO [NSFCourter2016].[dbo].[BIRD_ORDER] (~) VALUES (!)";
//                    String sqlStringOrderSelect = "SELECT * FROM [NSFCourter2016].[dbo].[BIRD_ORDER] WHERE !";
//                    String sqlStringFamilyInsert = "INSERT INTO [NSFCourter2016].[dbo].[BIRD_FAMILY] (~) VALUES (!)";
//                    String sqlStringFamilySelect = "SELECT * FROM [NSFCourter2016].[dbo].[BIRD_FAMILY] WHERE !";
//                    String[] values = line.split(",");
//                    Set<Integer> keySet = fileMap.keySet();
//
//                    for(Integer key : keySet.toArray(new Integer[0]))
//                    {
//                        String keyMap = fileMap.get(key);
//
//                        if(keyMap != null)
//                        {
//                            if(keyMap.equals("ORDER_NAME") || keyMap.equals("FAMILY_NAME"))
//                            {
//                                StringTokenizer type = new StringTokenizer(values[key], "()");
//                                
//                                if(type.countTokens() > 1)
//                                {
//                                    String familyName = type.nextToken().trim();
//                                    String typeName = type.nextToken().trim();
//                                    
//                                    sqlStringFamilyInsert = sqlStringFamilyInsert.replaceAll("~", matches.get(keyMap) + ", ~").replaceAll("!", "'" + familyName + "', !");
//                                    sqlStringFamilySelect = sqlStringFamilySelect.replaceAll("!", matches.get(keyMap) + "=" + "'" + familyName + "' AND !");
//                                    
//                                    sqlStringFamilyInsert = sqlStringFamilyInsert.replaceAll("~", "TYPE, ~").replaceAll("!", "'" + typeName + "', !");
//                                    sqlStringFamilySelect = sqlStringFamilySelect.replaceAll("!", "TYPE=" + "'" + typeName + "' AND !");
//                                }
//                                else
//                                {
//                                    sqlStringOrderInsert = sqlStringOrderInsert.replaceAll("~", matches.get(keyMap) + ", ~").replaceAll("!", "'" + values[key] + "', !");
//                                    sqlStringOrderSelect = sqlStringOrderSelect.replaceAll("!", matches.get(keyMap) + "=" + "'" + values[key] + "' AND !");
//                                }
//                            }
//                            else
//                            {
//                                try
//                                {
//                                    Double.valueOf(values[key]);
//
//
//                                    sqlStringBird = sqlStringBird.replaceAll("~", matches.get(keyMap) + ", ~").replaceAll("!", values[key] + ", !");
//        //                            System.out.println("Key: " + keyMap + "\tValue: " + values[key]);
//                                }
//                                catch(Exception ex)
//                                {
//                                    sqlStringBird = sqlStringBird.replaceAll("~", matches.get(keyMap) + ", ~").replaceAll("!", "\'" + values[key].replaceAll("'", "''") + "\', !");
//        //                            System.out.println("Key: " + keyMap + "\tValue: " + values[key]);
//                                }
//                            }
//                        }
//                    }
//                    
//                    System.out.println(sqlStringBird.replaceAll(", ~", "").replaceAll(", !", ""));
//                    System.out.println(sqlStringFamilySelect.replaceAll("AND !", ""));
//                    System.out.println(sqlStringFamilyInsert.replaceAll(", ~", "").replaceAll(", !", ""));
//                    System.out.println(sqlStringOrderSelect.replaceAll("AND !", ""));
//                    System.out.println(sqlStringOrderInsert.replaceAll(", ~", "").replaceAll(", !", ""));
//                    System.out.println();
//                    
//                    if(!access.getTable(sqlStringFamilySelect.replaceAll("AND !", "")).next())
//                    {
//                        Table order = access.getTable(sqlStringOrderSelect.replaceAll("AND !", ""));
//                        if(!order.next())
//                        {
//                            access.execute(sqlStringOrderInsert.replaceAll(", ~", "").replaceAll(", !", ""));
//                            order = access.getTable(sqlStringOrderSelect.replaceAll("AND !", ""));
//                            order.next();
//                        }
//                        access.execute(sqlStringFamilyInsert.replaceAll("~", "ORDER_ID").replaceAll("!", Integer.toString(order.getInt("APPID"))));
//                    }
//                    
//                    Table idTableGen = access.getTable(sqlStringFamilySelect.replaceAll("AND !", ""));
//                    idTableGen.next();
//                    int idG = idTableGen.getInt("APPID");
//                    
//                    access.execute(sqlStringBird.replaceAll("~", "FAMILY_ID").replaceAll("!", Integer.toString(idG)));
//                }
//            }
//            catch(Exception ex)
//            {
//                ex.printStackTrace();
//            }
//        }
//        catch (Exception e) 
//        {
//            e.printStackTrace();
//        }
//    }
//}
