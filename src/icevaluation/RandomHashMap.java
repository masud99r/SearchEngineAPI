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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
 
/**
 * @author Crunchify.com
 * 
 */
 
public class RandomHashMap {
	public static void main(String[] args) {
		//
		// Create a hashtable and put some key-value pair.
		//
		HashMap<String, String> companies = new HashMap<String, String>();
		companies.put("eBay", "South San Jose");
		companies.put("Paypal", "North San Jose");
		companies.put("Google", "Mountain View");
		companies.put("Yahoo", "Santa Clara");
		companies.put("Twitter", "San Francisco");
 
		// Get a random entry from the HashMap.
		//Object[] crunchifyKeys = companies.keySet().toArray();
		//Object key = crunchifyKeys[new Random().nextInt(crunchifyKeys.length)];
		//System.out.println("************ Random Value ************ \n" + key + " :: " + companies.get(key));
 
		List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(companies.entrySet());
		System.out.println("\n************ Now Let's start shuffling list ************");
		Collections.shuffle(list);
		for (Map.Entry<String, String> entry : list) {
			System.out.println(entry.getKey() + " :: " + entry.getValue());
		}
	}
}
