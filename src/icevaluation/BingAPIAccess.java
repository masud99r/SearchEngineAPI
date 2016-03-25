/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icevaluation;

/**
 *
 * @author Masud
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author mr5ba
 */
public class BingAPIAccess {
     public static void main(String[] args) {
       String searchText = "arts site:wikipedia.org";
       searchText = searchText.replaceAll(" ", "%20");
       String accountKey="jTRIJt9d8DR2QT/Z3BJCAvY1BfoXj0zRYgSZ8deqHHo";

    byte[] accountKeyBytes = Base64.encodeBase64((accountKey + ":" + accountKey).getBytes());
    String accountKeyEnc = new String(accountKeyBytes);
    URL url;
    try {
        url = new URL(
                "https://api.datamarket.azure.com/Bing/Search/v1/Composite?Sources=%27Web%27&Query=%27" + searchText + "%27&$format=JSON");
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Authorization", "Basic " + accountKeyEnc);

    BufferedReader br = new BufferedReader(new InputStreamReader(
            (conn.getInputStream())));
    StringBuilder sb = new StringBuilder();
    String output;
    System.out.println("Output from Server .... \n");
    //write json to string sb
    int c=0;
    if ((output = br.readLine()) != null) {
        System.out.println("Output is: "+output);
        sb.append(output);
        c++;
        System.out.println("C:"+c);

    }

    conn.disconnect();
     //find webtotal among output      
   int find= sb.indexOf("\"WebTotal\":\"");
   int startindex = find + 12;
        System.out.println("Find: "+find);

   int lastindex = sb.indexOf("\",\"WebOffset\"");

    System.out.println(sb.substring(startindex,lastindex));

    } catch (MalformedURLException e1) {
        e1.printStackTrace();
    } catch (IOException e) {

        e.printStackTrace();
    }


}
}

