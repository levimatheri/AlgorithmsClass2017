/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author Levi
 */
public class Version implements Comparable<Version> {

    private final int firstNo, secondNo, thirdNo;
    public Version(String vNumber)
    {
       String[] noArray = vNumber.split("\\.");
       this.firstNo =  Integer.valueOf(noArray[0]);
       this.secondNo = Integer.valueOf(noArray[1]);
       this.thirdNo = Integer.valueOf(noArray[2]);
    }

    public int getFirstNo() {
        return firstNo;
    }

    public int getSecondNo() {
        return secondNo;
    }

    public int getThirdNo() {
        return thirdNo;
    }   
    

    @Override
    public int compareTo(Version o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static class FirstNoOrder implements Comparator<Version>
    {
        @Override
        public int compare(Version v1, Version v2) {
            return Integer.compare(v1.firstNo, v2.firstNo);
        }        
    }
    
    public static class SecondNoOrder implements Comparator<Version>
    {
        @Override
        public int compare(Version v1, Version v2) {
            if(v1.firstNo == v2.firstNo)
                return Integer.compare(v1.secondNo, v2.secondNo);
            return 0;
        }        
    }
    
    public static class ThirdNoOrder implements Comparator<Version>
    {
        @Override
        public int compare(Version v1, Version v2) {
            if(v1.secondNo == v2.secondNo)
                return Integer.compare(v1.thirdNo, v2.thirdNo);
            return 0;
        }        
    }
    
    @Override
    public String toString() {
        return firstNo + "." + secondNo + "." + thirdNo;
    }
    private static void sort(Version[] a)
    {
        Arrays.sort(a, new Version.FirstNoOrder());
        Arrays.sort(a, new Version.SecondNoOrder());
        Arrays.sort(a, new Version.ThirdNoOrder());
    }
    
    public static void main(String[] args)
    {
        Version[] v = new Version[5];
        
        v[0] = new Version("115.1.1");
        v[1] = new Version("114.10.2");
        v[2] = new Version("115.10.2");
        v[3] = new Version("115.10.1");
        v[4] = new Version("116.1.1");
        
        sort(v);
        
        for(Version version : v)
            StdOut.println(version.toString());
        
    }
}
