package search;

import com.google.appengine.api.datastore.Key;

//represents a single item
public class Product {
	
	//properties
	String name;
	String store;
	String price;
	String productlink;
	float price_int=0.0f;
	int relevance;
	String image;
	String description;
	String upc;
	long id;
	
	//store link builder
	public String getStoreLink(){
		try {
			if(store.equals("Costco")){return "http://www.costco.com.mx/";}
			if(store.equals("Liverpool")){return "http://www.liverpool.com.mx/";}
			if(store.equals("Sams")){return "http://www.sams.com.mx/";}
			if(store.equals("Sanborns")){return "http://www.sanborns.com.mx/";}
			if(store.equals("Sears")){return "http://www.sears.com.mx/";}
			if(store.equals("Walmart")){return "http://www.walmart.com.mx/";}
			if(store.equals("Bestbuy")){return "http://www.bestbuy.com.mx/";}
			if(store.equals("Coppel")){return "http://www.coppel.com/";}
			if(store.equals("Elektra")){return "http://www.elektra.com.mx/";}
			if(store.equals("Famsa")){return "http://www.famsa.com/";}
			return "";
		} catch(Exception e){
			return "";
		}
	}
	
	//detail link builder
	public String getDetailLink(){
		String link = SearchServlet.basepage;
		link += "search?q=viewProductDetail&id=";
		link += this.id;
		return link;
	}
	
	//returns store image
	public String getStoreImage(){
		String basepage = SearchServlet.basepage;
		try {
			if(store.equals("Costco")){return (basepage+"costco.jpg");}
			if(store.equals("Liverpool")){return (basepage+"liverpool.jpg");}
			if(store.equals("Sams")){return (basepage+"sams.jpg");}
			if(store.equals("Sanborns")){return (basepage+"sanborns.jpg");}
			if(store.equals("Sears")){return (basepage+"sears.jpg");}
			if(store.equals("Walmart")){return (basepage+"walmart.jpg");}
			if(store.equals("Bestbuy")){return (basepage+"bestbuy.jpg");}
			if(store.equals("Coppel")){return (basepage+"coppel.jpg");}
			if(store.equals("Elektra")){return (basepage+"elektra.png");}
			if(store.equals("Famsa")){return (basepage+"famsa.jpg");}
			return "";
		} catch(Exception err){
			return "";
		}
	}
	
	//returns product image
	public String getProductImage(boolean thumbnail){
		String imageHTML = "";
		imageHTML += "<image src=\"";
		//set image file name
		if((this.image==null) || this.image.equals("")){
			imageHTML += "kompramex.png";
		} else {
			imageHTML += "http://storage.googleapis.com/kompramex-"+this.image;
		}
		imageHTML += "\" ";
		//set image size
		if(thumbnail==true){
			if((this.image!=null) && this.image.equals("")){
				if(store.equals("Costco"))		{imageHTML+="width =\"100\" height=\"83\"";}
				if(store.equals("Liverpool"))	{imageHTML+="width =\"100\" height=\"83\"";}
				if(store.equals("Sams"))		{imageHTML+="width =\"100\" height=\"83\"";}
				if(store.equals("Sanborns"))	{imageHTML+="width =\"100\" height=\"83\"";}
				if(store.equals("Sears"))		{imageHTML+="width =\"100\" height=\"83\"";}
				if(store.equals("Walmart"))		{imageHTML+="width =\"100\" height=\"83\"";}
				if(store.equals("Bestbuy"))		{imageHTML+="width =\"100\" height=\"83\"";}
				if(store.equals("Famsa"))		{imageHTML+="width =\"100\" height=\"83\"";}
				if(store.equals("Coppel"))		{imageHTML+="width =\"100\" height=\"83\"";}
			} else {
				imageHTML+="width =\"100\" height=\"83\"";
			}
		}
		imageHTML+=">";
		return imageHTML;
	}
	
	//empty constructor
	public Product(){
		name = "";
		store = "";
		price = "";
		price_int = 0;
		productlink = "";
		relevance = 0;
		image = "";
		description = "";
	}
	
	//gets the link to the redirect page
	public String getProductLink(){
		String url = SearchServlet.basepage;
		url += "search?q=visitProductURL&id=";
		url += id;
		return url;
	}
	
	//get the price in integer
	public void price_calc(){
		try {
			if(price_int==0){
				String temp = price;
				temp = temp.replace("$","");
				temp = temp.replace(",","");
				temp = temp.trim();
				price_int = Float.parseFloat(temp);
			}
		} catch (Exception e){
			price_int = 1;
		}
	}
	
	//constructor without product link, image, description
	public Product(String name, String store, String price){
		this.name = name;
		this.store = store;
		this.price = price;
		price_calc();
		this.productlink = "";
		this.image = "";
		this.description = "";
	}
	
	//constructor without image, description
	public Product(String name, String store, String price, String productlink){
		this.name = name;
		this.store = store;
		this.price = price;
		this.productlink = productlink;
		price_calc();
		this.image = "";
		this.description = "";
	}
}