/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icevaluation;

import java.awt.image.SampleModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Masud
 */
public class SampleQueries {
    private HashMap<String, ArrayList<String>> topicQueryLogs;
    private HashMap<String, ArrayList<String>> semanticQueryLogs;
    private HashMap<String, ArrayList<String>> pdsQueryLogs;
    public static void main(String[] args) throws IOException {
        SampleQueries sq = new SampleQueries();
        sq.createHashMaps();
        sq.createQueryMapping();
        
       
    }
    public void createQueryMapping() throws IOException{
        List<Map.Entry<String, ArrayList<String>>> list = new ArrayList<>(topicQueryLogs.entrySet());
        System.out.println("\n************ Now Let's start shuffling list ************");
        Collections.shuffle(list);
        for (Map.Entry<String, ArrayList<String>> entry : list) {
                System.out.println(entry.getKey() + " :: " + entry.getValue());
                topicQueryLogs.put(entry.getKey(), entry.getValue());//shuffle all those
        }
        //map to other
        
        int sampleNumber=0;
        for(String keyOriginalQ:topicQueryLogs.keySet()){
            if(sampleNumber>1500){
                break;
            }
            if(pdsQueryLogs.containsKey(keyOriginalQ)&&topicQueryLogs.containsKey(keyOriginalQ)){
                FileWriter fw_topic;
                fw_topic = new FileWriter("data/topic/QueryLogs.txt",true);
                fw_topic.write(keyOriginalQ+"\n");
                fw_topic.write(topicQueryLogs.get(keyOriginalQ).get(0)+"\n");
                fw_topic.write(topicQueryLogs.get(keyOriginalQ).get(1)+"\n");
                fw_topic.close();
                
                FileWriter fw_semantic;
                fw_semantic = new FileWriter("data/semantic/QueryLogs.txt",true);
                fw_semantic.write(keyOriginalQ+"\n");
                fw_semantic.write(semanticQueryLogs.get(keyOriginalQ).get(0)+"\n");
                fw_semantic.write(semanticQueryLogs.get(keyOriginalQ).get(1)+"\n");
                fw_semantic.close();
                
                FileWriter fw_pds;
                fw_pds = new FileWriter("data/pds/QueryLogs.txt",true);
                fw_pds.write(keyOriginalQ+"\n");
                fw_pds.write(pdsQueryLogs.get(keyOriginalQ).get(0)+"\n");
                fw_pds.write(pdsQueryLogs.get(keyOriginalQ).get(1)+"\n");
                fw_pds.close();
                
                sampleNumber++;
                
            }
            
        }
    }
    public void createHashMaps(){
        topicQueryLogs = new HashMap<>();
        semanticQueryLogs = new HashMap<>();
        pdsQueryLogs = new HashMap<>();
        
    BufferedReader reader = null;
    try {
        File file = new File("data/models_queries/queryLogsTopic_topic.txt");
        reader = new BufferedReader(new FileReader(file));
        String line;
        
        //numCoverQ
        String originQuery="";
        String coverQuery="";
        while ((line = reader.readLine()) != null) {//original query
            String originalQ = line;
            
            ArrayList<String> tempCoverq = new ArrayList<>();
            line = reader.readLine();
            tempCoverq.add(line);
            line = reader.readLine();
            tempCoverq.add(line);
            
            topicQueryLogs.put(originalQ, tempCoverq);
            //}
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            reader.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
    BufferedReader reader2 = null;
    try {
        File file = new File("data/models_queries/queryLogsTopic_semantic.txt");
        reader2 = new BufferedReader(new FileReader(file));
        String line;

        while ((line = reader2.readLine()) != null) {//original query
            String originalQ = line;
            
            ArrayList<String> tempCoverq = new ArrayList<>();
            line = reader2.readLine();
            tempCoverq.add(line);
            line = reader2.readLine();
            tempCoverq.add(line);
            
            semanticQueryLogs.put(originalQ, tempCoverq);
            //}
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            reader2.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
    
    BufferedReader reader3 = null;
    try {
        File file = new File("data/models_queries/queryLogsTopic_pds.txt");
        reader3 = new BufferedReader(new FileReader(file));
        String line;

        while ((line = reader3.readLine()) != null) {//original query
            String originalQ = line;
            
            ArrayList<String> tempCoverq = new ArrayList<>();
            line = reader3.readLine();
            tempCoverq.add(line);
            line = reader3.readLine();
            tempCoverq.add(line);
            
            //skip last two cover queries
            line = reader3.readLine();
            line = reader3.readLine();
            
            pdsQueryLogs.put(originalQ, tempCoverq);
            //}
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            reader3.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
    
  }
}
