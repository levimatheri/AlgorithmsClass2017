/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author Levi
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONObject;

public class Test {
    public static void getHTML() throws Exception
    {
        
        //int offset = 1;
        //int limit = 1000;
        //boolean found = false;
        
//        ArrayList<String> myResult = new ArrayList<>();
//        
//        do {
            HttpClient client = HttpClientBuilder.create().build();
            //HttpGet request = new HttpGet("http://www.ncdc.noaa.gov/cdo-web/api/v2/locations?locationcategoryid=CNTY&offset=" + String.valueOf(offset) + "&limit="+ String.valueOf(limit));

            HttpGet request = new HttpGet("https://www.ncdc.noaa.gov/cdo-web/api/v2/datatypes?datasetid=GHCNM&limit=1000");
            // add request header
            request.addHeader(new BasicHeader("token", "zmLVPbXogSuUbsQLXLvgGGFhblAsRsKP"));
            HttpResponse response = client.execute(request);

            System.out.println("Response Code : "
                            + response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                    result.append(line);
            }
        
            if(result.charAt(0) == '[')
            {
                result.insert(0, "{ \"results\": ");
                result.append("}");
            }

            System.out.println(result.toString());
//            JSONObject json = new JSONObject(result.toString());
//            JSONArray json2 = json.getJSONArray("results");
//        
//        
//            for(int i = 0; i < json2.length(); i++)
//            {
//                      
//                if(json2.getJSONObject(i).get("name").toString().endsWith(stateAbbr)){ //look for counties of a state
//                    myResult.add(json2.get(i).toString());
//                    found = true;          
//                }           
//           }
//        
//            offset += 1000;
//                
//            } while(!found);
        

        //return myResult.toString();
    }
        
   public static void main(String[] args) throws Exception{
       //Scanner a = new Scanner(System.in);
       
       //String inputState = "";
               
       //System.out.println("Enter a state abbreviation: ");
       
       //inputState = (String) a.nextLine();
       
       getHTML();
       
       
       //System.out.println(Results);
   } 
}

