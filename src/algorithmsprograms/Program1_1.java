/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmsprograms;

import edu.princeton.cs.algs4.In;
import java.util.Arrays;

/**
 *
 * @author Levi
 */
public class Program1_1 {
    //indexPosInGroup tracks position of the elements in the "groups"
    private static String[] indexPosInGroup;
    
    private static String[] groups;
    
    
    private static String[] getGroups(In pairs)
    {
        int length = Integer.parseInt(pairs.readLine());
        
        //System.out.println("Length: " + length);
        //keeping this will lead to out of memory error. 
        //test with small samples to make sure it works correctly.
        //int[][] groups = new int[length][length]; 
        groups = new String[length + 1];
        int groupCount = 0;
        indexPosInGroup = new String[length + 1]; //keep this
        int[] insertedItems = new int[length + 1];
        initializeArray(insertedItems);
        
        //System.out.println("Inserted items: " + Arrays.toString(insertedItems));
        
        
        //read in the pairs
        while(pairs.hasNextLine())
        {
            String line = pairs.readLine();
            int first = Integer.parseInt(line.split(" ")[0]);
            int second = Integer.parseInt(line.split(" ")[1]);
            
            //System.out.println("First: " + first + " Second: " + second);
            
            //if first has not been encountered
            if(insertedItems[first] == -1)
            {    
                //if second has not been encountered
                //put it in same group as first
                if(insertedItems[second] == -1)
                {
                    insertedItems[first] = first;
                    //groups[first][first] = first; 
                    groups[first] = String.valueOf(first); 
                    groupCount++;
                    indexPosInGroup[first] = first + "," + first;
                    
                    insertedItems[second] = second;
                    //groups[first][second] = second;
                    groups[first] += "," + String.valueOf(second);
                    indexPosInGroup[second] = second + "," + first;
                }
                else
                {
                    int groupNumber = Integer.parseInt(
                        indexPosInGroup[second].split(",")[1]);
                    
                    insertedItems[first] = first;
                    //groups[groupNumber][first] = first; 
                    groups[groupNumber] += "," + String.valueOf(first);
                    indexPosInGroup[first] = first + "," + groupNumber;
                }
            }
            else
            {
                int groupNumber = Integer.parseInt(
                        indexPosInGroup[first].split(",")[1]);
                if(insertedItems[second] == -1)
                {
                    insertedItems[second] = second;
                    //groups[groupNumber][second] = second;
                    groups[groupNumber] += "," + String.valueOf(second);
                    indexPosInGroup[second] = second + "," + groupNumber;
                }
            }
        }
        return groups;
    }
    
    private static void join(int a, int b)
    {
        int aGroupPos = Integer.parseInt(indexPosInGroup[a].split(",")[1]);
        int bGroupPos = Integer.parseInt(indexPosInGroup[b].split(",")[1]);
        
        groups[aGroupPos] += "," + groups[bGroupPos];
        groups[bGroupPos] = null;
    }
    
    private static int numGroups(String[] groups)
    {
        int count = 0;
        
        for(String group : groups)
        {
            if(group != null)
                count++;
        }
        return count;
    }
    
    private static String inSameGroup(int index1, int index2)
    {
        try
        {
            if(indexPosInGroup[index1] != null && indexPosInGroup[index2] != null)
            {
                if(indexPosInGroup[index1].split(",")[1].equals(indexPosInGroup[index2].split(",")[1]))
                    return "Yes";
                else
                    return "No";
            }

            return "One or both elements not found";   
        } catch(ArrayIndexOutOfBoundsException exception)
        {
            exception.printStackTrace();
        }
        return "ERROR!! Check your indexes";
    }
    
    private static void initializeArray(int[] array)
    {
        for(int i = 0; i < array.length; i++)
            array[i] = -1;
    }
    public static void main(String[] args)
    {
        In file = new In(args[0]);
        
        String[] g = getGroups(file);
        
        for(String group : g)
        {
            if(group != null)
                System.out.println("Group: " + group);
        }
        System.out.println("Groups count: " + numGroups(g));
        
        //System.out.println("no of groups: " + noOfGroups);
        
        System.out.println("2 and 4. Same group? " + inSameGroup(2, 4));
        
    }
}
