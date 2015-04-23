//represents a single item
public class Product {
	
	//properties
	String name;
	String store;
	String price;
	String productlink;
    String image;
    String description;
	float price_int=0.0f;
    String upc;
	
	//store link builder
	public String getStoreLink(){
		if(store.equals("Costco")){return "http://www.costco.com.mx/";}
		if(store.equals("Liverpool")){return "http://www.liverpool.com.mx/";}
		if(store.equals("Sams")){return "http://www.sams.com.mx/";}
		if(store.equals("Sanborns")){return "http://www.sanborns.com.mx/";}
		if(store.equals("Sears")){return "http://www.sears.com.mx/";}
		if(store.equals("Walmart")){return "http://www.walmart.com.mx/";}
		return "";
	}
	
	//returns store image
	public String getStoreImage(){
		if(store.equals("Costco")){return "costco.jpg";}
		if(store.equals("Liverpool")){return "liverpool.jpg";}
		if(store.equals("Sams")){return "sams.jpg";}
		if(store.equals("Sanborns")){return "sanborns.jpg";}
		if(store.equals("Sears")){return "sears.jpg";}
		if(store.equals("Walmart")){return "walmart.jpg";}
		return "";
	}
    
    //returns product image tag
    public String getProductImage(){
        return "";
    }
	
	//empty constructor
	public Product(){
		name = "";
		store = "";
		price = "";
		price_int = 0;
		productlink = "";
        image = "";
        description = "";
        upc = "";
	}
	
	//get the price in int
	public void price_calc(){
		if(price_int==0){
			String temp = price;
			temp = temp.replace("$","");
			temp = temp.replace(",","");
			temp = temp.trim();
			try {
				price_int = Float.parseFloat(temp);
			} catch (Exception e){
				price_int = 1;
			}
		}
	}
	
	//full constructor without product link
	public Product(String name, String store, String price){
		this.name = name;
		this.store = store;
		this.price = price;
		price_calc();
        this.productlink = "";
        this.image = "";
        this.description = "";
        upc = "";
	}
	
	//full constructor without image and description
	public Product(String name, String store, String price, String productlink){
		this.name = name;
		this.store = store;
		this.price = price;
		this.productlink = productlink;
		price_calc();
        this.image = "";
        this.description = "";
        upc = "";
	}
}