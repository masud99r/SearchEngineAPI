/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icevaluation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Masud
 */
public class GetWikiURL {
    private HashMap<String, Integer>keyMap;
    private int totalkey = 0;

    /**
     * @param args the command line arguments
     */
    public GetWikiURL(){
        keyMap = new HashMap<>();
        
    }
public static void main(String[] args) {
    // TODO code application logic here
    GetWikiURL getWiki = new GetWikiURL();
   // getWiki.processHierarchyData();
    //System.out.println("Query: "+getWiki.formatQuery("Top/Sports/Cricket/Ball/"));

}
public String formatQuery(String directoryString){
    String [] formatedQ = directoryString.trim().split("/");
    String query="";
    for(int i=1;i<formatedQ.length;i++){
        query = query +" "+formatedQ[i];
    }
    return query.trim();
}
public void processHierarchyData(){
    loadkeyInfo();
    System.out.println("Total Key: "+totalkey);
    //String queryOriginal = "sports game";
    //String queryCover = "jack raihanee";
    BufferedReader reader = null;
    try {
        File file = new File("data/topic/QueryLogs.txt");
        reader = new BufferedReader(new FileReader(file));
        String query=null;
        String line = null;
        while ((line = reader.readLine()) != null) {//original query
            //System.out.println("Original: "+line);
            query=formatQuery(line);
            for(int i=0;i<numCoverQ;i++){
                line = reader.readLine();
                if(line==null){
                    System.out.println("Please uptate query file correctly");
                    return;
                }
                coverQuery=line;
                double IC= getIC(originQuery, coverQuery);
                
                FileWriter fw;
                fw = new FileWriter("data/topic/IC_topic.txt",true);
                fw.write(IC+"\n");
                fw.close();
                
                System.out.println(IC);
                
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            reader.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    writeKeyInfoBack();
    System.out.println("Key status write succesfully");
}
}
public void startICEval(){
    loadkeyInfo();
    System.out.println("Total Key: "+totalkey);
    //String queryOriginal = "sports game";
    //String queryCover = "jack raihanee";
    BufferedReader reader = null;
    try {
        File file = new File("data/topic/QueryLogs.txt");
        reader = new BufferedReader(new FileReader(file));
        String line;
        line = reader.readLine();
        if(line==null){
            System.out.println("Please uptate query file correctly");
            return;
        }
        int numCoverQ = Integer.parseInt(line);
        String originQuery="";
        String coverQuery="";
        while ((line = reader.readLine()) != null) {//original query
            //System.out.println("Original: "+line);
            originQuery=line;
            for(int i=0;i<numCoverQ;i++){
                line = reader.readLine();
                if(line==null){
                    System.out.println("Please uptate query file correctly");
                    return;
                }
                coverQuery=line;
                double IC= getIC(originQuery, coverQuery);
                
                FileWriter fw;
                fw = new FileWriter("data/topic/IC_topic.txt",true);
                fw.write(IC+"\n");
                fw.close();
                
                System.out.println(IC);
                
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            reader.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    writeKeyInfoBack();
    System.out.println("Key status write succesfully");
}
}
public double getIC(String originQuery, String coverQuery){
    double IC=-99999;//default garbase value indicate key issue
    String apiKey = getValidBestKey();
    if(apiKey.isEmpty()){
        return IC;
    }
    double icOrigin =calculateHits(originQuery, apiKey);
    double icCover = calculateHits(coverQuery, apiKey);
    //System.out.println("OriginHit: "+icOrigin+" CoverHits: "+icCover);
    if(icOrigin>0 && icCover>0){
        IC=Math.max(icOrigin, icCover)/Math.min(icOrigin, icCover);
    }else{
        IC=9999999;//maximum value
    }
    
   return IC;
}
public String getWikiURL(String query, String bingAPIKey){
    //update key use by incrementing key use
    Integer n = keyMap.get(bingAPIKey);
    if(n==null){
        n=1;
    }else{
        n=n+1;
    }
    keyMap.put(bingAPIKey, n);
    
    double icHit = 1;
    String searchText = query;
    searchText = searchText.replaceAll(" ", "%20");
    String accountKey=bingAPIKey;

    byte[] accountKeyBytes = Base64.encodeBase64((accountKey + ":" + accountKey).getBytes());
    String accountKeyEnc = new String(accountKeyBytes);
    URL url;
    try {
        url = new URL(
                "https://api.datamarket.azure.com/Bing/Search/v1/Composite?Sources=%27Web%27&Query=%27" + searchText + "%27&$format=JSON");
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
    //conn.addRequestProperty(accountKeyEnc, "Mozilla/4.76"); 
    BufferedReader br = new BufferedReader(new InputStreamReader(
            (conn.getInputStream())));
    StringBuilder sb = new StringBuilder();
    String output;
    //System.out.println("Output from Server .... \n");
    //write json to string sb
    while ((output = br.readLine()) != null) {
        //System.out.println("Output is: "+output);
        sb.append(output);

    }

    conn.disconnect();
     //find webtotal among output      
   int find= sb.indexOf("\"WebTotal\":\"");
   int startindex = find + 12;
    //    System.out.println("Find: "+find);

   int lastindex = sb.indexOf("\",\"WebOffset\"");
    String ICString = sb.substring(startindex,lastindex);
    //System.out.println(ICString);
    icHit = Double.valueOf(ICString);
    } catch (MalformedURLException e1) {
        icHit=1;
        e1.printStackTrace();
    } catch (IOException e) {
        icHit=1;
        e.printStackTrace();
    }

        return null;
    
}
public double calculateHits(String query, String bingAPIKey){
    //update key use by incrementing key use
    Integer n = keyMap.get(bingAPIKey);
    if(n==null){
        n=1;
    }else{
        n=n+1;
    }
    keyMap.put(bingAPIKey, n);
    
    double icHit = 1;
    String searchText = query;
    searchText = searchText.replaceAll(" ", "%20");
    String accountKey=bingAPIKey;

    byte[] accountKeyBytes = Base64.encodeBase64((accountKey + ":" + accountKey).getBytes());
    String accountKeyEnc = new String(accountKeyBytes);
    URL url;
    try {
        url = new URL(
                "https://api.datamarket.azure.com/Bing/Search/v1/Composite?Sources=%27Web%27&Query=%27" + searchText + "%27&$format=JSON");
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
    //conn.addRequestProperty(accountKeyEnc, "Mozilla/4.76"); 
    BufferedReader br = new BufferedReader(new InputStreamReader(
            (conn.getInputStream())));
    StringBuilder sb = new StringBuilder();
    String output;
    //System.out.println("Output from Server .... \n");
    //write json to string sb
    while ((output = br.readLine()) != null) {
        //System.out.println("Output is: "+output);
        sb.append(output);

    }

    conn.disconnect();
     //find webtotal among output      
   int find= sb.indexOf("\"WebTotal\":\"");
   int startindex = find + 12;
    //    System.out.println("Find: "+find);

   int lastindex = sb.indexOf("\",\"WebOffset\"");
    String ICString = sb.substring(startindex,lastindex);
    //System.out.println(ICString);
    icHit = Double.valueOf(ICString);
    } catch (MalformedURLException e1) {
        icHit=1;
        e1.printStackTrace();
    } catch (IOException e) {
        icHit=1;
        e.printStackTrace();
    }

        return icHit;
    }
public void loadkeyInfo(){
   BufferedReader reader = null;
try {
    File file = new File("data/apiKeySettings.txt");
    reader = new BufferedReader(new FileReader(file));
    String line;
    while ((line = reader.readLine()) != null) {
        System.out.println(line);
        String[] linerParts = line.split(" ");
        keyMap.put(linerParts[0], Integer.parseInt(linerParts[1]));
        totalkey++;
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

}

private void writeKeyInfoBack() {
    try {
        File file = new File("data/apiKeySettings.txt");
        // if file doesnt exists, then create it
        if (!file.exists()) {
                file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);

        for(String key:keyMap.keySet()){
            bw.write(key+" "+keyMap.get(key)+"\n");
        }
        bw.close();
        System.out.println("Done");

} catch (IOException e) {
        e.printStackTrace();
}
}
public String getValidBestKey(){
    String validKey="";
    for(String key:keyMap.keySet()){
        if(keyMap.get(key)<4900){
            validKey=key;
        }
    }
    if(validKey.isEmpty()){
        System.out.println("All keys are out of limit, Please add more new keys!");
    }
    return validKey;
}
         
}
