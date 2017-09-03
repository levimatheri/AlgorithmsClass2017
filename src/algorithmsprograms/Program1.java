/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmsprograms;

import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Levi
 */
public class Program1 {
    static final HashMap<Integer, Integer> GROUPS = new HashMap<>();
    
    static HashMap<Integer, ArrayList<Integer>> groupings;
    
    static int newGroup = 1;
    
   
//    public static List<ArrayList<Integer>> getGroups(In txt)
//    {
//        List<ArrayList<Integer>> groups = new ArrayList<>();
//        
//        HashSet<Integer> inserted = new HashSet<>();
//        
//        
//        while(txt.hasNextLine())
//        {
//            String line = txt.readLine();
//            int firstElt = Integer.parseInt(line.split(" ")[0]);
//            int secondElt = Integer.parseInt(line.split(" ")[1]);
//            
//            
//            if(inserted.add(firstElt))
//            {
//                inserted.add(firstElt);
//                   
//                if(!inserted.add(secondElt))
//                {
//                    for(ArrayList<Integer> group : groups)
//                    {
//                        if(group.contains(secondElt))
//                            group.add(firstElt);
//                    }
//                }
//                else
//                {
//                    ArrayList<Integer> groupElt = new ArrayList<>();
//                    groupElt.add(firstElt);
//                    
//                    inserted.add(secondElt);
//                    groupElt.add(secondElt);
//                    groups.add(groupElt);
//                }     
//            }    
//            else 
//            {
//                if(inserted.add(secondElt))
//                {
//                    inserted.add(secondElt);
//                    for(ArrayList<Integer> group : groups)
//                    {
//                        if(group.contains(firstElt))
//                            group.add(secondElt);
//                    }
//                }
//            }        
//        }
//        return groups;
//    }
    
    static void getGroups(In txt)
    {
        while(txt.hasNextLine())
        {
            String line = txt.readLine();
           
            int firstElt = Integer.parseInt(line.split(" ")[0]);
            int secondElt = Integer.parseInt(line.split(" ")[1]);
            
            
            if(GROUPS.get(firstElt) == null)
            {
                if(GROUPS.get(secondElt) == null)
                {
                    GROUPS.put(firstElt, newGroup);
                    GROUPS.put(secondElt, newGroup);
                    newGroup++;
                }
                else
                    GROUPS.put(firstElt, GROUPS.get(secondElt));
            }
            else
            {
                if(GROUPS.get(secondElt) == null)
                    GROUPS.put(secondElt, GROUPS.get(firstElt));
            }
        }   
    }
    
    static HashMap<Integer, ArrayList<Integer>> getGroupings(HashMap<Integer, Integer> groupMap)
    {
        groupings = new HashMap<>();
        groupMap.entrySet().stream().forEach((entry) -> {
            
            if(groupings.get(entry.getValue()) == null)
            {
                ArrayList<Integer> members = new ArrayList<>();
                members.add(entry.getKey());
                groupings.put(entry.getValue(), members);
            }
            else
            {
                groupings.get(entry.getValue()).add(entry.getKey());
            }
        });
        return groupings;
    }
    
    static void printGroupings(HashMap<Integer, ArrayList<Integer>> myGroups)
    {
        myGroups.values().stream().forEach((ArrayList<Integer> items) -> {
            System.out.println("groups: " + items);
        });
    }
    
   
    static boolean isInSameGroup(int item1_index, int item2_index)
    {
//        for(ArrayList<Integer> group : groupsList)
//        {
//            if(group.contains(item1_index))
//            {
//                return group.contains(item2_index);
//            }
//            
//            else
//            {
//                if(group.contains(item2_index))
//                    return false;
//            }
//        }
//        return false;
        return GROUPS.get(item1_index).equals(GROUPS.get(item2_index));
    }
    
    
    public static void main(String[] args)
    {
        //to get the arrayList of the groups
//        In file = new In("textFile.txt");
//        System.out.println("groups: " + getGroups(file));
        
        In file = new In(args[0]);
        file.readLine();
        
        getGroups(file);
        getGroupings(GROUPS);
        //printGroupings(getGroupings(GROUPS));
        
        System.out.println("size of groups: " + groupings.size());      
    }
}
