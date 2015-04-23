package search;

import java.util.Arrays;
import java.util.List;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.users.*;
import com.google.appengine.api.datastore.Entity;

public class SearchAll {
	
	public static float max;
	public static float min;
	
	//concatenates two arrays
	public static Product[] concat(Product[] a, Product[] b){
		Product[] c = Arrays.copyOf(a,a.length+b.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}
	
	//search live
	public static Product[] search(String terms){
		//get results from all stores
		Product[] results = new Product[1];
		try {
			results = Bestbuy.search(terms);
			results = concat(results, Costco.search(terms));
			results = concat(results, Liverpool.search(terms));
			results = concat(results, Sams.search(terms));
			results = concat(results, Sanborns.search(terms));
			results = concat(results, Sears.search(terms));
			results = concat(results, Walmart.search(terms));
		} catch (Exception e) {
			e.printStackTrace();
		}
		//sort
		for(int i=0; i<results.length; i++){
			for(int j=0; (j<results.length-1); j++){
				if((results[j]!=null)&&(results[j+1]!=null)){
					if(results[j].price_int>results[j+1].price_int){
						//swap
						Product temp2 = results[j];
						results[j] = results[j+1];
						results[j+1] = temp2;
					}
				}
			}
		}
		
		return results;
	}
	
	//sort on datastore
	public static Product[] search(String search, Product[] products, int index){
		//empty search
		if(search.equals("")){
			return (new Product[0]);
		}
		if(products.length==0){
			return (new Product[0]);
		}
		//obtain search key words
		search = search.toLowerCase();
		search = search.replace("á","a");
		search = search.replace("é","e");
		search = search.replace("í","i");
		search = search.replace("ó","o");
		search = search.replace("ú","u");
		String[] keywords = search.split(" ");
		//obtain relevance based on contained word matches
		for(int i=0; i<index; i++){
			//eliminate accents and capital letters
			String name = products[i].name.toLowerCase();
			name = name.replace("á", "a");
			name = name.replace("é", "e");
			name = name.replace("í", "i");
			name = name.replace("ó", "o");
			name = name.replace("ú", "u");
			//calculate and store relevance
			int previousMatch = 0;
			String[] nameArray = name.split(" ");
			for(int j=0; j<keywords.length; j++){
				if((!keywords.equals("para"))&&(!keywords.equals("de"))){
					if(name.contains(keywords[j])){
						//add points for containing word
						products[i].relevance++;
						previousMatch++;
						//add points for continuous word match
						if(previousMatch>1){
							products[i].relevance+=previousMatch;
						}
						//add points for same word position
						if(j<nameArray.length)
						if(nameArray[j].equals(keywords[j])){
							products[i].relevance+=2;
						}
					} else {
						previousMatch = 0;
					}
				}
			}
		}
		//sort based on relevance
		int n = index;
		max = 0.0f;
		try {
			min = products[0].price_int;
		} catch(Exception e){
			return (new Product[0]);
		}
		do {
			int newn = 0;
			for(int i=1; i<(n-1); i++){
				//record max and min
				if(products[i].price_int>max){
					max = products[i].price_int;
				}
				if(products[i].price_int<min){
					min = products[i].price_int;
				}
				//sort by price
				if(products[i-1].price_int < products[i].price_int){
					//swap
					Product temp = products[i-1];
					products[i-1] = products[i];
					products[i] = temp;
					newn = i;
				}
				//sort by relevance
				if(products[i-1].relevance < products[i].relevance){
					//swap
					Product temp = products[i-1];
					products[i-1] = products[i];
					products[i] = temp;
					newn = i;
				}
			}
			n = newn;
		} while(n!=0);
		//eliminate repeated products
		//TODO
		return products;
	}		
}