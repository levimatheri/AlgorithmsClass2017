/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmsprograms;

import edu.princeton.cs.algs4.In;

/**
 *
 * @author Levi
 */
public class Program1_1 {
    //indexPosInGroup tracks position of the elements in the "groups"
    private static String[] indexPosInGroup;
    private static int groups(In pairs)
    {
        int length = Integer.parseInt(pairs.readLine());
        
        //keeping this will lead to out of memory error. 
        //test with small samples to make sure it works correctly.
        //int[][] groups = new int[length + 1][length + 1]; 
        int groupCount = 0;
        indexPosInGroup = new String[length]; //keep this
        int[] insertedItems = new int[length];
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
                    groupCount++;
                    indexPosInGroup[first] = first + "," + first;
                    
                    insertedItems[second] = second;
                    //groups[first][second] = second;
                    indexPosInGroup[second] = second + "," + first;
                }
                else
                {
                    int groupNumber = Integer.parseInt(
                        indexPosInGroup[second].split(",")[1]);
                    
                    insertedItems[first] = first;
                    //groups[groupNumber][first] = first; 
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
                    indexPosInGroup[second] = second + "," + groupNumber;
                }
            }
        }
        return groupCount;
    }
    
    private static String areInSameGroup(int index1, int index2)
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
        
        int noOfGroups = groups(file);
        
        System.out.println("no of groups: " + noOfGroups);
        
        System.out.println("64 and 2477. Same group? " + areInSameGroup(64, 2477));
    }
}
