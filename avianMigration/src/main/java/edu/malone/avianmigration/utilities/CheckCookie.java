/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.avianmigration.utilities;

import java.util.Iterator;
import java.util.List;
import javax.servlet.http.Cookie;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author cjedwards1
 */
public class CheckCookie
{
    private final Cookie cookie;
    public CheckCookie(Cookie cookie)
    {
        this.cookie = cookie;
    }
    
    /**
     * Check if the cookie has expired or not.
     * @return False iff the cookie's time alive is less than [expired], true otherwise.
     * @throws JSONException 
     */
    public boolean hasCookieExpired()
    {
        try
        {
            RecieveCookie recieve = new RecieveCookie();
            JSONObject value = new JSONObject(new String(recieve.decrypt(cookie.getValue())));
            return convertFromSeconds((System.currentTimeMillis() - value.getLong("time")) / 1000) > ServletFunctions.properties.getInt("cookie.time.value");
        }
        catch(JSONException ex)
        {
//            Logger.getLogger(CheckCookie.class.getName()).log(Level.SEVERE, null, ex);
            return true;
        }
    }
    
    private long convertFromSeconds(long time)
    {
        switch(ServletFunctions.properties.getString("cookie.time.name"))
        {
            case "minute":
                return time / 60;
            case "hour":
                return time / 60 / 60;
            case "day":
                return time / 60 / 60 / 24;
            case "week":
                return time / 60 / 60 / 24 / 7;
            case "month":
                return time / 60 / 60 / 24 / 7 / 4;
            case "year":
                return time / 60 / 60 / 24 / 7 / 4 / 12;
            default:
                return time; //seconds
        }
    }
    
    /**
     * Take the property cookie.time.value and convert it to seconds given cookie.time.name.
     * @return The max age of the cookie in seconds.
     */
    public int convertToSeconds()
    {
        //System.out.println(ServletFunctions.properties.getString("cookie.time.name"));
        switch(ServletFunctions.properties.getString("cookie.time.name"))
        {
            case "minute":
                return ServletFunctions.properties.getInt("cookie.time.value") * 60;
            case "hour":
                return ServletFunctions.properties.getInt("cookie.time.value") * 60 * 60;
            case "day":
                return ServletFunctions.properties.getInt("cookie.time.value") * 60 * 60 * 24;
            case "week":
                return ServletFunctions.properties.getInt("cookie.time.value") * 60 * 60 * 24 * 7;
            case "month":
                return ServletFunctions.properties.getInt("cookie.time.value") * 60 * 60 * 24 * 7 * 4;
            case "year":
                return ServletFunctions.properties.getInt("cookie.time.value") * 60 * 60 * 24 * 7 * 4 * 12;
            default:
                return ServletFunctions.properties.getInt("cookie.time.value"); //seconds
        }
    }
    
    /**
     * Resets the time for the cookie.
     * @return The cookie with the new time.
     * @throws JSONException 
     */
    public Cookie setNewTime() throws JSONException
    {
        RecieveCookie recieve = new RecieveCookie();
        JSONObject value = new JSONObject(new String(recieve.decrypt(cookie.getValue())));
        value.put("time", System.currentTimeMillis());
        cookie.setValue(new SendCookie().encode(value.toString().getBytes()));
        cookie.setMaxAge(convertToSeconds());
        return cookie;
    }
    
    /**
     * Checks if the cookie is a valid cookie from us or not.
     * @return True iff the cookie passes all tests, false otherwise.
     */
    public boolean verifyCookie()
    {
        try
        {
            RecieveCookie recieve = new RecieveCookie();
            JSONObject object = new JSONObject(new String(recieve.decrypt(cookie.getValue())));
            Iterator keys = object.keys();
            List<String> properValues = ServletFunctions.properties.getList(String.class, "cookie.variables");

            while(keys.hasNext())
            {
                String key = keys.next().toString();
                if(properValues.contains(key))
                    properValues.remove(key);
                else
                    return false;
            }
            
            //Check Access as well to see if variables match.

            return properValues.isEmpty();
        }
        catch(JSONException ex)
        {
//            Logger.getLogger(CheckCookie.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    /**
     * For testing purposes, this cookie can be made to bypass the sign in.
     * @return A valid session cookie.
     * @throws JSONException 
     */
    public Cookie createTestCookie() throws JSONException
    {
        SendCookie send = new SendCookie();
        JSONObject returnValue = new JSONObject();
        
        for(String key : ServletFunctions.properties.getList(String.class, "cookie.variables"))
        {
            if(key.equals("time"))
                returnValue.put("time", System.currentTimeMillis());
            else
                returnValue.put(key, ServletFunctions.properties.getString(key));
        }
        
        //Create the cookie with name object and value being the encryoted json object
        Cookie returnCookie = new Cookie("object", send.encode(returnValue.toString().getBytes()));

        //Set the cookie's max age to 2 hours
        returnCookie.setMaxAge(convertToSeconds());
        return returnCookie;
    }
    
    /**
     * Get a value from the cookie.
     * @param key The name of the variable that the value will come from.
     * @return The value.
     * @throws JSONException 
     */
    public String getValue(String key) throws JSONException
    {
        RecieveCookie recieve = new RecieveCookie();
        return new JSONObject(new String(recieve.decrypt(cookie.getValue()))).getString(key);
    }
    
    /**
     * 
     * @return A string that contains all of the values of the cookie.
     * @throws JSONException 
     */
    public String printCookie() throws JSONException
    {
        RecieveCookie recieve = new RecieveCookie();
        JSONObject object = new JSONObject(new String(recieve.decrypt(cookie.getValue())));
        Iterator keys = object.keys();
        String returnString = "";
        
        while(keys.hasNext())
        {
            String key = keys.next().toString();
            returnString += key + ":" + object.get(key) + "/r/n";
        }
        
        return returnString;
    }
}
