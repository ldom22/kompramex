package search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.users.*;

@SuppressWarnings("serial")
public class SearchServlet extends HttpServlet {
	
	//SET TO LOCAL TEST OR PRODUCTION SERVER!!!!
	//static String basepage = "http://127.0.0.1:8888/";
	static String basepage = "http://www.kompramex.com/";
	
	//check if a string is a number
	public static boolean isNumber(String s){
		try {
			int i = Integer.parseInt(s);
		} catch(Exception e){
			return false;
		}
		return true;
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		//get the search string
		String q = req.getParameter("q");
		if(q==null){
			resp.getWriter().println("<script>window.location.href=\"http://www.kompramex.com\"</script>");
			return;
		}
		//access the data store
		UserService us = UserServiceFactory.getUserService();
		User user = us.getCurrentUser();
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		//for deleting a store
		/*if(q.equals("deleteFamsat")){
			Query query = new Query("Product");
			query.addFilter("store", FilterOperator.EQUAL, "sdfsdf");
			List<Entity> products = ds.prepare(query).asList(FetchOptions.Builder.withDefaults());
			for(Entity e: products){
				ds.delete(e.getKey());
			}
		} else*/
		//administrator was uploading data
		if(q.equals("g3h34389h7432")){
			//check if product is repeated
			Query query = new Query("Product");
			query.addFilter("store", FilterOperator.EQUAL, "Walmart");
			query.addFilter("upc", FilterOperator.EQUAL, req.getParameter("upc"));
			List<Entity> products = ds.prepare(query).asList(FetchOptions.Builder.withLimit(1));
			if(!products.isEmpty()){
				resp.getWriter().println("Success: repeated");
			}
			//product is not repeated, save entity
			else {
				Entity newproduct = new Entity("Product");
				newproduct.setProperty("name", req.getParameter("name"));
				newproduct.setProperty("store", req.getParameter("store"));
				newproduct.setProperty("price", req.getParameter("price"));
				newproduct.setProperty("productlink", req.getParameter("productlink"));
				newproduct.setProperty("image", req.getParameter("image"));
				newproduct.setProperty("description", req.getParameter("description"));
				newproduct.setProperty("upc", req.getParameter("upc"));
				//generate keywords
				String words[] = req.getParameter("name").split(" ");
				//remove short words
				int numkw = words.length;
				for(int i=0; i<words.length; i++){
					//remove capital letters
					words[i] = words[i].toLowerCase();
					//remove accents
					words[i] = words[i].replace("á","a");
					words[i] = words[i].replace("é","e");
					words[i] = words[i].replace("í","i");
					words[i] = words[i].replace("ó","o");
					words[i] = words[i].replace("ú","u");
				}
				//add to entity
				for(int i=0; (i<numkw)&&(i<10); i++){
					if(!words[i].equals("")){
						newproduct.setProperty("keyword"+i,words[i]);
					}	
				}
				//save the data
				ds.put(newproduct);
				resp.getWriter().println("Success: saved");
			}
		//user was requesting detail page
		} else if(q.equals("viewProductDetail")){
			//fetch product
			try{
				long id = Long.parseLong(req.getParameter("id"));
				Key k = KeyFactory.createKey("Product", id);
				Entity product = ds.get(k);
				Product result = new Product(
					(String)product.getProperty("name"),
					(String)product.getProperty("store"),
					(String)product.getProperty("price"),
					(String)product.getProperty("productlink")
				);
				//patch image, description, UPC, id
				result.image = (String)product.getProperty("image");
				result.description = (String)product.getProperty("description");
				result.upc = (String)product.getProperty("upc");
				result.id = product.getKey().getId();
				//save view log
				Entity saveViewLog = new Entity("ViewLog");
				saveViewLog.setProperty("product", k);
				saveViewLog.setProperty("time", new Date().toString());
				saveViewLog.setProperty("country",req.getHeader("X-AppEngine-Country"));
				saveViewLog.setProperty("region",req.getHeader("X-AppEngine-Region"));
				saveViewLog.setProperty("city",req.getHeader("X-AppEngine-City"));
				saveViewLog.setProperty("coordinates",req.getHeader("X-AppEngine-CityLatLong"));
				ds.put(saveViewLog);
				//show description page
				resp.getWriter().println(WriteProductDetail.generate(result));
			} catch(Exception e){
				//print error page
				e.printStackTrace();
				resp.getWriter().println("error");
			}
		//user wants to visit a page
		} else if(q.equals("visitProductURL")){
			//get product id
			long id = Long.parseLong(req.getParameter("id"));
			Key k = KeyFactory.createKey("Product", id);
			//save visit log
			Entity visitLog = new Entity("VisitLog");
			visitLog.setProperty("product", k);
			visitLog.setProperty("time", new Date().toString());
			visitLog.setProperty("country",req.getHeader("X-AppEngine-Country"));
			visitLog.setProperty("region",req.getHeader("X-AppEngine-Region"));
			visitLog.setProperty("city",req.getHeader("X-AppEngine-City"));
			visitLog.setProperty("coordinates",req.getHeader("X-AppEngine-CityLatLong"));
			ds.put(visitLog);
			//get URL to visit
			try {
				Entity product = ds.get(k);
				String URL = (String)product.getProperty("productlink");
				resp.getWriter().println("<script>window.location.href=\""+URL+"\"</script>");
			} catch(Exception e){
				e.printStackTrace();
				resp.getWriter().println("error");
			}
		//user was searching
		} else {
			//divide query on keywords
			String[] searchkw = q.split(" ");
			//remove capitals and accents
			for(int i=0; i<searchkw.length; i++){
				searchkw[i] = searchkw[i].toLowerCase();
				searchkw[i] = searchkw[i].replace("á","a");
				searchkw[i] = searchkw[i].replace("é","e");
				searchkw[i] = searchkw[i].replace("í","i");
				searchkw[i] = searchkw[i].replace("ó","o");
				searchkw[i] = searchkw[i].replace("ú","u");
			}
			//setup queries
			Product[] results = new Product[5000];
			int index = 0;
			for(int i=0; (i<searchkw.length)&&(i<10); i++){
				for(int j=0; j<10; j++){
					//ignore stupid words
					if((!searchkw[i].equals("para"))&&(!searchkw[i].equals("de"))&&(!isNumber(searchkw[i]))){
						//fetch matching items
						Query query = new Query("Product");
						query.addFilter("keyword"+j, FilterOperator.EQUAL, searchkw[i]);
						List<Entity> products = ds.prepare(query).asList(FetchOptions.Builder.withLimit(50));
						//add to result list
						for(Entity e: products){
							results[index] = new Product(
								(String)e.getProperty("name"),
								(String)e.getProperty("store"),
								(String)e.getProperty("price"),
								(String)e.getProperty("productlink")
							);
							//patch id
							results[index].id = e.getKey().getId();
							//patch image, description, upc
							results[index].image = (String)e.getProperty("image");
							results[index].description = (String)e.getProperty("description");
							results[index].upc = (String)e.getProperty("upc");
							index++;
						}
					}
				}
			}
			results = SearchAll.search(q, results, index);
			resp.getWriter().println(WriteResults.generate(q, results, index));
            //save for request statistics
			Entity newsearch = new Entity("SearchLog");
			newsearch.setProperty("query", q);
			newsearch.setProperty("time", new Date().toString());
			newsearch.setProperty("country",req.getHeader("X-AppEngine-Country"));
			newsearch.setProperty("region",req.getHeader("X-AppEngine-Region"));
			newsearch.setProperty("city",req.getHeader("X-AppEngine-City"));
			newsearch.setProperty("coordinates",req.getHeader("X-AppEngine-CityLatLong"));
            newsearch.setProperty("results",index);
			ds.put(newsearch);

		}
	}
}
