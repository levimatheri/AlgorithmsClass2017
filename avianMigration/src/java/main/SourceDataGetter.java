///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
package main;
//
//
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
//
//
///**
// *
// * @author lmmuriuki1
// * 
// * This class will be used to keep track of the sources for the project.
// * All code to get data from all sources for the project will be contained 
// * in this class.
// */
@WebServlet("/my_resource")
public class SourceDataGetter extends HttpServlet
{
//    public static String procDate;
//    public static HashMap<String, String> types = new HashMap();
//    public static HashMap<String, File> files = new HashMap();
//    public static HashMap<String, String> converstions = new HashMap();
//    public static FTPClient ftpClient = new FTPClient();
    public static Access access = new Access();
//    

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    {
        
        
        try(FileInputStream fis = new FileInputStream(new File("C:\\NSF Projects\\AvianMigration\\avianMigration\\src\\java\\resources\\BAOR_Patuxent.xls")))
        {           
////            Document doc = Jsoup.connect("http://www.esrl.noaa.gov/psd/data/usclimdivs/data/map.html").get();
////            Elements elem = doc.getElementsByTag("tbody").get(1).parent().getElementsByTag("tr");
////            String state = "";
////            String stateCode = "";
////            String region = "";
////            String regionCode = "";
////            String code = "";
////            
////            String stateName = " ";
////            String fips = " ";
////            String county = " ";
////            String cdId = " ";
////            
//              getBirdName();
////            
////            ArrayList<String> results = new ArrayList();
////            
////            ArrayList<String> countyTableResults = new ArrayList();
////
////            for(Element element : elem)
////            {
////                for(Element innerElement : element.getElementsByTag("td"))
////                {
////  //                        System.out.println(innerElement.text());
////
////                    if(innerElement.text().contains("("))
////                    {
////                        state = "";
////                        StringTokenizer text = new StringTokenizer(innerElement.text(), "()");
////
////                        if(!innerElement.text().contains("-"))
////                        {
////                            while(text.hasMoreTokens())
////                            {
////                                if(text.countTokens() == 1)
////                                {
////                                    if(state.contains("Vermont"))
////                                    {
////                                        stateCode = "43";
////                                        text.nextToken();
////                                    }
////                                    else
////                                        stateCode = text.nextToken();
////                                }
////                                else
////                                {
////                                    state += text.nextToken().trim();
////                                }
////                            }
////                        }
////                        else
////                        {
////                            state += text.nextToken().trim();
////                            stateCode = text.nextToken().trim();
////                        }
////                    }
////                    else
////                    {
////                        String temp = innerElement.text();
////                        if(temp.length() > 1)
////                        {
////                            regionCode = temp.substring(0, 2).trim();
////                            region = temp.substring(2);
////                        }
////                    }
////
////                }
////                stateCode = stateCode.length() == 1 ? "0" + stateCode : stateCode;
////                regionCode = regionCode.length() == 1 ? "0" + regionCode : regionCode;
////                code = stateCode + regionCode;
////                results.add(code + ",\'" + state.substring(1) + "\',\'" + region.trim() + "\'");
////            }
////
////            HashSet<String> hs  = new HashSet<>();
////            hs.addAll(results);
////            results.clear();
////            results.addAll(hs);
////            String[] sr = results.toArray(new String[0]);
////            Arrays.sort(sr);
//
////            for(String result : sr)
////            {
////                System.out.println(result);
//                //access.execute("INSERT INTO NSFBeltz2017.dbo.CLIM_DIV VALUES(" + result + ")");
////            }
//            
////            for(String[] data : censusResults){
////                stateName = data[0];
////                county = data[2];
////                fips = data[1];
////                cdId = data[3];
////                
////                countyTableResults.add("'" + stateName + "',\'" + county.replaceAll("'", "''") + "\',\'" + fips + "\',\'" + cdId + "\'");
////            }
//            
//            
//            //System.out.println(countyTableResults);
//            
////            for(String censusResult : countyTableResults){
////                //System.out.println(censusResult);
////                access.execute("INSERT INTO NSFBeltz2017.dbo.COUNTY VALUES(" + censusResult + ")");
////            }
//        setProcDate();
//        setTypes();  
//        getFTPFile();
            
//            result[r][0] = row.getCell(0).toString();
//                result[r][1] = row.getCell(40).toString();
//                result[r][2] = row.getCell(41) == null ? "" : row.getCell(41).toString();
//                result[r][3] = row.getCell(6) == null ? row.getCell(5).toString().concat("-01-01 00:00:00") : row.getCell(6).toString();
//                
////                if("".equals(row.getCell(6).toString()))
////                {
////                    result[r][3] = row.getCell(5).toString().concat("-01-01 00:00:00");
////                }
////                else
////                {
////                   result[r][3] =  row.getCell(6).toString();
////                }
//                        
//                result[r][4] = "30632";
//                result[r][5] = row.getCell(5).toString();                         
//           
//        //insertDateCensus();
            
            System.out.println("Working dir: " + System.getProperty("user.dir"));
            startParse(0, fis);
            
           
//            
//        try
//        {            
//            ftpClient.connect("ftp.ncdc.noaa.gov", 21);
//            ftpClient.enterLocalPassiveMode();
//            ftpClient.login("anonymous", "anything");
//            ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
//            int myCode = ftpClient.getReplyCode();
//            System.out.println(myCode);
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
////            for(String[]cResult : censusResults) {
////                System.out.println(cResult[0] + cResult[1] + cResult[2] + cResult[3] + cResult[4] + cResult[5]);
////                access.execute("INSERT INTO NSFBeltz2017.dbo.CENSUS VALUES (" + cResult[0] +  ", " +  cResult[1] + ", " 
////                                  + "'" + cResult[2] + "'" + ", " + "'" + cResult[3] + "'" +  ", " + "'" + cResult[4] + "'" + ", " +  cResult[5] + ")");
////            }
////        }
//        
        }  catch (Exception ex)
        {
            ex.printStackTrace();
        } 
    }
//        
//    public static void getBirdName() throws Exception
//    {
//        StringBuilder data = new StringBuilder();
//        
//        data.append("{");
//        
//        //String currentBird = "sci_radio";
//        Table table = access.getTable("SELECT [Taxonomy #] FROM [NSFCourter2016].[dbo].[BIRD_VIEW]"); 
//        
//        data.append("\"tax_radio\"").append(": ").append("[");
//               
//        
//        while(table.next())
//        {
//            data.append("\"").append(table.getString("Taxonomy #")).append("\",");
//        }
//        
//        //data.deleteCharAt(data.length()-1);
//        data.append("]").append(System.getProperty("line.separator")).append("}");
//        
//        //System.out.println(data);
//        
//        final String FILENAME = "C:\\NSF Projects\\AvianMigration\\avianMigration\\web\\birdData2.json";
//        
//        BufferedWriter bw = null;
//        FileWriter fw = null;
//        
//        try {
//            fw = new FileWriter(FILENAME);
//            bw = new BufferedWriter(fw);
//            bw.write(data.toString());
//
//            System.out.println("Done");
//
//        } 
//         catch (IOException e) {
//
//         e.printStackTrace();
//
//         } 
//        finally {
//
//            try {
//
//                if (bw != null)
//                        bw.close();
//
//                if (fw != null)
//                        fw.close();
//
//            } 
//            catch (IOException ex) {
//
//                ex.printStackTrace();
//
//            }
//        }
//    }
//       
//    
//    public static void getCensusData()throws Exception 
//    {
//        
//        BufferedWriter bw = null;
//        FileWriter fw = null;
//        
//        String Filename = "C:\\NSF Projects\\SQLString.sql";
//        
//        int year;
//        
//        int resultValue = 0;
//        
//        String stateFIPS;
//        String countyFIPS;
//        String FIPS = null;
//        
//        int cal_table_id = 0;
//        
//        boolean isNull = false; //this will be used to handle responses that have no content, i.e. HTTP status code NOT 200 OK
//        
//        String beginningString = "";
//        StringBuilder result = new StringBuilder();
//        
//        try(BufferedReader br = new BufferedReader(new FileReader("C:\\NSF Projects\\WestNileSpread\\WestNileSpread\\src\\java\\resources\\CENSUS_VARIABLES.txt")))          
//            {
//                String line = br.readLine();
//
//                while (line != null) 
//                {
//                    //Lines that start with "*" are comments.
//                    if(!line.startsWith("*") && !line.equals(""))
//                    {
//                        beginningString += line;
//                    }
//
//                    line = br.readLine();
//                }       
//            } catch(Exception ex){System.err.println("File not found!");}
//           
//
//            //Delimit each row in the text file by the ;.
//            String[] lines = beginningString.split(";");
//
//            String content = "";
//
//            String[] parts = null;
//
//        for(int i = 0; i < lines.length; i++){
//            for(year = 2009; year <= 2015; year++){
//                System.out.println("Year " + year);
//                String v_name = null; 
//                String gender = null;
//                String range = null;            
//                String v_codes;
//                //get FIPS from county table
//                Table table = access.getTable("SELECT [FIPS] FROM [NSFBeltz2017].[dbo].[COUNTY]"); 
//
//                //iterate through the FIPS table
//                while(table.next()){
//                    System.out.println("FIPS no: " + table.getInt("FIPS"));
//                    FIPS = String.valueOf(table.getInt("FIPS"));
//                    //for FIPS codes with 4 digits
//                    if(FIPS.length() == 4){
//                        stateFIPS = FIPS.substring(0,1); //get 1st digit as state
//                        countyFIPS = FIPS.substring(1,4); //get 2nd to last digits as county
//                    }
//                    //for FIPS codes with 5 digits
//                    else {
//                        stateFIPS = FIPS.substring(0,2); //get 1st & 2nd digits as state
//                        countyFIPS = FIPS.substring(2,5); //get 3rd to last digits as county
//                    }
//              
//                    //Seperate all parts of the line.
//                    parts = lines[i].split("\\\\");
//                    v_name = parts[0];
//                    gender = parts[1];
//                    range = parts[2];
//                    v_codes = parts[3];
//                    //separate codes by comma
//                    String[] codes = v_codes.split(",");
//                    
//                    //iterate through the codes
//                    for(String code : codes)
//                    {
//                        //make HTTP GET request from CensusBureau API with *key*, year, code, countyFIPS and stateFIPS
//                        HttpClient client = HttpClientBuilder.create().build();
//                        HttpGet request = new HttpGet("http://api.census.gov/data/" + String.valueOf(year)
//                                +"/acs5?key=58572ae63234bc2b4fb0955f9597c15877b466fd&get=" + code + ",NAME&for=county:" 
//                                + countyFIPS + "&in=state:" + stateFIPS);
//
//                        //obtain response
//                        HttpResponse response = client.execute(request);
//                        System.out.println("Response code: " + response.getStatusLine());
//                        
//                        //if status is 200 OK
//                        if(response.getStatusLine().getStatusCode() == 200) {
//                            BufferedReader rd = new BufferedReader(
//                                new InputStreamReader(response.getEntity().getContent()));
//
//                            String myLine;
//                            //read response line by line
//                            while ((myLine = rd.readLine()) != null) {
//                                result.append(myLine);
//                            }
//                            
//                            if(result.toString().endsWith("]")) {
//                                result.deleteCharAt(result.length() - 1);
//                            }
//                           //adjust response to be JSON formatted
//                            if(result.charAt(0) == '[')
//                            {
//                                result.replace(0, 1, "{\"results\": ");
//                                result.append("}");
//                            }
//                            //insert data header at character 52. This will contain the actual numbers/data
//                            result.insert(52, " \"data\":");
//                            
//                            System.out.println(result);
//                            //create JSONObject
//                            JSONObject json = new JSONObject(result.toString());
//                            //create JSONArray from data section of the JSONObject
//                            JSONArray json2 = json.getJSONArray("data");
//                            //store number in resultValue and add to it at every iteration of the codes
//                            resultValue += Integer.valueOf((String)json2.get(0));
//
//                            result.setLength(0); //reverts back the result to accomodate other responses
//                            System.out.println(resultValue);
//                        }
//                        //no content
//                        else 
//                            isNull = true;
//                    }
//                    
//                    try {
//                        Table cal_table = access.getTable("SELECT APPID FROM [NSFBeltz2017].[dbo].[CALENDAR_TABLE] WHERE YEAR(DATE) ='" + year + "'"
//                                + "AND DATEPART(mm, DATE) = 01");
////                        
//                        if(cal_table.next())
//                        {
//                            cal_table_id = cal_table.getInt("APPID");
//                        }
////                        else
////                        {
////                            cal_insert = "INSERT INTO [NSFBeltz2017].[dbo].[CALENDAR_TABLE] VALUES ('" + sdf.format(date) + "')";
////                            System.out.println(cal_insert);
////                            access.execute(cal_insert);
////                            
////                            
////                        }
//                        
//                        System.out.println(cal_table_id);
//                        //if isNull is still false then write to file
//                        if(!isNull){
//                            if(range.contains("'")){
//                                //handle apostrophes
//                                range = range.substring(0, range.indexOf("'")+1) + "'" + range.substring(range.indexOf("'")+1, range.length()); 
//                            }
//                            //this will be the INSERT statement on the sql file
//                            content += "INSERT INTO NSFBeltz2017.dbo.CENSUS (CALENDAR_TABLE_ID, NAME, GENDER, RANGE, VALUE, COUNTY_ID) VALUES (" + cal_table_id 
//                                    + ",'" + v_name + "','" + gender + "','" + range + "'," + resultValue + "," + Integer.valueOf(FIPS) + ");\n";
//                                
//                        
//                            fw = new FileWriter(Filename);
//                            bw = new BufferedWriter(fw);
//                            bw.write(content);
//                            
//                        
//                        
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } finally {
//                        try {
//                            if(bw != null)
//                                bw.close();
//                            if(fw != null)
//                                fw.close();
//                        } catch(IOException ex) {
//                            ex.printStackTrace();
//                        }
//                    }
//                    
//                    resultValue = 0;
//                    
//                    //must set isNull back to false for the subsequent counties that HAVE content
//                    isNull = false; 
//                }
//                    
//                                    
//            }
//        }
//    }
////        
////        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
////        Connection con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=NSFBeltz2017", "username", "password");
//////        
////        try {
////            ScriptRunner sr = new ScriptRunner(con, false, false);
////            
////            BufferedReader r = new BufferedReader(new FileReader(Filename));
////            
////            sr.runScript(r);
////        } catch(Exception e){
////            e.printStackTrace();
////        }
////        SQLExec executer = new SQLExec();
////        executer.setSrc(new File(Filename));
////        executer.setDriver("com.microsoft.sqlserver.jdbc.SQLServerDriver");
////        
////        executer.setUserid("");
////        executer.setPassword("Mariachi2017");
////        executer.setUrl("jdbc:sqlserver://localhost:1433;databaseName=NSFBeltz2017;");
////        executer.execute();
//    //}           
//        //return censusResult;                   
// 
////    public static void XMLParser() throws MalformedURLException, IOException, ParserConfigurationException, SAXException {
////        //ArrayList<String[]> result = new ArrayList<>();
////        String filename = "C:\\NSF Projects\\Cases.txt";
////        
////        FileWriter f = null;
////        //BufferedWriter b = null;
////        
////        File file = new File(filename);
////        FileOutputStream fos = new FileOutputStream(file);
////        
////        BufferedWriter b = new BufferedWriter(new OutputStreamWriter(fos));
////        
////        String content;
////        
////        int year;
////        String county;
////        int cases;
////        boolean alreadySeen = false;
////        try
////        {
////            URL link = new URL("https://chhs.data.ca.gov/api/views/qdgd-p3y3/rows.xml?accessType=DOWNLOAD");
////            InputStream stream = link.openStream();
////            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
////            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); 
////            Document doc = dBuilder.parse(stream);
////            doc.getDocumentElement().normalize();
////            //System.out.println("Root Element: " + doc.getDocumentElement().getNodeName());
////            NodeList nList = doc.getDocumentElement().getChildNodes();
////            //System.out.println("nList length: " + nList.getLength());
////            String previousCounty = "Alameda";
////            
////            
////            for(int i = 0; i < nList.getLength(); i++){
////                //Node nNode = nList.item(i);
////                NodeList n = nList.item(i).getChildNodes();
////                System.out.println("n length: " + n.getLength());
////                for(int j = 0; j < n.getLength(); j++){
////                    Node nNode = n.item(j);                  
////                    if(nNode.getNodeType() == Node.ELEMENT_NODE){
////                        Element e = (Element) nNode;
////                        
////                        //System.out.println("uuid: " + e.getAttribute("_uuid"));
////                        //System.out.println("County: " + e.getElementsByTagName("county").item(0).getTextContent());
////                        if(e.getElementsByTagName("county").item(0).getTextContent().equals(previousCounty)){
////                            year = Integer.valueOf(e.getElementsByTagName("year").item(0).getTextContent());
////                            county = e.getElementsByTagName("county").item(0).getTextContent();
////                            cases = Integer.valueOf(e.getElementsByTagName("positive_cases").item(0).getTextContent());
////                            
////                            if(!alreadySeen){ 
////                                content = year + "/" + county + "/" + cases;
////                                b.write(content);  
////                                alreadySeen = true;
////                            }
////                            
////                            else {
////                                content = "," + cases;
////                                b.append(content);
////                                //b.newLine();
////                                alreadySeen = true;
////                            }
////                            
////                            
////                        }
////                        
////                        else {
////                            //cases = 0;
////                            b.newLine();
////                            previousCounty = e.getElementsByTagName("county").item(0).getTextContent();
////                            alreadySeen = true;
////                            year = Integer.valueOf(e.getElementsByTagName("year").item(0).getTextContent());
////                            county = e.getElementsByTagName("county").item(0).getTextContent();
////                            cases = Integer.valueOf(e.getElementsByTagName("positive_cases").item(0).getTextContent());
////                            content = year + "/" + county + "/" + cases;
////                            b.write(content);
////                        }                     
////                         
////                    }        
////                }               
////            }         
////        } catch (IOException | NumberFormatException | ParserConfigurationException | DOMException | SAXException e){
////            e.printStackTrace();
////        }
////        
////        b.close();
////        //f.close();      
////    }
//    
//    
//            
//          
//                
//          
//        
//        //System.out.println(Arrays.deepToString(censusResult));
//
//    
////    public static void getCensusData() throws IOException {
////        //final int BUFFER_SIZE = 4096;
////        
////        String remoteFile = "/acs2002/Core_Tables/ACS_2002_META.zip";
////        File downloadFile = File.createTempFile("census.zip", null);
////        
////        
////        System.out.println("I'm here");
////        
////        
////        
////        try(OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile)))
////        {
////            boolean success = ftpClient.retrieveFile(remoteFile, outputStream);
////            
////            
////            
////            
////            if(success)
////            {
////                System.out.println("File retrieved successfully.");
////                System.out.println("File path: " + downloadFile.getAbsolutePath());
////                
////                
////                
//////                FileWriter writer = new FileWriter(downloadFile);
//////                writer.write(remoteFile);
////                
//////                FileOutputStream fos = null;
//////                
//////                InputStream is = new FileInputStream(downloadFile.getAbsolutePath());
//////                try (ZipInputStream zis = new ZipInputStream(is)) {
//////                    ZipEntry ze;
//////                    byte[] buff = new byte[8192];
//////                    while ((ze = zis.getNextEntry()) != null) {
//////                        fos = new FileOutputStream("C:\\Temp\\" + ze.getName());
//////                        
//////                        int l = 0;
//////                        
//////                        while((l = zis.read(buff)) > 0){
//////                            fos.write(buff, 0, l);
//////                        }
//////                        
//////                        //fos.close();
//////                        
//////                    }
//////                    
//////                    fos.close();
//////                    
//////                    
//////                    //files.put(type, downloadFile);
//////                }
//////                
////            }
////            else
////                System.out.println("File retrieval failed.");
////
////        }
////        catch(Exception ex)
////        {
////            ex.printStackTrace();
////        }
////            
////    }
//    
//    
//   
//        /**
//     * Parses an Excel file for any information, then captures that information
//     * in a string and returns it.
//     * 
//     * @param labelNumber the number of the column that holds the students
//     * @param sheetNumber the sheet number that the variables are on
//     * @param columnNumber the column number that holds all of the variable data
//     * @param file the file the user is going to parse
//     */
    public static void startParse(int sheetNumber, InputStream file)
    {  
        try 
        (
            //Create an abstract workbook based on the type of file comming
            //from the input stream.
            Workbook wb = WorkbookFactory.create(file);
        )
        {
            
            PointInPolygon pip = new PointInPolygon();
            //Get the very last sheet.
            Sheet sheet = wb.getSheetAt(sheetNumber);
            Row row;

            //Get the number of rows in the sheet.
            int rows = sheet.getLastRowNum();
            
            //System.out.println(rows);

            
            
            String query;

            for(int r = 0; r < rows; r++)
            {
                row = sheet.getRow(r+1); //avoid the column labels
                
                //row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                
//                if(row.getCell(0).getStringCellValue().equals("Total"))
//                    continue;
                
//                if(row.getCell(41) != null)
//                    System.out.println("row number: " + row.getRowNum() + " value: " + row.getCell(41).toString());

                String[] result = new String[6];
                
                result[0] = row.getCell(0).toString();
                result[1] = row.getCell(40).toString();
                result[2] = row.getCell(41) == null ? "" : row.getCell(41).toString(); 
                result[3] = row.getCell(6) == null ? row.getCell(5).toString().concat("-01-01 00:00:00") : row.getCell(6).toString();  
                
                String[] fad = result[3].split("-");
                
                int clim_div_id = 0;
                
                if(!"".equals(result[2]))
                {
                    //System.out.println("Row " + r + " Lat " + result[r][1] + " Long " + result[r][2]);
                    if(pip.getId(Double.parseDouble(result[1]), Double.parseDouble(result[2])) != null)
                        clim_div_id = pip.getId(Double.parseDouble(result[1]), Double.parseDouble(result[2]));
                    else
                        continue;              
                }
                
                if(Integer.parseInt(fad[0]) < 1895)
                {
                    continue;
                }

                Table clim_table = access.getTable("SELECT APPID FROM [NSFCourter2016].[dbo].[CLIMATE] WHERE CLIM_DIV_ID = " + clim_div_id
                        + " AND (YEAR(DATE) = " + fad[0] + " AND MONTH(DATE) = " + fad[1] + ")", new Object[]{});

                //System.out.println("year: " + fad[0] + "-" +  fad[1] + "clim_div: " + clim_div_id + " no of rows: " + clim_table.numberOfRows());

                while(clim_table.next())
                {
                    System.out.println("app id " + clim_table.getInt("APPID"));
                    result[4] = clim_table.getInt("APPID").toString();
                }

                result[5] = row.getCell(5).toString();  
                
                if("".equals(result[2]))
                    continue;
                
                query = "INSERT INTO [NSFCourter2016].[dbo].[HISTORICAL_OBSERVATIONS] VALUES (" +  result[0] + ", " + result[1] + ", " + result[2] + ", " + result[4] + ", '" +
                        result[3] + "', 30632, " + "'"  + result[5] + "')";
                
                System.out.println(query);
                
                access.execute(query, new Object[]{});
            }
           
            //return result;
        } 
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        //return new String[0][0];
    }
//    
//
//    public static void main(String[] args)
//    {
//        try {
//            System.out.println("Starting connection");
//            ftpClient.connect("ftp2.census.gov", 21);
//            System.out.println("Done");
//            ftpClient.enterLocalPassiveMode();
//            ftpClient.login("anonymous", "");
//            ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
//            
//            int code = ftpClient.getReplyCode();
//            System.out.println(code);
        
//        try(FileInputStream fis = new FileInputStream(new File("C:\\NSF Projects\\AvianMigration\\avianMigration\\src\\java\\resources\\BAOR_Patuxent.xls")))
//        {
//            String[][] myResult = startParse(0, fis);
//            
//            //System.out.println(Arrays.deepToString(myResult));
//            for(String[] r : myResult)
//            {
//                System.out.println(Arrays.toString(r));
//            }
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
        
//            
            
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
          
//    }
//    
//    public static String[][] getResults() throws IOException
//    {
//        
//        double lat = 0, lon = 0;
//        
//        String[][] results = new String[3071][4];
//        
//        //get excel file
//        File file = new File("C:\\NSF Projects\\WestNileSpread\\WestNileSpread\\src\\java\\resources\\Gaz_counties_national.xlsx");
//        FileInputStream fis = new FileInputStream(file);
//        String[][] myR = startParse(0, 0, 0, fis);
//        
//        int id = 0;
//        
//        //List<String[]> finalResult = new ArrayList<>();
//        
//        for(int i = 0; i < myR.length; i++){
//            //get lat and lon
//            lat = Double.parseDouble(myR[i][3]);
//            lon = Double.parseDouble(myR[i][4]);
//            
//            PointInPolygon pip = new PointInPolygon();
//            
//            
//           
//                id = pip.getId(lat, lon);    
//                for(int j = 0; j < 3; j++){
//                    results[i][j] = myR[i][j];                   
//                }  
//                results[i][3] = String.valueOf(id);
//                          
//        }
//      
////        for(String[] r : results) {
////            System.out.println(Arrays.deepToString(r));
////        }
//        
//      return results;
//       
//    }
    
    
//    
//    
//    
//    public static void setTypes()
//    {
//        //types: <clim_variable string code, numerical code>
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
//        
//        
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
//        
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
////    
////    
//    public static void insertDateCensus() throws Exception
//    {
//        Table censusTable = access.getTable("SELECT APPID, CALENDAR_TABLE_ID FROM [NSFBeltz2017].[dbo].[CENSUS]");
//
//        int censusId, ctId, dateId;
//        
//        
//
//        while(censusTable.next())
//        {
//            //get the APPID and use it as CENSUS_ID
//            censusId = censusTable.getInt("APPID");
//            
//            //get CALENDAR_TABLE id
//            ctId = censusTable.getInt("CALENDAR_TABLE_ID");
//            
//            System.out.println("ctId is " + ctId);
//            
//            //get year that corresponds to the APPID of CENSUS and get the ids that correspond to that year (count=12)
//            Table calendarTable = access.getTable("SELECT APPID FROM [NSFBeltz2017].[dbo].[CALENDAR_TABLE] WHERE YEAR(DATE)= "
//                    +"(SELECT YEAR(DATE) FROM [NSFBeltz2017].[dbo].[CALENDAR_TABLE] WHERE APPID = " + ctId + ")");
//            
//            StringBuilder insertQuery = new StringBuilder("");
//            
//            //go through the appIDs
//            while(calendarTable.next())
//            {
//                dateId = calendarTable.getInt("APPID");
//                
//                //System.out.println("dateId " + dateId);
//                
//                if(!insertQuery.toString().contains("INSERT"))
//                    insertQuery.append("INSERT INTO [NSFBeltz2017].[dbo].[DATE_CENSUS] (DATE_ID, CENSUS_ID) VALUES (").append(dateId).append(", ").append(censusId).append(")");
//                else 
//                    insertQuery.append(System.getProperty("line.separator")).append("INSERT INTO [NSFBeltz2017].[dbo].[DATE_CENSUS] (DATE_ID, CENSUS_ID) VALUES (").append(dateId).append(", ").append(censusId).append(")");                             
//            } 
//            System.out.println(insertQuery.toString());
//            access.execute(insertQuery.toString());
//        }
//
//    }
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
//////    
//    public static void getFTPFile() 
//    {
//        try 
//        {
//            SimpleDateFormat sdf = new SimpleDateFormat(
//                "MM-dd-yyyy");
//            int day = 1;
//            Calendar cal = Calendar.getInstance();
//            
//            
//            //access = new Access();
//            Table climDiv = access.getTable("SELECT CD_ID FROM [NSFCourter2016].[dbo].[CLIMATE_DIV]", new Object[]{});    
//            
//            int cal_table_id = 0;
//            
//            String cal_insert;
//            
//            for(int year = 1895; year <= 2004; year++)
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
//                                            
//                                            
//                                            if(!line.substring(month, month + 6).trim().equals("-9.99") && !line.substring(month, month + 6).trim().equals("-99.9"))
//                                                data.put(type, Double.valueOf(line.substring(month, month + 6).trim()));
//                                        }
//                                    }
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
                        
//                        Table cal_table = access.getTable("SELECT APPID FROM [NSFBeltz2017].[dbo].[CALENDAR_TABLE] WHERE DATE ='" + sdf.format(date)+ "'");
//                        
//                        if(cal_table.next())
//                        {
//                            cal_table_id = cal_table.getInt("APPID");
//                        }
//                        else
//                        {
//                            cal_insert = "INSERT INTO [NSFBeltz2017].[dbo].[CALENDAR_TABLE] VALUES ('" + sdf.format(date) + "')";
//                            System.out.println(cal_insert);
//                            access.execute(cal_insert);
//                            
//                            
//                        }
                        
                        //System.out.println(cal_table_id);
                        
//                        String sqlString = "INSERT INTO [NSFCourter2016].[dbo].[CLIMATE] (DATE, CLIM_DIV_ID, ~) VALUES ('" + sdf.format(date) + "', " + climDivCode + ", !" + ")";
//
//                        for(String dataPoint : data.keySet())
//                        {
//                            sqlString = sqlString.replaceAll("~", converstions.get(dataPoint) + ", ~").replaceAll("!", data.get(dataPoint) + ", !");
//                        }
//
//                        System.out.println(sqlString.replaceAll(", ~", "").replaceAll(", !", ""));
//
//                        access.execute(sqlString.replaceAll(", ~", "").replaceAll(", !", ""), new Object[]{});  
//                    }
//                }
//            }
//        }
//        catch(Exception ex)
//        {
//            ex.printStackTrace();
//        }
//    }
}