/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import edu.princeton.cs.algs4.Date;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author Levi
 */
public class SortFile implements Comparable<SortFile> {

    private final Date when;
    private final long amount;
    
    public SortFile(String sortFile) {
        String[] a = sortFile.split("\\s+");
        when = new Date(a[0]);
        amount = Long.parseLong(a[1]);
    }
    
    public Date when() {
        return when;
    }
    
    public Long amount() {
        return amount;
    }
    
    @Override
    public String toString() {
        return String.format("%10s %d", when, amount);
    }
    
    @Override
    public int compareTo(SortFile that) {
        return Long.compare(this.amount, that.amount);
    }
     
    public static class WhenOrder implements Comparator<SortFile> {

        @Override
        public int compare(SortFile a, SortFile b) {
            return a.when.compareTo(b.when);
        }    
    }
    
    public static class HowMuchOrder implements Comparator<SortFile> {

        @Override
        public int compare(SortFile a, SortFile b) {
            return Long.compare(a.amount, b.amount);
        }     
    }

    public static void main(String[] args) {
        SortFile[] f = new SortFile[4];
        f[0] = new SortFile("6/1/2002   3500");
        f[1] = new SortFile("7/17/2002   2566500096");
        f[2] = new SortFile("7/24/2002   2775559936");
        f[3] = new SortFile("1/23/2002   210000");
        
        StdOut.println("Sort by date");
        Arrays.sort(f, new SortFile.WhenOrder());
        
        System.out.println(Arrays.toString(f));
        
        StdOut.println("Sort by amount");
        Arrays.sort(f, new SortFile.HowMuchOrder()); 
        
        System.out.println(Arrays.toString(f));
        
        
    }
    
}
